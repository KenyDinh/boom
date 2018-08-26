package dev.boom.common.enums;

public enum EventFlagEnum {
	
	INVALID(0,"", false),
	REGISTRATON(1,"MSG_EVENT_REGISTRATON", true),
	ORDER_VOTING(2,"MSG_EVENT_ORDER_VOTING", true),
	RATING_CRONJOB(3,"MSG_EVENT_ORDER_RATING_CRON", false),
	;
	
	private int index;
	private String label;
	private boolean available;
	
	private EventFlagEnum(int index, String label, boolean available) {
		this.index = index;
		this.label = label;
		this.available = available;
	}

	public int getIndex() {
		return index;
	}

	public String getLabel() {
		return label;
	}
	
	public boolean isAvailable() {
		return available;
	}

	public static EventFlagEnum valueOf(int index) {
		for (EventFlagEnum event : EventFlagEnum.values()) {
			if (event.getIndex() == index) {
				return event;
			}
		}
		return EventFlagEnum.INVALID;
	}
	
}
