const JOKER_COUNT = 2;
const CART_TYPE_COUNT = 4;
const CARD_NUM_COUNT = 13;
const CARDS_IMAGE = [
	"all_card.png",
	];
const BG_IMAGE = [
	"bg_01.png",
];
const TBL_IMAGE = [
	"table_blue.png",
	"table_green.png",
	"table_red.png",
];
const USER_IMAGE = [
	"common-user.png",
];
const IMAGES_MAP = {};
(function initImage() {
	for (const src of CARDS_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/card/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of BG_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/poker/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of TBL_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/poker/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of USER_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/page/" + src;
		IMAGES_MAP[src] = image;
	}
})();

const getImage = (name) => {
	return IMAGES_MAP[name];
}

const getCardImg = (num, type) => {
	if (num < 0 || num > CARD_NUM_COUNT) {
		return null;
	}
	if (type < 0 || type >= CART_TYPE_COUNT) {
		return null;
	}
	return {
		src: CARDS_IMAGE[0],
		x: (type * 80),
		y: (num * 100),
		w: 80,
		h: 100
	}
}