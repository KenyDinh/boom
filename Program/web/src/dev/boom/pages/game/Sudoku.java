package dev.boom.pages.game;

import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.common.game.GameTypeEnum;
import dev.boom.common.game.SudokuLevel;
import dev.boom.pages.Game;

public class Sudoku extends Game {

	private static final long serialVersionUID = 1L;
	
	public Sudoku() {
		initTheme(null);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new CssImport("/css/game/sudoku.css"));
		headElements.add(new JsImport("/js/lib/qqwing-1.3.4.min.js"));
		headElements.add(new JsImport("/js/lib/minisat.js"));
		headElements.add(new JsImport("/js/game/sudoku.js"));
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
