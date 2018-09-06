package dev.boom.common.milktea;

import dev.boom.common.enums.UserFlagEnum;

public enum MilkteaMenuFlag {

	INVALID(0,UserFlagEnum.INVALID,"",""),
	DEV_SHOW(0x00001,UserFlagEnum.DEV_DP,"MSG_MILK_TEA_MENU_SHOW_DEV","DEV"),
	QA_SHOW(0x00002,UserFlagEnum.QA_DP,"MSG_MILK_TEA_MENU_SHOW_QA","QA"),
	CG_SHOW(0x00004,UserFlagEnum.CG_DP,"MSG_MILK_TEA_MENU_SHOW_CG","CG"),
	;
	
	private int flag;
	private UserFlagEnum userFlag;
	private String label;
	private String name;

	private MilkteaMenuFlag(int flag, UserFlagEnum userFlag, String label, String name) {
		this.flag = flag;
		this.userFlag = userFlag;
		this.label = label;
		this.name= name;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isValid(int flag) {
		return (this.flag & flag) > 0;
	}
	
	public boolean isValidUserFlag(int userFlag) {
		return (this.userFlag.isValid(userFlag));
	}
	
	public static MilkteaMenuFlag valueOf(int flag) {
		for (MilkteaMenuFlag mmf : MilkteaMenuFlag.values()) {
			if (mmf.getFlag() == flag) {
				return mmf;
			}
		}
		return MilkteaMenuFlag.INVALID;
	}
	
	public static int combineAllFlag() {
		int ret = 0;
		for (MilkteaMenuFlag mmf : MilkteaMenuFlag.values()) {
			if (mmf == MilkteaMenuFlag.INVALID) {
				continue;
			}
			ret |= mmf.getFlag();
		}
		return ret;
	}
}
