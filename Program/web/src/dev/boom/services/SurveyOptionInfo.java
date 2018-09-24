package dev.boom.services;

import dev.boom.tbl.info.TblSurveyOptionInfo;

public class SurveyOptionInfo {

	private int selectedCount = 0;
	private TblSurveyOptionInfo info = null;

	public SurveyOptionInfo() {
		this.info = new TblSurveyOptionInfo();
	}

	public SurveyOptionInfo(TblSurveyOptionInfo info) {
		this.info = info;
	}

	public TblSurveyOptionInfo getInfo() {
		return info;
	}

	public int getSelectedCount() {
		return selectedCount;
	}

	public void setSelectedCount(int selectedCount) {
		this.selectedCount = selectedCount;
	}

	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);
	}

	public long getSurveyId() {
		return this.info.getSurvey_id();
	}

	public void setSurveyId(long survey_id) {
		this.info.setSurvey_id(survey_id);
	}

	public String getName() {
		return this.info.getName();
	}

	public void setName(String name) {
		this.info.setName(name);
	}

	public String getContent() {
		return this.info.getContent();
	}

	public void setContent(String content) {
		this.info.setContent(content);
	}

	public String getImage() {
		return this.info.getImage();
	}

	public void setImage(String image) {
		this.info.setImage(image);
	}

	public String getVideo() {
		return this.info.getVideo();
	}

	public void setVideo(String video) {
		this.info.setVideo(video);
	}

	public String getRefUrl() {
		return this.info.getRef_url();
	}

	public void setRefUrl(String ref_url) {
		this.info.setRef_url(ref_url);
	}

}
