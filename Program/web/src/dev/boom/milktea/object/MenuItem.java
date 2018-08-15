package dev.boom.milktea.object;

import java.util.ArrayList;
import java.util.List;

import dev.boom.common.milktea.MilkTeaItemOptionType;

public class MenuItem {

	private String name;
	private String desc;
	private String image_url;
	private int price;
	private String type;
	private MenuItemOption[] list_size;
	private MenuItemOption[] list_topping;
	private MenuItemOption[] list_sugar;
	private MenuItemOption[] list_ice;
	private MenuItemOption[] list_addition;
	private MenuItemSelectionLimit limit_select;

	private String detail;
	private long id;
	private long shop_id;

	public MenuItem() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MenuItemOption[] getList_size() {
		if (list_size != null && list_size.length > 0) {
			return list_size;
		}
		return null;
	}

	public void setList_size(MenuItemOption[] list_size) {
		this.list_size = list_size;
	}

	public MenuItemOption[] getList_topping() {
		if (list_topping != null && list_topping.length > 0) {
			return list_topping;
		}
		return null;
	}

	public void setList_topping(MenuItemOption[] list_topping) {
		this.list_topping = list_topping;
	}

	public MenuItemOption[] getList_sugar() {
		if (list_sugar != null && list_sugar.length > 0) {
			return list_sugar;
		}
		return null;
	}

	public void setList_sugar(MenuItemOption[] list_sugar) {
		this.list_sugar = list_sugar;
	}

	public MenuItemOption[] getList_ice() {
		if (list_ice != null && list_ice.length > 0) {
			return list_ice;
		}
		return null;
	}

	public void setList_ice(MenuItemOption[] list_ice) {
		this.list_ice = list_ice;
	}
	
	public MenuItemOption[] getList_addition() {
		if (list_addition != null && list_addition.length > 0) {
			return list_addition;
		}
		return null;
	}

	public void setList_addition(MenuItemOption[] list_addition) {
		this.list_addition = list_addition;
	}

	public MenuItemSelectionLimit getLimit_select() {
		return limit_select;
	}

	public void setLimit_select(MenuItemSelectionLimit limit_select) {
		this.limit_select = limit_select;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getShop_id() {
		return shop_id;
	}

	public void setShop_id(long shop_id) {
		this.shop_id = shop_id;
	}

	public List<MenuItemOption> getListOptions() {
		List<MenuItemOption> listOptions = new ArrayList<>();
		if (list_ice != null && list_ice.length > 0) {
			for (MenuItemOption option : list_ice) {
				option.setType(MilkTeaItemOptionType.ICE.getType());
				listOptions.add(option);
			}
		}
		if (list_size != null && list_size.length > 0) {
			for (MenuItemOption option : list_size) {
				option.setType(MilkTeaItemOptionType.SIZE.getType());
				listOptions.add(option);
			}
		}
		if (list_sugar != null && list_sugar.length > 0) {
			for (MenuItemOption option : list_sugar) {
				option.setType(MilkTeaItemOptionType.SUGAR.getType());
				listOptions.add(option);
			}
		}
		if (list_topping != null && list_topping.length > 0) {
			for (MenuItemOption option : list_topping) {
				option.setType(MilkTeaItemOptionType.TOPPING.getType());
				listOptions.add(option);
			}
		}
		if (list_addition != null && list_addition.length > 0) {
			for (MenuItemOption option : list_addition) {
				option.setType(MilkTeaItemOptionType.ADDITION.getType());
				listOptions.add(option);
			}
		}
		return listOptions;
	}
}
