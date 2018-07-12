package dev.boom.pages;

import dev.boom.common.CommonMethod;
import dev.boom.common.enums.MainNavBarEnum;

public class Home extends BoomMainPage {

	private static final long serialVersionUID = 1L;

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
	}

	@Override
	protected int getMenuBarIndex() {
		return MainNavBarEnum.HOME.getIndex();
	}
	
}
