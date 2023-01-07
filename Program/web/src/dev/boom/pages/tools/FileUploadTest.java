package dev.boom.pages.tools;

import java.io.File;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.RandomStringUtils;

import dev.boom.core.GameLog;
import dev.boom.pages.PageBase;

public class FileUploadTest extends PageBase {

	private static final long serialVersionUID = 1L;

	@Override
	public void onPost() {
		super.onPost();

		FileItem fileItem = getContext().getFileItem("file");
		if (fileItem == null) {
			GameLog.getInstance().error("No file found!");
			return;
		}
		String fileName = fileItem.getName();
		GameLog.getInstance().info("Receive file : " + fileName);
		String fileSource = getContext().getRequestParameter("source");
		if (fileSource != null) {
			GameLog.getInstance().info("file Source : " + fileSource);
		}
		String imgExt = fileName.substring(fileName.lastIndexOf(".") + 1);
		String newName = RandomStringUtils.randomAlphanumeric(10) + "." + imgExt;
		File newFile = new File(getFileUploadDir(), newName);
		try {
			fileItem.write(newFile);
			GameLog.getInstance().info("[ManageVote] created new image file: " + newName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRender() {
		super.onRender();
		addModel("result", "{\"success\":1}");
	}

}
