package dev.boom.pages.game.cannon;

import java.util.List;

import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Button;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.element.CssImport;

import dev.boom.services.CannonBlockData;
import dev.boom.services.CannonBlockDataService;
import dev.boom.services.CannonBlockInfo;
import dev.boom.services.CannonBlockService;

public class CannonBlockMenu extends CannonBlockMainPage {
	private static final long serialVersionUID = 1L;
	private CannonBlockInfo cannonBlockInfo = null;
	private static boolean hasAlreadyGame = false;
	private Form formNewGame = new Form("newGame");;
	private Form formContinueGame = new Form("continueGame");

	public CannonBlockMenu() {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new CssImport("/css/game/cannonblock/menu.css"));

		return headElements;
	}

	@Override
	public void onInit() {
		addControl(formNewGame);
		addControl(formContinueGame);
		cannonPlayerInfo = getCannonPlayer();
		if (cannonPlayerInfo != null && cannonPlayerInfo.getStatus() == CannonBlockService.GAME_IS_PLAYING) {
			Button btnContinueGame = new Submit("continue_game", "");
			formContinueGame.add(btnContinueGame);
			btnContinueGame.setActionListener(new ActionListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean onAction(Control source) {
					return onClickContinueGame();
				}
			});
			cannonBlockInfo = CannonBlockService.getCannonBlockInfo(cannonPlayerInfo.getUserId(),CannonBlockService.GAME_IS_PLAYING);
			hasAlreadyGame = true;
		} else {
			Button btnNewGame = new Submit("new_game", "");
			formNewGame.add(btnNewGame);
			btnNewGame.setActionListener(new ActionListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean onAction(Control source) {

					return onClickNewGame();
				}
			});
		}
	}

	@Override
	public void onRender() {
		super.onRender();
		if (hasAlreadyGame) {
			if (cannonBlockInfo != null) {
				addModel("gameID", cannonBlockInfo.getGameID());
				addModel("userID", cannonBlockInfo.getUserID());
			}
		}

	}

	public boolean onClickNewGame() {
		if (formNewGame.isValid()) {
			setRedirect(CannonBlockPlay.class);
			CannonBlockData cannonBlockData = CannonBlockDataService.getCannonBlockDataById((short) 1);
			return CannonBlockService.createNewGame(getUserInfo(), cannonPlayerInfo, cannonBlockData);
		}
		return false;
	}

	public boolean onClickContinueGame() {
		if (formContinueGame.isValid()) {
			setRedirect(CannonBlockPlay.class);
			return false;
		}

		return false;
	}

}
