/**
 *
 */
package dev.boom.pages;

import java.util.HashMap;
import java.util.Map;

import dev.boom.core.BoomSession;
import dev.boom.core.GameLog;
import dev.boom.entity.info.UserInfo;
import dev.boom.services.UserService;
import net.arnx.jsonic.JSON;

public class JsonPageBase extends PageBase {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> jsondata = new HashMap<String, Object>();
	private boolean prettyPrint = false;

	protected UserInfo userInfo = null;

	@Override
	public String getContentType() {
		String charset = getContext().getRequest().getCharacterEncoding();

		if (charset == null) {
			return "application/json";
		} else {
			return "application/json; charset=" + charset;
		}
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		BoomSession boomSession = getBoomSession();
		if (boomSession == null) {
			GameLog.getInstance().error("[JsonPageBase] session is null!");
			return false;
		}
		userInfo = UserService.getUserById(boomSession.getId());
		if (userInfo == null) {
			GameLog.getInstance().error("[JsonPageBase] user is null!");
			return false;
		}
		
		return true;
	}

	protected UserInfo getCurrentUser() {
		return userInfo;
	}

	@Override
	public void onRender() {
		super.onRender();

		if (!jsondata.isEmpty()) {
			addModel("jsondata", JSON.encode(jsondata, prettyPrint));
		}
	}

	/**
	 * @param key
	 * @param value
	 */
	protected Object putJsonData(String key, Object value) {
		return jsondata.put(key, value);
	}

	/**
	 * @param m
	 */
	protected void putAllJsonData(Map<String, Object> m) {
		jsondata.putAll(m);
	}

	/**
	 * @param prettyPrint セットする prettyPrint
	 */
	protected void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

}
