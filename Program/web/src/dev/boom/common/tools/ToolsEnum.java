package dev.boom.common.tools;

import java.util.ArrayList;
import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.services.World;

public enum ToolsEnum {
	NONE(0, 0, "", "", 0, "", false), 
	NYAGA(1, 1, "MSG_TOOLS_NYAGA_SEARCH", "img/tools/nyaga/title_image_%d.png", 50, "tools/nyaga_cat_search.htm", false),
	GAME(2, 2, "MSG_TOOLS_DEVICE_MANAGEMENT", "img/tools/device/device_title_%d.png", 30, "tools/game_list.htm", true),
	;

	private int index;
	private int category;
	private String label;
	private String image;
	private int imageRotation;
	private String page;
	private boolean requireLogin;

	private ToolsEnum(int index, int category, String label, String image, int imageRotation, String page, boolean requireLogin) {
		this.index = index;
		this.category = category;
		this.label = label;
		this.image = image;
		this.imageRotation = imageRotation;
		this.page = page;
		this.requireLogin = requireLogin;
	}

	public int getIndex() {
		return index;
	}

	public int getCategory() {
		return category;
	}

	public String getLabel() {
		return label;
	}

	public String getImage() {
		if (imageRotation <= 0) {
			return image;
		}
		return String.format(image, CommonMethod.randomNumber(1, imageRotation));
	}

	public int getImageRotation() {
		return imageRotation;
	}

	public String getPage() {
		return page;
	}

	public boolean isRequireLogin() {
		return requireLogin;
	}

	public static ToolsEnum valueOf(int index) {
		for (ToolsEnum type : ToolsEnum.values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		return ToolsEnum.NONE;
	}

	public static List<ToolsEnum> listValidGame(World worldInfo) {
		List<ToolsEnum> ret = new ArrayList<>();
		for (ToolsEnum tool : ToolsEnum.values()) {
			if (tool == ToolsEnum.NONE || !worldInfo.isActiveToolsFlag(tool)) {
				continue;
			}
			ret.add(tool);
		}
		return ret;
	}
}
