package dev.boom.pages;

import java.util.ArrayList;
import java.util.List;

import dev.boom.common.CommonHtmlFunc;
import dev.boom.common.enums.FridayThemes;
import dev.boom.common.enums.MainNavBarEnum;
import dev.boom.common.tools.ToolsEnum;

public class Tools extends BoomMainPage {
	private static final long serialVersionUID = 1L;

	public Tools() {
		initTheme(FridayThemes.PARALLAX);
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (getToolsIndex() != 0 && !getWorldInfo().isActiveToolsFlag(getToolsIndex())) {
			setRedirect(Tools.class);
			return false;
		}
		if (getToolsIndex() != 0 && ToolsEnum.valueOf(getToolsIndex()).isRequireLogin()) {
			if (getUserInfo() == null) {
				setRedirect(Tools.class);
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onInit() {
		super.onInit();
	}

	@Override
	public void onRender() {
		super.onRender();
		if (getToolsIndex() != ToolsEnum.NONE.getIndex()) {
			return;
		}
		List<ToolsEnum> listAvailableTools = ToolsEnum.listValidGame(getWorldInfo());
		if (listAvailableTools == null || listAvailableTools.isEmpty()) {
			return;
		}
		String contextPath = getHostURL() + getContextPath();
		List<String> elements = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (ToolsEnum tool : listAvailableTools) {
			sb.setLength(0);
			sb.append("<div style=\"background-color:white;\" class=\"border\">");
			if (tool.isRequireLogin() && getUserInfo() == null) {
				sb.append("<a href=\"javascript:void(0);\" data-toggle=\"modal\" data-target=\"#login-form-modal\" >");
				sb.append(String.format("<img src=\"%s\" style=\"width:100%%;\" class=\"\"/>", contextPath + "/" + tool.getImage()));
				sb.append("</a>");
			} else {
				sb.append(String.format("<a href=\"%s\">", contextPath + "/" + tool.getPage()));
				sb.append(String.format("<img src=\"%s\" style=\"width:100%%;\" class=\"\"/>", contextPath + "/" + tool.getImage()));
				sb.append("</a>");
			}
			sb.append("<div class=\"font-weight-bold bg-success text-center\" data-toggle=\"tooltip\" data-placement=\"bottom\" style=\"text-overflow:ellipsis;overflow:hidden;white-space:nowrap;\" title=\"").append(getMessage(tool.getLabel())).append("\">").append(getMessage(tool.getLabel())).append("</div>");
			sb.append("</div>");
			elements.add(sb.toString());
		}
		addModel("list_tools", CommonHtmlFunc.getGridLayoutElement(elements, 5));
	}

	@Override
	protected int getMenuBarIndex() {
		return MainNavBarEnum.TOOLS.getIndex();
	}
	
	protected int getToolsIndex() {
		return ToolsEnum.NONE.getIndex();
	}
}
