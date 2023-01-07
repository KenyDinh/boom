package dev.boom.socket.func;

public class PatpatOutgoingMessage {

	private String channel;
	private String text;

	public PatpatOutgoingMessage() {
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
