package dev.boom.services;

import dev.boom.core.BoomProperties;
import dev.boom.tbl.info.TblCarouselInfo;

public class CarouselInfo {

	private TblCarouselInfo info = null;

	public CarouselInfo(TblCarouselInfo info) {
		this.info = info;
	}

	public CarouselInfo() {
		this.info = new TblCarouselInfo();
	}

	public TblCarouselInfo getTblCarouselInfo() {
		return info;
	}

	public int getId() {
		return this.info.getId();
	}

	public String getName() {
		return this.info.getName();
	}

	public String getDescription() {
		return this.info.getDescription();
	}

	public boolean isLocal() {
		return this.info.getLocal() > 0;
	}

	public boolean isAvailable() {
		return this.info.getAvailable() > 0;
	}

	public String getImageUrl(String context) {
		String url = this.info.getUrl();
		if (url == null || url.isEmpty()) {
			return BoomProperties.SERVICE_HOSTNAME + context + "/img/carousel/default.png";
		}
		if (isLocal()) {
			return BoomProperties.SERVICE_HOSTNAME + context + "/img/carousel/" + url;
		}
		return url;
	}

}
