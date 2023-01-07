package dev.boom.pages.tools;

import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.common.enums.DeviceDept;
import dev.boom.common.enums.DeviceFlag;
import dev.boom.common.enums.DeviceType;
import dev.boom.common.enums.FridayThemes;
import dev.boom.common.tools.ToolsEnum;
import dev.boom.pages.Tools;
import dev.boom.services.Device;
import dev.boom.services.DeviceService;
import dev.boom.socket.SocketSessionPool;
import dev.boom.socket.endpoint.DeviceEndPoint;

public class DeviceManager extends Tools {

	private static final long serialVersionUID = 1L;

	public DeviceManager() {
		setDataTableFormat(true);
		initTheme(FridayThemes.getRandomTheme());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new JsImport("/js/lib/jquery.auto-complete.js"));
		headElements.add(importJs("/js/tools/device-manager.js?device-manager.js"));
		headElements.add(new CssImport("/css/lib/jquery.auto-complete.css"));
		headElements.add(importCss("/css/tools/device_manager.css"));

		return headElements;
	}

	@Override
	public void onInit() {
		super.onInit();
		if (getUserInfo() != null) {
			addModel("user", getUserInfo());
			if (getUserInfo().isDeviceAdmin()) {
				addModel("admin", "1");
			}
		}
	}

	@Override
	public void onPost() {
		super.onPost();
	}

	@Override
	public void onGet() {
		super.onGet();
	}

	@Override
	public void onRender() {
		super.onRender();
		addModel("type_list", DeviceType.getAvailableType());
		addModel("dep_list", DeviceDept.values());
		addModel("permission_list", DeviceFlag.values());
		addModel("tabs", DeviceService.getRenderDeviceTab(getMessages()));
		if (getUserInfo() == null) {
			return;
		}
		String deviceToken = SocketSessionPool.generateValidToken(DeviceEndPoint.ENDPOINT_NAME, getUserInfo());
		addModel("deviceToken", getSocketUrl(DeviceEndPoint.SOCKET_PATH, "?" + DeviceEndPoint.VALIDATION_KEY + "=" + deviceToken));
		List<Device> deviceList = DeviceService.listAllDevice();
		if (deviceList == null || deviceList.isEmpty()) {
			return;
		}
		String contextPath = getHostURL() + getContextPath();
		addModel("device_table", DeviceService.getRenderTableDeviceList(getUserInfo(), deviceList, getMessages(), contextPath));
	}
	
	protected int getToolsIndex() {
		return ToolsEnum.GAME.getIndex();
	}

}
