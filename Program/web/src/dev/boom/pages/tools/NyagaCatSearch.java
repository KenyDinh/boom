package dev.boom.pages.tools;

import java.util.List;

import dev.boom.common.CommonHtmlFunc;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.FridayThemes;
import dev.boom.common.tools.ToolsEnum;
import dev.boom.pages.Tools;
import dev.boom.services.Card;
import dev.boom.services.Card.CardCost;
import dev.boom.services.Card.CardElement;
import dev.boom.services.Card.CardMilType;
import dev.boom.services.Card.CardRank;
import dev.boom.services.Card.JobType;
import dev.boom.services.CardService;

public class NyagaCatSearch extends Tools {

	private static final long serialVersionUID = 1L;
	private static final int MAX_VIEW_PER_PAGE = 20;

	private List<Card> cardList = null;
	private int page = 1;
	private int id = 0;
	private short dispNum = 0;
	private String cName = null;
	private String cSkill = null;
	private int cRank = 0;
	private int cRankStar = 0;
	private int cElem = 0;
	private int cCost = 0;
	private int cJob = 0;
	private int cMilType = 0;

	public NyagaCatSearch() {
		setDataTableFormat(true);
		initTheme(FridayThemes.BALL);
	}
	
	@Override
	public void onInit() {
		super.onInit();

		String strPage = getContext().getRequestParameter("page");
		if (strPage != null && CommonMethod.isValidNumeric(strPage, 1, Integer.MAX_VALUE)) {
			page = Integer.parseInt(strPage);
		}
		String strId = getContext().getRequestParameter("id");
		if (strId != null && CommonMethod.isValidNumeric(strId, 1, Integer.MAX_VALUE)) {
			id = Integer.parseInt(strId);
		}
		String strDispNum = getContext().getRequestParameter("disp");
		if (strDispNum != null && CommonMethod.isValidNumeric(strDispNum, 1, Short.MAX_VALUE)) {
			dispNum = Short.parseShort(strDispNum);
		}
		String strName = getContext().getRequestParameter("fname");
		if (strName != null && !strName.isEmpty()) {
			cName = strName;
		}
		String strSkill = getContext().getRequestParameter("skill");
		if (strSkill != null && !strSkill.isEmpty()) {
			strSkill = strSkill.trim().replace(",", " ").replaceAll("\\s+", "    ");
			if (strSkill.length() > 0) {
				cSkill = strSkill;
			}
		}
		String strRank = getContext().getRequestParameter("rank");
		if (strRank != null && CommonMethod.isValidNumeric(strRank, 1, Integer.MAX_VALUE)) {
			cRank = Integer.parseInt(strRank);
		}
		String strRankStart = getContext().getRequestParameter("star");
		if (strRankStart != null && CommonMethod.isValidNumeric(strRankStart, 1, Integer.MAX_VALUE)) {
			cRankStar = Integer.parseInt(strRankStart);
		}
		String strElem = getContext().getRequestParameter("element");
		if (strElem != null && CommonMethod.isValidNumeric(strElem, 1, Integer.MAX_VALUE)) {
			cElem = Integer.parseInt(strElem);
		}
		String strCost = getContext().getRequestParameter("cost");
		if (strCost != null && CommonMethod.isValidNumeric(strCost, 1, Integer.MAX_VALUE)) {
			cCost = Integer.parseInt(strCost);
		}
		String strJob = getContext().getRequestParameter("job");
		if (strJob != null && CommonMethod.isValidNumeric(strJob, 1, Integer.MAX_VALUE)) {
			cJob = Integer.parseInt(strJob);
		}
		String strMilType = getContext().getRequestParameter("miltype");
		if (strMilType != null && CommonMethod.isValidNumeric(strMilType, 1, Integer.MAX_VALUE)) {
			cMilType = Integer.parseInt(strMilType);
		}
		addModel("id", (id == 0 ? "" : id));
		addModel("disp", (dispNum == 0 ? "" : dispNum));
		addModel("fname", (cName == null ? "" : cName));
		addModel("skill", (cSkill == null ? "" : cSkill));
		addModel("rank", cRank);
		addModel("star", cRankStar);
		addModel("element", cElem);
		addModel("cost", cCost);
		addModel("job", cJob);
		addModel("miltype", cMilType);
	}

