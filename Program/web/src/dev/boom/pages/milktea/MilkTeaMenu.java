package dev.boom.pages.milktea;

import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.util.ClickUtils;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaSocketType;
import dev.boom.common.milktea.MilkTeaTabEnum;
import dev.boom.milktea.object.MenuItem;
import dev.boom.services.MenuInfo;
import dev.boom.services.MenuService;
import dev.boom.services.OrderInfo;
import dev.boom.services.OrderService;
import dev.boom.socket.SocketSessionPool;
import dev.boom.socket.endpoint.MilkTeaEndPoint;

public class MilkTeaMenu extends MilkTeaMainPage {

	private static final long serialVersionUID = 1L;
	
	private MenuInfo menuInfo;

	public MilkTeaMenu() {
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new JsImport("/js/milktea/milktea-menu.js"));
		headElements.add(new CssImport("/css/milktea/milktea-menu.css"));
		return headElements;
	}



	@Override
	public void onInit() {
		super.onInit();
		String strMenuId = getContext().getRequestParameter("id");
		if (CommonMethod.isValidNumeric(strMenuId, 1, Long.MAX_VALUE)) {
			menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
		}
	}



	@Override
	public void onRender() {
		super.onRender();
		String contextPath = getHostURL() + getContextPath();
		if (menuInfo != null) {
			List<MenuItem> listMenuItem = MenuService.getMenuItemListByShopId(menuInfo.getShopId());
			if (listMenuItem != null) {
				addModel("dish_list", MilkTeaCommonFunc.getHtmlListMenuItem(menuInfo, listMenuItem, contextPath, getUserInfo(), messages));
				addModel("dish_type", MilkTeaCommonFunc.getHtmlListMenuItemType(listMenuItem, contextPath));
			}
			List<OrderInfo> listOrder = OrderService.getOrderInfoListByMenuId(menuInfo.getId());
			addModel("order_list", MilkTeaCommonFunc.getHtmlListOrder(listOrder, menuInfo, getUserInfo(), contextPath, messages));
			addModel("menuInfo", menuInfo);
		} else {
			addModel("menuList", MilkTeaCommonFunc.getHtmlListMenu(contextPath, getMessages()));
		}
		if (getUserInfo() != null) {
			String milkteaToken = SocketSessionPool.generateValidToken(MilkTeaEndPoint.ENDPOINT_NAME, getUserInfo());
			String params = "?" + MilkTeaEndPoint.VALIDATION_KEY + "=" + milkteaToken;
			if (menuInfo != null) {
				params += "&type=" + MilkTeaSocketType.MENU_DETAIL.getType() + "&data=" + ClickUtils.encodeURL("menu_id=" + menuInfo.getId());
			} else {
				params += "&type=" + MilkTeaSocketType.MENU_LIST.getType();
			}
			addModel("milkteaToken", getSocketUrl(params));
		}
	}
	
	@Override
	protected String initMilkTeaIntro() {
		String intro = "";
		if (menuInfo != null) {
			intro = MilkTeaCommonFunc.getHtmlMenuDetail(menuInfo, userInfo, getHostURL() + getContextPath(), getMessages());
		}
		return intro;
	}

	@Override
	protected int getMilkTeaTabIndex() {
		return MilkTeaTabEnum.OPENING_MENU.getIndex();
	}

}
