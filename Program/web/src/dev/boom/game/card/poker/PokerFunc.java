package dev.boom.game.card.poker;

import dev.boom.game.card.CardNum;

public class PokerFunc {

	public static int compare(CardNum cardNum1, CardNum cardNum2) {
		if (cardNum1 == cardNum2) {
			return 0;
		}
		if (cardNum1 == CardNum.ACE || cardNum1 == CardNum.JOKER) {
			return 1;
		}
		if (cardNum2 == CardNum.ACE || cardNum2 == CardNum.JOKER) {
			return -1;
		}
		return cardNum1.getId() - cardNum2.getId();
	}
	
}
