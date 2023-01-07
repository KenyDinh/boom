package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.Department;
import dev.boom.common.milktea.MilkTeaMenuStatus;
import dev.boom.common.milktea.MilkteaMenuFlag;
import dev.boom.tbl.info.TblMenuInfo;

public class MenuInfo {

	private TblMenuInfo info = null;

	public MenuInfo(TblMenuInfo info) {
		this.info = info;
	}

	public MenuInfo() {
		this.info = new TblMenuInfo();
	}

	public TblMenuInfo getTblInfo() {
		return info;
	}
	
	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);
	}

	public String getName() {
		return this.info.getName();
	}

	public void setName(String name) {
		this.info.setName(name);
	}

	public long getShopId() {
		return this.info.getShop_id();
	}

	public void setShopId(long shop_id) {
		this.info.setShop_id(shop_id);
	}

	public short getSale() {
		return this.info.getSale();
	}

	public void setSale(short sale) {
		this.info.setSale(sale);
	}

	public String getCode() {
		return this.info.getCode();
	}

	public void setCode(String code) {
		this.info.setCode(code);
	}

	public long getMaxDiscount() {
		return this.info.getMax_discount();
	}

	public void setMaxDiscount(long max_discount) {
		this.info.setMax_discount(max_discount);
	}

	public long getShippingFee() {
		return this.info.getShipping_fee();
	}

	public void setShippingFee(long shipping_fee) {
		this.info.setShipping_fee(shipping_fee);
	}

	public String getDescription() {
		return this.info.getDescription();
	}

	public void setDescription(String description) {
		this.info.setDescription(description);
	}

	public byte getStatus() {
		return this.info.getStatus();
	}

	public void setStatus(byte status) {
		this.info.setStatus(status);
	}
	
	public int getDept() {
		return this.info.getDept();
	}
	
	public void setDept(int dept) {
		this.info.setDept(dept);
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

	public Date getExpired() {
		return this.info.getExpired();
	}
	
	public String getStrExpired() {
		return CommonMethod.getFormatDateString(getExpired(), CommonDefine.DATE_FORMAT_PATTERN);
	}

	public void setExpired(Date expired) {
		this.info.setExpired(expired);
	}
	
	public Date getUpdated() {
		return this.info.getUpdated();
	}

	public void setUpdated(Date updated) {
		this.info.setUpdated(updated);
	}
	
	public boolean isInState(MilkTeaMenuStatus status) {
		return (this.info.getStatus() == status.ordinal());
	}
	
	public boolean isOpening() {
		return (this.info.getStatus() == MilkTeaMenuStatus.OPENING.ordinal() && getExpired().after(new Date()));
	}
	
	public boolean isDelivering() {
		return (this.info.getStatus() == MilkTeaMenuStatus.DELIVERING.ordinal());
	}
	
	public boolean isCompleted() {
		return (this.info.getStatus() == MilkTeaMenuStatus.COMPLETED.ordinal());
	}
	
	public boolean isEditable() {
		return (this.info.getStatus() < MilkTeaMenuStatus.COMPLETED.ordinal());
	}
	
	public boolean isCanceled() {
		return (this.info.getStatus() == MilkTeaMenuStatus.CANCELED.ordinal());
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
	
	public boolean isAvailableForUser(UserInfo userInfo) {
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
