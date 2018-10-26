package dev.boom.pages.manage.milktea;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.click.element.JsImport;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaOrderFlag;
import dev.boom.core.GameLog;
import dev.boom.pages.manage.ManagePageBase;
import dev.boom.services.CommonDaoService;
import dev.boom.services.MenuInfo;
import dev.boom.services.MenuService;
import dev.boom.services.OrderInfo;
import dev.boom.services.OrderService;
import dev.boom.services.UserInfo;
import dev.boom.services.UserService;
import dev.boom.socket.SocketSessionPool;
import dev.boom.socket.endpoint.ManageMilkTeaEndPoint;

public class MilkTeaManageOrder extends ManagePageBase {

	private static final long serialVersionUID = 1L;

	private MenuInfo menuInfo = null;
	private OrderInfo orderInfo = null;
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
			if (menuInfo == null) {
				setRedirect(MilkTeaManageMenu.class);
				return;
			}
			orderList = OrderService.getOrderInfoListByMenuId(menuInfo.getId(), "ORDER BY (dish_price + attr_price) * quantity ASC");
			addModel("menuInfo", menuInfo);
			addModel("orderList", MilkTeaCommonFunc.getHtmlListOrder(orderList, menuInfo, userInfo, getHostURL() + getContextPath(), getMessages(), true));
			String token = SocketSessionPool.generateValidToken(ManageMilkTeaEndPoint.ENDPOINT_NAME, userInfo);
			String params = "?" + ManageMilkTeaEndPoint.VALIDATION_KEY + "=" + token;
			addModel("socket_url", getSocketUrl(ManageMilkTeaEndPoint.SOCKET_PATH, params));
		} else {
			String strOrderId = getContext().getRequestParameter("order_id");
			if (CommonMethod.isValidNumeric(strOrderId, 1, Long.MAX_VALUE)) {
				orderInfo = OrderService.getOrderInfoById(Long.parseLong(strOrderId));
			}
			if (orderInfo == null) {
				setRedirect(MilkTeaManageMenu.class);
				return;
			}
			menuInfo = MenuService.getMenuById(orderInfo.getMenuId());
			if (menuInfo == null || !menuInfo.isEditable()) {
				setRedirect(MilkTeaManageMenu.class);
				return;
			}
			addModel("orderInfo", orderInfo);
			StringBuilder flags = new StringBuilder();
			for (MilkTeaOrderFlag orderFlag : MilkTeaOrderFlag.values()) {
				flags.append("<div class=\"custom-control custom-checkbox\">");
				flags.append("<input type=\"checkbox\" class=\"custom-control-input\" name=\"flag\" id=\"flag-" + orderFlag.ordinal() + "\" value=\"" + orderFlag.getBitMask() + "\" " + (orderFlag.isValidFlag(orderInfo.getFlag()) ? "checked" : "") + "/>");
				flags.append("<label class=\"custom-control-label\" for=\"flag-" + orderFlag.ordinal() + "\">").append(orderFlag.name()).append("</label>");
				flags.append("</div>");
			}
			addModel("flags", flags.toString());
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (getRedirect() != null) {
			return;
		}
		if (orderInfo == null) {
			setRedirect(MilkTeaManageMenu.class);
			return;
		}
		String strMode = getContext().getRequestParameter("mode");
		if (strMode == null) {
			return;
		}
		if (strMode.equals("Update")) {
			String strUserName = getContext().getRequestParameter("username");
			if (strUserName != null && strUserName.length() > 0) {
				UserInfo _userInfo = UserService.getUserByName(strUserName);
				if (_userInfo == null) {
					GameLog.getInstance().error("User is not exist, username:" + strUserName);
					addModel("error", "Username is invalid!");
					return;
				}
				orderInfo.setUserId(_userInfo.getId());
				orderInfo.setUsername(_userInfo.getUsername());
			}
			orderInfo.setFlag(0);
			String[] flags = getContext().getRequestParameterValues("flag");
			if (flags != null && flags.length > 0) {
				for (String strFlag : flags) {
					if (CommonMethod.isValidNumeric(strFlag, 1, Integer.MAX_VALUE)) {
						MilkTeaOrderFlag orderFlag = MilkTeaOrderFlag.valueOf(Integer.parseInt(strFlag));
						if (orderFlag == null) {
							continue;
						}
						int newFlag = orderFlag.getValidFlag(orderInfo.getFlag());
						orderInfo.setFlag(newFlag);
					}
				}
			}
			if (!CommonDaoService.update(orderInfo.getTblInfo())) {
				GameLog.getInstance().error("update failed!");
				addModel("error", "update failed!");
				return;
			}
		} else if (strMode.equals("Delete")) {
			orderInfo.getTblInfo().setDelete();
			if (!CommonDaoService.delete(orderInfo.getTblInfo())) {
				GameLog.getInstance().error("delete failed!");
				addModel("error", "delete failed!");
				return;
			}
		}
		Map<String, String> params = new HashMap<>();
		params.put("menu_id", String.valueOf(menuInfo.getId()));
		setRedirect(this.getClass(), params);
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
