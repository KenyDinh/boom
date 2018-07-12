package dev.boom.entity.info;

import java.util.List;

import dev.boom.dao.core.IDaoValue;

public class ShopInfo extends DaoValueInfo implements IDaoValue {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "shop_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private String url;
	private String name;
	private String address;
	private String pre_image_url;
	private String image_url;
	private long opening_count;
	private long ordered_dish_count;
	private long voting_count;
	private long star_count;

	public ShopInfo() {
		this.id = 0;
		this.url = "";
		this.name = "";
		this.address = "";
		this.pre_image_url = "";
		this.image_url = "";
		this.opening_count = 0;
		this.ordered_dish_count = 0;
		this.voting_count = 0;
		this.star_count = 0;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPre_image_url() {
		return pre_image_url;
	}

	public void setPre_image_url(String pre_image_url) {
		this.pre_image_url = pre_image_url;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public long getOpening_count() {
		return opening_count;
	}

	public void setOpening_count(long opening_count) {
		this.opening_count = opening_count;
	}

	public long getOrdered_dish_count() {
		return ordered_dish_count;
	}

	public void setOrdered_dish_count(long ordered_dish_count) {
		this.ordered_dish_count = ordered_dish_count;
	}

	public long getVoting_count() {
		return voting_count;
	}

	public void setVoting_count(long voting_count) {
		this.voting_count = voting_count;
	}

	public long getStar_count() {
		return star_count;
	}

	public void setStar_count(long star_count) {
		this.star_count = star_count;
	}

	public List<String> getSubKey() {
		return null;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

}
