package dev.boom.milktea.object;

public class MenuOrderItem {

	private long dish_id;
	private int quantity;
//	private String note;
	private MenuOrderOption[] options;

	public MenuOrderItem() {
		super();
	}

	public long getDish_id() {
		return dish_id;
	}

	public void setDish_id(long dish_id) {
		this.dish_id = dish_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

//	public String getNote() {
//		return note;
//	}
//
//	public void setNote(String note) {
//		this.note = note;
//	}

	public MenuOrderOption[] getOptions() {
		return options;
	}

	public void setOptions(MenuOrderOption[] options) {
		this.options = options;
	}

}
