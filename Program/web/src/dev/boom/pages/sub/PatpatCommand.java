package dev.boom.pages.sub;

import org.apache.commons.lang.StringUtils;

import dev.boom.pages.PageBase;
import dev.boom.socket.func.PatpatCommandType;
import dev.boom.socket.func.PatpatIncomingMessage;

public class PatpatCommand extends PageBase {

	private static final long serialVersionUID = 1L;
	private static final String PATPAT_IDER = "6xNZP3cFW4JDpdZw"; 

	@Override
	public void onPost() {
		super.onPost();
		String strID = getContext().getRequestParameter("id"); 
		if (StringUtils.isBlank(strID) || !strID.equals(PATPAT_IDER)) {
			return;
		}
		String strChannel = getContext().getRequestParameter("channel");
		String strUsername = getContext().getRequestParameter("username");
		String strMessage = getContext().getRequestParameter("message");
		if (StringUtils.isBlank(strChannel) || StringUtils.isBlank(strUsername)) {
			return;
		}
		if (StringUtils.isBlank(strMessage)) {
			strMessage = "";
		}
		PatpatIncomingMessage pim = new PatpatIncomingMessage();
		pim.setChannel(strChannel);
		pim.setUsername(strUsername);
		pim.setMessage(strMessage);
		PatpatCommandType command = PatpatCommandType.getPatpatCommand(pim);
		command.processCommand(pim);
	}
	

}
