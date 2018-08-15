package dev.boom.pages.milktea;

import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaSocketMessage;
import dev.boom.services.MenuInfo;
import dev.boom.services.MenuService;
import dev.boom.services.OrderInfo;
import dev.boom.services.OrderService;

public class MilkTeaDetailUpdate extends MilkTeaAjaxPageBase {

	private static final long serialVersionUID = 1L;

	private int msg_id = 0;
	
	public MilkTeaDetailUpdate() {
	}
	
	@Override
	public String getContentType() {
		String charset = getContext().getRequest().getCharacterEncoding();

		if (charset == null) {
			return "text/html";
		} else {
			return "text/html; charset=" + charset;
		}
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!getContext().isPost()) {
			return false;
		}
		return true;
	}
	
	@Override
	public void onInit() {
		super.onInit();
		String strMsgId = getContext().getRequestParameter("msg_id");
		if (CommonMethod.isValidNumeric(strMsgId, 1, Integer.MAX_VALUE)) {
			msg_id = Integer.parseInt(strMsgId);
		}
	}
	
	@Override
	public void onPost() {
		MilkTeaSocketMessage msg = MilkTeaSocketMessage.valueOf(msg_id);
		if (msg == MilkTeaSocketMessage.INVALID) {
			return;
		}
		switch (msg) {
		case UPDATE_MENU_LIST:
			addModel("result", MilkTeaCommonFunc.getHtmlListMenu(getHostURL() + getContextPath(), getMessages()));
			break;
		case UPDATE_MENU_DETAIL:
		case UPDATE_ORDER_LIST:
			String strMenuId = getContext().getRequestParameter("menu_id");
			if (CommonMethod.isValidNumeric(strMenuId, 1, Long.MAX_VALUE)) {
				MenuInfo menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
				StringBuilder sb = new StringBuilder();
				if (msg == MilkTeaSocketMessage.UPDATE_MENU_DETAIL) {
					sb.append(MilkTeaCommonFunc.getHtmlMenuDetail(menuInfo, userInfo, getHostURL() + getContextPath(), getMessages()));
				}
				List<OrderInfo> listOrder = OrderService.getOrderInfoListByMenuId(menuInfo.getId());
				sb.append(MilkTeaCommonFunc.getHtmlListOrder(listOrder, menuInfo, userInfo, getHostURL() + getContextPath(), getMessages()));
				addModel("result", sb.toString());
			}
			break;
		default:
			break;
		}
	}
}
