package dev.boom.pages.game.json;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.entity.info.NihongoOwningInfo;
import dev.boom.entity.info.NihongoProgressInfo;
import dev.boom.entity.info.NihongoUserInfo;
import dev.boom.entity.info.NihongoWordInfo;
import dev.boom.entity.info.UserInfo;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.CommonDaoService;
import dev.boom.services.NihongoOwningService;
import dev.boom.services.NihongoPetService;
import dev.boom.services.NihongoProgressService;
import dev.boom.services.NihongoUserService;
import dev.boom.services.NihongoWordService;

public class NihongoJson extends JsonPageBase {

	private static final long serialVersionUID = 1L;

	private static final long defaultPetID = 1;

	private NihongoUserInfo nihonUser = null;
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		nihonUser = NihongoUserService.getNihongoUserInfo(userInfo.getId());
		if (nihonUser == null) {
			nihonUser = new NihongoUserInfo();
			nihonUser.setUser_id(userInfo.getId());
			if (CommonDaoService.insert(nihonUser) == null) {
				return false;
			}
		}
		Enumeration<String> headerNames = getContext().getRequest().getHeaderNames();
		while (headerNames.hasMoreElements()) {
			System.out.println(headerNames.nextElement());
		}
		return true;
	}

	@Override
	public void onGet() {
		super.onGet();

		UserInfo currentUser = getCurrentUser();

		List<NihongoWordInfo> allWordList = NihongoWordService.getWordList();
		if (allWordList != null) {
			Map<Integer, List<Map<String, Object>>> wordMap = new HashMap<>();
			for (NihongoWordInfo wordInfo : allWordList) {
				int ref = wordInfo.getReference();
				Map<String, Object> map = wordInfo.toMapObject();
				if(!wordMap.containsKey(ref)){
					wordMap.put(ref, new ArrayList<Map<String, Object>>());
				}
				wordMap.get(ref).add(map);
			}
			putJsonData("wordMap", wordMap);
		}

		List<NihongoProgressInfo> userProgressList = NihongoProgressService.getUserProgressList(currentUser.getId());
		if (userProgressList != null) {
			Map<Integer, Integer> userProgressMap = new HashMap<Integer, Integer>();
			for (NihongoProgressInfo progressInfo : userProgressList) {
				userProgressMap.put(progressInfo.getTest_id(), progressInfo.getProgress());
			}
			
			putJsonData("userProgress", userProgressMap);
		}

		List<NihongoOwningInfo> owningList = NihongoOwningService.getOwningList(currentUser.getId());
		if (owningList != null) {
			owningList = NihongoOwningService.getOwningList(currentUser.getId());
			List<Map<String, Object>> listMapOwning = new ArrayList<>();
			for (NihongoOwningInfo owningInfo : owningList) {
				Map<String, Object> mapOwning = owningInfo.toMapObject();
				mapOwning.put("imageUrl", getHostURL() + getContextPath() + "/img/game/nihongo/pet/" + owningInfo.getPet_id() + "_0" + owningInfo.getCurrent_level() + ".gif");
				listMapOwning.add(mapOwning);
			}
			
			putJsonData("owningList", listMapOwning);
		} else {
			NihongoOwningService.insertOwning(defaultPetID, currentUser.getId());
		}
		putJsonData("userStar", nihonUser.getStar());
		putJsonData("petMap", NihongoPetService.getPetMapObject());
	}

	@Override
	public void onPost() {
		super.onPost();

		UserInfo currentUser = getCurrentUser();

		String testId = getContext().getRequestParameter("testId");
		String progress = getContext().getRequestParameter("progress");
		boolean change = false;
		if(StringUtils.isNotBlank(testId) && StringUtils.isNotBlank(progress) && StringUtils.isNumeric(testId) && StringUtils.isNumeric(progress)){
			int intTestId = Integer.parseInt(testId);
			int intProgress = Integer.parseInt(progress);
			if (NihongoProgressService.updateProgress(intTestId, currentUser.getId(), intProgress)) {
				putJsonData("progressChanged", true);
				change = true;
			}
		}

		String owningId = getContext().getRequestParameter("owningId");
		if(StringUtils.isNotBlank(owningId) && StringUtils.isNumeric(owningId)){
			int intOwningId = Integer.parseInt(owningId);
			if(NihongoOwningService.levelUpOwning(intOwningId, currentUser.getId())){
				putJsonData("dataChanged", true);
				change = true;
			}
		}

		String petId = getContext().getRequestParameter("petId");
		if(StringUtils.isNotBlank(petId)&&StringUtils.isNumeric(petId)){
			int intPetId = Integer.parseInt(petId);
			if(NihongoOwningService.buyOwning(intPetId, currentUser.getId())){
				putJsonData("dataChanged", true);
				change = true;
			}
		}
		if (change) {
			List<NihongoOwningInfo> owningList = NihongoOwningService.getOwningList(currentUser.getId());
			if (owningList != null) {
				List<Map<String, Object>> listMapOwning = new ArrayList<>();
				for (NihongoOwningInfo owningInfo : owningList) {
					Map<String, Object> mapOwning = owningInfo.toMapObject();
					mapOwning.put("imageUrl", getHostURL() + getContextPath() + "/img/game/nihongo/pet/" + owningInfo.getPet_id() + "_0" + owningInfo.getCurrent_level() + ".gif");
					listMapOwning.add(mapOwning);
				}
				putJsonData("owningList", listMapOwning);
			}
			nihonUser = NihongoUserService.getNihongoUserInfo(currentUser.getId());
			if (nihonUser != null) {
				putJsonData("userStar", nihonUser.getStar());
			}
		}
	}



}
