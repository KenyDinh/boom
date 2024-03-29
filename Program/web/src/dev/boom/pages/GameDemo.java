package dev.boom.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;

import dev.boom.common.CommonMethod;
import dev.boom.common.enums.FridayThemes;
import dev.boom.common.enums.MainNavBarEnum;
import dev.boom.services.DemoSession;
import dev.boom.services.DemoSessionContent;
import dev.boom.services.DemoSessionService;
import dev.boom.services.DemoSignupService;

public class GameDemo extends BoomMainPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isSubmitted = false;
	
	public GameDemo() {
		hideMenubar = true;
		initTheme(FridayThemes.NIGHTSKY);
	}
	
	@Override
	public void onInit() {
		super.onInit();
	}
	
	@Override
	public void onPost() {
		// TODO Auto-generated method stub
		super.onPost();
		
		String strUpdate = getContext().getRequestParameter("submit");
		if (CommonMethod.isValidNumeric(strUpdate, 1, 1)) {
			
			String speakerName = getContext().getRequestParameter("speaker-name");
			String gameName = getContext().getRequestParameter("game-name");
			String description = getContext().getRequestParameter("description");
			
			if (speakerName != null && gameName != null && description != null) {
				if (DemoSignupService.createNewSignup(gameName, speakerName, description)) {
					isSubmitted = true;
				}
			}
			
		}
	}
	
	@Override
	public void onRender() {
		super.onRender();
		
		List<DemoSession> allDemoSessionInfo = DemoSessionService.getDemoSessionList();
		List<DemoSession> scheduledSessionInfo = new ArrayList<DemoSession>();
		List<DemoSessionContent> scheduledSessionContent = new ArrayList<DemoSessionContent>();
		if (allDemoSessionInfo != null && allDemoSessionInfo.size() > 0) {
			Date now = new Date();
			DemoSession sessionToDisplay = null;
			DemoSession upcomingSession = null;
			Collections.sort(allDemoSessionInfo);
			
			for (DemoSession demoSessionInfo : allDemoSessionInfo) {
				if (demoSessionInfo.getDemoFinishTime().after(now)) {
					if (sessionToDisplay == null) {
						sessionToDisplay = demoSessionInfo;
					} else {
						if (upcomingSession == null) {
							upcomingSession = demoSessionInfo;
						}
						scheduledSessionInfo.add(demoSessionInfo);
						scheduledSessionContent.add(new Gson().fromJson(StringEscapeUtils.unescapeHtml(demoSessionInfo.getContent()), DemoSessionContent.class));
					} 
				}
			}
			
			if (sessionToDisplay != null) {
				DemoSessionContent sessionContent = new Gson().fromJson(StringEscapeUtils.unescapeHtml(sessionToDisplay.getContent()), DemoSessionContent.class);
				
				if (sessionContent != null && sessionContent.isValid()) {
					addModel("nextSession", sessionToDisplay);
					addModel("sessionContent", sessionContent.getContent());
				}
				
				if (upcomingSession != null) {
					DemoSessionContent upcomingContent = new Gson().fromJson(StringEscapeUtils.unescapeHtml(upcomingSession.getContent()), DemoSessionContent.class);
					if (upcomingContent != null && upcomingContent.isValid()) {
						addModel("upcomingContent", upcomingContent.getContent().get(0));
						addModel("upcomingDate", upcomingSession.getFormattedDemoTime());
						addModel("scheduledSessions", scheduledSessionInfo);
						addModel("scheduledContents", scheduledSessionContent);
					}
				}
			}
		}
		
		if (isSubmitted) {
			addModel("submitted", true);
		}
	}

	@Override
	protected int getMenuBarIndex() {
		return MainNavBarEnum.GAME_DEMO.getIndex();
	}

}
