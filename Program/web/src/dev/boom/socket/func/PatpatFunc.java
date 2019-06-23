package dev.boom.socket.func;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import dev.boom.common.game.QuizFun;
import dev.boom.common.game.QuizStaticData;
import dev.boom.common.game.QuizStatus;
import dev.boom.common.game.QuizSubject;
import dev.boom.common.game.QuizTimer;
import dev.boom.dao.core.DaoValue;
import dev.boom.services.CommonDaoService;
import dev.boom.services.QuizDataService;
import dev.boom.services.QuizInfo;
import dev.boom.services.QuizPlayerInfo;
import dev.boom.services.QuizPlayerService;
import dev.boom.services.QuizService;
import dev.boom.services.UserInfo;
import dev.boom.services.UserService;
import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

public class PatpatFunc {

	private static final String WEBHOOK_URL = "";
	private static Log log = LogFactory.getLog(PatpatFunc.class);
	
	public static PatpatOutgoingMessage processMessage(PatpatCommandType command, PatpatIncomingMessage message) {
		if (command == null || message == null || !message.isValidMessage()) {
			return null;
		}
		PatpatCommandCategory category = command.getCategory();
		switch (category) {
		case INVALID:
			return listCommands(message);
		case CODE:
			return processCodeMessage(command, message);
		case QUIZ:
			return processQuizMessage(command, message);
		default:
			return processDefaultMessage(command, message);
		}
	}

	private static PatpatOutgoingMessage listCommands(PatpatIncomingMessage message) {
		return null;
	}

	private static PatpatOutgoingMessage processDefaultMessage(PatpatCommandType command, PatpatIncomingMessage message) {

		return null;
	}

