package dev.boom.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblAuthTokenInfo;

public class AuthTokenService {
	
	public static final String USER_TOKEN_KEY = "remember_me";
	private static final int MAX_TOKEN_LENGTH = 12;
	private static final int MAX_TOKEN_VALIDATOR = 64;
	private static final String TOKEN_VALIDATOR_SEPARATOR = ".";
	public static final long TOKEN_EXPIRED_DAY = 30;

	private AuthTokenService() {
	}

	public static List<AuthToken> getAuthTokenListAll(String option) {
		TblAuthTokenInfo tblInfo = new TblAuthTokenInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<AuthToken> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new AuthToken((TblAuthTokenInfo) dao));
		}

		return ret;
	}

	public static List<AuthToken> getAuthTokenListAll() {
		return getAuthTokenListAll(null);
	}
	
	public static AuthToken getAuthToken(String tokenValidator) {
		String[] arrToken = validateTokenValidator(tokenValidator);
		if (arrToken == null) {
			return null;
		}
		TblAuthTokenInfo info = new TblAuthTokenInfo();
		info.Set("token", arrToken[0]);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		AuthToken authToken = new AuthToken((TblAuthTokenInfo) list.get(0));
		if (!authToken.getValidator().equals(arrToken[1])) {
			return null;
		}
		Date expired = authToken.getExpiredDate();
		if (expired == null) {
			return null;
		}
		Date now = new Date();
		if (expired.compareTo(now) <= 0) {
			return null;
		}
		
		return authToken;
	}
	
	public static AuthToken generateAuthToken(User userInfo) {
		if (userInfo == null) {
			return null;
		}
		TblAuthTokenInfo info = new TblAuthTokenInfo();
		if (CommonDaoFactory.executeUpdate(String.format("DELETE FROM %s WHERE user_id = %d AND expired < NOW()", info.getTblName(), userInfo.getId())) < 0) {
			GameLog.getInstance().warn("[generateAuthToken] delete expired token failed!");
		}
		Date now = new Date();
		String gToken = RandomStringUtils.randomAlphanumeric(MAX_TOKEN_LENGTH);
		String validator = CommonMethod.getSHA256Encrypt(gToken);
		info.Set("user_id", userInfo.getId());
		info.Set("token", gToken);
		info.Set("validator", validator);
		info.Set("expired", CommonMethod.getFormatDateString(new Date(now.getTime() + CommonDefine.MILLION_SECOND_DAY * TOKEN_EXPIRED_DAY)));
		if (CommonDaoFactory.Insert(info) > 0) {
			return new AuthToken(info);
		}
		GameLog.getInstance().warn("[generateAuthToken] generate auth token failed!");
		return null;
	}
	
	public static boolean deleteAllUserToken(User userInfo) {
		if (userInfo == null) {
			return true;
		}
		TblAuthTokenInfo info = new TblAuthTokenInfo();
		return (CommonDaoFactory.executeUpdate(String.format("DELETE FROM %s WHERE user_id = %d", info.getTblName(), userInfo.getId())) > 0);
	}
	
	public static boolean deleteAuthToken(String token) {
		TblAuthTokenInfo info = new TblAuthTokenInfo();
		info.Set("token", token);
		return (CommonDaoFactory.executeUpdate(String.format("DELETE FROM %s WHERE token = '%s'", info.getTblName(), info.Get("token").toString())) > 0);
	}
	
	public static String getTokenValidator(AuthToken authToken) {
		if (authToken == null) {
			return null;
		}
		return authToken.getToken() + TOKEN_VALIDATOR_SEPARATOR + authToken.getValidator();
	}
	
	private static String[] validateTokenValidator(String tokenValidator) {
		if (StringUtils.isBlank(tokenValidator)) {
			return null;
		}
		if (tokenValidator.length() != (MAX_TOKEN_LENGTH + MAX_TOKEN_VALIDATOR + 1)) {
			return null;
		}
		int pos = tokenValidator.indexOf(TOKEN_VALIDATOR_SEPARATOR);
		if (pos != MAX_TOKEN_LENGTH) {
			return null;
		}
		String token = tokenValidator.substring(0, MAX_TOKEN_LENGTH);
		String validator = tokenValidator.substring(pos + 1);
		return new String[] {token, validator};
	}
	
}

