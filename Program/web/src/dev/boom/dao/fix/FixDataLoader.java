package dev.boom.dao.fix;

import java.util.Map;

import javax.servlet.ServletContext;

import dev.boom.dao.core.DaoValue;

public class FixDataLoader {

	private static final String[][] fixDataClassNames = {
			// { ClassName, DataFileName[.csv] }
			{ "SurveyValidCodeData", "survey_valid_code_data" },
			{ "CannonBlockData", "cannon_block_data" },

	};

	public static void init(ServletContext context) {
		for (String[] name : fixDataClassNames) {
			FixData data = (FixData) FixDataBase.getInstance(name[0]);
			data.setFilePath(context.getRealPath("/WEB-INF/data/" + name[1] + ".csv"));
			context.setAttribute(name[1], data);
			FixData scdata = (FixData) context.getAttribute(name[1]);
			@SuppressWarnings("unused")
			Map<Integer, DaoValue> mapdata = scdata.getData();
		}
		// dumpMemoryUsage();
	}

	protected static void dumpMemoryUsage() {
		Runtime rt = Runtime.getRuntime();
		long max = rt.maxMemory();
		long total = rt.totalMemory();
		long free = rt.freeMemory();
		long used = (total - free);
		System.out.println("  maxMemory = " + (max / 1024.0 / 1024));
		System.out.println("totalMemory = " + (total / 1024.0 / 1024));
		System.out.println(" freeMemory = " + (free / 1024.0 / 1024));
		System.out.println(" usedMemory = " + (used / 1024.0 / 1024));
	}

	public static void destroy(ServletContext context) {
		for (String[] name : fixDataClassNames) {
			context.removeAttribute(name[1]);
		}
	}
}
