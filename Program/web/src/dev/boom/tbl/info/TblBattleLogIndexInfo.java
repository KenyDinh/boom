package dev.boom.tbl.info;

import java.util.Date;

import dev.boom.dao.core.DaoValueInfo;

public class TblBattleLogIndexInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "battle_log_index_info";
	private static final String PRIMARY_KEY = "player_id";

	/** プレイヤーID（unsigned int） */
	private long player_id;

	/** 直近の回想logインデックス */
	private short log_index;

	/** 更新日 */
	private Date updated;

	public TblBattleLogIndexInfo() {
		player_id = 0;
		log_index = 0;
		updated = new Date();
		Sync();
	}

	public long getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(long player_id) {
		this.player_id = player_id;
	}

	public short getLog_index() {
		return log_index;
	}

	public void setLog_index(short log_index) {
		this.log_index = log_index;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
