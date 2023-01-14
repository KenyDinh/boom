package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.Department;
import dev.boom.common.milktea.MilkTeaMenuStatus;
import dev.boom.common.milktea.MilkteaMenuFlag;
import dev.boom.tbl.info.TblMenuInfo;

public class Menu {
	private TblMenuInfo menuInfo;

	public Menu() {
		menuInfo = new TblMenuInfo();
	}

	public Menu(TblMenuInfo menuInfo) {
		this.menuInfo = menuInfo;
	}

	public TblMenuInfo getMenuInfo() {
		return menuInfo;
	}

	public long getId() {
		return (Long) menuInfo.Get("id");
	}

	public void setId(long id) {
		menuInfo.Set("id", id);
	}

	public String getName() {
		return (String) menuInfo.Get("name");
	}

	public void setName(String name) {
		menuInfo.Set("name", name);
	}

	public long getShopId() {
		return (Long) menuInfo.Get("shop_id");
	}

	public void setShopId(long shopId) {
		menuInfo.Set("shop_id", shopId);
	}

	public short getSale() {
		return (Short) menuInfo.Get("sale");
	}

	public void setSale(short sale) {
		menuInfo.Set("sale", sale);
	}

	public String getCode() {
		return (String) menuInfo.Get("code");
	}

	public void setCode(String code) {
		menuInfo.Set("code", code);
	}

	public long getMaxDiscount() {
		return (Long) menuInfo.Get("max_discount");
	}

	public void setMaxDiscount(long maxDiscount) {
		menuInfo.Set("max_discount", maxDiscount);
	}

	public long getShippingFee() {
		return (Long) menuInfo.Get("shipping_fee");
	}

	public void setShippingFee(long shippingFee) {
		menuInfo.Set("shipping_fee", shippingFee);
	}

	public String getDescription() {
		return (String) menuInfo.Get("description");
	}

	public void setDescription(String description) {
		menuInfo.Set("description", description);
	}

	public byte getStatus() {
		return (Byte) menuInfo.Get("status");
	}

	public void setStatus(byte status) {
		menuInfo.Set("status", status);
	}

	public int getDept() {
		return (Integer) menuInfo.Get("dept");
	}

	public void setDept(int dept) {
		menuInfo.Set("dept", dept);
	}

	public int getFlag() {
		return (Integer) menuInfo.Get("flag");
	}

	public void setFlag(int flag) {
		menuInfo.Set("flag", flag);
	}

	public String getCreated() {
		return (String) menuInfo.Get("created");
	}

	public void setCreated(String created) {
		menuInfo.Set("created", created);
	}

	public Date getCreatedDate() {
		String strCreated = getCreated();
		if (strCreated == null) {
			return null;
		}
		return CommonMethod.getDate(strCreated, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getExpired() {
		return (String) menuInfo.Get("expired");
	}

	public void setExpired(String expired) {
		menuInfo.Set("expired", expired);
	}

	public Date getExpiredDate() {
		String strExpired = getExpired();
		if (strExpired == null) {
			return null;
		}
		return CommonMethod.getDate(strExpired, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getUpdated() {
		return (String) menuInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		menuInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}
	
	public boolean isInState(MilkTeaMenuStatus status) {
		return (getStatus() == status.ordinal());
	}
	
	public boolean isOpening() {
		return (getStatus() == MilkTeaMenuStatus.OPENING.ordinal() && getExpiredDate().after(new Date()));
	}
	
	public boolean isDelivering() {
		return (getStatus() == MilkTeaMenuStatus.DELIVERING.ordinal());
	}
	
	public boolean isCompleted() {
		return (getStatus() == MilkTeaMenuStatus.COMPLETED.ordinal());
	}
	
	public boolean isEditable() {
		return (getStatus() < MilkTeaMenuStatus.COMPLETED.ordinal());
	}
	
	public boolean isCanceled() {
		return (getStatus() == MilkTeaMenuStatus.CANCELED.ordinal());
	}
	
	public void addFlag(MilkteaMenuFlag mmf) {
		if (mmf == MilkteaMenuFlag.INVALID) {
			return;
		}
		int newFlag = getFlag() | mmf.getFlag();
		setFlag(newFlag);
	}
	
	public void addDeptFlag(Department dept) {
		if (dept == Department.NONE) {
			return;
		}
		setDept((getDept() | dept.getFlag())); 
	}
	
	public boolean isActiveDeptFlag(int dept) {
		return (getDept() & dept) > 0;
	}
	
	public boolean isActiveFlag(MilkteaMenuFlag mmf) {
		if (mmf == MilkteaMenuFlag.INVALID) {
			return false;
		}
		return mmf.isValidFlag(getFlag());
	}
	
	public boolean isAvailableForUser(User userInfo) {
		if (getDept() == 0 ) {
			return true;
		}
		if (getDept() == Department.getAllFlag()) {
			return true;
		}
		if (userInfo != null && userInfo.isMilkteaAdmin()) {
			return true;
		}
		if (userInfo != null && (getDept() & userInfo.getDept()) > 0) {
			return true;
		}
		
		return false;
	}

}

