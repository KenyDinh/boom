package dev.boom.services;

import dev.boom.tbl.info.TblShopInfo;

public class ShopInfo {

	private TblShopInfo info = null;

	public ShopInfo(TblShopInfo info) {
		this.info = info;
	}

	public ShopInfo() {
		this.info = new TblShopInfo();
	}
	
	public TblShopInfo getTblInfo() {
		return this.info;
	}
	
	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);
	}

	public String getUrl() {
		return this.info.getUrl();
	}

	public void setUrl(String url) {
		this.info.setUrl(url);
	}

	public String getName() {
		return this.info.getName();
	}

	public void setName(String name) {
		this.info.setName(name);
	}

	public String getAddress() {
		return this.info.getAddress();
	}

	public void setAddress(String address) {
		this.info.setAddress(address);
	}

	public String getPreImageUrl() {
		return this.info.getPre_image_url();
	}

	public void setPreImageUrl(String pre_image_url) {
		this.info.setPre_image_url(pre_image_url);
	}

	public String getImageUrl() {
		return this.info.getImage_url();
	}

	public void setImageUrl(String image_url) {
		this.info.setImage_url(image_url);
	}

	public long getOpeningCount() {
		return this.info.getOpening_count();
	}

	public void setOpeningCount(long opening_count) {
		this.info.setOpening_count(opening_count);
	}

	public long getOrderedDishCount() {
		return this.info.getOrdered_dish_count();
	}

	public void setOrderedDishCount(long ordered_dish_count) {
		this.info.setOrdered_dish_count(ordered_dish_count);
	}

	public long getVotingCount() {
		return this.info.getVoting_count();
	}

	public void setVotingCount(long voting_count) {
		this.info.setVoting_count(voting_count);
	}

	public long getStarCount() {
		return this.info.getStar_count();
	}

	public void setStarCount(long star_count) {
		this.info.setStar_count(star_count);
	}
}