	private static PatpatOutgoingMessage processQuizMessage(PatpatCommandType command, PatpatIncomingMessage message) {
		String username = message.getUsername();
		String channel = message.getChannel();
		String text = message.getMessage();
		UserInfo userInfo = UserService.getUserByName(username);
		if (userInfo == null) {
			log.error("[processQuizMessage] User is null!");
			return null;
		}
		PatpatOutgoingMessage returnMessage = new PatpatOutgoingMessage();
		QuizInfo existingQuizInfo = QuizService.getNotFinishQuizByName(channel);
		QuizPlayerInfo quizPlayerInfo = QuizPlayerService.getQuizPlayerById(userInfo.getId());
		List<DaoValue> updateList;
		QuizTimer quizTimer;
		String msg;
		switch (command) {
		case QUIZ_INIT: // [type] [level] [max_player] [number of question] [time per question]
			if (existingQuizInfo != null) {
				log.error("[processQuizMessage] Quiz is already created in this channel: " + channel);
				return null;
			}
			String arr[] = toArrayOptions(command.formatMessage(text));
			if (arr == null || arr.length == 0) {
				log.error("[processQuizMessage] No option found!");
				return null;
			}
			QuizSubject quizSubject = QuizSubject.getSubjectByName(arr[0]);
			if (quizSubject == QuizSubject.NONE) {
				log.error("[processQuizMessage] Invalid quiz subject!");
				return null;
			}
//			byte level = (byte)quizSubject.getMinLevel();
//			
//			if (arr.length > 1) {
//				String strLevel = arr[1];
//				if (!CommonMethod.isValidNumeric(strLevel, quizSubject.getMinLevel(), quizSubject.getMaxLevel())) {
//					log.error("[processQuizMessage] Invalid " + quizSubject.getName() + "'s level");
//					return null;
//				}
//			}
			existingQuizInfo = new QuizInfo();
			existingQuizInfo.setHost(userInfo.getId());
			existingQuizInfo.setName(channel);
			existingQuizInfo.setSubject(quizSubject.getSubject());
			List<Integer> questionIdList = QuizDataService.getRandomQuizDataIdList(existingQuizInfo.getQuestionNum());
			if (questionIdList == null || questionIdList.isEmpty()) {
				log.error("[processQuizMessage] No quiz data found!");
				return null;
			}
			StringBuilder quizData = new StringBuilder();
			for (Integer id : questionIdList) {
				if (quizData.length() > 0) {
					quizData.append(",");
				}
				quizData.append(id.intValue());
			}
			existingQuizInfo.setQuestionData(quizData.toString());
			if (quizPlayerInfo == null) {
				quizPlayerInfo = new QuizPlayerInfo();
				quizPlayerInfo.setUserId(userInfo.getId());
				quizPlayerInfo.setUsername(userInfo.getUsername());
			} 
			if (!QuizService.createQuiz(quizPlayerInfo, existingQuizInfo)) {
				log.error("[processQuizMessage] Create quiz fail!");
				return null;
			}
			break;
		case QUIZ_JOIN:
			if (existingQuizInfo == null) {
				log.error("[processQuizMessage] No quiz to join!");
				return null;
			}
			if (!existingQuizInfo.isPreparing()) {
				log.error("[processQuizMessage] Not able to join this quiz!");
				return null;
			}
			if (quizPlayerInfo == null) {
				quizPlayerInfo = new QuizPlayerInfo();
				quizPlayerInfo.setUserId(userInfo.getId());
				quizPlayerInfo.setUsername(userInfo.getUsername());
			} else {
				msg = QuizFun.canJoinQuiz(quizPlayerInfo, existingQuizInfo);
				if (msg != null) {
					log.error("[processQuizMessage] " + msg);
					return null;
				}
			}
			quizPlayerInfo.initNewQuiz(existingQuizInfo);
			existingQuizInfo.setPlayerNum((byte) (existingQuizInfo.getPlayerNum() + 1));
			updateList = new ArrayList<>();
			updateList.add(existingQuizInfo.getTblQuizInfo());
			updateList.add(quizPlayerInfo.getTblQuizPlayerInfo());
			if (!CommonDaoService.update(updateList)) {
				log.error("[processQuizMessage] Join quiz fail!");
				return null;
			}
			break;
		case QUIZ_QUIT:
			if (existingQuizInfo == null) {
				log.error("[processQuizMessage] No quiz to quit!");
				return null;
			}
			if (quizPlayerInfo == null) {
				log.error("[processQuizMessage] User is not in any quiz!");
				return null;
			}
			msg = QuizFun.canQuitQuiz(quizPlayerInfo, existingQuizInfo);
			if (msg != null) {
				log.error("[processQuizMessage] " + msg);
				return null;
			}
			quizPlayerInfo.setQuizId(0);
			existingQuizInfo.setPlayerNum((byte)(existingQuizInfo.getPlayerNum() - 1));
			if (existingQuizInfo.getPlayerNum() <= 0) {
				existingQuizInfo.setStatus(QuizStatus.FINISHED.getStatus());
				existingQuizInfo.setExpired(new Date());
			} else if (existingQuizInfo.getHost() == quizPlayerInfo.getUserId() && existingQuizInfo.isPreparing()) {
				QuizPlayerInfo newHost = QuizPlayerService.getQuizPlayerByQuizId(existingQuizInfo.getId(), "AND user_id <> " + quizPlayerInfo.getUserId() + " ORDER BY updated ASC");
				if (newHost == null) {
					log.warn("[processQuizMessage] Can not find a new host for this quiz!");
					existingQuizInfo.setStatus(QuizStatus.FINISHED.getStatus());
					existingQuizInfo.setExpired(new Date());
				} else {
					existingQuizInfo.setHost(newHost.getUserId());
					// change host TODO
				}
			}
			updateList = new ArrayList<>();
			updateList.add(existingQuizInfo.getTblQuizInfo());
			updateList.add(quizPlayerInfo.getTblQuizPlayerInfo());
			if (!CommonDaoService.update(updateList)) {
				log.error("[processQuizMessage] quit quiz fail!");
				return null;
			}
			if (existingQuizInfo.isFinish()) {
				// process Quiz end TODO (agent ? schedule ? thread)
			}
			break;
		case QUIZ_START:
			if (existingQuizInfo == null) {
				log.error("[processQuizMessage] No quiz to start!");
				return null;
			}
			if (quizPlayerInfo == null) {
				log.error("[processQuizMessage] User is not in any quiz!");
				return null;
			}
			if (!existingQuizInfo.isPreparing()) {
				log.error("[processQuizMessage] This quiz already started!");
				return null;
			}
			if (existingQuizInfo.getHost() != quizPlayerInfo.getUserId()) {
				log.error("[processQuizMessage] Don't have permission to start!");
				return null;
			}
			if (!QuizService.startQuiz(existingQuizInfo)) {
				log.error("[processQuizMessage] Start quiz fail!");
				return null;
			}
			existingQuizInfo.getTblQuizInfo().Sync();
			// TODO send message
			if (!QuizService.nextQuizQuestion(existingQuizInfo)) {
				log.error("[processQuizMessage] Start quiz fail!");
				return null;
			}
			quizTimer = new QuizTimer(existingQuizInfo.getId(), existingQuizInfo.getTimePerQuestion());
			QuizStaticData.addQuizTimer(quizTimer);
			quizTimer.start();
			break;
		case QUIZ_STOP:
			if (existingQuizInfo == null) {
				log.error("[processQuizMessage] No quiz to stop!");
				return null;
			}
			if (quizPlayerInfo == null) {
				log.error("[processQuizMessage] User is not in any quiz!");
				return null;
			}
			if (!existingQuizInfo.isInSession()) {
				log.error("[processQuizMessage] This quiz is not in session!");
				return null;
			}
			if (existingQuizInfo.getHost() != quizPlayerInfo.getUserId()) {
				log.error("[processQuizMessage] Don't have permission to stop!");
				return null;
			}
			if (!QuizService.endQuiz(existingQuizInfo)) {
				log.error("[processQuizMessage] end quiz failed!, id: " + existingQuizInfo.getId());
				return null;
			}
			quizTimer = QuizStaticData.getQuizTimerByQuizId(existingQuizInfo.getId());
			if (quizTimer != null) {
				quizTimer.cancel();
			}
			return null;
		case QUIZ_INFO:
			log.error("[processQuizMessage] Not available!");
			return null;
		case QUIZ_CHECK:
			log.error("[processQuizMessage] Not available!");
			return null;
		case QUIZ_OPTION_A:
		case QUIZ_OPTION_B:
		case QUIZ_OPTION_C:
		case QUIZ_OPTION_D:
		case QUIZ_OPTION_E:
		case QUIZ_OPTION_F:
			log.error("[processQuizMessage] Not available!");
			return null;
		default:
			log.error("[processQuizMessage] Not available!");
			return null;
		}
		return returnMessage;
	}

