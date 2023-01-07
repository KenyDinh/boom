package dev.boom.common.enums;

public enum DeviceDept {
	NONE((byte) 0, "ALL"),
	DEV((byte) 1, "DEV"), 
	QA((byte) 2, "QA"),
	CG((byte) 3, "CG"),
	ADMIN((byte) 4, "AD"),
	;

	private byte dep;
	private String label;

	private DeviceDept(byte dep, String label) {
		this.dep = dep;
		this.label = label;
	}

	public byte getDep() {
		return dep;
	}

	public String getLabel() {
		return label;
	}
	
	public boolean isValid(int userDept) {
		switch (this) {
		case DEV:
			return Department.DEV.isValid(userDept);
		case QA:
			return Department.QA.isValid(userDept);
		case ADMIN:
			return Department.ADMIN.isValid(userDept);
		case CG:
			return (Department.EVENT.isValid(userDept) || Department.MOTION.isValid(userDept) || Department.CHARA_DESIGN.isValid(userDept) || 
					Department.CHARA_MODEL.isValid(userDept) || Department.INTERACT_DESIGN.isValid(userDept) || Department.STAGE_MODEL.isValid(userDept));
		default:
			break;
		}
		return false;
	}

	public static DeviceDept valueOf(byte dep) {
		for (DeviceDept deviceDep : DeviceDept.values()) {
			if (deviceDep.getDep() == dep) {
				return deviceDep;
			}
		}

		return DeviceDept.NONE;
	}

}
