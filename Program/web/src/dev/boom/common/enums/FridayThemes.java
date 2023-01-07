package dev.boom.common.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.boom.common.CommonMethod;

public enum FridayThemes {
	BALL("ball", new String[] {"ball.js"}, new String[] {"ball.css"}, 200, true),
	NODE("node", new String[] {"nodes.js","main.js"}, new String[] {"node.css"}, 250, true),
	STAR("star", new String[] {"star.js"}, new String[] {"star.css"}, 200, true),
	SPACE("space", new String[] {"tween-lite.min.js","ease-pack.min.js","space.js"}, new String[] {"space.css"}, 50, true),
	AMBIENT("ambient", new String[] {"stop-execution.js","easeljs.min.js","tween-max.min.js","ambient.js"}, new String[] {"normalize.min.css","ambient.css"}, 50, true),
	PARALLAX("parallax", new String[] {"modernizr.min.js","prefixfree.min.js","stop-execution.js"}, new String[] {"normalize.min.css","parallax.css"}, 250, true),
	PARTICLES("particles", new String[] {"particles.js"}, new String[] {"particles.css"}, 15, false),
	RAINBOW("rainbow", new String[] {"rainbow.js"}, new String[] {"rainbow.css"}, 15, false),
	NIGHTSKY("nightsky", new String[] {"nightsky.js"}, new String[] {"nightsky.css"}, 15, false),
	CLOCK("clock", new String[] {"clock.js"}, new String[] {"clock.css"}, 15, false),
	;
	
	private String name;
	private String[] jsList;
	private String[] cssList;
	private int priority;
	private boolean available;

	private FridayThemes(String name, String[] jsList, String[] cssList, int priority, boolean available) {
		this.name = name;
		this.jsList = jsList;
		this.cssList = cssList;
		this.priority = priority;
		this.available = available;
	}

	public String getName() {
		return name;
	}

	public String[] getJsList() {
		return jsList;
	}

	public String[] getCssList() {
		return cssList;
	}

	public int getPriority() {
		return priority;
	}

	public boolean isAvailable() {
		return available;
	}
	
	public String getRenderHtml() {
		StringBuilder sb = new StringBuilder();
		switch (this) {
		case BALL:
		case PARTICLES:
			break;
		case NODE:
		case STAR:
		case RAINBOW:
			sb.append("<canvas id=\"canvas\" style=\"position:absolute;z-index:-1;\"></canvas>");
			break;
		case AMBIENT:
			sb.append("<canvas id=\"canvas\" style=\"position:absolute;\"></canvas>");
			break;
		case PARALLAX:
			sb.append("<script>window.console = window.console || function(t) {};</script>");
			sb.append("<div id='stars'></div>");
			sb.append("<div id='stars2'></div>");
			sb.append("<div id='stars3'></div>");
			break;
		case SPACE:
			sb.append("<div id=\"large-header\" class=\"large-header\">");
			sb.append("<canvas id=\"canvas\" style=\"position:absolute;\"></canvas>");
			sb.append("</div>");
			break;
		case NIGHTSKY:
			sb.append("<style>\r\n" + 
					"@import url('https://fonts.googleapis.com/css2?family=Monoton&display=swap');\r\n" + 
					"@import url('https://fonts.googleapis.com/css2?family=Gruppo&display=swap');\r\n" + 
					"</style>");
			sb.append("<div class=\"stars\"></div>");
			sb.append("<div class=\"twinkling\"></div>");
			break;
		case CLOCK:
			sb.append("<div id=\"calendar\">");
			sb.append("<div id=\"clock\" style=\"overflow:hidden;\">");
			sb.append("<div id=\"sec-plate\" class=\"plate\"></div>");
			sb.append("<div id=\"min-plate\" class=\"plate\"></div>");
			sb.append("<div id=\"hour-plate\" class=\"plate\"></div>");
			sb.append("<div id=\"day-plate\" class=\"plate\"></div>");
			sb.append("<div id=\"date-plate\" class=\"plate\"></div>");
			sb.append("<div id=\"month-plate\" class=\"plate\"></div>");
			sb.append("<div id=\"bran-plate\" class=\"plate\"></div>");
			sb.append("<div id=\"stem-plate\" class=\"plate\"></div>");
			sb.append("<div id=\"year-plate\" class=\"plate\"></div>");
			sb.append("</div>");
			sb.append("</div>");
			break;
		default:
			break;
		}
		
		return sb.toString();
	}
	
	public static List<FridayThemes> getAvailableThemes() {
		List<FridayThemes> list = new ArrayList<>();
		for (FridayThemes theme : FridayThemes.values()) {
			if (theme.isAvailable()) {
				list.add(theme);
			}
		}
		if (list.size() > 0) {
			Collections.sort(list, new Comparator<FridayThemes>() {
				@Override
				public int compare(FridayThemes arg0, FridayThemes arg1) {
					return arg1.getPriority() - arg0.getPriority();
				}
			});
		}
		return list;
	}
	
	public static FridayThemes getRandomTheme() {
		List<FridayThemes> listThemes = FridayThemes.getAvailableThemes();
		if (!listThemes.isEmpty()) {
			int total = 0;
			for (FridayThemes theme : listThemes) {
				total += theme.getPriority();
			}
			int selectPriority = CommonMethod.randomNumber(1, total);
			total = 0;
			for (FridayThemes theme : listThemes) {
				total += theme.getPriority();
				if (selectPriority <= total) {
					return theme;
				}
			}
		}
		
		return null;
	}
}
