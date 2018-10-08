package dev.boom.core;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.click.ClickServlet;

public class FileServlet extends ClickServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		String path = System.getProperty("catalina.base") + File.separator + "temp" + File.separator + getServletContext().getInitParameter("file.dir");
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
		if (filename.isEmpty()) {
			filename = "404.png";
		}
		String path = System.getProperty("catalina.base") + File.separator + "temp" + File.separator + getServletContext().getInitParameter("file.dir");
		File file = new File(path, filename);
		if (!file.exists()) {
			file = new File(path, "404.png");
		}
		try {
			response.setHeader("Content-Type", getServletContext().getMimeType(filename));
			response.setHeader("Content-Length", String.valueOf(file.length()));
			response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
			Files.copy(file.toPath(), response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
