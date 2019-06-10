package dev.boom.socket.func;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dev.boom.common.game.QuizFun;
import dev.boom.common.game.QuizPlayerStatus;
import dev.boom.common.game.QuizStatus;
import dev.boom.common.game.QuizSubject;
import dev.boom.dao.core.DaoValue;
import dev.boom.services.CommonDaoService;
import dev.boom.services.QuizInfo;
import dev.boom.services.QuizPlayerInfo;
import dev.boom.services.QuizPlayerService;
import dev.boom.services.QuizService;
import dev.boom.services.UserInfo;
import dev.boom.services.UserService;
import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

public class PatpatFunc {

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
			existingQuizInfo = new QuizInfo();
			existingQuizInfo.setHost(userInfo.getId());
			existingQuizInfo.setName(channel);
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
				log.error("[processQuizMessage] No quiz to join!");
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
			quizPlayerInfo.setStatus(QuizPlayerStatus.FINISHED.getStatus());
			existingQuizInfo.setPlayerNum((byte)(existingQuizInfo.getPlayerNum() - 1));
			if (existingQuizInfo.getPlayerNum() <= 0) {
				existingQuizInfo.setStatus(QuizStatus.FINISHED.getStatus());
				existingQuizInfo.setExpired(new Date());
			} else if (existingQuizInfo.getHost() == quizPlayerInfo.getUserId() && existingQuizInfo.isPreparing()) {
				QuizPlayerInfo newHost = QuizPlayerService.getQuizPlayerByQuizId(existingQuizInfo.getId(), " ORDER BY updated ASC");
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
				log.error("[processQuizMessage] No quiz to join!");
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
			// TODO
			break;
		case QUIZ_STOP:
			log.error("[processQuizMessage] Not available!");
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

	public static void main(String[] args) {
		String test = "  asd asd \"q82u398 asjd     asdas\"  hdada \"   ahsdja   as das";
		String[] arr = toArrayOptions(test);
		for (String s : arr) {
			System.out.println("\"" + s + "\"");
		}
	}
}
