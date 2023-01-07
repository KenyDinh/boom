package dev.boom.pages.game;

import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.common.game.GameTypeEnum;
import dev.boom.pages.Game;
import dev.boom.pages.Home;

public class Nihongo extends Game {

	private static final long serialVersionUID = 1L;

	public Nihongo(){
	}

	@Override
	public void onInit() {
		super.onInit();
	}
	
	@Override
	public void onRender() {
		super.onRender();
		addModel("title", getMessage("MSG_GAME_NIHONGO_TITLE"));
		if (userInfo == null) {
			setRedirect(Home.class);
			return;
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new CssImport("/css/game/nihongo.css"));
		headElements.add(new JsImport("/js/lib/angular-1.6.4.min.js"));
		headElements.add(new JsImport("/js/game/nihongo.js"));
		return headElements;
	}

	@Override
	protected int getGameIndex() {
		return GameTypeEnum.NIHONGO.getIndex();
	}
	
}
