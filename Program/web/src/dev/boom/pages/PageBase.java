package dev.boom.pages;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

import dev.boom.core.BoomSession;
import dev.boom.core.GameLog;
import dev.boom.pages.account.ChangePassword;
import dev.boom.pages.account.Login;
import dev.boom.pages.account.Register;
import dev.boom.services.WorldInfo;
import dev.boom.services.WorldService;
import dev.boom.tbl.info.TblUserInfo;

public class PageBase extends Page {

	private static final long serialVersionUID = 1L;
	protected WorldInfo worldInfo = null;

	protected void storeBoomSession(TblUserInfo info) {
		BoomSession boomSession = new BoomSession(info.getId());
		getContext().getSession().setAttribute("boom_session", boomSession);
	}

	protected BoomSession getBoomSession() {
		return (BoomSession) getContext().getSession().getAttribute("boom_session");
	}

	protected void removeBoomSession() {
		getContext().getSession().removeAttribute("boom_session");
	}

	protected String getHostURL() {
		return "";
	}

	protected String getContextPath() {
		return getContext().getRequest().getContextPath();
	}
	
	protected WorldInfo getWorldInfo() {
		if (worldInfo == null) {
			worldInfo = WorldService.getWorldInfo();
		}
		return worldInfo;
	}
	
	public PageBase() {
		logPageParameters();
		getMessages();
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (getWorldInfo() == null) {
			GameLog.getInstance().error("[BoomMainPage] World is null!");
			return false;
		}
		return true;
	}

	protected void logPageParameters() {
		if (GameLog.getInstance().isInfoEnabled()) {
			String strParameter = "";
			Context context = getContext();
			HttpServletRequest request = context.getRequest();
			Map<String, String[]> parameterMap = request.getParameterMap();
			for (Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext();) {
				String _key = it.next();
				if ((this.getClass() == Login.class || this.getClass() == Register.class || this.getClass() == ChangePassword.class) && _key.indexOf("password") >= 0 ) {
					continue;
				}
				String[] _value = parameterMap.get(_key);
				for (String __value : _value) {
					if (strParameter.equals("")) {
						strParameter += _key + "=" + __value;
					} else {
						strParameter += "&" + _key + "=" + __value;
					}
				}
			}

			if (strParameter.equals("") == false) {
				strParameter = "?" + strParameter;
			}
			GameLog.getInstance().info(String.format("[PAGE] %s : %s%s", request.getMethod(), context.getResourcePath(), strParameter));
		}
	}

	/**
	 * @param pageClass
	 * @return
	 */
	protected String getPagePath(Class<? extends Page> pageClass) {
		return getPagePath(pageClass, null);
	}

	/**
	 * @param pageClass
	 * @param params
	 * @return
	 */
	protected String getPagePath(Class<? extends Page> pageClass, Map<String, String> params) {

		String path = getContext().getRequest().getContextPath() + getContext().getPagePath(pageClass);
		if (params != null && !params.isEmpty()) {
			HtmlStringBuffer buffer = new HtmlStringBuffer();

			for (Iterator<String> i = params.keySet().iterator(); i.hasNext();) {
				String paramName = i.next().toString();
				Object paramValue = params.get(paramName);

				if (paramValue instanceof String[]) {
					String[] paramValues = (String[]) paramValue;
					for (int j = 0; j < paramValues.length; j++) {
						buffer.append(paramName);
						buffer.append("=");
						buffer.append(ClickUtils.encodeUrl(paramValues[j], getContext()));
						if (j < paramValues.length - 1) {
							buffer.append("&amp;");
						}
					}
				} else {
					if (paramValue != null) {
						buffer.append(paramName);
						buffer.append("=");
						buffer.append(ClickUtils.encodeUrl(paramValue, getContext()));
					}
				}
				if (i.hasNext()) {
					buffer.append("&amp;");
				}
			}
			if (buffer.length() > 0) {
				if (path.contains("?")) {
					path += "&amp;" + buffer.toString();
				} else {
					path += "?" + buffer.toString();
				}
			}
		}
		return path;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void setRedirect(String location, Map params) {
		String redirect = getRedirect();

		super.setRedirect(location, params);

		if (GameLog.getInstance().isDebugEnabled() && location != null) {
			if (redirect == null) {
				GameLog.getInstance().debug("[PAGE] REDIRECT : null => " + getRedirect());
			} else {
				GameLog.getInstance().debug("[PAGE] REDIRECT : " + redirect + " => " + getRedirect());
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new CssImport("/css/lib/bootstrap-darkly.min.css"));
//		headElements.add(new CssImport("/css/lib/fontawesome.css"));
//		headElements.add(new CssImport("/css/lib/solid.css"));
		headElements.add(new JsImport("/js/lib/jquery-3.3.1.min.js"));
		headElements.add(new JsImport("/js/lib/popper-1.14.0.min.js"));
		headElements.add(new JsImport("/js/lib/bootstrap.min.js"));
		headElements.add(new JsImport("/js/socket.js"));
		
		return headElements;
	}
}
