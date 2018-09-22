package dev.boom.tbl.info;

import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblSurveyOptionInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "survey_option_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long survey_id;
	private String name;
	private String content;
	private String image;
	private String video;
	private String ref_url;

	public TblSurveyOptionInfo() {
		this.id = 0;
		this.survey_id = 0;
		this.name = "";
		this.content = "";
		this.image = "";
		this.video = "";
		this.ref_url = "";
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSurvey_id() {
		return survey_id;
	}

	public void setSurvey_id(long survey_id) {
		this.survey_id = survey_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getRef_url() {
		return ref_url;
	}

	public void setRef_url(String ref_url) {
		this.ref_url = ref_url;
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
