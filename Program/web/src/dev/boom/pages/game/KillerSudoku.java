package dev.boom.pages.game;

import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.common.game.GameTypeEnum;
import dev.boom.common.game.SudokuLevel;
import dev.boom.pages.Game;

public class KillerSudoku extends Game {

	private static final long serialVersionUID = 1L;
	
	public KillerSudoku() {
		initTheme(null);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new CssImport("/css/game/killer_sudoku.css"));
		headElements.add(new JsImport("/js/game/killer_sudoku.js"));
		return headElements;
	}
	
	@Override
	public void onRender() {
		super.onRender();
		addModel("level_list", SudokuLevel.values());
	}

	@Override
	protected int getGameIndex() {
		return GameTypeEnum.SUDOKU.getIndex();
	}
}
