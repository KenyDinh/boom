package dev.boom.pages.command;

import org.apache.commons.lang.StringUtils;

import dev.boom.pages.JsonPageBase;
import dev.boom.socket.func.PatpatCommandType;
import dev.boom.socket.func.PatpatIncomingMessage;

public class Patpat extends JsonPageBase{

	private static final long serialVersionUID = 1L;
	private static final String MATTERMOST_TOKEN_PATPAT = "a63brfeyajrhpesausys9hzgeo";

	private String channelId = null;
	private String channelName = null;
	private String username = null;
	private String message = null;
	private boolean error = false;
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		String token = getContext().getRequestParameter("token");
		if (StringUtils.isBlank(token)) {
			return false;
		}
		if (!token.equals(MATTERMOST_TOKEN_PATPAT)) {
			return false;
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		String strCId = getContext().getRequestParameter("channel_id");
		if (StringUtils.isBlank(strCId)) {
			error = true;
			return;
		}
		channelId = strCId;
		String strCName = getContext().getRequestParameter("channel_name");
		if (StringUtils.isBlank(strCName)) {
			error = true;
			return;
		}
		channelName = strCName;
		String strUserName = getContext().getRequestParameter("user_name");
		if (StringUtils.isBlank(strUserName)) {
			error = true;
			return;
		}
		username = strUserName;
		String strMessage = getContext().getRequestParameter("text");
		if (StringUtils.isBlank(strMessage)) {
			error = true;
			return;
		}
		message = strMessage;
	}

	@Override
	public void onPost() {
		super.onPost();
		if (error) {
			return;
		}
		PatpatIncomingMessage incomingMessage = new PatpatIncomingMessage();
		incomingMessage.setChannel(channelName);
		incomingMessage.setMessage(message);
		incomingMessage.setUsername(username);
		incomingMessage.setSlash(true);
		PatpatCommandType command = PatpatCommandType.getPatpatCommand(incomingMessage);
		if (!isAvailableSlashCommand(command)) {
			error = true;
			return;
		}
		command.processCommand(incomingMessage);
	}
	
	@Override
	public void onRender() {
		if (error) {
			return;
		}
		putJsonData("response_type", "in_channel");
		putJsonData("text", String.format("@%s has answered!", username));
		super.onRender();
	}
	
	private boolean isAvailableSlashCommand(PatpatCommandType command) {
		switch (command) {
		case QUIZ_OPTION_A:
		case QUIZ_OPTION_B:
		case QUIZ_OPTION_C:
		case QUIZ_OPTION_D:
		case QUIZ_OPTION_E:
		case QUIZ_OPTION_F:
			return true;
		default:
			return false;
		}
	}

}
