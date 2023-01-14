package dev.boom.pages.tools.sub;

import java.util.List;

import dev.boom.core.BoomSession;
import dev.boom.core.GameLog;
import dev.boom.pages.PageBase;
import dev.boom.services.Device;
import dev.boom.services.DeviceService;
import dev.boom.services.User;
import dev.boom.services.UserService;

public class DeviceLoader extends PageBase {

	private static final long serialVersionUID = 1L;
	
	private  User userInfo = null;
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
	@Override
	public void onRender() {
		super.onRender();
		List<Device> deviceList = DeviceService.listAllDevice();
		if (deviceList == null || deviceList.isEmpty()) {
			return;
		}
		String contextPath = getHostURL() + getContextPath();
		addModel("device_table", DeviceService.getRenderTableDeviceList(userInfo, deviceList, getMessages(), contextPath));
		//addModel("tabs", DeviceService.getRenderDeviceTab(getMessages()));
	}
	
}
