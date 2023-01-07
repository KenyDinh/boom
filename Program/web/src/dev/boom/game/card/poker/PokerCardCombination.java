package dev.boom.game.card.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dev.boom.common.CommonMethod;
import dev.boom.game.card.Card;
import dev.boom.game.card.CardNum;
import dev.boom.game.card.CardType;

public class PokerCardCombination {

	private List<Card> handCards;
	private Map<CardNum, Integer> cardNumCount;
	private Map<CardType, Integer> cardTypeCount;
	private List<CardNum> highCards;
	private PokerHand pokerHandDecision;

	public PokerCardCombination(List<Card> handCards) {
		super();
		this.handCards = new ArrayList<>(handCards);
		this.highCards = new ArrayList<>();
		this.cardNumCount = new HashMap<>();
		this.cardTypeCount = new HashMap<>();
		if (this.handCards != null && this.handCards.size() == PokerDef.MAX_POKER_COMBINATION_CARD) {
			for (Card card : handCards) {
				cardNumCount.put(card.getCardNum(), (cardNumCount.containsKey(card.getCardNum()) ? cardNumCount.get(card.getCardNum()) : 0) + 1);
				cardTypeCount.put(card.getCardType(), (cardTypeCount.containsKey(card.getCardType()) ? cardTypeCount.get(card.getCardType()) : 0) + 1);
			}
			for (PokerHand pokerHand : PokerHand.values()) {
				if (checkHand(pokerHand)) {
					this.pokerHandDecision = pokerHand;
					break;
				}
			}
		}
	}
	
	public boolean isValid() {
		return (this.pokerHandDecision != null);
	}
	
	public PokerHand getPokerHandDecision() {
		return this.pokerHandDecision;
	}
	
	public int compare(PokerCardCombination combination) {
		if (combination == null || !combination.isValid()) {
			return 1;
		}
		if (!this.isValid()) {
			return -1;
		}
		if (this.getPokerHandDecision() != combination.getPokerHandDecision()) {
			return this.getPokerHandDecision().getRank() - combination.getPokerHandDecision().getRank();
		}
		int size = CommonMethod.max(this.highCards.size(), combination.highCards.size());
		for (int i = 0; i < size; i++) {
			if (i >= this.highCards.size() || i >= combination.highCards.size()) {
				if (i >= this.highCards.size()) {
					return -1;
				} else {
					return 1;
				}
			}
			int ret = PokerFunc.compare(this.highCards.get(i), combination.highCards.get(i));
			if (ret != 0) {
				return ret;
			}
		}
		return 0;
	}
	
