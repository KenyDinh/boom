package dev.boom.pages.game.json;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonMethod;
import dev.boom.common.game.SudokuFunc;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.SudokuData;
import dev.boom.services.SudokuDataService;

public class KillerSudokuLoader extends JsonPageBase {

	private static final long serialVersionUID = 1L;
	private static final int TYPE_GET_PROBLEM = 1;
	private static final int TYPE_GET_CNF = 2;

	private int type = 0;
	
	@Override
	public void onInit() {
		super.onInit();
		String strType = getContext().getRequestParameter("type");
		if (CommonMethod.isValidNumeric(strType, 1, 2)) {
			type = Integer.parseInt(strType);
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		switch (type) {
		case TYPE_GET_PROBLEM:
			doGenerateProblem();
			break;
		case TYPE_GET_CNF:
			doGenerateCnf();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRender() {
		super.onRender();
	}
	
	private void doGenerateCnf() {
		String strBoardData = getContext().getRequestParameter("board");
		if (StringUtils.isBlank(strBoardData)) {
			return;
		}
		int[][] board = SudokuFunc.getSudokuBoard(strBoardData);
		if (board == null) {
			return;
		}
		String cnf = SudokuFunc.getCnfFormula(board);
		if (cnf == null) {
			return;
		}
		putJsonData("cnf", cnf);
	}
	
	private void doGenerateProblem() {
		byte level = 1;
		String strLevel = getContext().getRequestParameter("level");
		if (CommonMethod.isValidNumeric(strLevel, 1, 1)) {
			level = Byte.parseByte(strLevel);
		}
		SudokuData sudokuData = SudokuDataService.getRandomSudokuData(level);
		if (sudokuData != null) {
			putJsonData("data", sudokuData.getData());
		}
	}
	
}
