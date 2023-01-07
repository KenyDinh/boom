package dev.boom.common.enums;

public enum Department {
	
	NONE(0, "MSG_GENERAL_NONE"),
	DEV(0x0001, "MSG_GENERAL_USER_FLAG_DEP_DEV"),
	QA(0x0002, "MSG_GENERAL_USER_FLAG_DEP_QA"),
	ADMIN(0x0004, "MSG_GENERAL_USER_FLAG_DEP_ADMIN"),
	EVENT(0x0008, "MSG_GENERAL_USER_FLAG_DEP_EVENT"),
	MOTION(0x0010, "MSG_GENERAL_USER_FLAG_DEP_MOTION"),
	CHARA_DESIGN(0x0020, "MSG_GENERAL_USER_FLAG_DEP_CHARA_DESIGN"),
	CHARA_MODEL(0x0040, "MSG_GENERAL_USER_FLAG_DEP_CHARA_MODEL"),
	INTERACT_DESIGN(0x0080, "MSG_GENERAL_USER_FLAG_DEP_INTERACT_DESIGN"),
	STAGE_MODEL(0x0100, "MSG_GENERAL_USER_FLAG_DEP_STAGE_MODEL"),
	;
	
	private int flag;
	private String label;

	private Department(int flag, String label) {
		this.flag = flag;
		this.label = label;
	}

	public int getFlag() {
		return flag;
	}

	public String getLabel() {
		return label;
	}

	public boolean isValid(int flag) {
		return (boolean) ((this.flag & flag) != 0);
	}

	public static Department valueOf(int index) {
		for (Department dept : Department.values()) {
			if (dept.getFlag() == index) {
				return dept;
			}
		}
		return Department.NONE;
	}
	
	public static int getAllFlag() {
		int flags = 0;
		for (Department dept : Department.values()) {
			if (dept == Department.NONE) {
				continue;
			}
			flags |= dept.getFlag();
		}
		
		return flags;
	}
}