	@Override
	public void onGet() {
		super.onGet();

	}

	@Override
	public void onRender() {
		super.onRender();

		if (cardList == null) {
			cardList = CardService.getCardList(id, dispNum, cName, cElem, cRank, cRankStar, cCost, cJob, cMilType, cSkill);
		}
		int maxPage = (cardList.size() - 1) / MAX_VIEW_PER_PAGE + 1;
		page = Math.min(maxPage, page);
		int offset = (page - 1) * MAX_VIEW_PER_PAGE;
		String url = getUrlPatameter();
		if (cardList.size() > 0) {
			addModel("paging", CommonHtmlFunc.getPagination(url, page, maxPage, ""));
		}
		addModel("Input", renderInput());
		addModel("Content", renderTable(cardList.subList(offset, Math.min(offset + MAX_VIEW_PER_PAGE, cardList.size()))));
	}

	private String renderInput() {
		String contextPath = getHostURL() + getContextPath();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"modal fade\" id=\"cat-filter-modal\">");
		sb.append("<div class=\"modal-dialog modal-dialog-centered modal-md\" >");
		sb.append("<div class=\"modal-content\" >");
		// header
		sb.append("<div class=\"modal-header bg-primary\">");
		sb.append("<h4 class=\"modal-title\">").append("Filter Card").append("</h4>");
		sb.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>");
		sb.append("</div>");
		// body
		sb.append("<div class=\"modal-body\">");
		sb.append("<div id=\"cat-filter-form\">");
		int idx = 0;
		String content;
		
		// filter element
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		sb.append("<label class=\"font-weight-bold\" style=\"font-size:1.125rem;\">").append("Element").append("</label>");
		sb.append("</div></div>");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		for (CardElement element : CardElement.values()) {
			if (element == CardElement.NONE) {
				continue;
			}
			content = String.format("<img src=\"%s\" />", contextPath + "/img/nyaga_card/icon/" + element.getImgName());
			sb.append(String.format("<button class=\"cat-filter btn %s\" style=\"%s\" data=\"elem-%s\">%s</button>", (element.isValidFlag(cElem) ? "btn-info" : "btn-secondary"), (idx > 0 ? "margin-left:0.5em;" : ""), element.getMask(), content));
			idx++;
		}
		sb.append("</div></div>");
		sb.append("</div>");

		// filter job type
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		sb.append("<label class=\"font-weight-bold\" style=\"font-size:1.125rem;\">").append("Card Job").append("</label>");
		sb.append("</div></div>");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		idx = 0;
		for (JobType job : JobType.values()) {
			if (idx % 5 == 0) {
				sb.append("</div></div>");
				sb.append("<div class=\"row\" style=\"height:0.5em;\"></div>");
				sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
				idx = 0;
			}
			content = getMessage(job.getLabel());
			sb.append(String.format("<button class=\"cat-filter btn %s\" style=\"width:3.75em;%s\" data=\"job-%s\">%s</button>", (job.isValidFlag(cElem) ? "btn-info" : "btn-secondary"), (idx > 0 ? "margin-left:0.5em;" : ""), job.getMask(), content));
			idx++;
		}
		sb.append("</div></div>");
		sb.append("</div>");

