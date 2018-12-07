package dev.boom.common.enums;

public enum TicketType {
	INVALID((short)0,"MSG_GENERAL_NONE"),
	KING_OF_CHASUA((short)1,""),
	;
	
	private short type;
	private String label;
	
	private TicketType(short type, String label) {
		this.type = type;
		this.label = label;
	}
	
	public short getType() {
		return this.type;
	}
	
	public String getLabel() {
		return label;
	}

	public static TicketType valueOf(short type) {
		for (TicketType ticketType : TicketType.values()) {
			if (ticketType.getType() == type) {
				return ticketType;
			}
		}
		return TicketType.INVALID;
	}
}
