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
	QUIZ_HELP("quiz", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_A("A", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_B("B", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_C("C", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_D("D", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_E("E", PatpatCommandCategory.QUIZ),
	QUIZ_OPTION_F("F", PatpatCommandCategory.QUIZ),
	DEVICE("device", PatpatCommandCategory.DEVICE),
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
	
	public String getFullCommand() {
		return "patpat " + getCommand();
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
		String[] _input = text.split(" ");
		String[] _command;
		for (PatpatCommandType type : values) {
			_command = type.getCommand().split(" ");
			if (_input.length < _command.length) {
				continue;
			}
			boolean match = true;
			for (int i = 0; i < _command.length ; i++) {
				if (!_input[i].equalsIgnoreCase(_command[i])) {
					match = false;
					break;
				}
			}
			if (!match) {
				continue;
			}
			return type;
		}
		return PatpatCommandType.INVALID;
	}
}
