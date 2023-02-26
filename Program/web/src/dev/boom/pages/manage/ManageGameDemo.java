package dev.boom.pages.manage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.pages.GameDemo;
import dev.boom.pages.Home;
import dev.boom.services.DemoSession;
import dev.boom.services.DemoSessionContent;
import dev.boom.services.DemoSessionContent.DemoSessionSingleContent;
import dev.boom.services.DemoSessionService;

public class ManageGameDemo extends ManagePageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int IS_UPDATE = 1;
	private static final int IS_DELETE = 2;
	
	private boolean isUpdate = false;
	private int updateOption = 0;
	
	public ManageGameDemo() {
		setDataTableFormat(true);
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			setRedirect(Home.class);
			return false;
		}
		if (userInfo == null || !userInfo.isAdministrator()) {
			setRedirect(Home.class);
			return false;
		}
		
		return true;
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		super.onInit();
		
		String strUpdate = getContext().getRequestParameter("update");
		if (CommonMethod.isValidNumeric(strUpdate, 1, 2)) {
			isUpdate = true;
			updateOption = Integer.parseInt(strUpdate);
		}
	}
	
	@Override
	public void onPost() {
		// TODO Auto-generated method stub
		super.onPost();
		if (isUpdate && updateOption == IS_UPDATE) {
			String demoId = getContext().getRequestParameter("demo-id");
			String demoLocation = getContext().getRequestParameter("demo-location");
			String demoTime = getContext().getRequestParameter("demo-time");
			String demoContent = getContext().getRequestParameter("demo-content");
			
			int intDemoId = 0;
			if (demoId != null && demoLocation != null && demoTime != null && demoContent != null) {
				intDemoId = Integer.parseInt(demoId);
				
				try {
					Date demoDate = new SimpleDateFormat(CommonDefine.GAME_DEMO_DATE_FORMAT).parse(demoTime);
					
					if (intDemoId > 0) {
						DemoSession currentDemoSessionInfo = DemoSessionService.getDemoSessionById(intDemoId);
						if (currentDemoSessionInfo != null) {
							currentDemoSessionInfo.updateValue(demoLocation, demoDate, demoContent);
						}
						
						DemoSessionService.update(currentDemoSessionInfo);
					} else {
						DemoSessionService.createNewSession(demoLocation, demoDate, demoContent);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		setRedirect(ManageGameDemo.class);
	}

	@Override
	public void onGet() {
		super.onGet();
		addBackLink(GameDemo.class, "MSG_MAIN_NAV_BAR_GAMEDEMO");
		
		if (!isUpdate) {
			initGameDemoTable(DemoSessionService.getDemoSessionList());
		} else {
			if (updateOption == IS_UPDATE) {
				String demoId = getContext().getRequestParameter("demo-id");
				int intDemoId = 0;
				if (demoId != null) {
					intDemoId = Integer.parseInt(demoId);
				}
				if (intDemoId > 0) {
					DemoSession currentDemoSessionInfo = DemoSessionService.getDemoSessionById(intDemoId);
					if (currentDemoSessionInfo != null) {
						addModel("demoId", intDemoId);
						addModel("demoTime", currentDemoSessionInfo.getFormattedForJSDemoTime());
						addModel("demoLocation", currentDemoSessionInfo.getDemoLocation());
						addModel("demoContent", currentDemoSessionInfo.getContent());
					} else {
						addModel("demoId", 0);
					}
				} else {
					addModel("demoId", 0);
				}
			}
			
			if (updateOption == IS_DELETE) {
				String demoId = getContext().getRequestParameter("demo-id");
				int intDemoId = 0;
				if (demoId != null) {
					intDemoId = Integer.parseInt(demoId);
				}
				if (intDemoId > 0) {
					DemoSession currentDemoSessionInfo = DemoSessionService.getDemoSessionById(intDemoId);
					if (currentDemoSessionInfo != null) {
						DemoSessionService.delete(currentDemoSessionInfo);
					}
				}
				setRedirect(ManageGameDemo.class);
			}
		}
	}
	
	private void initGameDemoTable(List<DemoSession> demoSessionList) {
		StringBuilder gameDemoContainer = new StringBuilder();
			
		String addBtn = String.format("<a href=\"%s\"><button class=\"btn btn-info\" style=\"text-align: center; margin-bottom: 20px; box-shadow: 5px 5px 5px 0px rgba(0,0,0,0.75);\">"
				+ "Add</button></a>", getPagePath(this.getClass()) + "?update=1&demo_id=0");
			
		gameDemoContainer.append(addBtn);
		gameDemoContainer.append("<div style=\"width: 90%; margin: auto;\">");
		if (demoSessionList != null && demoSessionList.size() > 0) {
			Collections.sort(demoSessionList);
			Collections.reverse(demoSessionList);
			int count = demoSessionList.size();
			for (DemoSession demoSessionInfo : demoSessionList) {
				gameDemoContainer.append(getGameDemoInfo(demoSessionInfo, count));
				count--;
			}
		}
		gameDemoContainer.append("</div>");
		
		addModel("gameDemo", gameDemoContainer.toString());
	}
	
	private String getGameDemoInfo(DemoSession info, int count) {
		StringBuilder gameDemoBox = new StringBuilder();
		
		DemoSessionContent sessionContent = new Gson().fromJson(StringEscapeUtils.unescapeHtml(info.getContent()), DemoSessionContent.class);
		List<DemoSessionSingleContent> contentList = null;
		if (sessionContent != null && sessionContent.isValid()) {
			contentList = sessionContent.getContent();
		}
		
		String editBtn = String.format("<a href=\"%s\"><button class=\"btn btn-info\" style=\"text-align: center; float: right; box-shadow: 1px 1px 5px 0px rgba(0,0,0,0.75);\">"
				+ "Edit</button></a>", getPagePath(this.getClass()) + "?update=1&demo-id=" + info.getId());
		String paddingDiv = "<div style=\"height: 10px; width: 10px; float: right; font-size: 0px;\">.</div>";
		String deleteBtn = String.format("<a href=\"%s\"><button class=\"btn btn-info\" style=\"text-align: center; float: right; box-shadow: 1px 1px 5px 0px rgba(0,0,0,0.75);\">"
				+ "Delete</button></a>", getPagePath(this.getClass()) + "?update=2&demo-id=" + info.getId());
		
		gameDemoBox.append("<div class=\"card bg-primary mb-3\" style=\"width: 100%; box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);\">");
		gameDemoBox.append("<div class=\"card-header bg-light mb-3\">"
				+ "<h5 style=\"float: left;\">" + count + ". " + info.getDemoLocation() + " " + info.getFormattedDemoTime() + "</h5>" 
				+ editBtn + paddingDiv + deleteBtn + "</div>");
		gameDemoBox.append("<div class=\"card-body text-white\" style=\"flex-direction: row;\">");
		if (contentList != null) {
			for (DemoSessionSingleContent demoSessionSingleContent : contentList) {
				gameDemoBox.append("<div class=\"card\" style=\"width: 250px; float: left; padding: 5px; border: 5px solid #375a7f;\">");
				gameDemoBox.append("<img class=\"card-img-top\" src=\"" + demoSessionSingleContent.getImageUrl() +"\" width=\"250\" height=\"250\">");
				gameDemoBox.append("<div class=\"card-body\"><p class=\"card-text\">" + demoSessionSingleContent.getName()
				+ " by " + demoSessionSingleContent.getSpeaker() + "</p></div>");
				gameDemoBox.append("</div>");
			}
		}
		gameDemoBox.append("</div>");
		gameDemoBox.append("</div>");
		return gameDemoBox.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		headElements = super.getHeadElements();
		if (headElements != null) {
			headElements.add(new CssImport("/css/lib/bootstrap-datetimepicker.min.css"));
			headElements.add(new JsImport("/js/lib/bootstrap-datetimepicker.min.js"));
			headElements.add(new JsImport("/js/manage/game_demo.js"));
		}
		return headElements;
	}

}
