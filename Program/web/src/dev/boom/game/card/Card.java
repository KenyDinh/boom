package dev.boom.game.card;

public class Card {
	private CardNum cardNum;
	private CardType cardType;

	public Card(byte num, byte type) {
		this(CardNum.valueOf(num), CardType.valueOf(type));
	}

	public Card(CardNum cardNum, CardType cardType) {
		super();
		this.cardNum = cardNum;
		this.cardType = cardType;
	}

	public boolean isValid() {
		return (cardNum.isValid() && cardType.isValid());
	}

	public CardNum getCardNum() {
		return cardNum;
	}

	public void setCardNum(CardNum cardNum) {
		this.cardNum = cardNum;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public CardType getCardType() {
		return cardType;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Card) {
			Card check = (Card) obj;
			return (this.getCardNum() == check.getCardNum() && this.getCardType() == check.getCardType());
		}
		return false;
	}
	
}
