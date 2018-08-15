package dev.boom.common.milktea;

public enum MilkTeaItemOptionType {
	NONE((short)0,""),
	ICE((short)1, "MSG_MILK_TEA_OPTION_ICE"),
	SIZE((short)2, "MSG_MILK_TEA_OPTION_SIZE"),
	SUGAR((short)3, "MSG_MILK_TEA_OPTION_SUGAR"),
	TOPPING((short)4, "MSG_MILK_TEA_OPTION_TOPPING"),
	ADDITION((short)5, "MSG_MILK_TEA_OPTION_ADDITION"),
	;
	
	public static final short MAX_TYPE = ADDITION.getType();
	
	private short type;
	private String label;
	
	private MilkTeaItemOptionType(short type, String label) {
		this.type = type;
		this.label = label;
	}

	public short getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}
	
	public static MilkTeaItemOptionType valueOf(short type) {
		for (MilkTeaItemOptionType optionType : MilkTeaItemOptionType.values()) {
			if (optionType.getType() == type) {
				return optionType;
			}
		}
		return MilkTeaItemOptionType.NONE;
	}
}
