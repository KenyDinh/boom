package dev.boom.socket.func;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.DeviceDept;
import dev.boom.common.enums.DeviceType;
import dev.boom.common.game.DeviceAction;
import dev.boom.common.game.DeviceDefine;
import dev.boom.common.game.LanguageType;
import dev.boom.common.game.QuizDefine;
import dev.boom.common.game.QuizFunc;
import dev.boom.common.game.QuizJapaneseLevel;
import dev.boom.common.game.QuizPlayerStatus;
import dev.boom.common.game.QuizStaticData;
import dev.boom.common.game.QuizStatus;
import dev.boom.common.game.QuizSubject;
import dev.boom.common.game.QuizTimer;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.services.Device;
import dev.boom.services.DeviceRegister;
import dev.boom.services.DeviceRegisterService;
import dev.boom.services.DeviceService;
import dev.boom.services.Quiz;
import dev.boom.services.QuizData;
import dev.boom.services.QuizDataService;
import dev.boom.services.QuizLog;
import dev.boom.services.QuizPlayer;
import dev.boom.services.QuizPlayerService;
import dev.boom.services.QuizService;
import dev.boom.services.User;
import dev.boom.services.UserService;
import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

public class PatpatFunc {

	private static final String WEBHOOK_URL = "http://10.24.36.13/hooks/t8qh6bx7wfrbbjcojcytexuhca";
	private static Log log = LogFactory.getLog(PatpatFunc.class);
	
	public static PatpatOutgoingMessage processMessage(PatpatCommandType command, PatpatIncomingMessage message) {
		if (command == null || message == null || !message.isValidMessage()) {
			return null;
		}
		GameLog.getInstance().info("[PatPat] receive message: " + message.getMessage());
		PatpatCommandCategory category = command.getCategory();
		switch (category) {
		case INVALID:
			return listCommands(message);
		case CODE:
			return processCodeMessage(command, message);
		case QUIZ:
			return processQuizMessage(command, message);
		case DEVICE:
			return processDeviceMessage(command, message);
		default:
			return processDefaultMessage(command, message);
		}
	}

	private static PatpatOutgoingMessage listCommands(PatpatIncomingMessage message) {
		StringBuilder sb = new StringBuilder();
		sb.append("### Current available PATPAT command :");
		sb.append("\n");
		sb.append("- `patpat quiz` - command for creating a quiz!");
		sb.append("\n");
		sendMessageToChannel(message.getChannel(), sb.toString());
		return null;
	}

	private static PatpatOutgoingMessage processDefaultMessage(PatpatCommandType command, PatpatIncomingMessage message) {

		return null;
	}

