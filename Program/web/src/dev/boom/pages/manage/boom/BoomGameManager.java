package dev.boom.pages.manage.boom;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonMethod;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.pages.game.Boom;
import dev.boom.pages.manage.ManagePageBase;
import dev.boom.services.BoomGroup;
import dev.boom.services.BoomGroupService;
import dev.boom.services.BoomPlayerStage;
import dev.boom.services.BoomPlayerStageService;
import dev.boom.services.BoomSeason;
import dev.boom.services.BoomSeasonService;
import dev.boom.services.BoomStage;
import dev.boom.services.BoomStageService;
import dev.boom.services.User;
import dev.boom.services.UserService;

public class BoomGameManager extends ManagePageBase {

	private static final long serialVersionUID = 1L;
	private static final int MODE_SEASON_UPDATE 			= 1;
	private static final int MODE_SEASON_DELETE 			= 2;
	private static final int MODE_SEASON_STAGE_UPDATE 		= 3;
	private static final int MODE_SEASON_STAGE_DELETE		= 4;
	private static final int MODE_PLAYER_STAGE_UPDATE		= 5;
	private static final int MODE_PLAYER_STAGE_DELETE		= 6;
	private static final int MODE_GROUP_UPDATE				= 7;
	private static final int MODE_GROUP_DELETE				= 8;
	
