package dev.boom.socket;

import javax.websocket.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dev.boom.socket.func.PatpatCommandType;
import dev.boom.socket.func.PatpatFunc;
import dev.boom.socket.func.PatpatIncomingMessage;

public class PatpatSocketSession extends SocketSessionBase {
	
	private Log log = LogFactory.getLog(getClass());

	public PatpatSocketSession(Session session, String endPointName, String token) {
		super(session, endPointName, token);
	}

	@Override
	public void process(String message) {
		PatpatIncomingMessage incomingMessage = PatpatFunc.parseIncomingMessage(message);
		if (incomingMessage == null) {
			log.error("[PatpatSocketSession] invalid message: " + message);
			return;
		}
		PatpatCommandType command = PatpatCommandType.getPatpatCommand(incomingMessage);
		command.processCommand(incomingMessage);
	}

}
