package dev.boom.services;

import dev.boom.core.BoomProperties;
import dev.boom.tbl.info.TblCarouselInfo;

public class Carousel {
	private TblCarouselInfo carouselInfo;

	public Carousel() {
		carouselInfo = new TblCarouselInfo();
	}

	public Carousel(TblCarouselInfo carouselInfo) {
		this.carouselInfo = carouselInfo;
	}

	public TblCarouselInfo getCarouselInfo() {
		return carouselInfo;
	}

	public int getId() {
		return (Integer) carouselInfo.Get("id");
	}

	public void setId(int id) {
		carouselInfo.Set("id", id);
	}

	public String getName() {
		return (String) carouselInfo.Get("name");
	}

	public void setName(String name) {
		carouselInfo.Set("name", name);
	}

	public String getDescription() {
		return (String) carouselInfo.Get("description");
	}

	public void setDescription(String description) {
		carouselInfo.Set("description", description);
	}

	public String getUrl() {
		return (String) carouselInfo.Get("url");
	}

	public void setUrl(String url) {
		carouselInfo.Set("url", url);
	}

	public byte getLocal() {
		return (Byte) carouselInfo.Get("local");
	}

	public void setLocal(byte local) {
		carouselInfo.Set("local", local);
	}

	public byte getAvailable() {
		return (Byte) carouselInfo.Get("available");
	}

	public void setAvailable(byte available) {
		carouselInfo.Set("available", available);
	}
	
	public boolean isLocal() {
		return getLocal() > 0;
	}

	public boolean isAvailable() {
		return getAvailable() > 0;
	}
	
	public String getImageUrl(String context) {
		String url = getUrl();
		if (url == null || url.isEmpty()) {
			return BoomProperties.SERVICE_HOSTNAME + context + "/img/carousel/default.png";
		}
		if (isLocal()) {
			return BoomProperties.SERVICE_HOSTNAME + context + "/img/carousel/" + url;
		}
		return url;
	}

}