		// filter card cost
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		sb.append("<label class=\"font-weight-bold\" style=\"font-size:1.125rem;\">").append("Card Cost").append("</label>");
		sb.append("</div></div>");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		idx = 0;
		for (CardCost cost : CardCost.values()) {
			if (cost == CardCost.NONE) {
				continue;
			}
			content = cost.getName();
			sb.append(String.format("<button class=\"cat-filter btn %s\" style=\"%s\" data=\"cost-%s\">%s</button>", (cost.isValidFlag(cElem) ? "btn-info" : "btn-secondary"), (idx > 0 ? "margin-left:0.5em;" : ""), cost.getMask(), content));
			idx++;
		}
		sb.append("</div></div>");
		sb.append("</div>");

		// filter card rank
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		sb.append("<label class=\"font-weight-bold\" style=\"font-size:1.125rem;\">").append("Card Rank").append("</label>");
		sb.append("</div></div>");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		idx = 0;
		for (CardRank rank : CardRank.values()) {
			if (rank == CardRank.INVALID) {
				continue;
			}
			content = String.format("<img src=\"%s\" style=\"position:absolute;top:0.625em;left:0.625em;\" />", contextPath + "/img/nyaga_card/icon/" + rank.getImgName());
			sb.append(String.format("<button class=\"cat-filter btn %s\" style=\"position:relative;width:3.125em;height:3.125em;%s\" data=\"rank-%s\">%s</button>", (rank.isValidFlag(cElem) ? "btn-info" : "btn-secondary"), (idx > 0 ? "margin-left:0.5em;" : ""), rank.getMask(), content));
			idx++;
		}
		sb.append("</div></div>");
		sb.append("</div>");
		
		// filter card rank star
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		sb.append("<label class=\"font-weight-bold\" style=\"font-size:1.125rem;\">").append("Rank Star").append("</label>");
		sb.append("</div></div>");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		for (int star = 0; star < 5; star ++) {
			boolean valid = ((cRankStar & (1 << star)) > 0);
			content = String.format("<span class=\"font-weight-bold\">%d</span>", (star + 1));
			sb.append(String.format("<button class=\"cat-filter btn %s\" style=\"position:relative;width:3.125em;height:3.125em;%s\" data=\"star-%s\">%s</button>", (valid ? "btn-info" : "btn-secondary"), (star > 0 ? "margin-left:0.5em;" : ""), (1 << star), content));
		}
		sb.append("</div></div>");
		sb.append("</div>");

		// filter card military type
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		sb.append("<label class=\"font-weight-bold\" style=\"font-size:1.125rem;\">").append("Card Military Type").append("</label>");
		sb.append("</div></div>");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		idx = 0;
		for (CardMilType milType : CardMilType.values()) {
			content = String.format("<img src=\"%s\" style=\"position:absolute;top:0.625em;left:0.625em;\" />", contextPath + "/img/nyaga_card/icon/" + milType.getImgName());
			sb.append(String.format("<button class=\"cat-filter btn %s\" style=\"position:relative;width:3.125em;height:3.125em;%s\" data=\"miltype-%s\">%s</button>", (milType.isValidFlag(cMilType) ? "btn-info" : "btn-secondary"), (idx > 0 ? "margin-left:0.5em;" : ""), milType.getMask(), content));
			idx++;
		}
		sb.append("</div></div>");
		sb.append("</div>");
		
		sb.append("</div>");
		sb.append("</div>");
		// footer
		sb.append("<div class=\"modal-footer\">");
		sb.append("<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\" style=\"width:50%;\">").append(messages.get("MSG_GENERAL_CLOSE")).append("</button>");
		sb.append("<button type=\"button\" class=\"btn btn-primary\" id=\"filter-card\" style=\"width:50%;\">").append(messages.get("MSG_GENERAL_OK")).append("</button>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");

