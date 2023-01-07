package dev.boom.pages.sub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.pages.JsonPageBase;
import dev.boom.services.UserInfo;
import dev.boom.services.UserService;

public class SearchUser extends JsonPageBase {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!initUserInfo()) {
			return false;
		}
		return true;
	}

	@Override
	public void onRender() {
		String strName = getContext().getRequestParameter("username");
		if (StringUtils.isNotBlank(strName)) {
			strName = strName.replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\");
			List<UserInfo> userList = UserService.searchForUser(strName);
			if (userList == null || userList.isEmpty()) {
				return;
			}
			List<Map<String, String>> userMapData = new ArrayList<>();
			for (UserInfo userInfo : userList) {
				Map<String, String> map = new HashMap<>();
				map.put("id", String.valueOf(userInfo.getId()));
				map.put("username", userInfo.getUsername());
				userMapData.add(map);
			}
			putJsonData("user_list", userMapData);
		}
		super.onRender();
	}

}
