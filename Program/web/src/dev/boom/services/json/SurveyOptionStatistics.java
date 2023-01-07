package dev.boom.services.json;

import java.text.DecimalFormat;

public class SurveyOptionStatistics {

	public long id;
	public long questId;
	public String title;
	public int count;
	public int total;

	public SurveyOptionStatistics() {
	}

	public SurveyOptionStatistics(long id, long questId, String title, int count, int total) {
		super();
		this.id = id;
		this.questId = questId;
		this.title = title;
		this.count = count;
		this.total = total;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getQuestId() {
		return questId;
	}

	public void setQuestId(long questId) {
		this.questId = questId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void addCount() {
		this.count++;
	}

	public String getRate() {
		if (this.total == 0 || this.count == 0) {
			return "0.00";
		}
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			double rate = ((double)this.count * 100) / this.total;
			return df.format(rate);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "0.00";
	}

}
