/**
 *
 */
package dev.boom.dao.fix;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class FixDataBase {

	private static Map<String, FixDataBase> data = new HashMap<>();

	private static Object _lock = new Object();

	protected FixDataBase() {
		synchronized (_lock) {
			String className = this.getClass().getSimpleName().substring(3);
			if (data.get(className) != null) {
				throw new RuntimeException("Already created: " + className);
			}
			data.put(className, this);
		}
	}

	public static FixDataBase getInstance(String key) {
		synchronized (_lock) {
			FixDataBase obj = (FixDataBase) data.get(key);
			if (obj == null) {
				try {
					Class<? extends Object> cls = Class.forName("dev.boom.dao.data.Dao" + key);
					Constructor<? extends Object> constructer = cls.getConstructor();
					obj = (FixDataBase) constructer.newInstance();
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(key + " is not found");
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(key + " no default constructer found.");
				} catch (IllegalAccessException e) {
					throw new RuntimeException(key + " cannot be accessed.");
				} catch (InstantiationException e) {
					throw new RuntimeException(key + " cannot be instantiated.");
				} catch (InvocationTargetException e) {
					throw new RuntimeException(key + " cannot be invoked.");
				}
			}
			return obj;
		}
	}
}
