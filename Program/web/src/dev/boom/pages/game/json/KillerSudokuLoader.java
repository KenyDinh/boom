package dev.boom.pages.game.json;

import dev.boom.common.CommonMethod;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.KillerSudokuData;
import dev.boom.services.KillerSudokuDataService;

public class KillerSudokuLoader extends JsonPageBase {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void onInit() {
		super.onInit();
	}

	@Override
	public void onPost() {
		super.onPost();
		doGenerateProblem();
	}

	@Override
	public void onRender() {
		super.onRender();
	}
	
	private void doGenerateProblem() {
		byte level = 1;
		String strLevel = getContext().getRequestParameter("level");
		if (CommonMethod.isValidNumeric(strLevel, 1, 1)) {
			level = Byte.parseByte(strLevel);
		}
		KillerSudokuData sudokuData = KillerSudokuDataService.getRandomSudokuData(level);
		if (sudokuData != null) {
			putJsonData("data", sudokuData.getData());
			putJsonData("cage_data", sudokuData.getCageData());
		}
	}
	
}
