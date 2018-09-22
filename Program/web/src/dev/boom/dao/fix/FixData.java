package dev.boom.dao.fix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dev.boom.dao.core.DaoValue;

public abstract class FixData extends FixDataBase {

	private static Log log = LogFactory.getLog(FixData.class);

	private Map<Integer, DaoValue> fixdata = new LinkedHashMap<>();
	private Object __lock = new Object();

	public Map<Integer, DaoValue> getData() {
		File file = loadFile();
		if (file != null) {
			synchronized (__lock) {
				if (isModified(file)) {
					update(file);
				}
			}
		}
		return fixdata;
	}

	protected abstract List<Field> getField();

	private Date lastModified = new Date(0);

	private String filePath = null;

	public void setFilePath(String path) {
		filePath = path;
	}

	private File loadFile() {
		File file = new File(filePath);
		if (!file.exists()) {
			log.error(filePath + " not found.");
			return null;
		}
		return file;
	}

	private boolean isModified(File file) {
		return (lastModified.compareTo(new Date(file.lastModified())) != 0);
	}

	private void update(File file) {
		if (!fixdata.isEmpty()) {
			fixdata.clear();
		}

		BufferedReader breader = null;
		try {
			breader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			try {
				Class<? extends Object> retValue = Class.forName("dev.boom.tbl.data.Tbl" + this.getClass().getSimpleName().substring(3));
				List<Field> fields = getField();
				String line = null;
				while ((line = breader.readLine()) != null) {
					if (line.matches("^$")) {
						continue;
					}
					if (line.matches("^#.*")) {
						continue;
					}

					String[] tokens = line.split(",\\s*", fields.size());
					try {
						DaoValue retObject = (DaoValue) retValue.newInstance();
						int i = 0;
						for (String token : tokens) {
							Field field = fields.get(i++);
							try {
								if (field.getType() == String.class) {
									retObject.Set(field.getName(), token);
								} else if (field.getType() == Byte.TYPE) {
									retObject.Set(field.getName(), Byte.parseByte(token));
								} else if (field.getType() == Short.TYPE) {
									retObject.Set(field.getName(), Short.parseShort(token));
								} else if (field.getType() == Integer.TYPE) {
									retObject.Set(field.getName(), Integer.parseInt(token));
								} else if (field.getType() == Long.TYPE) {
									retObject.Set(field.getName(), Long.parseLong(token));
								} else {
									log.error("Invalid type(" + field.getType() + ")");
									retObject.Set(field.getName(), token);
								}
							} catch (NumberFormatException e) {
								log.error(line);
								log.error("[" + field.getName() + "(" + field.getType() + ")] <= " + token);
								e.printStackTrace();
							}
						}

						fixdata.put(Integer.valueOf(retObject.Get(retObject.getPrimaryKey()).toString()), retObject);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				breader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		lastModified.setTime(file.lastModified());

		log.info(filePath + " loaded(" + lastModified.toString() + ").");
	}

}
