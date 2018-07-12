package dev.boom.core;

import java.io.Serializable;

public class BoomSession implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;

	public BoomSession(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
