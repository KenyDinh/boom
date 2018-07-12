package dev.boom.pages;

import java.util.List;

import dev.boom.common.enums.MainNavBarEnum;
import dev.boom.common.game.GameTypeEnum;

public class Game extends BoomMainPage {

	private static final long serialVersionUID = 1L;

	public Game() {
	}
	
	@Override
	public void onInit() {
		super.onInit();
	}
	
	@Override
	public void onRender() {
		super.onRender();
		StringBuilder sb = new StringBuilder();
		String contextPath = getHostURL() + getContextPath();
		List<GameTypeEnum> validGames = GameTypeEnum.listValidGame();
		int size = validGames.size();
		int maxGameperRow = 5;
		int rowNum = (size - 1) / maxGameperRow + 1;
		int left = 0;
		int rest = 0;
		int col;
		for (int i = 0; i < rowNum; i++) {
			left = size - i * maxGameperRow;
			if (left < maxGameperRow) {
				rest = (12 - left * 2) / 2;
				col = left;
			} else {
				rest = (12 - maxGameperRow * 2) / 2;
				col = maxGameperRow;
			}
			sb.append("<div class=\"row\" style=\"\">");
			if (rest > 0) {
				sb.append(String.format("<div class=\"col-sm-%d\"></div>", rest));
			}
			for (int j = 0; j < col; j++) {
				GameTypeEnum type = validGames.get(i * maxGameperRow + j);
				sb.append("<div class=\"col-sm-2\" style=\"position:relative;padding:0.5rem;\">");
				sb.append("<div style=\"background-color:white;\" class=\"border\">");
					if (getUserInfo() == null) {
						sb.append("<a href=\"javascript:void(0);\" data-toggle=\"modal\" data-target=\"#login-form-modal\" >");
						sb.append(String.format("<img src=\"%s\" style=\"width:100%%;\" class=\"\"/>", contextPath + "/" + type.getImage()));
						sb.append("</a>");
					} else {
						sb.append(String.format("<a href=\"%s\">", contextPath + "/" + type.getPage()));
						sb.append(String.format("<img src=\"%s\" style=\"width:100%%;\" class=\"\"/>", contextPath + "/" + type.getImage()));
						sb.append("</a>");
					}
					sb.append("<div class=\"font-weight-bold bg-success text-center\" data-toggle=\"tooltip\" data-placement=\"bottom\" style=\"text-overflow:ellipsis;overflow:hidden;white-space:nowrap;\" title=\"").append(getMessage(type.getLabel())).append("\">").append(getMessage(type.getLabel())).append("</div>");
				sb.append("</div>");
				sb.append("</div>");
			}
			if ((rest + col) * 2 < 12) {
				for (int k = 0; k < 12 - 2 * (rest + col); k++) {
					sb.append("<div class=\"col-sm-1\"></div>");
				}
			}
			if (rest > 0) {
				sb.append(String.format("<div class=\"col-sm-%d\"></div>", rest));
			}
			sb.append("</div>");
		}
		addModel("list_game", sb.toString());
	}

	@Override
	protected int getMenuBarIndex() {
		return MainNavBarEnum.GAME.getIndex();
	}
	
}
