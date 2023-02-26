package dev.boom.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaItemOptionType;
import dev.boom.common.milktea.MilkTeaOrderFlag;
import dev.boom.tbl.info.TblOrderInfo;

public class Order {
	private TblOrderInfo orderInfo;
	private int limitSplitOption = 0;

	public Order() {
		orderInfo = new TblOrderInfo();
	}

	public Order(TblOrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public TblOrderInfo getOrderInfo() {
		return orderInfo;
	}

	public int getLimitSplitOption() {
		return limitSplitOption;
	}

	public void setLimitSplitOption(int limitSplitOption) {
		this.limitSplitOption = limitSplitOption;
	}
	
	public long getId() {
		return (Long) orderInfo.Get("id");
	}

	public void setId(long id) {
		orderInfo.Set("id", id);
	}

	public long getUserId() {
		return (Long) orderInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		orderInfo.Set("user_id", userId);
	}

	public String getUsername() {
		return (String) orderInfo.Get("username");
	}

	public void setUsername(String username) {
		orderInfo.Set("username", username);
	}

	public long getMenuId() {
		return (Long) orderInfo.Get("menu_id");
	}

	public void setMenuId(long menuId) {
		orderInfo.Set("menu_id", menuId);
	}

	public long getShopId() {
		return (Long) orderInfo.Get("shop_id");
	}

	public void setShopId(long shopId) {
		orderInfo.Set("shop_id", shopId);
	}

	public String getDishName() {
		return (String) orderInfo.Get("dish_name");
	}

	public void setDishName(String dishName) {
		orderInfo.Set("dish_name", dishName);
	}

	public String getDishType() {
		return (String) orderInfo.Get("dish_type");
	}

	public void setDishType(String dishType) {
		orderInfo.Set("dish_type", dishType);
	}

	public long getDishPrice() {
		return (Long) orderInfo.Get("dish_price");
	}

	public void setDishPrice(long dishPrice) {
		orderInfo.Set("dish_price", dishPrice);
	}

	public long getAttrPrice() {
		return (Long) orderInfo.Get("attr_price");
	}

	public void setAttrPrice(long attrPrice) {
		orderInfo.Set("attr_price", attrPrice);
	}

	public long getFinalPrice() {
		return (Long) orderInfo.Get("final_price");
	}

	public void setFinalPrice(long finalPrice) {
		orderInfo.Set("final_price", finalPrice);
	}

	public int getDishCode() {
		return (Integer) orderInfo.Get("dish_code");
	}

	public void setDishCode(int dishCode) {
		orderInfo.Set("dish_code", dishCode);
	}

	public short getVotingStar() {
		return (Short) orderInfo.Get("voting_star");
	}

	public void setVotingStar(short votingStar) {
		orderInfo.Set("voting_star", votingStar);
	}

	public String getComment() {
		String cmt = (String) orderInfo.Get("comment");
		if (cmt == null || cmt.length() == 0) {
			return "";
		}
		cmt = cmt.replace("<", "&lt;").replace(">", "&gt;");
		return cmt;
	}
	
	public String getFormatComment() {
		String cmt = (String) orderInfo.Get("comment");
		if (cmt == null || cmt.length() == 0) {
			return "";
		}
		cmt = cmt.replace("<", "&lt;").replace(">", "&gt;");
		cmt = cmt.replaceAll("\r\n", "<br/>").replaceAll("[\r\n]", "<br/>");
		return cmt;
	}

	public void setComment(String comment) {
		orderInfo.Set("comment", comment);
	}

	public long getQuantity() {
		return (Long) orderInfo.Get("quantity");
	}

	public void setQuantity(long quantity) {
		orderInfo.Set("quantity", quantity);
	}

	public String getSize() {
		String str = (String) orderInfo.Get("size");
		if (str != null) {
			str = StringEscapeUtils.unescapeHtml(str);
		}
		return str;
	}

	public void setSize(String size) {
		orderInfo.Set("size", size);
	}

	public String getIce() {
		String str = (String) orderInfo.Get("ice");
		if (str != null) {
			str = StringEscapeUtils.unescapeHtml(str);
		}
		return str;
	}

	public void setIce(String ice) {
		orderInfo.Set("ice", ice);
	}

	public String getSugar() {
		String str = (String) orderInfo.Get("sugar");
		if (str != null) {
			str = StringEscapeUtils.unescapeHtml(str);
		}
		return str;
	}

	public void setSugar(String sugar) {
		orderInfo.Set("sugar", sugar);
	}

	public String getOptionList() {
		String str = (String) orderInfo.Get("option_list");
		if (str != null) {
			str = StringEscapeUtils.unescapeHtml(str);
		}
		return str;
	}

	public void setOptionList(String optionList) {
		orderInfo.Set("option_list", optionList);
	}

	public int getFlag() {
		return (Integer) orderInfo.Get("flag");
	}

	public void setFlag(int flag) {
		orderInfo.Set("flag", flag);
	}

	public String getCreated() {
		return (String) orderInfo.Get("created");
	}

	public void setCreated(String created) {
		orderInfo.Set("created", created);
	}

	public Date getCreatedDate() {
		String strCreated = getCreated();
		if (strCreated == null) {
			return null;
		}
		return CommonMethod.getDate(strCreated, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getUpdated() {
		return (String) orderInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		orderInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}
	
	public boolean isVoted() {
		int flag = getFlag();
		if (MilkTeaOrderFlag.VOTE.isValidFlag(flag) || MilkTeaOrderFlag.VOTE_CRON.isValidFlag(flag)) {
			return true;
		}
		return false;
	}
	
	public boolean isCommented() {
		return MilkTeaOrderFlag.COMMENT.isValidFlag(getFlag());
	}
	
	public boolean isPaid() {
		return MilkTeaOrderFlag.PAID.isValidFlag(getFlag());
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
			for (String name : optionList.split(",", getLimitSplitOption())) {
				if (name.isEmpty()) {
					total += 100;
					continue;
				}
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
			for (String name : optionList.split(",", getLimitSplitOption())) {
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
	
	public boolean isRoughtlySimilar(Order other) {
		if (this.getDishPrice() != other.getDishPrice()) {
			return false;
		}
		if (this.getAttrPrice() != other.getAttrPrice()) {
			return false;
		}
		if (!this.getDishName().equals(other.getDishName())) {
			return false;
		}
		if (!this.getSize().equals(other.getSize())) {
			return false;
		}
		if (!this.getSugar().equals(other.getSugar())) {
			return false;
		}
		if (!this.getDishType().equals(other.getDishType())) {
			return false;
		}
		if (!this.getIce().equals(other.getIce())) {
			return false;
		}
		if (!this.getOptionList().equals(other.getOptionList())) {
			return false;
		}
		return true;
	}

}