	private static PatpatOutgoingMessage processCodeMessage(PatpatCommandType command, PatpatIncomingMessage message) {

		return null;
	}

	private static String[] toArrayOptions(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		}
		char[] array = str.toCharArray();
		List<String> options = new ArrayList<>();
		StringBuilder word = new StringBuilder();
		boolean open = false;
		for (char c : array) {
			if (c == ' ') {
				if (open) {
					word.append(c);
				} else if (word.length() > 0) {
					options.add(word.toString());
					word.setLength(0);
				}
			} else if (c == '"') {
				open = !open;
			} else {
				word.append(c);
			}
		}
		if (word.length() > 0) {
			options.add(word.toString());
		}
		return options.toArray(new String[0]);
	}

	public static PatpatIncomingMessage parseIncomingMessage(String message) {
		if (message == null || message.isEmpty()) {
			return null;
		}
		try {
			return JSON.decode(message, PatpatIncomingMessage.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertOutgoingMessage(PatpatOutgoingMessage message) {
		if (message == null) {
			return null;
		}
		try {
			return JSON.encode(message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean sendMessageToChannel(String jsonData) {
		if (jsonData == null || jsonData.isEmpty()) {
			return false;
		}
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(WEBHOOK_URL);
		StringEntity entity = new StringEntity(jsonData, ContentType.APPLICATION_JSON);
		post.setEntity(entity);
		try {
			HttpResponse response = client.execute(post);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
			return true;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
