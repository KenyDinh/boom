package dev.boom.socket.func;

public enum PatpatCommandCategory {

	INVALID(0),
	CODE(1),
	QUIZ(2),
	;
	
	private int categoryId;
	
	private PatpatCommandCategory(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryId() {
		return categoryId;
	}
	
}
