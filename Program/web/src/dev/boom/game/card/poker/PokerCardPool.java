package dev.boom.game.card.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.boom.game.card.Card;
import dev.boom.game.card.CardNum;
import dev.boom.game.card.CardType;

public class PokerCardPool {

	private List<Card> cardPool;
	private boolean hasJokerCard = false;
	private int currentIndex = 0;

	public PokerCardPool() {
		super();
		cardPool = new ArrayList<>();
		for (int type = 0; type < PokerDef.MAX_CARD_TYPE; type++) {
			for (int num = 0; num < PokerDef.MAX_CARD_NUM; num++) {
				CardNum cardNum = CardNum.valueOf((byte)(num + 1));
				CardType cardType = CardType.valueOf((byte) (type + 1));
				if (!cardNum.isValid() || !cardType.isValid()) {
					return;
				}
				cardPool.add(new Card(cardNum, cardType));
			}
		}
		if (hasJokerCard) {
			for (int i = 0; i < PokerDef.MAX_CARD_JOKER; i++) {
				cardPool.add(new Card(CardNum.JOKER, (i % 2 == 0 ? CardType.BLACK_JOKER : CardType.RED_JOKER)));
			}
		}
		Collections.shuffle(cardPool);
	}
	
	public void burnCard() {
		burnNCard(1);
	}
	
	public void burnNCard(int num) {
		if (num <= 0 || currentIndex + num > cardPool.size()) {
			return;
		}
		currentIndex += num;
	}
	
	public Card nextCard() {
		if (currentIndex >= cardPool.size()) {
			return null;
		}
		Card card = cardPool.get(currentIndex);
		currentIndex++;
		return card;
	}
	
	public List<Card> nextNCard(int num) {
		if (num <= 0 || currentIndex + num > cardPool.size()) {
			return null;
		}
		List<Card> ret = new ArrayList<>();
		for (int i = currentIndex; i < currentIndex + num; i++) {
			ret.add(cardPool.get(i));
		}
		currentIndex += num;
		return ret;
	}
}
