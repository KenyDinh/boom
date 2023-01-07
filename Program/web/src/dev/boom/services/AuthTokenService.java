package dev.boom.services;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblAuthTokenInfo;

public class AuthTokenService {
	
	public static final String USER_TOKEN_KEY = "remember_me";
	private static final int MAX_TOKEN_LENGTH = 12;
	private static final int MAX_TOKEN_VALIDATOR = 64;
	private static final String TOKEN_VALIDATOR_SEPARATOR = ".";
	public static final long TOKEN_EXPIRED_DAY = 30;

	public static AuthToken getAuthToken(String tokenValidator) {
		String[] arrToken = validateTokenValidator(tokenValidator);
		if (arrToken == null) {
			return null;
		}
		TblAuthTokenInfo info = new TblAuthTokenInfo();
		info.Set("token", arrToken[0]);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		AuthToken authToken = new AuthToken((TblAuthTokenInfo) list.get(0));
		if (!authToken.getValidator().equals(arrToken[1])) {
			return null;
		}
		if (authToken.isExpired()) {
			return null;
		}
		return authToken;
	}
	
	public static AuthToken generateAuthToken(UserInfo userInfo) {
		if (userInfo == null) {
			return null;
		}
		TblAuthTokenInfo info = new TblAuthTokenInfo();
		info.Set("user_id", userInfo.getId());
		info.setSelectOption("AND expired < NOW()");
		if (!CommonDaoService.delete(info)) {
			GameLog.getInstance().warn("[generateAuthToken] delete expired token failed!");
		}
		Date now = new Date();
		info = new TblAuthTokenInfo();
		String gToken = RandomStringUtils.randomAlphanumeric(MAX_TOKEN_LENGTH);
		String validator = CommonMethod.getSHA256Encrypt(gToken);
		info.setUser_id(userInfo.getId());
		info.setToken(gToken);
		info.setValidator(validator);
		info.setExpired(new Date(now.getTime() + CommonDefine.MILLION_SECOND_DAY * TOKEN_EXPIRED_DAY));
		if (CommonDaoService.insert(info) != null) {
			return new AuthToken(info);
		}
		GameLog.getInstance().warn("[generateAuthToken] generate auth token failed!");
		return null;
	}
	
	public static boolean deleteAllUserToken(UserInfo userInfo) {
		if (userInfo == null) {
			return true;
		}
		TblAuthTokenInfo info = new TblAuthTokenInfo();
		info.Set("user_id", userInfo.getId());
		return CommonDaoService.delete(info);
	}
	
	public static boolean deleteAuthToken(String token) {
		TblAuthTokenInfo info = new TblAuthTokenInfo();
		info.Set("token", token);
		return CommonDaoService.delete(info);
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
