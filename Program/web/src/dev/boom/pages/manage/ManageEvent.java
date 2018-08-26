package dev.boom.pages.manage;

import java.util.Map;

import dev.boom.common.CommonMethod;
import dev.boom.common.enums.EventFlagEnum;
import dev.boom.core.GameLog;
import dev.boom.pages.Home;
import dev.boom.services.CommonDaoService;

public class ManageEvent extends ManagePageBase{

	private static final long serialVersionUID = 1L;

	public ManageEvent() {
	}

	@Override
	public boolean onSecurityCheck() {
		return super.onSecurityCheck();
	}
	
	@Override
	public void onInit() {
		super.onInit();
	}

	@Override
	public void onPost() {
		super.onPost();
		Map<String, String[]> params = getContext().getRequest().getParameterMap();
		worldInfo.setEventFlag(0);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				if (key.equals("event-flag")) {
					String[] values = params.get(key);
					for (String strEventId : values) {
						if (CommonMethod.isValidNumeric(strEventId, 1, Integer.MAX_VALUE)) {
							int eventId = Integer.parseInt(strEventId);
							EventFlagEnum eventFlagEnum = EventFlagEnum.valueOf(eventId);
							if (eventFlagEnum == EventFlagEnum.INVALID || !eventFlagEnum.isAvailable()) {
								continue;
							}
							worldInfo.addEventFlag(eventFlagEnum);
						}
					}
				}
			}
		}
		if (!CommonDaoService.update(worldInfo.getTblInfo())) {
			GameLog.getInstance().error("[ManageEvent] update failed!");
			return;
		}
	}
	
	@Override
	public void onRender() {
		super.onRender();
		StringBuilder sb = new StringBuilder();
		sb.append("<form id=\"form-event\" method=\"post\" action=\"" + getPagePath(this.getClass()) + "\">");
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\"><div class=\"col-lg-6\">");
		sb.append("<label class=\"font-weight-bold text-info\" style=\"font-size:1.125rem;\">").append("Events List").append("</label>");
		sb.append("</div></div>");
		for (EventFlagEnum event : EventFlagEnum.values()) {
			if (event == EventFlagEnum.INVALID || !event.isAvailable()) {
				continue;
			}
			int idx = event.getIndex();
			sb.append("<div class=\"row\">");
				sb.append("<div class=\"col-sm-12\">");
				sb.append("<div class=\"custom-control custom-checkbox\">");
					sb.append("<input type=\"checkbox\" class=\"custom-control-input\" id=\"event-" + idx + "\" name=\"event-flag\" value=\"" + idx + "\" " + (worldInfo.isActiveEventFlag(event) ? "checked" : "") + "/>");
					sb.append("<label class=\"custom-control-label text-success\" for=\"event-" + idx + "\">").append(getMessage(event.getLabel())).append("</label>");
				sb.append("</div>");
				sb.append("</div>");
			sb.append("</div>");
		}
		sb.append("</div>");
		sb.append("<button type=\"submit\" class=\"btn btn-primary\">").append(getMessage("MSG_GENERAL_SUBMIT")).append("</button>");
		sb.append("</form>");
		addModel("html", sb.toString());
		addBackLink(Home.class, "MSG_MAIN_NAV_BAR_HOME");
	}

}
