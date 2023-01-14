package dev.boom.pages.milktea;

import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaSocketMessage;
import dev.boom.core.BoomSession;
import dev.boom.services.Menu;
import dev.boom.services.MenuService;
import dev.boom.services.Order;
import dev.boom.services.OrderService;
import dev.boom.services.UserService;

public class MilkTeaDetailUpdate extends MilkTeaAjaxPageBase {

	private static final long serialVersionUID = 1L;

	private int msg_id = 0;
	private int page = 1;
	
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
		BoomSession boomSession = getBoomSession();
		if (boomSession != null) {
			userInfo = UserService.getUserById(boomSession.getId());
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
		String strPage = getContext().getRequestParameter("page");
		if (CommonMethod.isValidNumeric(strPage, 1, Integer.MAX_VALUE)) {
			page = Integer.parseInt(strPage);
		}
	}
	
	@Override
	public void onPost() {
		MilkTeaSocketMessage msg = MilkTeaSocketMessage.valueOf(msg_id);
		if (msg == MilkTeaSocketMessage.INVALID) {
			return;
		}
		String contextPath = getHostURL() + getContextPath(); 
		switch (msg) {
		case UPDATE_MENU_LIST:
			addModel("result", MilkTeaCommonFunc.getHtmlListMenu(userInfo, contextPath, getMessages(), page));
			break;
		case UPDATE_MENU_DETAIL:
		case UPDATE_ORDER_LIST:
			String strMenuId = getContext().getRequestParameter("menu_id");
			if (CommonMethod.isValidNumeric(strMenuId, 1, Long.MAX_VALUE)) {
				Menu menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
				if (!menuInfo.isAvailableForUser(userInfo)) {
					return;
				}
				StringBuilder sb = new StringBuilder();
				if (msg == MilkTeaSocketMessage.UPDATE_MENU_DETAIL) {
					sb.append(MilkTeaCommonFunc.getHtmlMenuDetail(menuInfo, userInfo, contextPath, getMessages()));
				}
				List<Order> listOrder = OrderService.getOrderInfoListByMenuId(menuInfo.getId());
				sb.append(MilkTeaCommonFunc.getHtmlListOrder(listOrder, menuInfo, userInfo, contextPath, getMessages(), getWorldInfo()));
				addModel("result", sb.toString());
			}
			break;
		case UPDATE_SHOP_LIST:
			addModel("result", MilkTeaCommonFunc.getHtmlListShop(contextPath, getMessages(), page));
			break;
		default:
			break;
		}
	}
}
