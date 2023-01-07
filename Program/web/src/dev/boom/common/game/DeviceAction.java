package dev.boom.common.game;

public enum DeviceAction {
	
	NONE((byte) 0, "", new String[] {}),
	LIST((byte) 1, "list", new String[] {DeviceDefine.PARAM_DEPT, DeviceDefine.PARAM_TYPE}),
	BORROW((byte) 2, "borrow", new String[] {DeviceDefine.PARAM_ID, DeviceDefine.PARAM_DEPT_START_DATE, DeviceDefine.PARAM_DEPT_END_DATE}),
	DETAIL((byte) 3, "detail", new String[] {DeviceDefine.PARAM_ID}),
	RESPONSE((byte) 3, "response", new String[] {DeviceDefine.PARAM_ID, DeviceDefine.PARAM_RESPONSE_USER, DeviceDefine.PARAM_RESPONSE}),
	;
	
	private byte action;
	private String name;
	private String[] options;
	
	private DeviceAction(byte action, String name, String[] options) {
		this.action = action;
		this.name = name;
		this.options = options;
	}

	public byte getAction() {
		return action;
	}

	public String getName() {
		return name;
	}

	public String[] getOptions() {
		return options;
	}

	public static DeviceAction getActionByName(String name) {
		DeviceAction[] list = DeviceAction.values();
		for (DeviceAction _action : list) {
			if (_action.getName().equalsIgnoreCase(name)) {
				return _action;
			}
		}
		return DeviceAction.NONE;
	}
	
	public static DeviceAction valueOf(byte action) {
		DeviceAction[] list = DeviceAction.values();
		for (DeviceAction _action : list) {
			if (_action.getAction() == action) {
				return _action;
			}
		}
		return DeviceAction.NONE;
	}
	
	public String getParameter(String param, String[] options) {
		if (param == null || param.isEmpty()) {
			return null;
		}
		if (options == null || options.length == 0) {
			return null;
		}
		int index = -1;
		for (int i = 0; i < getOptions().length; i++) {
			if (param.equals(getOptions()[i])) {
				index = i;
			}
		}
		if (index == -1) {
			return null;
		}
		++index; // 1st is action key
		if (options.length <= index) {
			return null;
		}
		String ret = options[index] ;
		if (ret == null || ret.isEmpty()) {
			return null;
		}
		
		return ret;
	}
}
