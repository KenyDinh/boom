package dev.boom.pages.game.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.NihongoOwningService;
import dev.boom.services.NihongoPetService;
import dev.boom.services.NihongoProgressService;
import dev.boom.services.NihongoUserService;
import dev.boom.services.NihongoWordService;
import dev.boom.tbl.info.TblNihongoOwningInfo;
import dev.boom.tbl.info.TblNihongoProgressInfo;
import dev.boom.tbl.info.TblNihongoUserInfo;
import dev.boom.tbl.info.TblNihongoWordInfo;

public class NihongoJson extends JsonPageBase {

	private static final long serialVersionUID = 1L;

	private static final long defaultPetID = 1;
	
	private TblNihongoUserInfo nihonUser = null;
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!initUserInfo()) {
			return false;
		}
		nihonUser = NihongoUserService.getNihongoUserInfo(userInfo.getId());
		if (nihonUser == null) {
			nihonUser = new TblNihongoUserInfo();
			nihonUser.Set("user_id", userInfo.getId());
			nihonUser.Set("username", userInfo.getUsername());
			if (CommonDaoFactory.Insert(nihonUser) <= 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onGet() {
		super.onGet();

		List<TblNihongoWordInfo> allWordList = NihongoWordService.getWordList();
		if (allWordList != null) {
			Map<Integer, List<Map<String, Object>>> wordMap = new HashMap<>();
			for (TblNihongoWordInfo wordInfo : allWordList) {
				int ref = (Integer)wordInfo.Get("reference");
				Map<String, Object> map = wordInfo.toMapObject();
				if(!wordMap.containsKey(ref)){
					wordMap.put(ref, new ArrayList<Map<String, Object>>());
				}
				wordMap.get(ref).add(map);
			}
			putJsonData("wordMap", wordMap);
		}

		List<TblNihongoProgressInfo> userProgressList = NihongoProgressService.getUserProgressList(userInfo.getId());
		if (userProgressList != null) {
			Map<Integer, Integer> userProgressMap = new HashMap<Integer, Integer>();
			for (TblNihongoProgressInfo progressInfo : userProgressList) {
				userProgressMap.put((Integer)progressInfo.Get("test_id"), (Integer)progressInfo.Get("progress"));
			}
			putJsonData("userProgress", userProgressMap);
		}

		List<TblNihongoOwningInfo> owningList = NihongoOwningService.getOwningList(userInfo.getId());
		if (owningList != null) {
			List<Map<String, Object>> listMapOwning = new ArrayList<>();
			for (TblNihongoOwningInfo owningInfo : owningList) {
				Map<String, Object> mapOwning = owningInfo.toMapObject();
				mapOwning.put("imageUrl", getHostURL() + getContextPath() + "/img/game/nihongo/pet/" + owningInfo.Get("pet_id").toString() + "_0" + owningInfo.Get("current_level").toString() + ".gif");
				listMapOwning.add(mapOwning);
			}
			putJsonData("owningList", listMapOwning);
		} else {
			NihongoOwningService.insertOwning(defaultPetID, userInfo.getId());
		}
		putJsonData("userStar", nihonUser.Get("star"));
		putJsonData("petMap", NihongoPetService.getPetMapObject());
	}

	@Override
	public void onPost() {
		super.onPost();

		String testId = getContext().getRequestParameter("testId");
		String progress = getContext().getRequestParameter("progress");
		boolean change = false;
		if(StringUtils.isNotBlank(testId) && StringUtils.isNotBlank(progress) && StringUtils.isNumeric(testId) && StringUtils.isNumeric(progress)){
			int intTestId = Integer.parseInt(testId);
			int intProgress = Integer.parseInt(progress);
			if (NihongoProgressService.updateProgress(intTestId, userInfo.getId(), intProgress)) {
				putJsonData("progressChanged", true);
				change = true;
			}
		}

		String owningId = getContext().getRequestParameter("owningId");
		if(StringUtils.isNotBlank(owningId) && StringUtils.isNumeric(owningId)){
			int intOwningId = Integer.parseInt(owningId);
			if(NihongoOwningService.levelUpOwning(intOwningId, userInfo.getId())){
				putJsonData("dataChanged", true);
				change = true;
			}
		}

		String petId = getContext().getRequestParameter("petId");
		if(StringUtils.isNotBlank(petId)&&StringUtils.isNumeric(petId)){
			int intPetId = Integer.parseInt(petId);
			if(NihongoOwningService.buyOwning(intPetId, userInfo.getId())){
				putJsonData("dataChanged", true);
				change = true;
			}
		}
		if (change) {
			List<TblNihongoOwningInfo> owningList = NihongoOwningService.getOwningList(userInfo.getId());
			if (owningList != null) {
				List<Map<String, Object>> listMapOwning = new ArrayList<>();
				for (TblNihongoOwningInfo owningInfo : owningList) {
					Map<String, Object> mapOwning = owningInfo.toMapObject();
					mapOwning.put("imageUrl", getHostURL() + getContextPath() + "/img/game/nihongo/pet/" + owningInfo.Get("pet_id").toString() + "_0" + owningInfo.Get("current_level").toString() + ".gif");
					listMapOwning.add(mapOwning);
				}
				putJsonData("owningList", listMapOwning);
			}
			nihonUser = NihongoUserService.getNihongoUserInfo(userInfo.getId());
			if (nihonUser != null) {
				putJsonData("userStar", nihonUser.Get("star"));
			}
		}
	}



}
