package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblBoomGameItemData extends DaoValueData {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "boom_game_item_data";
	private static final String PRIMARY_KEY = "id";

	public int id;
	public int type;
	public int target_type;
	public int effect_id_1;
	public int effect_param_1;
	public int effect_duration_1;
	public int effect_id_2;
	public int effect_param_2;
	public int effect_duration_2;
	public int effect_id_3;
	public int effect_param_3;
	public int effect_duration_3;
	public int imageID;
	public int prob_rate;
	public String label_explain;

	public TblBoomGameItemData() {
		this.id = 0;
		this.type = 0;
		this.target_type = 0;
		this.effect_id_1 = 0;
		this.effect_param_1 = 0;
		this.effect_duration_1 = 0;
		this.effect_id_2 = 0;
		this.effect_param_2 = 0;
		this.effect_duration_2 = 0;
		this.effect_id_3 = 0;
		this.effect_param_3 = 0;
		this.effect_duration_3 = 0;
		this.imageID = 0;
		this.prob_rate = 0;
		this.label_explain = "";
		Sync();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTarget_type() {
		return target_type;
	}

	public void setTarget_type(int target_type) {
		this.target_type = target_type;
	}

	public int getEffect_id_1() {
		return effect_id_1;
	}

	public void setEffect_id_1(int effect_id_1) {
		this.effect_id_1 = effect_id_1;
	}

	public int getEffect_param_1() {
		return effect_param_1;
	}

	public void setEffect_param_1(int effect_param_1) {
		this.effect_param_1 = effect_param_1;
	}

	public int getEffect_duration_1() {
		return effect_duration_1;
	}

	public void setEffect_duration_1(int effect_duration_1) {
		this.effect_duration_1 = effect_duration_1;
	}

	public int getEffect_id_2() {
		return effect_id_2;
	}

	public void setEffect_id_2(int effect_id_2) {
		this.effect_id_2 = effect_id_2;
	}

	public int getEffect_param_2() {
		return effect_param_2;
	}

	public void setEffect_param_2(int effect_param_2) {
		this.effect_param_2 = effect_param_2;
	}

	public int getEffect_duration_2() {
		return effect_duration_2;
	}

	public void setEffect_duration_2(int effect_duration_2) {
		this.effect_duration_2 = effect_duration_2;
	}

	public int getEffect_id_3() {
		return effect_id_3;
	}

	public void setEffect_id_3(int effect_id_3) {
		this.effect_id_3 = effect_id_3;
	}

	public int getEffect_param_3() {
		return effect_param_3;
	}

	public void setEffect_param_3(int effect_param_3) {
		this.effect_param_3 = effect_param_3;
	}

	public int getEffect_duration_3() {
		return effect_duration_3;
	}

	public void setEffect_duration_3(int effect_duration_3) {
		this.effect_duration_3 = effect_duration_3;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}

	public int getProb_rate() {
		return prob_rate;
	}

	public void setProb_rate(int prob_rate) {
		this.prob_rate = prob_rate;
	}

	public String getLabel_explain() {
		return label_explain;
	}

	public void setLabel_explain(String label_explain) {
		this.label_explain = label_explain;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

}
