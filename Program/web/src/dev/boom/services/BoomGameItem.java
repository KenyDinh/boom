package dev.boom.services;

import dev.boom.game.boom.BoomGameItemEffect;
import dev.boom.game.boom.BoomGameItemTargetType;
import dev.boom.game.boom.BoomGameItemType;
import dev.boom.tbl.data.TblBoomGameItemData;

public class BoomGameItem {

	public static final int MAX_ITEM_EFFECT = 3;
	private TblBoomGameItemData tblData;

	public BoomGameItem() {
		tblData = new TblBoomGameItemData();
	}

	public BoomGameItem(TblBoomGameItemData tblData) {
		super();
		this.tblData = tblData;
	}
	
	public int getId() {
		return (Integer) tblData.Get("id");
	}
	
	public int getType() {
		return (Integer) tblData.Get("type");
	}
	
	public int getTargetType() {
		return (Integer) tblData.Get("target_type");
	}
	
	public BoomGameItemType getItemType() {
		return BoomGameItemType.valueOf(getId());
	}
	
	public BoomGameItemTargetType getItemTargetType() {
		return BoomGameItemTargetType.valueOf(getTargetType());
	}
	
	/**
	 * 
	 * @param index (1-3)
	 * @return effect id of the item
	 */
	public int getItemEffectId(int index) {
		if (index  <= 0 || index > MAX_ITEM_EFFECT) {
			return 0;
		}
		return (Integer) tblData.Get("effect_id_" + index);
	}
	
	public int getItemEffectParam(int index) {
		if (index  <= 0 || index > MAX_ITEM_EFFECT) {
			return 0;
		}
		return (Integer) tblData.Get("effect_param_" + index);
	}
	
	public int getItemEffectDuration(int index) {
		if (index  <= 0 || index > MAX_ITEM_EFFECT) {
			return 0;
		}
		return (Integer) tblData.Get("effect_duration_" + index);
	}
	
	public int getImageID() {
		return (Integer) tblData.Get("imageID");
	}
	
	public int getProbRate() {
		return (Integer) tblData.Get("prob_rate");
	}
	
	public String getLableExplain() {
		return (String) tblData.Get("label_explain");
	}
	
	public boolean isHealingItem() {
		for (int i = 0; i < BoomGameItem.MAX_ITEM_EFFECT; i++) {
			int effectID = getItemEffectId(i + 1);
			if (effectID <= 0) {
				continue;
			}
			BoomGameItemEffect effect = BoomGameItemEffect.valueOf(effectID);
			if (effect == BoomGameItemEffect.NONE) {
				continue;
			}
			if (effect == BoomGameItemEffect.RECOVER_HP) {
				return true;
			}
		}
		return false;
	}
	
}
