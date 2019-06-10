package dev.boom.socket.func;

public enum PatpatCommandType {
	
	INVALID("", PatpatCommandCategory.INVALID),
	SHOW_CODE("show code", PatpatCommandCategory.CODE),
	SAVE_CODE("save code", PatpatCommandCategory.CODE),
	QUIZ_INIT("quiz create", PatpatCommandCategory.QUIZ),
	QUIZ_JOIN("quiz join", PatpatCommandCategory.QUIZ),
	QUIZ_QUIT("quiz quit", PatpatCommandCategory.QUIZ),
	QUIZ_START("quiz start", PatpatCommandCategory.QUIZ),
	QUIZ_STOP("quiz stop", PatpatCommandCategory.QUIZ),
	QUIZ_INFO("quiz info", PatpatCommandCategory.QUIZ),
	QUIZ_CHECK("are you okay to start the quiz?", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_A("A", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_B("B", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_C("C", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_D("D", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_E("E", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_F("F", PatpatCommandCategory.QUIZ),
	
	;
	
	private String command;
	private PatpatCommandCategory category;
	
	private PatpatCommandType(String command, PatpatCommandCategory category) {
		this.command = command;
		this.category = category;
	}
	
	public String getCommand() {
		return command;
	}
	
	public PatpatCommandCategory getCategory() {
		return category;
	}

	public PatpatOutgoingMessage processCommand(PatpatIncomingMessage message) {
		return PatpatFunc.processMessage(this, message);
	}
	
	public String formatMessage(String text) {
		int cmdLength = getCommand().length();
		if (cmdLength + 1 > text.length()) {
			return text;
		}
		return text.substring(cmdLength + 1).trim();
	}
	
	
	public static PatpatCommandType getPatpatCommand(PatpatIncomingMessage message) {
		String text = message.getMessage();
		if (text == null || text.isEmpty()) {
			return PatpatCommandType.INVALID;
		}
		PatpatCommandType[] values = PatpatCommandType.values();
		String strCommand;
		int cmdLength;
		for (PatpatCommandType type : values) {
			strCommand = type.getCommand();
			cmdLength = strCommand.length();
			if (cmdLength == 0) {
				continue;
			}
			if (cmdLength > text.length()) {
				continue;
			}
			if (text.substring(0, cmdLength).equalsIgnoreCase(strCommand)) {
				return type;
			}
		}
		return PatpatCommandType.INVALID;
	}
}
