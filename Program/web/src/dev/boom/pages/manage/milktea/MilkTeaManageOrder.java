package dev.boom.pages.manage.milktea;

import java.util.List;

import org.apache.click.element.JsImport;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.pages.manage.ManagePageBase;
import dev.boom.services.MenuInfo;
import dev.boom.services.MenuService;
import dev.boom.services.OrderInfo;
import dev.boom.services.OrderService;
import dev.boom.socket.SocketSessionPool;
import dev.boom.socket.endpoint.ManageMilkTeaEndPoint;

public class MilkTeaManageOrder extends ManagePageBase {

	private static final long serialVersionUID = 1L;

	private MenuInfo menuInfo = null;
	private List<OrderInfo> orderList = null;

	public MilkTeaManageOrder() {
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new JsImport("/js/manage/milk_tea_manage.js"));
		return headElements;
	}

	@Override
	public boolean onSecurityCheck() {
		return super.onSecurityCheck();
	}

	@Override
	public void onInit() {
		super.onInit();
		String strMenuId = getContext().getRequestParameter("menu_id");
		if (CommonMethod.isValidNumeric(strMenuId, 1, Long.MAX_VALUE)) {
			menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
		}
		if (menuInfo == null) {
			setRedirect(MilkTeaManageMenu.class);
			return;
		}
		orderList = OrderService.getOrderInfoListByMenuId(menuInfo.getId());
		addModel("menuInfo", menuInfo);
		addModel("orderList", MilkTeaCommonFunc.getHtmlListOrder(orderList, menuInfo, userInfo, getHostURL() + getContextPath(), getMessages(), true));
		String token = SocketSessionPool.generateValidToken(ManageMilkTeaEndPoint.ENDPOINT_NAME, userInfo);
		String params = "?" + ManageMilkTeaEndPoint.VALIDATION_KEY + "=" + token;
		addModel("socket_url", getSocketUrl(params));
	}

	@Override
	public void onPost() {
		super.onPost();
		if (getRedirect() != null) {
			return;
		}
	}

	@Override
	public void onRender() {
		super.onRender();
		if (getRedirect() != null) {
			return;
		}
		if (getModel().get("error") != null) {
			return;
		}
	}
	
}
