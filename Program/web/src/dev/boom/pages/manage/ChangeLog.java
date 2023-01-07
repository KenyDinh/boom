package dev.boom.pages.manage;

import java.util.List;

import dev.boom.common.CommonHtmlFunc;
import dev.boom.common.CommonMethod;
import dev.boom.pages.Home;
import dev.boom.services.CommonDaoService;
import dev.boom.services.ManageLog;
import dev.boom.services.ManageLogService;
import dev.boom.tbl.info.TblManageLogInfo;

public class ChangeLog extends ManagePageBase {

	private static final long serialVersionUID = 1L;
	private static final int MAX_VIEW = 20;

	private int page = 1;
	@Override
	public void onRender() {
		super.onRender();
		addBackLink(Home.class, "MSG_MAIN_NAV_BAR_HOME");
		String strPage = getContext().getRequestParameter("page");
		if (CommonMethod.isValidNumeric(strPage, 1, Integer.MAX_VALUE)) {
			page = Integer.parseInt(strPage);
		}
		long total = CommonDaoService.count(new TblManageLogInfo());
		int maxPage = (int)((total - 1) / MAX_VIEW + 1);
		page = Math.min(page, maxPage);
		int offset = (page - 1) * MAX_VIEW;
		List<ManageLog> manageLogs = ManageLogService.getManageLogList("ORDER BY id DESC", MAX_VIEW, offset);
		addModel("manage_log", manageLogs);
		if (maxPage > 1) {
			addModel("paging", CommonHtmlFunc.getPagination(getHostURL() + getPagePath(this.getClass()) + "?page=", page, maxPage, ""));
		}
	}

	@Override
	protected int getTabIndex() {
		return 2;
	}
	
}
