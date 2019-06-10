package dev.boom.socket.func;

public class PatpatIncomingMessage {

	private String username;
	private String channel;
	private String message;

	public PatpatIncomingMessage() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isValidMessage() {
		return (channel != null && !channel.isEmpty() && username != null && !username.isEmpty() && message != null);
	}

}