	private static PatpatOutgoingMessage processQuizMessage(PatpatCommandType command, PatpatIncomingMessage message) {
		String username = message.getUsername();
		String channel = message.getChannel();
		String text = message.getMessage();
		User userInfo = UserService.getUserByName(username);
		if (userInfo == null) {
			log.error("[processQuizMessage] User is null!");
			return null;
		}
		PatpatOutgoingMessage returnMessage = new PatpatOutgoingMessage();
		Quiz existingQuizInfo = QuizService.getNotFinishQuizByName(channel);
		QuizPlayer quizPlayerInfo = QuizPlayerService.getQuizPlayerById(userInfo.getId());
		List<DaoValue> updateList;
		QuizTimer quizTimer;
		String msg;
		switch (command) {
		case QUIZ_INIT:
			if (existingQuizInfo != null) {
				log.error("[processQuizMessage] Quiz is already created in this channel: " + channel);
				sendMessageToChannel(channel, "Quiz is already created in this channel!");
				return null;
			}
			String arr[] = toArrayOptions(command.formatMessage(text));
			if (arr == null || arr.length == 0) {
				log.error("[processQuizMessage] No option found!");
				sendMessageToChannel(channel, "No option found!");
				return null;
			}
			QuizSubject quizSubject = QuizSubject.getSubjectByName(arr[0]);
			if (quizSubject == QuizSubject.NONE) {
				log.error("[processQuizMessage] Invalid quiz subject!");
				sendMessageToChannel(channel, "Invalid quiz subject!");
				return null;
			}
			byte type = 0;
			byte level = 0;
			switch (quizSubject) {
			case Programming:
				if (arr.length < 2) {
					log.error("[processQuizMessage] Invalid quiz type (missing param)!");
					sendMessageToChannel(channel, "Invalid options!");
					return null;
				}
				type = LanguageType.getLanguageByName(quizSubject.getParameter(QuizDefine.PARAM_Q_LANG, arr)).getLang();
				if (type <= 0) {
					log.error("[processQuizMessage] Invalid parameter!");
					sendMessageToChannel(channel, "Invalid options!");
					return null;
				}
				break;
			case Japanese:
				String strLevel = quizSubject.getParameter(QuizDefine.PARAM_Q_LEVEL, arr);
				if (strLevel == null) {
					log.warn("[processQuizMessage] Invalid quiz level (missing param)!");
					sendMessageToChannel(channel, "Invalid level option!");
					return null;
				}
				level = QuizJapaneseLevel.getJapaneseLevelByName(strLevel).getLevel();
				break;
			default:
				break;
			}
			existingQuizInfo = new Quiz();
			Date now = new Date();
			existingQuizInfo.setCreated(CommonMethod.getFormatDateString(now));
			existingQuizInfo.setExpired(CommonMethod.getFormatDateString(new Date(now.getTime() + CommonDefine.MILLION_SECOND_MINUTE * 15)));
			String strQNum = quizSubject.getParameter(QuizDefine.PARAM_Q_NUM, arr);
			if (strQNum != null) {
				if (!CommonMethod.isValidNumeric(strQNum, QuizDefine.MIN_QUESTION, QuizDefine.MAX_QUESTION)) {
					log.warn("[processQuizMessage] Number of Q is invalid!");
					sendMessageToChannel(channel, "Invalid options!");
					return null;
				}
				existingQuizInfo.setQuestionNum(Byte.parseByte(strQNum));
			}
			String strQTime = quizSubject.getParameter(QuizDefine.PARAM_Q_TIME, arr);
			if (strQTime != null) {
				if (!CommonMethod.isValidNumeric(strQTime, QuizDefine.MIN_TIME_PER_QUESTION, QuizDefine.MAX_TIME_PER_QUESTION)) {
					log.warn("[processQuizMessage] Quiz time is invalid!");
					sendMessageToChannel(channel, "Invalid options!");
					return null;
				}
				existingQuizInfo.setTimePerQuestion((Long.parseLong(strQTime) * CommonDefine.MILLION_SECOND_SECOND));
			}
			String strQPlayer = quizSubject.getParameter(QuizDefine.PARAM_Q_PLAYER, arr);
			if (strQPlayer != null) {
				if (!CommonMethod.isValidNumeric(strQPlayer, QuizDefine.MIN_PLAYER, QuizDefine.MAX_PLAYER)) {
					log.warn("[processQuizMessage] Quiz num player is invalid!");
					sendMessageToChannel(channel, "Invalid options!");
					return null;
				}
				existingQuizInfo.setMaxPlayer(Byte.parseByte(strQPlayer));
			}
			String strQretry = quizSubject.getParameter(QuizDefine.PARAM_Q_RETRY, arr);
			if (strQretry != null) {
				if (!CommonMethod.isValidNumeric(strQretry, QuizDefine.MIN_RETRY, QuizDefine.MAX_RETRY)) {
					log.warn("[processQuizMessage] Quiz retry num is invalid!");
					sendMessageToChannel(channel, "Invalid options!");
					return null;
				}
				byte retry = (byte) (Byte.parseByte(strQretry) + 1);
				existingQuizInfo.setRetry(retry);
			}
			String strQdesc = quizSubject.getParameter(QuizDefine.PARAM_Q_DESC, arr);
			if (strQdesc != null) {
				if (StringUtils.isBlank(strQdesc) || (!strQdesc.equalsIgnoreCase("y") && !strQdesc.equalsIgnoreCase("n"))) {
					log.warn("[processQuizMessage] Description param value is invalid!");
					sendMessageToChannel(channel, "Invalid options!");
					return null;
				}
				if (strQdesc.equalsIgnoreCase("y")) {
					existingQuizInfo.setFlag((byte) 1); //TODO
				}
			}
			
 			existingQuizInfo.updateExpired();
			existingQuizInfo.setHost(userInfo.getId());
			existingQuizInfo.setName(channel);
			existingQuizInfo.setSubject(quizSubject.getSubject());
			existingQuizInfo.setLevel(level);
			List<QuizData> questionList = QuizDataService.getRandomQuizDataList(quizSubject, type, level, existingQuizInfo.getQuestionNum());
			if (questionList == null || questionList.isEmpty()) {
				log.error("[processQuizMessage] No quiz data found!");
				sendMessageToChannel(channel, "No quiz data found!");
				return null;
			}
			if (!existingQuizInfo.initQuizData(questionList)) {
				log.error("[processQuizMessage] Init quiz data error!");
				sendMessageToChannel(channel, "Init quiz data error!");
				return null;
			}
			if (quizPlayerInfo == null) {
				quizPlayerInfo = new QuizPlayer();
				quizPlayerInfo.setUserId(userInfo.getId());
				quizPlayerInfo.setUsername(userInfo.getUsername());
			} 
			if (!QuizService.createQuiz(quizPlayerInfo, existingQuizInfo)) {
				log.error("[processQuizMessage] Create quiz fail!");
				sendMessageToChannel(channel, "Create quiz fail!");
				return null;
			}
			break;
		case QUIZ_JOIN:
			if (existingQuizInfo == null) {
				log.error("[processQuizMessage] No quiz to join!");
				sendMessageToChannel(channel, "@" + username + " No quiz to join!");
				return null;
			}
			if (!existingQuizInfo.isPreparing()) {
				log.error("[processQuizMessage] Not able to join this quiz!");
				sendMessageToChannel(channel, "@" + username + " Unable to join this quiz!");
				return null;
			}
			if (quizPlayerInfo == null) {
				quizPlayerInfo = new QuizPlayer();
				quizPlayerInfo.setUserId(userInfo.getId());
				quizPlayerInfo.setUsername(userInfo.getUsername());
			} else {
				msg = QuizFunc.canJoinQuiz(quizPlayerInfo, existingQuizInfo);
				if (msg != null) {
					log.error("[processQuizMessage] " + msg);
					sendMessageToChannel(channel, "@" + username + " " + msg);
					return null;
				}
			}
			quizPlayerInfo.initNewQuiz(existingQuizInfo);
			existingQuizInfo.setPlayerNum((byte) (existingQuizInfo.getPlayerNum() + 1));
			updateList = new ArrayList<>();
			updateList.add(existingQuizInfo.getQuizInfo());
			updateList.add(quizPlayerInfo.getQuizPlayerInfo());
			if (CommonDaoFactory.Update(updateList) < 0) {
				log.error("[processQuizMessage] Join quiz fail!");
				sendMessageToChannel(channel, "@" + username + " Join quiz fail!");
				return null;
			}
			sendMessageToChannel(channel, "@" + username + " has joined the quiz :tada:");
			break;
		case QUIZ_QUIT:
			if (existingQuizInfo == null) {
				log.error("[processQuizMessage] No quiz to quit!");
				sendMessageToChannel(channel, "@" + username + " No quiz to quit!");
				return null;
			}
			if (quizPlayerInfo == null) {
				log.error("[processQuizMessage] User is not in any quiz!");
				sendMessageToChannel(channel, "@" + username + " You are not in any quiz!");
				return null;
			}
			msg = QuizFunc.canQuitQuiz(quizPlayerInfo, existingQuizInfo);
			if (msg != null) {
				log.error("[processQuizMessage] " + msg);
				sendMessageToChannel(channel, "@" + username + " " + msg);
				return null;
			}
			quizPlayerInfo.setQuizId(0);
			existingQuizInfo.setPlayerNum((byte)(existingQuizInfo.getPlayerNum() - 1));
			if (existingQuizInfo.getPlayerNum() <= 0) {
				existingQuizInfo.setStatus(QuizStatus.FINISHED.getStatus());
				existingQuizInfo.setExpired(CommonMethod.getFormatStringNow());
			} else if (existingQuizInfo.getHost() == quizPlayerInfo.getUserId() && existingQuizInfo.isPreparing()) {
				QuizPlayer newHost = QuizPlayerService.getQuizPlayerByQuizId(existingQuizInfo.getId(), "AND user_id <> " + quizPlayerInfo.getUserId() + " ORDER BY updated ASC");
				if (newHost == null) {
					log.warn("[processQuizMessage] Can not find a new host for this quiz!");
					sendMessageToChannel(channel, "Can not find a new host for this quiz! -> Ended");
					existingQuizInfo.setStatus(QuizStatus.FINISHED.getStatus());
					existingQuizInfo.setExpired(CommonMethod.getFormatStringNow());
				} else {
					existingQuizInfo.setHost(newHost.getUserId());
					// change host TODO
				}
			}
			updateList = new ArrayList<>();
			updateList.add(existingQuizInfo.getQuizInfo());
			updateList.add(quizPlayerInfo.getQuizPlayerInfo());
			if (CommonDaoFactory.Update(updateList) < 0) {
				log.error("[processQuizMessage] quit quiz fail!");
				sendMessageToChannel(channel, "An unexpected error occurred!");
				return null;
			}
			sendMessageToChannel(channel, "@" + username + " give up :trollface:");
			if (existingQuizInfo.isFinish()) {
				if (existingQuizInfo.getCurrentQuestion() > 0) {
					QuizService.showQuizAnswer(existingQuizInfo);
				} else {
					sendMessageToChannel(channel, "Quiz finished!");
				}
			}
			break;
		case QUIZ_START:
			if (existingQuizInfo == null) {
				log.error("[processQuizMessage] No quiz to start!");
				sendMessageToChannel(channel, "@" + username + " No quiz to start!");
				return null;
			}
			if (quizPlayerInfo == null) {
				log.error("[processQuizMessage] User is not in any quiz!");
				sendMessageToChannel(channel, "@" + username + " You are not in any quiz!");
				return null;
			}
			if (!existingQuizInfo.isPreparing()) {
				log.error("[processQuizMessage] This quiz already started!");
				sendMessageToChannel(channel, "@" + username + " Quiz already started!");
				return null;
			}
			if (existingQuizInfo.getHost() != quizPlayerInfo.getUserId()) {
				log.error("[processQuizMessage] Don't have permission to start!");
				sendMessageToChannel(channel, "@" + username + " You do not have permission to start the quiz!");
				return null;
			}
			if (!QuizService.startQuiz(existingQuizInfo)) {
				log.error("[processQuizMessage] Start quiz fail!");
				sendMessageToChannel(channel, "An unexpected error occurred!");
				return null;
			}
			existingQuizInfo.getQuizInfo().Sync();
			quizTimer = new QuizTimer(existingQuizInfo.getId());
			QuizStaticData.addQuizTimer(quizTimer);
			quizTimer.start();
			break;
		case QUIZ_STOP:
			if (existingQuizInfo == null) {
				log.error("[processQuizMessage] No quiz to stop!");
				sendMessageToChannel(channel, "No quiz to stop!");
				return null;
			}
			if (quizPlayerInfo == null) {
				log.error("[processQuizMessage] User is not in any quiz!");
				sendMessageToChannel(channel, "You're not in any quiz!");
				return null;
			}
			if (!existingQuizInfo.isInSession()) {
				log.error("[processQuizMessage] This quiz is not in session!");
				sendMessageToChannel(channel, "Quiz haven't started yet!");
				return null;
			}
			if (existingQuizInfo.getHost() != quizPlayerInfo.getUserId()) {
				log.error("[processQuizMessage] Don't have permission to stop!");
				sendMessageToChannel(channel, "Permission denied!");
				return null;
			}
			if (!QuizService.endQuiz(existingQuizInfo)) {
				log.error("[processQuizMessage] end quiz failed!, id: " + existingQuizInfo.getId());
				sendMessageToChannel(channel, "An unexpected error occurred!");
				return null;
			}
			quizTimer = QuizStaticData.getQuizTimerByQuizId(existingQuizInfo.getId());
			if (quizTimer != null) {
				quizTimer.cancel();
				QuizStaticData.removeQuizTimer(quizTimer);
			}
			sendMessageToChannel(channel, "Quiz ended!!");
			return null;
		case QUIZ_INFO:
			if (existingQuizInfo == null) {
				log.error("[processQuizMessage] No quiz found!");
				sendMessageToChannel(channel, "@" + username + " No quiz found!");
				return null;
			}
			if (quizPlayerInfo == null) {
				log.error("[processQuizMessage] User is not in any quiz!");
				sendMessageToChannel(channel, "@" + username + " You are not in any quiz!");
				return null;
			}
			QuizService.showQuizDetail(existingQuizInfo, false);
			return null;
		case QUIZ_OPTION_A:
		case QUIZ_OPTION_B:
		case QUIZ_OPTION_C:
		case QUIZ_OPTION_D:
		case QUIZ_OPTION_E:
		case QUIZ_OPTION_F:
			if (!message.isSlash()) {
				log.error("[processQuizMessage] Cammand is not available!");
				sendMessageToChannel(channel, "@" + username + " plz use `/patpat [option]` instead");
				return null;
			}
			if (existingQuizInfo == null) {
				log.error("[processQuizMessage] No quiz to answer!");
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
			byte retry = quizPlayerInfo.getRetry();
			if (retry <= 0) {
				log.error("[processQuizMessage] Exceed retry time!");
				return null;
			}
			String correctAnswer = existingQuizInfo.getCorrectAnswer();
			if (correctAnswer == null) {
				log.error("[processQuizMessage] This quiz do not have correct answer!");
				return null;
			}
			updateList = new ArrayList<>();
			quizPlayerInfo.setRetry((byte) (retry - 1));
			quizPlayerInfo.setAnswer(command.getCommand());
			if (correctAnswer.equals(command.getCommand())) {
				quizPlayerInfo.incCorrectCount();
				quizPlayerInfo.incCorrectPoint();
			} else {
			}
			updateList.add(quizPlayerInfo.getQuizPlayerInfo());
			QuizLog quizLogInfo = new QuizLog();
			quizLogInfo.setQuizId(existingQuizInfo.getId());
			quizLogInfo.setQuestionIndex(existingQuizInfo.getCurrentQuestion());
			quizLogInfo.setUserId(quizPlayerInfo.getUserId());
			quizLogInfo.setUsername(quizPlayerInfo.getUsername());
			quizLogInfo.setPlayerAnswer(command.getCommand());
			quizLogInfo.setCorrectAnswer(correctAnswer);
			updateList.add(quizLogInfo.getQuizLogInfo());
			if (CommonDaoFactory.Update(updateList) < 0) {
				log.error("[processQuizMessage] Update quiz player answer failed!");
				sendMessageToChannel(channel, "An unexpected error occurred!");
				return null;
			}
			/////////////////////////////////////////////
			if (QuizPlayerService.isAllPlayerFinishTurn(existingQuizInfo.getId())) {
				quizTimer = QuizStaticData.getQuizTimerByQuizId(existingQuizInfo.getId());
				if (quizTimer != null) {
					quizTimer.cancel();
					QuizStaticData.removeQuizTimer(quizTimer);
					QuizTimer newQuizTimer = new QuizTimer(existingQuizInfo.getId());
					newQuizTimer.setDelay(0);
					newQuizTimer.setStep(QuizTimer.STEP_BREAK_TIME);
					newQuizTimer.start();
					QuizStaticData.addQuizTimer(newQuizTimer);
				}
			}
			break;
		default:
			StringBuilder sb = new StringBuilder();
			if (existingQuizInfo != null && existingQuizInfo.isInSession()) {
				if (quizPlayerInfo != null && quizPlayerInfo.getQuizId() == existingQuizInfo.getId() && quizPlayerInfo.getStatus() == QuizPlayerStatus.PLAYING.getStatus()) {
					sb.append("Answer the question by using one of this command : `/patpat [option]`");
					sb.append("\n");
					sb.append("- `option` is one of `A,B,C,D,E`");
				} else {
					sb.append("Join the quiz(before it started) by using this command : `" + PatpatCommandType.QUIZ_JOIN.getFullCommand() + "`");
				}
			} else {
				sb.append("Please use `patpat quiz create [subject] [q_lang] [q_level] [q_num] [q_time] [q_player] [q_retry] [q_desc]` for creating new quiz.");
				sb.append("\n");
				sb.append("- `subject` : quiz subject, current available :");
				sb.append("\n");
				sb.append(" - `toeic` - TOEIC QUIZ");
				sb.append("\n");
				sb.append(" - `programming` - PROGRAMMING QUIZ");
				sb.append("\n");
				sb.append(" - `japanese` - JAPANESE QUIZ");
				sb.append("\n");
				sb.append("- `q_lang` : `programming` only, language|tech, current available `js`");
				sb.append("\n");
				sb.append("- `q_level` : `japanese` only, current available `N5, N4, N3`");
				sb.append("\n");
				sb.append("- `q_num` : number of question from ").append(QuizDefine.MIN_QUESTION).append(" to ").append(QuizDefine.MAX_QUESTION);
				sb.append(", default ").append(QuizDefine.DEFAULT_QUESTION_NUM).append(" if not specified");
				sb.append("\n");
				sb.append("- `q_time` : time limit(in sec) per question from ").append(QuizDefine.MIN_TIME_PER_QUESTION).append(" to ").append(QuizDefine.MAX_TIME_PER_QUESTION);
				sb.append(", default ").append(QuizDefine.DEFAULT_TIME_PER_QUESTION/ CommonDefine.MILLION_SECOND_SECOND).append(" if not specified");
				sb.append("\n");
				sb.append("- `q_player` : max number of players from ").append(QuizDefine.MIN_PLAYER).append(" to ").append(QuizDefine.MAX_PLAYER);
				sb.append(", default ").append(QuizDefine.DEFAULT_PLAYER_NUM).append(" if not specified");
				sb.append("\n");
				sb.append("- `q_retry` : able to re-answer from ").append(QuizDefine.MIN_RETRY).append(" to ").append(QuizDefine.MAX_RETRY).append(" times");
				sb.append(", default ").append(QuizDefine.MIN_RETRY).append(" if not specified");
				sb.append("\n");
				sb.append("- `q_desc` : `toeic, japanese` only, show description for each question, choose `y` or `n`, default `n` : do not show");
				sb.append("\n");
				sb.append("Type `patpat quiz info` to show quiz information.");
				sb.append("\n");
				sb.append("Type `patpat quiz join` to participate in quiz.");
				sb.append("\n");
				sb.append("Type `patpat quiz quit` to quit quiz.");
				sb.append("\n");
				sb.append("Type `patpat quiz start` - host only to start quiz.");
				sb.append("\n");
				sb.append("Type `patpat quiz stop` - host only to end quiz.");
				sb.append("\n");
			}
			log.info("[processQuizMessage] Help command!");
			sendMessageToChannel(channel, sb.toString());
			return null;
		}
		return returnMessage;
	}

	private static PatpatOutgoingMessage processCodeMessage(PatpatCommandType command, PatpatIncomingMessage message) {

		return null;
	}
	
	private static PatpatOutgoingMessage processDeviceMessage(PatpatCommandType command, PatpatIncomingMessage message) {
		String channel = message.getChannel();
		String text = message.getMessage();
		String username = message.getUsername();
		User userInfo = UserService.getUserByName(username);
		switch (command) {
		case DEVICE:
			String arr[] = toArrayOptions(command.formatMessage(text));
			if (arr == null || arr.length == 0) {
				log.error("[processDeviceMessage] No option found!");
				sendMessageToChannel(channel, "No option found!");
				return null;
			}
			DeviceAction deviceAction = DeviceAction.getActionByName(arr[0]);
			if (deviceAction == DeviceAction.NONE) {
				log.error("[processDeviceMessage] No option found!");
				sendMessageToChannel(channel, "No option found!");
				return null;
			}
			String response = doDeviceAction(userInfo, deviceAction, arr);
			if (response == null) {
				return null;
			}
			sendMessageToChannel(channel, response);
			// TODO
		default:
			break;
		}
		return null;
	}
	
	private static String doDeviceAction(User user, DeviceAction deviceAction, String[] arr) {
		List<Device> deviceList = null;
		StringBuilder option = new StringBuilder();
		switch (deviceAction) {
		case LIST:
		{
			byte dept = 0;
			String strDept = getParameter(DeviceDefine.PARAM_DEPT, arr, deviceAction.getOptions(), 1);
			if (StringUtils.isNotBlank(strDept)) {
				strDept = strDept.toUpperCase().trim();
				for (DeviceDept deviceDept : DeviceDept.values()) {
					if (deviceDept.getLabel().equals(strDept)) {
						dept = deviceDept.getDep();
						break;
					}
				}
			}
			byte type = 0;
			String strType = getParameter(DeviceDefine.PARAM_TYPE, arr, deviceAction.getOptions(), 1);
			if (StringUtils.isNotBlank(strType)) {
				strType = strType.toUpperCase().trim();
				for (DeviceType deviceType : DeviceType.values()) {
					if (deviceType.name().equals(strType)) {
						type = deviceType.getType();
						break;
					}
				}
			}
			if (dept > 0) {
				option.append(" AND dept = ").append(dept);
			}
			if (type > 0) {
				option.append(" AND type = ").append(type);
			}
			deviceList = DeviceService.listAllDevice(option.toString().trim(), -1, -1);
			return DeviceService.getRenderTableDevice(deviceList);
		}
		case BORROW:
		{
			String strId = getParameter(DeviceDefine.PARAM_ID, arr, deviceAction.getOptions(), 1);
			if (!StringUtils.isNotBlank(strId) || !CommonMethod.isValidNumeric(strId, 0, Integer.MAX_VALUE)) {
				log.error("[processDeviceMessage] device id is invalid!");
				return null;
			}
			Device device = DeviceService.getDeviceById(Integer.parseInt(strId));
			String startDate = getParameter(DeviceDefine.PARAM_DEPT_START_DATE, arr, deviceAction.getOptions(), 1);
			String endDate = getParameter(DeviceDefine.PARAM_DEPT_END_DATE, arr, deviceAction.getOptions(), 1);
			return DeviceService.doBorrowDevice(user, device, startDate, endDate);
		}
		case DETAIL:
		{
			String strId = getParameter(DeviceDefine.PARAM_ID, arr, deviceAction.getOptions(), 1);
			if (!CommonMethod.isValidNumeric(strId, 0, Integer.MAX_VALUE)) {
				log.error("[processDeviceMessage] device id is invalid!");
				return null;
			}
			Device device = DeviceService.getDeviceById(Integer.parseInt(strId));
			if (device == null) {
				log.error("[processDeviceMessage] device not found!");
				return "Device not found!";
			}
			Map<Integer, List<DeviceRegister>> deviceRegisterMap = DeviceRegisterService.getRegisterListById(Arrays.asList(device.getId()));
			if (deviceRegisterMap == null) {
				return DeviceService.getRenderTableDevice(Arrays.asList(device));
			}
			return String.format("%s\n\n%s", DeviceService.getRenderTableDevice(Arrays.asList(device)), DeviceService.getRenderTableRegister(deviceRegisterMap.get(device.getId())));
		}
		case RESPONSE:
			
			break;
		default:
			break;
		}
		
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
			} else if (c == '\'') {
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
	
	public static boolean sendMessageToChannel(String channel, String msg) {
		PatpatOutgoingMessage message = new PatpatOutgoingMessage();
		message.setChannel(channel);
		message.setText(msg);
		return sendMessageToChannel(PatpatFunc.convertOutgoingMessage(message));
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
			return true;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static Properties messageMap = new Properties();
	
	public static void loadMessage() {
		try {
			messageMap.load(PatpatFunc.class.getClassLoader().getResourceAsStream("quiz-message.properties"));
			messageMap.load(PatpatFunc.class.getClassLoader().getResourceAsStream("quiz-description.properties"));
//			messageMap.load(PatpatFunc.class.getClassLoader().getResourceAsStream("click-page.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getMessage(String label) {
		return messageMap.getProperty(label);
	}
	
	public static String getParameter(String name, String[] params, String[] options, int startIndex) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		if (params == null || params.length == 0) {
			return null;
		}
		if (options == null || options.length == 0) {
			return null;
		}
		int index = -1;
		for (int i = 0; i < options.length; i++) {
			if (name.equals(options[i])) {
				index = i;
			}
		}
		if (index == -1) {
			return null;
		}
		index += Math.max(startIndex, 0);
		if (params.length <= index) {
			return null;
		}
		String ret = params[index] ;
		if (ret == null || ret.isEmpty()) {
			return null;
		}
		
		return ret;
	
	}
	
}
