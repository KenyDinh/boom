package dev.boom.services;

import java.util.List;

public class DemoSessionContent {
	private List<DemoSessionSingleContent> content;

	public DemoSessionContent(List<DemoSessionSingleContent> content) {
		super();
		this.content = content;
	}

	public List<DemoSessionSingleContent> getContent() {
		return content;
	}

	public void setContent(List<DemoSessionSingleContent> content) {
		this.content = content;
	}
	
	public class DemoSessionSingleContent {
		private String name;
		private String imageUrl;
		private String speaker;
		
		public DemoSessionSingleContent(String name, String imageUrl, String speaker) {
			super();
			this.name = name;
			this.imageUrl = imageUrl;
			this.speaker = speaker;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public String getSpeaker() {
			return speaker;
		}
		public void setSpeaker(String speaker) {
			this.speaker = speaker;
		}
	}
	
	public boolean isValid() {
		return (content != null && !content.isEmpty());
	}
}
