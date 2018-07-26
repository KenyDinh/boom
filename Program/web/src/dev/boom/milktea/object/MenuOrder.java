package dev.boom.milktea.object;

public class MenuOrder {
	
	private long id;
	private String name;
	private long price;
	private boolean sent;
	private MenuOrderOption[] options;
	
	public MenuOrder() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public MenuOrderOption[] getOptions() {
		return options;
	}

	public void setOptions(MenuOrderOption[] options) {
		this.options = options;
	}
	
}