	private boolean checkHand(PokerHand pokerHand) {
		final int jokerCount = (cardNumCount.containsKey(CardNum.JOKER) ? cardNumCount.get(CardNum.JOKER) : 0);
		switch (pokerHand) {
		case FIVE_OF_A_KIND: {
			if (cardNumCount.size() == 2 && jokerCount > 0) {
				for (Card card : handCards) {
					if (card.getCardNum() != CardNum.JOKER) {
						highCards.add(card.getCardNum());
						break;
					}
				}
				return true;
			}
		}
			break;
		case ROYAL_STRAINGHT_FLUSH: 
		case STRAINGHT_FLUSH: {
			if (cardNumCount.size() < 4) {
				return false;
			}
			int count = 0;
			CardType check = CardType.NONE;
			for (Card card : handCards) {
				if (card.getCardNum() == CardNum.JOKER) {
					count++;
					continue;
				}
				if (check == CardType.NONE) {
					check = card.getCardType();
					count++;
					continue;
				}
				if (check == card.getCardType()) {
					count++;
				}
			}
			if (count != PokerDef.MAX_POKER_COMBINATION_CARD) {
				return false;
			}
			CardNum[] list = new CardNum[] {CardNum.ACE, CardNum.TWO, CardNum.THREE, CardNum.FOUR, CardNum.FIVE, CardNum.SIX, CardNum.SEVEN, 
					CardNum.EIGHT, CardNum.NINE, CardNum.TEN, CardNum.JACK, CardNum.QUEEN, CardNum.KING, CardNum.ACE, };
			int size = list.length;
			for (int i = 0; i < size; i++) {
				int tmpJokerCount = jokerCount;
				if (!cardNumCount.containsKey(list[i])) {
					if (tmpJokerCount <= 0 || i > 0) {
						continue;
					}
					//tmpJokerCount--;
				}
				boolean found = true;
				int startIdx = (i + PokerDef.MAX_POKER_COMBINATION_CARD - 1 < size) ? i : (size - PokerDef.MAX_POKER_COMBINATION_CARD);
				for (int j = startIdx; j < startIdx+ PokerDef.MAX_POKER_COMBINATION_CARD; j++) {
					if (!cardNumCount.containsKey(list[j])) {
						if (tmpJokerCount <= 0) {
							found = false;
							break;
						}
						tmpJokerCount--;
					}
				}
				if (found) {
					CardNum head = list[startIdx + PokerDef.MAX_POKER_COMBINATION_CARD - 1];
					if (pokerHand != PokerHand.ROYAL_STRAINGHT_FLUSH || head == CardNum.ACE) {
						if (startIdx == 0) {
							head = CardNum.ACE;
						}
						highCards.add(head);
						return true;
					}
				}
			}
		}
			break;
		case FOUR_OF_A_KIND: {
			for (Entry<CardNum, Integer> entry : cardNumCount.entrySet()) {
				if (entry.getKey() != CardNum.JOKER && (entry.getValue() + jokerCount == 4)) {
					highCards.add(entry.getKey());
					return true;
				}
			}
		}
			break;
		case FULL_HOUSE: {
			CardNum max = CardNum.NONE;
			for (Entry<CardNum, Integer> entry : cardNumCount.entrySet()) {
				if (entry.getKey() != CardNum.JOKER && entry.getValue() + jokerCount == 3) {
					if (PokerFunc.compare(entry.getKey(), max) > 0) {
						max = entry.getKey();
					}
					break;
				}
			}
			if (max == CardNum.NONE) {
				return false;
			}
			for (Entry<CardNum, Integer> entry : cardNumCount.entrySet()) {
				if (entry.getKey() != CardNum.JOKER && entry.getKey() != max && entry.getValue() == 2) {
					highCards.add(max);
					return true;
				}
			}
		}
			break;
		case FLUSH: {
			int count = 0;
			CardType check = CardType.NONE;
			CardNum max = CardNum.NONE;
			for (Card card : handCards) {
				if (card.getCardNum() == CardNum.JOKER) {
					max = CardNum.ACE;
					count++;
					continue;
				}
				if (PokerFunc.compare(card.getCardNum(), max) > 0) {
					max = card.getCardNum();
				}
				if (check == CardType.NONE) {
					check = card.getCardType();
					count++;
					continue;
				}
				if (check == card.getCardType()) {
					count++;
				}
			}
			if (count == PokerDef.MAX_POKER_COMBINATION_CARD) {
				highCards.add(max);
				return true;
			}
		}
			break;
		case STRAIGHT: {
			if (cardNumCount.size() < 4) {
				return false;
			}
			CardNum[] list = new CardNum[] {CardNum.ACE, CardNum.TWO, CardNum.THREE, CardNum.FOUR, CardNum.FIVE, CardNum.SIX, CardNum.SEVEN, 
					CardNum.EIGHT, CardNum.NINE, CardNum.TEN, CardNum.JACK, CardNum.QUEEN, CardNum.KING, CardNum.ACE, };
			int size = list.length;
			for (int i = 0; i < size; i++) {
				int tmpJokerCount = jokerCount;
				if (!cardNumCount.containsKey(list[i])) {
					if (tmpJokerCount <= 0 || i > 0) {
						continue;
					}
					//tmpJokerCount--;
				}
				boolean found = true;
				int startIdx = (i + PokerDef.MAX_POKER_COMBINATION_CARD - 1 < size) ? i : (size - PokerDef.MAX_POKER_COMBINATION_CARD);
				for (int j = startIdx; j < startIdx + PokerDef.MAX_POKER_COMBINATION_CARD; j++) {
					if (!cardNumCount.containsKey(list[j])) {
						if (tmpJokerCount <= 0) {
							found = false;
							break;
						}
						tmpJokerCount--;
					}
				}
				if (found) {
					CardNum head = list[startIdx + PokerDef.MAX_POKER_COMBINATION_CARD - 1];
					if (startIdx == 0) {
						head = CardNum.ACE;
					}
					highCards.add(head);
					return true;
				}
			}
		}
			break;
		case THREE_OF_A_KIND: {
			boolean found = false;
			CardNum max = CardNum.NONE;
			for (Entry<CardNum, Integer> entry : cardNumCount.entrySet()) {
				if (entry.getKey() != CardNum.JOKER && (entry.getValue() + jokerCount == 3)) {
					if (PokerFunc.compare(entry.getKey(), max) > 0) {
						max = entry.getKey();
					} 
					found = true;
				}
			}
			if (found) {
				highCards.add(max);
				return true;
			}
		}
			break;
		case TWO_PAIR: {
			if (cardNumCount.size() == 3) {
				for (Entry<CardNum, Integer> entry : cardNumCount.entrySet()) {
					if (entry.getValue() == 2 && entry.getKey() != CardNum.JOKER) {
						highCards.add(entry.getKey());
					}
				}
				sortHighCards();
				for (Entry<CardNum, Integer> entry : cardNumCount.entrySet()) {
					if (entry.getValue() != 2 && entry.getKey() != CardNum.JOKER) {
						highCards.add(entry.getKey());
					}
				}
				return true;
			}
		}
			break;
		case ONE_PAIR: {
			boolean found = false;
			CardNum max = CardNum.NONE;
			for (Entry<CardNum, Integer> entry : cardNumCount.entrySet()) {
				if (entry.getKey() != CardNum.JOKER && (entry.getValue() + jokerCount == 2)) {
					if (PokerFunc.compare(entry.getKey(), max) > 0) {
						max = entry.getKey();
					} 
					found = true;
				}
			}
			if (found) {
				for (Entry<CardNum, Integer> entry : cardNumCount.entrySet()) {
					if (entry.getKey() != max && entry.getKey() != CardNum.JOKER) {
						highCards.add(entry.getKey());
					}
				}
				sortHighCards();
				highCards.add(0, max);
				return true;
			}
		}
			break;
		case HIGH_CARD: {
			if (cardNumCount.size() == PokerDef.MAX_POKER_COMBINATION_CARD) {
				for (Entry<CardNum, Integer> entry : cardNumCount.entrySet()) {
					if (entry.getKey() != CardNum.JOKER) {
						highCards.add(entry.getKey());
					}
				}
				sortHighCards();
				return true;
			}
		}
			break;
		default:
			break;
		}
		return false;
	}
	
