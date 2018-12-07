package dev.boom.common.enums;

public enum TicketCategory {
	
	INVALID(0,new TicketType[]{}), 
	MILKTEA_TICKET(1, new TicketType[]{TicketType.KING_OF_CHASUA}),
	;

	private int category;
	private TicketType[] ticketTypes;

	private TicketCategory(int category, TicketType[] ticketTypes) {
		this.category = category;
		this.ticketTypes = ticketTypes;
	}

	public int getCategory() {
		return this.category;
	}
	
	public TicketType[] getTicketTypes() {
		return this.ticketTypes;
	}
}
