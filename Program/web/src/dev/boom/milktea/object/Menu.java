package dev.boom.milktea.object;

public class Menu {

	private String menu_name;
	private String address;
	private String url;
	private String pre_image_url;
	private String image_url;
	private int sale;
	private long max_discount;
	private String code;
	private int shipping_fee;

	public Menu() {
		super();
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public int getSale() {
		return sale;
	}

	public void setSale(int sale) {
		this.sale = sale;
	}

	public long getMax_discount() {
		return max_discount;
	}

	public void setMax_discount(long max_discount) {
		this.max_discount = max_discount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getShipping_fee() {
		return shipping_fee;
	}

	public void setShipping_fee(int shipping_fee) {
		this.shipping_fee = shipping_fee;
	}

}
