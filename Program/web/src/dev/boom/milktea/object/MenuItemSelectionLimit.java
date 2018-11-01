package dev.boom.milktea.object;

import dev.boom.common.milktea.MilkTeaItemOptionType;

public class MenuItemSelectionLimit {

	private int ice_min;
	private int ice_max;
	private int size_min;
	private int size_max;
	private int sugar_min;
	private int sugar_max;
	private int topping_min;
	private int topping_max;
	private int addition_min;
	private int addition_max;
	private long ice_opt_id;
	private long size_opt_id;
	private long sugar_opt_id;
	private long topping_opt_id;
	private long addition_opt_id;

	public MenuItemSelectionLimit() {
		this.ice_min = 0;
		this.ice_max = 1;
		this.size_min = 0;
		this.size_max = 1;
		this.sugar_min = 0;
		this.sugar_max = 1;
		this.topping_min = 0;
		this.topping_max = 3;
		this.addition_min = 0;
		this.addition_max = 3;
	}
	
	public int getIce_min() {
		return ice_min;
	}

	public void setIce_min(int ice_min) {
		this.ice_min = ice_min;
	}

	public int getIce_max() {
		return ice_max;
	}

	public void setIce_max(int ice_max) {
		this.ice_max = ice_max;
	}

	public int getSize_min() {
		return size_min;
	}

	public void setSize_min(int size_min) {
		this.size_min = size_min;
	}

	public int getSize_max() {
		return size_max;
	}

	public void setSize_max(int size_max) {
		this.size_max = size_max;
	}

	public int getSugar_min() {
		return sugar_min;
	}

	public void setSugar_min(int sugar_min) {
		this.sugar_min = sugar_min;
	}

	public int getSugar_max() {
		return sugar_max;
	}

	public void setSugar_max(int sugar_max) {
		this.sugar_max = sugar_max;
	}

	public int getTopping_min() {
		return topping_min;
	}

	public void setTopping_min(int topping_min) {
		this.topping_min = topping_min;
	}

	public int getTopping_max() {
		return topping_max;
	}

	public void setTopping_max(int topping_max) {
		this.topping_max = topping_max;
	}

	public int getAddition_min() {
		return addition_min;
	}

	public void setAddition_min(int addition_min) {
		this.addition_min = addition_min;
	}

	public int getAddition_max() {
		return addition_max;
	}

	public void setAddition_max(int addition_max) {
		this.addition_max = addition_max;
	}
	
	public long getIce_opt_id() {
		return ice_opt_id;
	}

	public void setIce_opt_id(long ice_opt_id) {
		this.ice_opt_id = ice_opt_id;
	}

	public long getSize_opt_id() {
		return size_opt_id;
	}

	public void setSize_opt_id(long size_opt_id) {
		this.size_opt_id = size_opt_id;
	}

	public long getSugar_opt_id() {
		return sugar_opt_id;
	}

	public void setSugar_opt_id(long sugar_opt_id) {
		this.sugar_opt_id = sugar_opt_id;
	}

	public long getTopping_opt_id() {
		return topping_opt_id;
	}

	public void setTopping_opt_id(long topping_opt_id) {
		this.topping_opt_id = topping_opt_id;
	}

	public long getAddition_opt_id() {
		return addition_opt_id;
	}

	public void setAddition_opt_id(long addition_opt_id) {
		this.addition_opt_id = addition_opt_id;
	}

	public int getMinSelect(MilkTeaItemOptionType optionType) {
		switch (optionType) {
		case ICE:
			return getIce_min();
		case SIZE:
			return getSize_min();
		case SUGAR:
			return getSugar_min();
		case TOPPING:
			return getTopping_min();
		case ADDITION:
			return getAddition_min();
		default:
			return 0;
		}
	}

	public int getMaxSelect(MilkTeaItemOptionType optionType) {
		switch (optionType) {
		case ICE:
			return getIce_max();
		case SIZE:
			return getSize_max();
		case SUGAR:
			return getSugar_max();
		case TOPPING:
			return getTopping_max();
		case ADDITION:
			return getAddition_max();
		default:
			return 0;
		}
	}
	
	public long getOptionId(MilkTeaItemOptionType optionType) {
		switch (optionType) {
		case ICE:
			return getIce_opt_id();
		case SIZE:
			return getSize_opt_id();
		case SUGAR:
			return getSugar_opt_id();
		case TOPPING:
			return getTopping_opt_id();
		case ADDITION:
			return getAddition_opt_id();
		default:
			return 0;
		}
	}
	
}
