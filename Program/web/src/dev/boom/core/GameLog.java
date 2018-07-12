package dev.boom.core;

import org.apache.click.service.ConsoleLogService;
import org.apache.click.util.HtmlStringBuffer;

import dev.boom.common.CommonMethod;

public class GameLog extends ConsoleLogService {
	
	static class SingletonHolder {
		public static GameLog instance = new GameLog();
	}
	
	public static GameLog getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	protected void log(int level, String message, Throwable error) {
		if (level < logLevel) {
            return;
        }

        HtmlStringBuffer buffer = new HtmlStringBuffer();

        buffer.append("[");
		buffer.append(CommonMethod.getFormatStringNow());
		buffer.append("] ");
        buffer.append("[");
        buffer.append(name);
        buffer.append("]");

        buffer.append(LEVELS[level + 1]);
        buffer.append(message);

        if (error != null) {
            System.out.print(buffer.toString());
            error.printStackTrace(System.out);
        } else {
            System.out.println(buffer.toString());
        }
	}

	/**
	 * for socket log
	 * @param level
	 * @param message
	 */
	public void log(int level, String message) {
		log(level, message, null);
	}
}
