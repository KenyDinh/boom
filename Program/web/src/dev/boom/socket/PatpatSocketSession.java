package dev.boom.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import dev.boom.socket.func.PatpatCommandType;
import dev.boom.socket.func.PatpatFunc;
import dev.boom.socket.func.PatpatIncomingMessage;
import net.arnx.jsonic.JSON;

public class PatpatSocketSession extends SocketSessionBase {
	
	private Log log = LogFactory.getLog(getClass());
	private static final String WEBHOOK_URL = "http://192.168.89.168:8065/hooks/n7151ntg6tf4xqfgy7khx9qysa";

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

	private void sendPostMessage(String jsondata) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(WEBHOOK_URL);
		StringEntity entity = new StringEntity(jsondata, ContentType.APPLICATION_JSON);
		post.setEntity(entity);
		try {
			HttpResponse response = client.execute(post);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
