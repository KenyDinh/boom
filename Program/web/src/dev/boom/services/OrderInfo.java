package dev.boom.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaItemOptionType;
import dev.boom.common.milktea.MilkTeaOrderFlag;
import dev.boom.tbl.info.TblOrderInfo;

public class OrderInfo {

	private TblOrderInfo info = null;

	public OrderInfo(TblOrderInfo info) {
		this.info = info;
	}

	public OrderInfo() {
		this.info = new TblOrderInfo();
	}
	
	public TblOrderInfo getTblInfo() {
		return this.info;
	}
	
	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);
	}

	public long getUserId() {
		return this.info.getUser_id();
	}

	public void setUserId(long user_id) {
		this.info.setUser_id(user_id);
	}

	public String getUsername() {
		return this.info.getUsername();
	}

	public void setUsername(String username) {
		this.info.setUsername(username);
	}

	public long getMenuId() {
		return this.info.getMenu_id();
	}

	public void setMenuId(long menu_id) {
		this.info.setMenu_id(menu_id);
	}

	public long getShopId() {
		return this.info.getShop_id();
	}

	public void setShopId(long shop_id) {
		this.info.setShop_id(shop_id);
	}

	public String getDishName() {
		return this.info.getDish_name();
	}

	public void setDishName(String dish_name) {
		this.info.setDish_name(dish_name);
	}

	public String getDishType() {
		return this.info.getDish_type();
	}

	public void setDishType(String dish_type) {
		this.info.setDish_type(dish_type);
	}

	public long getDishPrice() {
		return this.info.getDish_price();
	}

	public void setDishPrice(long dish_price) {
		this.info.setDish_price(dish_price);
	}

	public long getAttrPrice() {
		return this.info.getAttr_price();
	}

	public void setAttrPrice(long attr_price) {
		this.info.setAttr_price(attr_price);
	}

	public long getFinalPrice() {
		return this.info.getFinal_price();
	}

	public void setFinalPrice(long final_price) {
		this.info.setFinal_price(final_price);
	}
	
	public int getDishCode() {
		return this.info.getDish_code();
	}

	public void setDishCode(int dish_code) {
		this.info.setDish_code(dish_code);
	}

	public byte getVotingStar() {
		return this.info.getVoting_star();
	}

	public void setVotingStar(byte voting_star) {
		this.info.setVoting_star(voting_star);
	}

	public long getQuantity() {
		return this.info.getQuantity();
	}

	public void setQuantity(long quantity) {
		this.info.setQuantity(quantity);
	}

	public String getSize() {
		return this.info.getSize();
	}

	public void setSize(String size) {
		this.info.setSize(size);
	}

	public String getIce() {
		return this.info.getIce();
	}

	public void setIce(String ice) {
		this.info.setIce(ice);
	}

	public String getSugar() {
		return this.info.getSugar();
	}

	public void setSugar(String sugar) {
		this.info.setSugar(sugar);
	}

	public String getOptionList() {
		return this.info.getOption_list();
	}

	public void setOptionList(String option_list) {
		this.info.setOption_list(option_list);
	}

	public int getFlag() {
		return this.info.getFlag();
	}

	public void setFlag(int flag) {
		this.info.setFlag(flag);
	}

	public Date getCreated() {
		return this.info.getCreated();
	}

	public void setCreated(Date created) {
		this.info.setCreated(created);
	}

	public Date getUpdated() {
		return this.info.getUpdated();
	}

	public void setUpdated(Date updated) {
		this.info.setUpdated(updated);
	}
	
	public boolean isVoted() {
		int flag = getFlag();
		if (MilkTeaOrderFlag.VOTE.isValidFlag(flag) || MilkTeaOrderFlag.VOTE_CRON.isValidFlag(flag)) {
			return true;
		}
		return false;
	}
	
	public long getTotalOption(MilkTeaItemOptionType type) {
		String optionList = null;
		switch (type) {
		case SIZE:
			return 0;
		case ICE:
		case SUGAR:
			if (type == MilkTeaItemOptionType.ICE) {
				optionList = getIce();
			} else {
				optionList = getSugar();
			}
			if (optionList == null || optionList.isEmpty()) {
				return 100;
			}
			int total = 0;
			for (String name : optionList.split(",")) {
				total += MilkTeaCommonFunc.calcOptionAmount(name);
			}
			return total;
		case TOPPING:
		case ADDITION:
			optionList = getOptionList();
			if (optionList == null || optionList.isEmpty()) {
				return 0;
			}
			int count = 0;
			for (String name : optionList.split(",")) {
				if (name.trim().isEmpty()) {
					continue;
				}
				count++;
			}
			return count;
		default:
			return 0;
		}
	}
	
	public List<String> getAllOptionList() {
		List<String> options = new ArrayList<>();
		if (getIce() != null && !getIce().isEmpty()) {
			for (String name : getIce().split(",")) {
				options.add(name);
			}
		}
		if (getSize() != null && !getSize().isEmpty()) {
			for (String name : getSize().split(",")) {
				options.add(name);
			}
		}
		if (getSugar() != null && !getSugar().isEmpty()) {
			for (String name : getSugar().split(",")) {
				options.add(name);
			}
		}
		if (getOptionList() != null && !getOptionList().isEmpty()) {
			for (String name : getOptionList().split(",")) {
				options.add(name);
			}
		}
		return options;
	}
	
}