	private int mode;
	private BoomSeason boomSeason;
	private BoomStage boomStage;
	private BoomPlayerStage boomPlayerStage;
	private BoomGroup boomGroup;
	private List<BoomGroup> groupList;

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!userInfo.isGameAdmin()) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(importJs("/js/manage/manage_boom.js"));
		headElements.add(new JsImport("/js/lib/jquery.auto-complete.js"));
		headElements.add(new CssImport("/css/lib/jquery.auto-complete.css"));
		
		return headElements;
	}
	
	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		super.onInit();
		String strMode = getContext().getRequestParameter("mode");
		if (CommonMethod.isValidNumeric(strMode, 1, Integer.MAX_VALUE)) {
			mode = Integer.parseInt(strMode);
		}
		String strSeasonId = getContext().getRequestParameter("season_id");
		if (CommonMethod.isValidNumeric(strSeasonId, 1, Long.MAX_VALUE)) {
			boomSeason = BoomSeasonService.getBoomSeasonById(Long.parseLong(strSeasonId));
		}
		String strStageId = getContext().getRequestParameter("stage_id");
		if (CommonMethod.isValidNumeric(strStageId, 1, Long.MAX_VALUE)) {
			boomStage = BoomStageService.getBoomStage(Long.parseLong(strStageId));
		}
		String strPlayerStageId = getContext().getRequestParameter("pid");
		if (CommonMethod.isValidNumeric(strPlayerStageId, 1, Long.MAX_VALUE)) {
			boomPlayerStage = BoomPlayerStageService.getBoomPlayerStageById(Long.parseLong(strPlayerStageId));
		}
		String strGroupId = getContext().getRequestParameter("gid");
		if (CommonMethod.isValidNumeric(strGroupId, 1, Long.MAX_VALUE)) {
			boomGroup = BoomGroupService.getBoomGroup(Long.parseLong(strGroupId));
		}
		groupList = BoomGroupService.getBoomGroupListAll();
	}
	
	@Override
	public void onPost() {
		super.onPost();
		switch (mode) {
		case MODE_SEASON_UPDATE:
			checkUpdateSeason();
			break;
		case MODE_SEASON_DELETE:
			checkDeleteSeason();
			break;
		case MODE_SEASON_STAGE_UPDATE:
			checkUpdateStage();
			break;
		case MODE_SEASON_STAGE_DELETE:
			checkDeleteStage();
			break;
		case MODE_PLAYER_STAGE_UPDATE:
			checkUpdatePlayerStage();
			break;
		case MODE_PLAYER_STAGE_DELETE:
			checkDeletePlayerStage();
			break;
		case MODE_GROUP_UPDATE:
			checkUpdateGroup();
			break;
		case MODE_GROUP_DELETE:
			checkDeleteGroup();
			break;
		default:
			break;
		}
		Map<String, Object> params = new HashedMap<>();
		if (boomSeason != null) {
			params.put("season_id", boomSeason.getId());
		}
		if (boomStage != null) {
			params.put("stage_id", boomStage.getId());
		}
		setRedirect(this.getClass(), params);
	}
	
	private void checkUpdateSeason() {
		String strName = getContext().getRequestParameter("name");
		if (StringUtils.isBlank(strName.trim())) {
			return;
		}
		String strStart = getContext().getRequestParameter("start");
		if (!CommonMethod.isValidDateTimeFormat(strStart.trim())) {
			return;
		}
		String strEnd = getContext().getRequestParameter("end");
		if (!CommonMethod.isValidDateTimeFormat(strEnd.trim())) {
			return;
		}
		if (boomSeason == null) {
			boomSeason = new BoomSeason();
		}
		boomSeason.setName(strName.trim());
		boomSeason.setStartTime(strStart.trim());
		boomSeason.setEndTime(strEnd.trim());
		if (boomSeason.getBoomSeasonInfo().isInsert()) {
			CommonDaoFactory.Insert(boomSeason.getBoomSeasonInfo());
		} else {
			CommonDaoFactory.Update(boomSeason.getBoomSeasonInfo());
		}
	}
	
	private void checkDeleteSeason() {
		return;
		// not allow delete
	}
	
	private void checkUpdateStage() {
		if (boomSeason == null) {
			return;
		}
		String strName = getContext().getRequestParameter("name");
		if (StringUtils.isBlank(strName.trim())) {
			return;
		}
		if (boomStage == null) {
			boomStage = new BoomStage();
			boomStage.setSeasonId(boomSeason.getId());
		}
		boomStage.setName(strName.trim());
		if (boomStage.getBoomStageInfo().isInsert()) {
			CommonDaoFactory.Insert(boomStage.getBoomStageInfo());
		} else {
			CommonDaoFactory.Update(boomStage.getBoomStageInfo());
		}
	}
	
	private void checkDeleteStage() {
		if (boomStage == null) {
			return;
		}
		List<BoomPlayerStage> playerStageList = BoomPlayerStageService.getBoomPlayerStageListAll(String.format("WHERE stage_id = %d", boomStage.getId()));
		CommonDaoFactory.functionTransaction((Connection conn) -> {
			if (CommonDaoFactory.Delete(boomStage.getBoomStageInfo()) < 0) {
				return false;
			}
			if (playerStageList != null) {
				for (BoomPlayerStage playerStage : playerStageList) {
					if (CommonDaoFactory.Delete(playerStage.getBoomPlayerStageInfo()) < 0) {
						return false;
					}
				}
			}
			return true;
		});
	}
	
	private void checkUpdatePlayerStage() {
		if (boomStage == null) {
			return;
		}
		String strPlayerName = getContext().getRequestParameter("username");
		if (StringUtils.isBlank(strPlayerName)) {
			return;
		}
		String strGroupId = getContext().getRequestParameter("group_id");
		if (!CommonMethod.isValidNumeric(strGroupId, 0, Long.MAX_VALUE)) {
			return;
		}
		User player = UserService.getUserByName(strPlayerName.trim());
		if (player == null) {
			return;
		}
		long groupID = Long.parseLong(strGroupId);
		BoomGroup group = (groupID > 0 ?BoomGroupService.getBoomGroup(Long.parseLong(strGroupId)) : null);
		if (boomPlayerStage == null) {
			boomPlayerStage = new BoomPlayerStage();
			boomPlayerStage.setStageId(boomStage.getId());
		}
		boomPlayerStage.setPlayerId(player.getId());
		boomPlayerStage.setPlayerName(StringEscapeUtils.unescapeHtml(player.getUsername()));
		if (group == null) {
			boomPlayerStage.setGroupId(0);
		} else {
			boomPlayerStage.setGroupId(group.getId());
		}
		if (boomPlayerStage.getBoomPlayerStageInfo().isInsert()) {
			CommonDaoFactory.Insert(boomPlayerStage.getBoomPlayerStageInfo());
		} else {
			CommonDaoFactory.Update(boomPlayerStage.getBoomPlayerStageInfo());
		}
	}
	
	private void checkDeletePlayerStage() {
		if (boomPlayerStage == null) {
			return;
		}
		CommonDaoFactory.Delete(boomPlayerStage.getBoomPlayerStageInfo());
	}
	
	private void checkUpdateGroup() {
		String strName = getContext().getRequestParameter("name");
		if (StringUtils.isBlank(strName.trim())) {
			return;
		}
		if (boomGroup == null) {
			boomGroup = new BoomGroup();
		}
		boomGroup.setName(strName.trim());
		if (boomGroup.getBoomGroupInfo().isInsert()) {
			CommonDaoFactory.Insert(boomGroup.getBoomGroupInfo());
		} else {
			CommonDaoFactory.Update(boomGroup.getBoomGroupInfo());
		}
	}
	
	private void checkDeleteGroup() {
		if (boomGroup == null) {
			return;
		}
		List<BoomPlayerStage> playerStageList = BoomPlayerStageService.getBoomPlayerStageListAll(String.format("WHERE group_id = %d", boomGroup.getId()));
		CommonDaoFactory.functionTransaction((Connection conn) -> {
			if (CommonDaoFactory.Delete(boomGroup.getBoomGroupInfo()) < 0) {
				return false;
			}
			if (playerStageList != null) {
				for (BoomPlayerStage playerStage : playerStageList) {
					playerStage.setGroupId(0);
					if (CommonDaoFactory.Update(playerStage.getBoomPlayerStageInfo()) < 0) {
						return false;
					}
				}
			}
			return true;
		});
	}

	@Override
	public void onRender() {
		// TODO Auto-generated method stub
		super.onRender();
		switch (mode) {
		case MODE_SEASON_UPDATE:
			renderUpdateSeason();
			addBackLink(this.getClass(), null);
			break;
		case MODE_SEASON_STAGE_UPDATE:
			renderUpdateStage();
			addBackLink(this.getClass(), null);
			break;
		case MODE_PLAYER_STAGE_UPDATE:
			renderUpdatePlayerStage();
			addBackLink(this.getClass(), null);
			break;
		case MODE_GROUP_UPDATE:
			renderUpdateGroup();
			addBackLink(this.getClass(), null);
			break;
		default:
			renderBoomGroupList();
			renderBoomSeasonList();
			renderBoomStageList();
			renderPlayerStageList();
			addBackLink(Boom.class, null);
			break;
		}
	}
	
	private void renderUpdateSeason() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='form-container'>");
		sb.append("<label class='form-label'>Boom season form</label>");
		sb.append(String.format("<form action='%s' method='post'>", getPagePath(this.getClass())));
		sb.append(String.format("<input type='hidden' name='mode' value='%d'>", MODE_SEASON_UPDATE));
		if (boomSeason != null) {
			sb.append(String.format("<input type='hidden' name='season_id' value='%d'>", boomSeason.getId()));
		}
		sb.append("<div class='form-group'>");
		sb.append("<fieldset>");
		sb.append("<label class='control-label' for='name-field'>Name:</label>");
		sb.append(String.format("<input type='text' class='form-control' id='name-field' name='name' value='%s'>", (boomSeason == null ? "" : boomSeason.getName())));
		sb.append("</fieldset>");
		sb.append("</div>");
		sb.append("<div class='form-group'>");
		sb.append("<fieldset>");
		sb.append("<label class='control-label' for='start-field'>Start time:</label>");
		sb.append(String.format("<input type='text' class='form-control' id='start-field' name='start' value='%s'>", (boomSeason == null ? CommonMethod.getFormatStringNow() : boomSeason.getStartTime())));
		sb.append("</fieldset>");
		sb.append("</div>");
		sb.append("<div class='form-group'>");
		sb.append("<fieldset>");
		sb.append("<label class='control-label' for='end-field'>End time:</label>");
		sb.append(String.format("<input type='text' class='form-control' id='end-field' name='end' value='%s'>", (boomSeason == null ? CommonMethod.getFormatStringNow() : boomSeason.getEndTime())));
		sb.append("</fieldset>");
		sb.append("</div>");
		sb.append("<div class='d-flex'>");
		sb.append(String.format("<button type='submit' class='btn btn-primary'>%s</button>", (boomSeason == null ? "Submit" : "Update")));
		if (boomSeason != null) {
			sb.append(String.format("<button type='button' class='btn btn-danger ml-auto delete-button' data-mode='%d'>Delete</button>", MODE_SEASON_DELETE));
		}
		sb.append("</div>");
		sb.append("</form>");
		sb.append("</div>");
		addModel("seasonForm", sb.toString());
	}
	
	private void renderUpdateStage() {
		if (boomSeason == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='form-container'>");
		sb.append("<label class='form-label'>Season stage form</label>");
		sb.append(String.format("<form action='%s?season_id=%d' method='post'>", getPagePath(this.getClass()), boomSeason.getId()));
		sb.append(String.format("<input type='hidden' name='mode' value='%d'>", MODE_SEASON_STAGE_UPDATE));
		if (boomStage != null) {
			sb.append(String.format("<input type='hidden' name='stage_id' value='%d'>", boomStage.getId()));
		}
		sb.append("<div class='form-group'>");
		sb.append("<fieldset>");
		sb.append("<label class='control-label' for='name-field'>Name:</label>");
		sb.append(String.format("<input type='text' class='form-control' id='name-field' name='name' value='%s'>", (boomStage == null ? "" : boomStage.getName())));
		sb.append("</fieldset>");
		sb.append("</div>");
		sb.append("<div class='d-flex'>");
		sb.append(String.format("<button type='submit' class='btn btn-primary'>%s</button>", (boomStage == null ? "Submit" : "Update")));
		if (boomStage != null) {
			sb.append(String.format("<button type='button' class='btn btn-danger ml-auto delete-button' data-mode='%d'>Delete</button>", MODE_SEASON_STAGE_DELETE));
		}
		sb.append("</div>");
		sb.append("</form>");
		sb.append("</div>");
		addModel("stageForm", sb.toString());
	}
	
	private void renderUpdatePlayerStage() {
		if (boomSeason == null || boomStage == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='form-container'>");
		sb.append("<label class='form-label'>Player stage form</label>");
		sb.append(String.format("<form action='%s?season_id=%d&amp;stage_id=%d' method='post'>", getPagePath(this.getClass()), boomSeason.getId(), boomStage.getId()));
		sb.append(String.format("<input type='hidden' name='mode' value='%d'>", MODE_PLAYER_STAGE_UPDATE));
		if (boomPlayerStage != null) {
			sb.append(String.format("<input type='hidden' name='pid' value='%d'>", boomPlayerStage.getId()));
		}
		sb.append("<div class='form-group'>");
		sb.append("<fieldset>");
		sb.append("<label class='control-label' for='username-field'>Player:</label>");
		sb.append(String.format("<input type='text' class='form-control' id='username-field' name='username' value='%s'>", (boomPlayerStage == null ? "" : boomPlayerStage.getPlayerName())));
		sb.append("</fieldset>");
		sb.append("</div>");
		sb.append("<div class='form-group'>");
		sb.append("<fieldset>");
		sb.append("<label class='control-label' for='group-field'>Group:</label>");
		sb.append("<select id='group-field' name='group_id' class='form-control'>");
		sb.append("<option value='0'>none</option>");
		if (groupList != null) {
			for (BoomGroup group : groupList) {
				sb.append(String.format("<option value='%d' %s>%s</option>", group.getId(), ((boomPlayerStage != null && boomPlayerStage.getGroupId() == group.getId()) ? "selected" : ""), group.getName()));
			}
		}
		sb.append("</select>");
		sb.append("</fieldset>");
		sb.append("</div>");
		sb.append("<div class='d-flex'>");
		sb.append(String.format("<button type='submit' class='btn btn-primary'>%s</button>", (boomPlayerStage == null ? "Submit" : "Update")));
		if (boomPlayerStage != null) {
			sb.append(String.format("<button type='button' class='btn btn-danger ml-auto delete-button' data-mode='%d'>Delete</button>", MODE_PLAYER_STAGE_DELETE));
		}
		sb.append("</div>");
		sb.append("</form>");
		sb.append("</div>");
		addModel("playerStageForm", sb.toString());
	}
	
	private void renderUpdateGroup() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='form-container'>");
		sb.append("<label class='form-label'>Group form</label>");
		sb.append(String.format("<form action='%s' method='post'>", getPagePath(this.getClass())));
		sb.append(String.format("<input type='hidden' name='mode' value='%d'>", MODE_GROUP_UPDATE));
		if (boomGroup != null) {
			sb.append(String.format("<input type='hidden' name='gid' value='%d'>", boomGroup.getId()));
		}
		sb.append("<div class='form-group'>");
		sb.append("<fieldset>");
		sb.append("<label class='control-label' for='name-field'>Name:</label>");
		sb.append(String.format("<input type='text' class='form-control' id='name-field' name='name' value='%s'>", (boomGroup == null ? "" : boomGroup.getName())));
		sb.append("</fieldset>");
		sb.append("</div>");
		sb.append("<div class='d-flex'>");
		sb.append(String.format("<button type='submit' class='btn btn-primary'>%s</button>", (boomGroup == null ? "Submit" : "Update")));
		if (boomGroup != null) {
			sb.append(String.format("<button type='button' class='btn btn-danger ml-auto delete-button' data-mode='%d'>Delete</button>", MODE_GROUP_DELETE));
		}	
		sb.append("</div>");
		sb.append("</form>");
		sb.append("</div>");
		addModel("groupForm", sb.toString());
	}
	
	private void renderBoomGroupList() {
		StringBuilder sb = new StringBuilder();
		if (groupList == null) {
			groupList = new ArrayList<>();
		}
		sb.append("<div class='group-container'>");
		sb.append("<label class='table-label'>Group list</label>");
		sb.append("<table class=' table group-table'>");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>");
			sb.append("Name");
		sb.append("</th>");
		sb.append("<th>");
			sb.append(" ");
		sb.append("</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
			for (BoomGroup group : groupList) {
				sb.append("<tr>");
				sb.append("<td>");
					sb.append(group.getName());
				sb.append("</td>");
				sb.append("<td class='action-col'>");
				sb.append(String.format("<a href='%s?gid=%d&amp;mode=%d'>Edit</a>", getPagePath(this.getClass()), group.getId(), MODE_GROUP_UPDATE));
				sb.append("</td>");
				sb.append("</tr>");
			}
			sb.append("<tr>");
			sb.append("<td></td>");
			sb.append("<td class='action-col'>");
			sb.append(String.format("<a href='%s?mode=%d'><i class='fas fa-plus-square'></i></a>", getPagePath(this.getClass()), MODE_GROUP_UPDATE));
			sb.append("</td>");
			sb.append("</tr>");
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		addModel("groupTable", sb.toString());
	}
	
	private void renderBoomSeasonList() {
		List<BoomSeason> boomSeasonList = BoomSeasonService.getBoomSeasonListAll();
		StringBuilder sb = new StringBuilder();
		if (boomSeasonList == null) {
			boomSeasonList = new ArrayList<>();
		}
		sb.append("<div class='season-container'>");
		sb.append("<label class='table-label'>Boom season list</label>");
		sb.append("<table class=' table season-table'>");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>");
			sb.append("Name");
		sb.append("</th>");
		sb.append("<th>");
			sb.append("Start Time");
		sb.append("</th>");
		sb.append("<th>");
			sb.append("End Time");
		sb.append("</th>");
		sb.append("<th>");
			sb.append(" ");
		sb.append("</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
			for (BoomSeason season : boomSeasonList) {
				if (boomSeason != null && boomSeason.getId() == season.getId()) {
					sb.append("<tr class='row-selected'>");
				} else {
					sb.append("<tr>");
				}
				sb.append("<td>");
					sb.append(String.format("<a href='%s?season_id=%d'>%s</a>", getPagePath(this.getClass()), season.getId(), season.getName()));
				sb.append("</td>");
				sb.append("<td>");
					sb.append(season.getStartTime());
				sb.append("</td>");
				sb.append("<td>");
					sb.append(season.getEndTime());
				sb.append("</td>");
				sb.append("<td class='action-col'>");
				sb.append(String.format("<a href='%s?season_id=%d&amp;mode=%d'>Edit</a>", getPagePath(this.getClass()), season.getId(), MODE_SEASON_UPDATE));
				sb.append("</td>");
				sb.append("</tr>");
			}
			sb.append("<tr>");
			sb.append("<td></td>");
			sb.append("<td></td>");
			sb.append("<td></td>");
			sb.append("<td class='action-col'>");
			sb.append(String.format("<a href='%s?mode=%d'><i class='fas fa-plus-square'></i></a>", getPagePath(this.getClass()), MODE_SEASON_UPDATE));
			sb.append("</td>");
			sb.append("</tr>");
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		addModel("seasonTable", sb.toString());
	}
	
	private void renderBoomStageList() {
		if (boomSeason == null) {
			return;
		}
		List<BoomStage> boomStageList = BoomStageService.getBoomStageListAll(String.format("WHERE season_id = %d", boomSeason.getId()));
		StringBuilder sb = new StringBuilder();
		if (boomStageList == null) {
			boomStageList = new ArrayList<>();
		}
		sb.append("<div class='stage-container'>");
		sb.append("<label class='table-label'>Season stage list</label>");
		sb.append("<table class='table stage-table'>");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>");
			sb.append("Name");
		sb.append("</th>");
		sb.append("<th>");
			sb.append(" ");
		sb.append("</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
			for (BoomStage stage : boomStageList) {
				if (boomStage != null && boomStage.getId() == stage.getId()) {
					sb.append("<tr class='row-selected'>");
				} else {
					sb.append("<tr>");
				}
				sb.append("<td>");
					sb.append(String.format("<a href='%s?season_id=%d&amp;stage_id=%d'>%s</a>", getPagePath(this.getClass()), boomSeason.getId(), stage.getId(), stage.getName()));
				sb.append("</td>");
				sb.append("<td class='action-col'>");
					sb.append(String.format("<a href='%s?season_id=%d&amp;stage_id=%d&amp;mode=%d'>Edit</a>", getPagePath(this.getClass()), boomSeason.getId(), stage.getId(), MODE_SEASON_STAGE_UPDATE));
				sb.append("</td>");
				sb.append("</tr>");
			}
			sb.append("<tr>");
			sb.append("<td></td>");
			sb.append("<td class='action-col'>");
			sb.append(String.format("<a href='%s?season_id=%d&amp;mode=%d'><i class='fas fa-plus-square'></i></a>", getPagePath(this.getClass()), boomSeason.getId(), MODE_SEASON_STAGE_UPDATE));
			sb.append("</td>");
			sb.append("</tr>");
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		addModel("stageTable", sb.toString());
	}
	
	private void renderPlayerStageList() {
		if (boomSeason == null || boomStage == null) {
			return;
		}
		List<BoomPlayerStage> playerStageList = BoomPlayerStageService.getBoomPlayerStageListAll(String.format("WHERE stage_id = %d", boomStage.getId()));
		StringBuilder sb = new StringBuilder();
		if (playerStageList == null) {
			playerStageList = new ArrayList<>();
		}
		Map<Long, String> mapGroupIdName = new HashedMap<>();
		if (groupList != null && !groupList.isEmpty()) {
			for (BoomGroup group : groupList) {
				mapGroupIdName.put(group.getId(), group.getName());
			}
		}
		sb.append("<div class='player-stage-container'>");
		sb.append("<label class='table-label'>Player stage list</label>");
		sb.append("<table class='table player-stage-table'>");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>");
			sb.append("Name");
		sb.append("</th>");
		sb.append("<th>");
			sb.append("Group");
		sb.append("</th>");
		sb.append("<th>");
			sb.append(" ");
		sb.append("</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
			for (BoomPlayerStage playerStage : playerStageList) {
				sb.append("<tr>");
				sb.append("<td>");
				sb.append(playerStage.getPlayerName());
				sb.append("</td>");
				sb.append("<td>");
				if (mapGroupIdName.containsKey(playerStage.getGroupId())) {
					sb.append(mapGroupIdName.get(playerStage.getGroupId()));
				} else {
					sb.append("");
				}
				sb.append("</td>");
				sb.append("<td class='action-col'>");
				sb.append(String.format("<a href='%s?season_id=%d&amp;stage_id=%d&amp;pid=%d&amp;mode=%d'>Edit</a>", getPagePath(this.getClass()), boomSeason.getId(), boomStage.getId(), playerStage.getId(), MODE_PLAYER_STAGE_UPDATE));
				sb.append("</td>");
				sb.append("</tr>");
			}
			sb.append("<tr>");
			sb.append("<td></td>");
			sb.append("<td></td>");
			sb.append("<td class='action-col'>");
			sb.append(String.format("<a href='%s?season_id=%d&amp;stage_id=%d&amp;mode=%d'><i class='fas fa-plus-square'></i></a>", getPagePath(this.getClass()), boomSeason.getId(), boomStage.getId(), MODE_PLAYER_STAGE_UPDATE));
			sb.append("</td>");
			sb.append("</tr>");
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		addModel("playerStageTable", sb.toString());
	}
	
	@Override
	protected int getTabIndex() {
		// TODO Auto-generated method stub
		return 1;
	}
}
