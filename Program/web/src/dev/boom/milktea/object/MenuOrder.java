package dev.boom.milktea.object;

public class MenuOrder {

	private long cart_id;
	private MenuOrderItem[] dishes;

	public MenuOrder() {
		super();
	}

	public long getCart_id() {
		return cart_id;
	}

	public void setCart_id(long cart_id) {
		this.cart_id = cart_id;
	}

	public MenuOrderItem[] getDishes() {
		return dishes;
	}

	public void setDishes(MenuOrderItem[] dishes) {
		this.dishes = dishes;
	}

}