		return sb.toString();
	}

	private String renderTable(List<Card> cardList) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table id=\"cat-seacrh-table\" class=\"table table-striped table-hover nowrap\" style=\"width:100%;\">");
		sb.append("<thead>");
		sb.append("<tr role=\"row\" class=\"text-success\">");
		sb.append("<th>").append("Id").append("</th>");
		sb.append("<th>").append("Display Num").append("</th>");
		sb.append("<th>").append("Img").append("</th>");
		sb.append("<th>").append("Busho Name").append("</th>");
		sb.append("<th>").append("Cat name").append("</th>");
		sb.append("<th>").append("Element").append("</th>");
		sb.append("<th>").append("Rank").append("</th>");
		sb.append("<th>").append("Military Type").append("</th>");
		sb.append("<th>").append("Cost").append("</th>");
		sb.append("<th>").append("Job").append("</th>");
		sb.append("<th>").append("Skill").append("</th>");
		sb.append("</tr>");
		sb.append("</thead>");

		sb.append("<tbody>");
		if (cardList != null && !cardList.isEmpty()) {
			for (Card card : cardList) {
				sb.append("<tr class='cat-data' role=\"row\" element='" + (card.getElement() + 1) + "'>");
				sb.append("<td>").append(card.getCardID()).append("</td>");
				sb.append("<td>").append(card.getCardDispNo()).append("</td>");
				sb.append("<td>").append("<img src='" + card.getImgSrc(getContextPath()) + "'>").append("</td>");
				sb.append("<td>").append(card.getName()).append("</td>");
				sb.append("<td>").append(card.getCatName()).append("</td>");
				sb.append("<td>").append("<img src='" + card.getElementImg(getContextPath()) + "'>").append("</td>");
				sb.append("<td>").append("<img src='" + card.getRarityImg(getContextPath()) + "'>").append("</td>");
				sb.append("<td>").append("<img src='" + card.getMilTypeImg(getContextPath()) + "'>").append("</td>");
				sb.append("<td>").append((float) card.getCost() / 2).append("</td>");
				sb.append("<td>").append(getMessage(Card.JobType.valueOf(card.getJob()).getLabel())).append("</td>");
				sb.append("<td>").append(card.getStringSkillName(getContextPath())).append("</td>");
				sb.append("</tr>");
			}
		}
		sb.append("</tbody>");
		if (cardList != null && cardList.size() > 10) {
			sb.append("<tfoot>");
			sb.append("<tr role=\"row\" class=\"text-success\">");
			sb.append("<th>").append("Id").append("</th>");
			sb.append("<th>").append("Display Num").append("</th>");
			sb.append("<th>").append("Img").append("</th>");
			sb.append("<th>").append("Busho Name").append("</th>");
			sb.append("<th>").append("Cat name").append("</th>");
			sb.append("<th>").append("Element").append("</th>");
			sb.append("<th>").append("Rank").append("</th>");
			sb.append("<th>").append("Military Type").append("</th>");
			sb.append("<th>").append("Cost").append("</th>");
			sb.append("<th>").append("Job").append("</th>");
			sb.append("<th>").append("Skill").append("</th>");
			sb.append("</tr>");
			sb.append("</tfoot>");
		}

		sb.append("</table>");
		return sb.toString();
	}

	private String getUrlPatameter() {
		StringBuilder sb = new StringBuilder();
		sb.append(getHostURL() + getPagePath(this.getClass()));
		sb.append("?id=").append(id);
		sb.append("&disp=").append(dispNum);
		sb.append("&fname=").append((cName == null ? "" : cName));
		sb.append("&skill=").append((cSkill == null ? "" : cSkill));
		sb.append("&element=").append(cElem);
		sb.append("&cost=").append(cCost);
		sb.append("&rank=").append(cRank);
		sb.append("&star=").append(cRankStar);
		sb.append("&job=").append(cJob);
		sb.append("&miltype=").append(cMilType);
		sb.append("&page=");
		return sb.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(importJs("/js/card/nyaga_cat_search.js"));
		headElements.add(importCss("/css/card/nyaga_cat_search.css"));

		return headElements;
	}
	
	protected int getToolsIndex() {
		return ToolsEnum.NYAGA.getIndex();
	}
}
