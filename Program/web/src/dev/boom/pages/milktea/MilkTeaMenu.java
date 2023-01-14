package dev.boom.pages.milktea;

import java.util.List;

import org.apache.click.util.ClickUtils;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaSocketMessage;
import dev.boom.common.milktea.MilkTeaSocketType;
import dev.boom.common.milktea.MilkTeaTabEnum;
import dev.boom.milktea.object.MenuItem;
import dev.boom.services.Menu;
import dev.boom.services.MenuService;
import dev.boom.services.Order;
import dev.boom.services.OrderService;
import dev.boom.socket.SocketSessionPool;
import dev.boom.socket.endpoint.MilkTeaEndPoint;

public class MilkTeaMenu extends MilkTeaMainPage {

	private static final long serialVersionUID = 1L;
	
	private Menu menuInfo;
	private int page = 1;

	public MilkTeaMenu() {
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(importJs("/js/milktea/milktea-menu.js"));
		headElements.add(importJs("/js/milktea/milktea-order.js"));
		headElements.add(importJs("/js/milktea/milktea-history.js"));
		headElements.add(importCss("/css/milktea/milktea-menu.css"));
		return headElements;
	}

	@Override
	public void onInit() {
		super.onInit();
		String strMenuId = getContext().getRequestParameter("id");
		if (CommonMethod.isValidNumeric(strMenuId, 1, Long.MAX_VALUE)) {
			menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
			if (menuInfo != null && !menuInfo.isAvailableForUser(getUserInfo())) {
				setRedirect(this.getClass());
				return;
			}
		}
		String strPage = getContext().getRequestParameter("page");
		if (CommonMethod.isValidNumeric(strPage, 1, Integer.MAX_VALUE)) {
			page = Integer.parseInt(strPage);
		}
	}



	@Override
	public void onRender() {
		super.onRender();
		if (getRedirect() != null) {
			return;
		}
		String contextPath = getHostURL() + getContextPath();
		if (menuInfo != null) {
			List<MenuItem> listMenuItem = MenuService.getMenuItemListByShopId(menuInfo.getShopId());
			if (listMenuItem != null) {
				addModel("dish_list", MilkTeaCommonFunc.getHtmlListMenuItem(menuInfo, listMenuItem, contextPath, getUserInfo(), getMessages()));
				addModel("dish_type", MilkTeaCommonFunc.getHtmlListMenuItemType(listMenuItem, contextPath));
			}
			List<Order> listOrder = OrderService.getOrderInfoListByMenuId(menuInfo.getId());
			addModel("order_list", MilkTeaCommonFunc.getHtmlListOrder(listOrder, menuInfo, getUserInfo(), contextPath, getMessages(), getWorldInfo()));
			addModel("menuInfo", menuInfo);
		} else {
			addModel("menuList", MilkTeaCommonFunc.getHtmlListMenu(getUserInfo(), contextPath, getMessages(), page));
			addModel("msg_id", MilkTeaSocketMessage.UPDATE_MENU_LIST.getId());
		}
		if (getUserInfo() != null) {
			String milkteaToken = SocketSessionPool.generateValidToken(MilkTeaEndPoint.ENDPOINT_NAME, getUserInfo());
			String params = "?" + MilkTeaEndPoint.VALIDATION_KEY + "=" + milkteaToken;
			if (menuInfo != null) {
				params += "&type=" + MilkTeaSocketType.MENU_DETAIL.getType() + "&data=" + ClickUtils.encodeURL("menu_id=" + menuInfo.getId());
			} else {
				params += "&type=" + MilkTeaSocketType.MENU_LIST.getType();
			}
			addModel("milkteaToken", getSocketUrl(MilkTeaEndPoint.SOCKET_PATH, params));
		}
	}
	
	@Override
	protected String initMilkTeaIntro() {
		if (menuInfo != null) {
			return MilkTeaCommonFunc.getHtmlMenuDetail(menuInfo, userInfo, getHostURL() + getContextPath(), getMessages());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"col-lg-12\">");
		sb.append("<div style=\"height:100%;");
			sb.append("background-image:");
				sb.append("url('" + getContextPath() + "/img/milktea/ranking_banner_c.png');");
			sb.append("background-repeat:repeat-x;");
		sb.append("\">");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

	@Override
	protected int getMilkTeaTabIndex() {
		return MilkTeaTabEnum.OPENING_MENU.getIndex();
	}

}
