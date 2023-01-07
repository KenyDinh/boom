package dev.boom.pages;

import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.click.element.JsScript;

import dev.boom.common.CommonMethod;
import dev.boom.common.enums.MainNavBarEnum;
import dev.boom.core.GameLog;
import dev.boom.services.AuthToken;
import dev.boom.services.AuthTokenService;
import dev.boom.services.CarouselInfo;
import dev.boom.services.CarouselService;
import dev.boom.socket.SocketSessionPool;

public class Home extends BoomMainPage {

	private static final long serialVersionUID = 1L;
	private static final int MAX_CAROUSEL_SHOW = 5;

	public Home() {
	}

	@Override
	public void onInit() {
		super.onInit();
	}
	
	@Override
	public void onPost() {
		super.onPost();
		if(userInfo == null) {
			setRedirect(Home.class);
			return;
		}
		String strFormName = getContext().getRequestParameter("form_name");
		if (strFormName != null && strFormName.equals("logout")) {
			removeBoomSession();
			//  Remove socket session
			SocketSessionPool.removeSocketSession(userInfo.getId());
			// Remove cookie
			Cookie[] cookies = getContext().getRequest().getCookies();
			if (cookies != null && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(AuthTokenService.USER_TOKEN_KEY)) {
						AuthToken authToken = AuthTokenService.getAuthToken(cookie.getValue());
						if (authToken != null) {
							if (!AuthTokenService.deleteAuthToken(authToken.getToken())) {
								GameLog.getInstance().error("Delete auth token fail. ID: " + authToken.getId());
							}
						}
						break;
					}
				}
			}
			
			String redirect = "";
			String strIndex = getContext().getRequestParameter("index");
			if (CommonMethod.isValidNumeric(strIndex, 1, Integer.MAX_VALUE)) {
				redirect = MainNavBarEnum.valueOf(Integer.parseInt(strIndex)).getViewPage();
			}
			if (redirect.isEmpty()) {
				setRedirect(Home.class);
				return;
			} else {
				setRedirect(getHostURL() + getContextPath() + "/" + redirect);
				return;
			}
		}
	}
	
	@Override
	public void onRender() {
		super.onRender();
		if (getRedirect() != null) {
			return;
		}
		List<CarouselInfo> carouselList = CarouselService.getCarouselList(MAX_CAROUSEL_SHOW);
		if (carouselList != null) {
			addModel("carouselList", carouselList);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(importCss("/css/home.css"));
		headElements.add(new JsScript("$j('#content').removeClass('container').css('max-width','');"));
		
		return headElements;
	}

	@Override
	protected int getMenuBarIndex() {
		return MainNavBarEnum.HOME.getIndex();
	}
	
}
