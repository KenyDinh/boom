package dev.boom.services;

import dev.boom.tbl.info.TblShopInfo;

public class Shop {
	private TblShopInfo shopInfo;

	public Shop() {
		shopInfo = new TblShopInfo();
	}

	public Shop(TblShopInfo shopInfo) {
		this.shopInfo = shopInfo;
	}

	public TblShopInfo getShopInfo() {
		return shopInfo;
	}

	public long getId() {
		return (Long) shopInfo.Get("id");
	}

	public void setId(long id) {
		shopInfo.Set("id", id);
	}

	public String getName() {
		return (String) shopInfo.Get("name");
	}

	public void setName(String name) {
		shopInfo.Set("name", name);
	}

	public String getUrl() {
		return (String) shopInfo.Get("url");
	}

	public void setUrl(String url) {
		shopInfo.Set("url", url);
	}

	public String getAddress() {
		return (String) shopInfo.Get("address");
	}

	public void setAddress(String address) {
		shopInfo.Set("address", address);
	}

	public String getPreImageUrl() {
		return (String) shopInfo.Get("pre_image_url");
	}

	public void setPreImageUrl(String preImageUrl) {
		shopInfo.Set("pre_image_url", preImageUrl);
	}

	public String getImageUrl() {
		return (String) shopInfo.Get("image_url");
	}

	public void setImageUrl(String imageUrl) {
		shopInfo.Set("image_url", imageUrl);
	}

	public long getOpeningCount() {
		return (Long) shopInfo.Get("opening_count");
	}

	public void setOpeningCount(long openingCount) {
		shopInfo.Set("opening_count", openingCount);
	}

	public long getOrderedDishCount() {
		return (Long) shopInfo.Get("ordered_dish_count");
	}

	public void setOrderedDishCount(long orderedDishCount) {
		shopInfo.Set("ordered_dish_count", orderedDishCount);
	}

	public long getVotingCount() {
		return (Long) shopInfo.Get("voting_count");
	}

	public void setVotingCount(long votingCount) {
		shopInfo.Set("voting_count", votingCount);
	}

	public long getStarCount() {
		return (Long) shopInfo.Get("star_count");
	}

	public void setStarCount(long starCount) {
		shopInfo.Set("star_count", starCount);
	}

}