	private void sortHighCards() {
		Collections.sort(highCards, new Comparator<CardNum>() {

			@Override
			public int compare(CardNum o1, CardNum o2) {
				return PokerFunc.compare(o2, o1);
			}
		});
	}
	
	public static PokerCardCombination getStrongestCombination(List<Card> playerCards, List<Card> sharedCards) {
		if (playerCards == null || sharedCards == null || playerCards.size() != PokerDef.MAX_PLAYER_HOLD_CARD_NUM || sharedCards.size() != PokerDef.MAX_POKER_COMBINATION_CARD) {
			return null;
		}
		PokerCardCombination strongest = new PokerCardCombination(sharedCards);
		int replaces[] = new int[] {1, 2};
		for (int rep : replaces) {
			Card repCard1 = (rep >= 1) ? playerCards.get(0) : null;
			Card repCard2 = (rep >= 2) ? playerCards.get(1) : null;;
			for (int i = 0; i < PokerDef.MAX_POKER_COMBINATION_CARD - rep + 1; i++) {
				List<Card> cardList1 = new ArrayList<>();
				cardList1.addAll(sharedCards);
				if (repCard1 != null) {
					cardList1.set(i, repCard1);
				}
				if (repCard2 != null) {
					for (int j = i + 1; j < PokerDef.MAX_POKER_COMBINATION_CARD; j++) {
						List<Card> cardList2 = new ArrayList<>();
						cardList2.addAll(cardList1);
						cardList2.set(j, repCard2);
						PokerCardCombination newComb = new PokerCardCombination(cardList2);
						if (strongest.compare(newComb) < 0) {
							strongest = newComb;
						}
					}
				} else {
					PokerCardCombination newComb = new PokerCardCombination(cardList1);
					if (strongest.compare(newComb) < 0) {
						strongest = newComb;
					}
				}
			}
		}
		return strongest;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			PokerCardPool pool = new PokerCardPool();
			List<Card> playerCards = pool.nextNCard(PokerDef.MAX_PLAYER_HOLD_CARD_NUM);
			List<Card> list = null;
			while (playerCards != null && (list = pool.nextNCard(PokerDef.MAX_POKER_COMBINATION_CARD)) != null) {
				for (Card card : playerCards) {
					System.out.println(String.format("Card : %s - %s", card.getCardNum().name(), card.getCardType().name()));
				}
				System.out.println("+++++");
				for (Card card : list) {
					System.out.println(String.format("Card : %s - %s", card.getCardNum().name(), card.getCardType().name()));
				}
				PokerCardCombination com = PokerCardCombination.getStrongestCombination(playerCards, list);
				if (com.isValid()) {
					System.out.println(com.pokerHandDecision.name());
					System.out.println("High Card: ");
					for (CardNum num : com.highCards) {
						System.out.println(num.name());
					}
					System.out.println("------------------");
				}
			}
		}
	}
}
