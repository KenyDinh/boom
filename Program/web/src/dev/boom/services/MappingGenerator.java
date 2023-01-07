package dev.boom.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.boom.dao.core.DaoValueInfo;
import dev.boom.tbl.info.TblSurveyInfo;
import dev.boom.tbl.info.TblSurveyOptionInfo;
import dev.boom.tbl.info.TblSurveyQuestionInfo;
import dev.boom.tbl.info.TblSurveyResultInfo;

public class MappingGenerator {
	
	private static final String MAPPING_PATH = "D:\\Developers\\mapping";
	private static final String MAPPING_START = "<?xml version=\"1.0\"?>\n"
			+ "<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n"
			+ "\"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">\n\n"
			+ "<hibernate-mapping>";
	private static final String MAPPING_END = "</hibernate-mapping>";
	

	public static void main(String[] args) {
		List<DaoValueInfo> daoList = new ArrayList<>();
		daoList.add(new TblSurveyInfo());
		daoList.add(new TblSurveyOptionInfo());
		daoList.add(new TblSurveyQuestionInfo());
		daoList.add(new TblSurveyResultInfo());
		generateMapping(daoList);
	}
	
	private static void generateMapping(List<DaoValueInfo> daoList) {
		if (daoList == null || daoList.isEmpty()) {
			return;
		}
		for (DaoValueInfo info : daoList) {
			StringBuilder sb = new StringBuilder();
			sb.append(MAPPING_START);
			sb.append(getMappingClass(info));
			sb.append(MAPPING_END);
			String fileName = String.format("%s\\%s.hbm.xml", MAPPING_PATH, info.getClass().getSimpleName());
			createFile(fileName, sb.toString());
		}
	}
	
	private static String getMappingClass(DaoValueInfo daoInfo) {
		StringBuilder sb = new StringBuilder();
		try {
			List<Field> fieldList = daoInfo.getFieldList();
			sb.append("\n");
			sb.append(String.format("\t<class name=\"dev.boom.tbl.info.%s\" table=\"%s\" catalog=\"boom\">", daoInfo.getClass().getSimpleName(), daoInfo.getTableName()));
			sb.append("\n");
			for (Field field : fieldList) {
				if (field.getName().equals(daoInfo.getPrimaryKey())) {
					sb.append(String.format("\t\t<id name=\"%s\" type=\"%s\">", field.getName(), getStrFieldType(field)));
					sb.append("\n");
					sb.append(String.format("\t\t\t<column name=\"%s\" />", field.getName()));
					sb.append("\n");
					sb.append("\t\t\t<generator class=\"increment\" />");
					sb.append("\n");
					sb.append("\t\t</id>");
				} else {
					sb.append(String.format("\t\t<property name=\"%s\" type=\"%s\">", field.getName(), getStrFieldType(field)));
					sb.append("\n");
					sb.append(String.format("\t\t\t<column name=\"%s\" not-null=\"true\" />", field.getName()));
					sb.append("\n");
					sb.append("\t\t</property>");
				}
				sb.append("\n");
 			}
			sb.append("\t</class>");
			sb.append("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private static String getStrFieldType(Field field) {
		if (field.getType() == Byte.TYPE) {
			return "java.lang.Byte";
		} else if (field.getType() == Short.TYPE) {
			return "java.lang.Short";
		} else if (field.getType() == Integer.TYPE) {
			return "java.lang.Integer";
		} else if (field.getType() == Long.TYPE) {
			return "java.lang.Long";
		} else if (field.getType() == Date.class) {
			return "java.util.Date";
		} else {
			return "java.lang.String";
		}
	}
	
	private static void createFile(String filename, String content) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename)), StandardCharsets.UTF_8));
			bw.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
