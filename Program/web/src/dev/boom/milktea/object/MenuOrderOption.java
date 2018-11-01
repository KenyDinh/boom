package dev.boom.milktea.object;

import java.util.ArrayList;
import java.util.List;

public class MenuOrderOption {

	private long id;
	private List<MenuOrderOptionItem> option_items;

	public MenuOrderOption() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<MenuOrderOptionItem> getOption_items() {
		return option_items;
	}

	public void setOption_items(List<MenuOrderOptionItem> option_items) {
		this.option_items = option_items;
	}

	public void addOption_items(MenuOrderOptionItem option_item) {
		if (this.option_items == null) {
			this.option_items = new ArrayList<>();
		}
		this.option_items.add(option_item);
	}
}
