const CHAR_IMG_SIZE = 32;
const MAPS_IMAGE = [
	"map/map_default.png",
	"map/map_01.png",
	"map/map_02.png",
	"map/map_03.png",
	"map/map_04.png",
]
const MAPS_FOREGROUND_IMAGE = [
	"map/map_fg_01.png",
	"map/map_fg_02.png",
	]
const CHARACTERS_IMAGE = [
	"char/pipo-nekonin001.png",
	"char/pipo-nekonin002.png",
	"char/pipo-nekonin003.png",
	"char/pipo-nekonin004.png",
	"char/pipo-nekonin005.png",
	"char/pipo-nekonin006.png",
	"char/pipo-nekonin007.png",
	"char/pipo-nekonin008.png",
	"char/pipo-nekonin009.png",
	"char/pipo-nekonin010.png",
	"char/pipo-nekonin011.png",
	"char/pipo-nekonin012.png",
	"char/pipo-nekonin013.png",
	"char/pipo-nekonin014.png",
	"char/pipo-nekonin015.png",
	"char/pipo-nekonin016.png",
	"char/pipo-nekonin017.png",
	"char/pipo-nekonin018.png",
	"char/pipo-nekonin019.png",
	"char/pipo-nekonin020.png",
	"char/pipo-nekonin021.png",
	"char/pipo-nekonin022.png",
	"char/pipo-nekonin023.png",
	"char/pipo-nekonin024.png",
	"char/pipo-nekonin025.png",
	"char/pipo-nekonin026.png",
	"char/pipo-nekonin027.png",
	"char/pipo-nekonin028.png",
	"char/pipo-nekonin029.png",
	"char/pipo-nekonin030.png",
	"char/pipo-nekonin031.png",
	"char/pipo-nekonin032.png",
]
const TREES_IMAGE = [
	"trees/tree_01.png",
	"trees/tree_02.png",
	"trees/tree_03.png",
	"trees/tree_04.png",
]
const BOMB_IMAGE = [
	"bomb/bombx_01.png",
	"bomb/bombx_02.png",
	"bomb/bombx_03.png",
	"bomb/bombx_04.png",
	"bomb/bombx_05.png",
	"bomb/bombx_06.png",
	"bomb/bombx_07.png",
	"bomb/bombx_08.png",
]
const BOMB_ITEM = [
	"map/item_update.png"
]
const BOMB_EXPLOSION_IMAGE = [
	"bomb/explodex.png"
]
const GHOST_DEADTH_IMAGE = [
	"char/ghost-dead.png",
]
const EFFECT_IMAGE = [
	"map/punch_effect.png",
	"map/sword_attack_effect.png",
]
const FIRE_EFFECT = [
	"map/fire_loop_2.png",
	"map/fire_loop.png",
]
const TELEPORT_IMAGE = [
	"map/teleport_on_anim.png",
	"map/teleport_off_anim.png",
]
const IMAGES_MAP = {};
(function initImage() {
	for (const src of MAPS_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of MAPS_FOREGROUND_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of CHARACTERS_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of TREES_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of BOMB_EXPLOSION_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of BOMB_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of GHOST_DEADTH_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of BOMB_ITEM) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of EFFECT_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of TELEPORT_IMAGE) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
	for (const src of FIRE_EFFECT) {
		const image = new Image();
		image.src = CONTEXT + "/img/game/boom/" + src;
		IMAGES_MAP[src] = image;
	}
})();
const getImage = (name) => {
	return IMAGES_MAP[name];
}

const EFFECT_IMAGE_FRAMES = {
	"map/sword_attack_effect.png": {
		"maxFrame" : 10,
		"maxFrameHold": 2,
	},
	"map/punch_effect.png": {
		"maxFrame" : 4,
		"maxFrameHold": 4,
	}
}

const CHARACTERS_IMAGE_PUT={
	"char/pipo-nekonin001.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":20,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":18,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":7,"y":6,"width":18,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":6,"y":7,"width":20,"height":25}],
	"char/pipo-nekonin002.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":20,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":18,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":7,"y":6,"width":18,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":6,"y":7,"width":20,"height":25}],
	"char/pipo-nekonin003.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":20,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":18,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":7,"y":6,"width":18,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":6,"y":7,"width":20,"height":25}],
	"char/pipo-nekonin004.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":20,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":18,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":7,"y":6,"width":18,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":6,"y":7,"width":20,"height":25}],
	"char/pipo-nekonin005.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":20,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":18,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":7,"y":6,"width":18,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":6,"y":7,"width":20,"height":25}],
	"char/pipo-nekonin006.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":20,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":18,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":7,"y":6,"width":18,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":6,"y":7,"width":20,"height":25}],
	"char/pipo-nekonin007.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":19,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":17,"height":25},{"id":5,"x":8,"y":7,"width":17,"height":25},{"id":6,"x":8,"y":7,"width":17,"height":25},{"id":7,"x":8,"y":6,"width":17,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":7,"y":7,"width":19,"height":25}],
	"char/pipo-nekonin008.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":20,"height":25},{"id":3,"x":8,"y":7,"width":20,"height":25},{"id":4,"x":8,"y":6,"width":19,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":6,"y":6,"width":19,"height":25},{"id":8,"x":5,"y":7,"width":20,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":6,"y":7,"width":20,"height":25}],
	"char/pipo-nekonin009.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":20,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":18,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":7,"y":6,"width":18,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":6,"y":7,"width":20,"height":25}],
	"char/pipo-nekonin010.png":[{"id":0,"x":5,"y":4,"width":23,"height":28},{"id":1,"x":5,"y":3,"width":23,"height":28},{"id":2,"x":5,"y":4,"width":23,"height":28},{"id":3,"x":5,"y":4,"width":23,"height":28},{"id":4,"x":5,"y":3,"width":23,"height":28},{"id":5,"x":5,"y":4,"width":23,"height":28},{"id":6,"x":5,"y":4,"width":23,"height":28},{"id":7,"x":5,"y":3,"width":23,"height":28},{"id":8,"x":5,"y":4,"width":23,"height":28},{"id":9,"x":5,"y":4,"width":23,"height":28},{"id":10,"x":5,"y":3,"width":23,"height":28},{"id":11,"x":5,"y":4,"width":23,"height":28}],
	"char/pipo-nekonin011.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":19,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":17,"height":25},{"id":5,"x":8,"y":7,"width":17,"height":25},{"id":6,"x":8,"y":7,"width":17,"height":25},{"id":7,"x":8,"y":6,"width":17,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":7,"y":7,"width":19,"height":25}],
	"char/pipo-nekonin012.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":20,"height":25},{"id":3,"x":7,"y":7,"width":19,"height":25},{"id":4,"x":7,"y":6,"width":19,"height":25},{"id":5,"x":7,"y":7,"width":21,"height":25},{"id":6,"x":5,"y":7,"width":21,"height":25},{"id":7,"x":7,"y":6,"width":19,"height":25},{"id":8,"x":7,"y":7,"width":19,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":6,"y":7,"width":20,"height":25}],
	"char/pipo-nekonin013.png":[{"id":0,"x":5,"y":1,"width":24,"height":31},{"id":1,"x":5,"y":1,"width":24,"height":30},{"id":2,"x":5,"y":2,"width":24,"height":30},{"id":3,"x":5,"y":1,"width":24,"height":31},{"id":4,"x":5,"y":1,"width":24,"height":30},{"id":5,"x":5,"y":2,"width":24,"height":30},{"id":6,"x":4,"y":1,"width":24,"height":31},{"id":7,"x":4,"y":1,"width":24,"height":30},{"id":8,"x":4,"y":2,"width":24,"height":30},{"id":9,"x":5,"y":1,"width":24,"height":31},{"id":10,"x":5,"y":1,"width":24,"height":30},{"id":11,"x":5,"y":2,"width":24,"height":30}],
	"char/pipo-nekonin014.png":[{"id":0,"x":6,"y":7,"width":21,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":6,"y":7,"width":21,"height":25},{"id":3,"x":8,"y":7,"width":18,"height":25},{"id":4,"x":8,"y":6,"width":18,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":7,"y":6,"width":18,"height":25},{"id":8,"x":7,"y":7,"width":18,"height":25},{"id":9,"x":7,"y":7,"width":21,"height":25},{"id":10,"x":6,"y":6,"width":21,"height":25},{"id":11,"x":5,"y":7,"width":21,"height":25}],
	"char/pipo-nekonin015.png":[{"id":0,"x":1,"y":2,"width":30,"height":30},{"id":1,"x":2,"y":1,"width":29,"height":30},{"id":2,"x":2,"y":2,"width":30,"height":30},{"id":3,"x":6,"y":2,"width":23,"height":30},{"id":4,"x":6,"y":1,"width":23,"height":30},{"id":5,"x":6,"y":2,"width":25,"height":30},{"id":6,"x":2,"y":2,"width":25,"height":30},{"id":7,"x":4,"y":1,"width":23,"height":30},{"id":8,"x":4,"y":2,"width":23,"height":30},{"id":9,"x":2,"y":2,"width":30,"height":30},{"id":10,"x":2,"y":1,"width":29,"height":31},{"id":11,"x":1,"y":2,"width":30,"height":30}],
	"char/pipo-nekonin016.png":[{"id":0,"x":8,"y":8,"width":17,"height":24},{"id":1,"x":8,"y":7,"width":17,"height":24},{"id":2,"x":8,"y":8,"width":17,"height":24},{"id":3,"x":8,"y":8,"width":17,"height":24},{"id":4,"x":8,"y":7,"width":17,"height":24},{"id":5,"x":8,"y":8,"width":17,"height":24},{"id":6,"x":8,"y":8,"width":17,"height":24},{"id":7,"x":8,"y":7,"width":17,"height":24},{"id":8,"x":8,"y":8,"width":17,"height":24},{"id":9,"x":8,"y":8,"width":17,"height":24},{"id":10,"x":8,"y":7,"width":17,"height":24},{"id":11,"x":8,"y":8,"width":17,"height":24}],
	"char/pipo-nekonin017.png":[{"id":0,"x":8,"y":8,"width":17,"height":24},{"id":1,"x":8,"y":7,"width":17,"height":24},{"id":2,"x":8,"y":8,"width":17,"height":24},{"id":3,"x":8,"y":8,"width":17,"height":24},{"id":4,"x":8,"y":7,"width":17,"height":24},{"id":5,"x":8,"y":8,"width":17,"height":24},{"id":6,"x":8,"y":8,"width":17,"height":24},{"id":7,"x":8,"y":7,"width":17,"height":24},{"id":8,"x":8,"y":8,"width":17,"height":24},{"id":9,"x":8,"y":8,"width":17,"height":24},{"id":10,"x":8,"y":7,"width":17,"height":24},{"id":11,"x":8,"y":8,"width":17,"height":24}],
	"char/pipo-nekonin018.png":[{"id":0,"x":8,"y":8,"width":17,"height":24},{"id":1,"x":8,"y":7,"width":17,"height":24},{"id":2,"x":8,"y":8,"width":17,"height":24},{"id":3,"x":8,"y":8,"width":17,"height":24},{"id":4,"x":8,"y":7,"width":17,"height":24},{"id":5,"x":8,"y":8,"width":17,"height":24},{"id":6,"x":8,"y":8,"width":17,"height":24},{"id":7,"x":8,"y":7,"width":17,"height":24},{"id":8,"x":8,"y":8,"width":17,"height":24},{"id":9,"x":8,"y":8,"width":17,"height":24},{"id":10,"x":8,"y":7,"width":17,"height":24},{"id":11,"x":8,"y":8,"width":17,"height":24}],
	"char/pipo-nekonin019.png":[{"id":0,"x":1,"y":1,"width":31,"height":31},{"id":1,"x":1,"y":0,"width":31,"height":31},{"id":2,"x":1,"y":1,"width":31,"height":31},{"id":3,"x":8,"y":1,"width":23,"height":31},{"id":4,"x":8,"y":0,"width":23,"height":31},{"id":5,"x":8,"y":1,"width":23,"height":31},{"id":6,"x":2,"y":1,"width":23,"height":31},{"id":7,"x":2,"y":0,"width":23,"height":31},{"id":8,"x":2,"y":1,"width":23,"height":31},{"id":9,"x":1,"y":1,"width":31,"height":31},{"id":10,"x":1,"y":0,"width":31,"height":31},{"id":11,"x":1,"y":1,"width":31,"height":31}],
	"char/pipo-nekonin020.png":[{"id":0,"x":2,"y":8,"width":29,"height":24},{"id":1,"x":2,"y":7,"width":29,"height":24},{"id":2,"x":2,"y":8,"width":29,"height":24},{"id":3,"x":8,"y":8,"width":23,"height":24},{"id":4,"x":8,"y":7,"width":23,"height":24},{"id":5,"x":8,"y":8,"width":23,"height":24},{"id":6,"x":2,"y":8,"width":23,"height":24},{"id":7,"x":2,"y":7,"width":23,"height":24},{"id":8,"x":2,"y":8,"width":23,"height":24},{"id":9,"x":2,"y":8,"width":29,"height":24},{"id":10,"x":2,"y":7,"width":29,"height":24},{"id":11,"x":2,"y":8,"width":29,"height":24}],
	"char/pipo-nekonin021.png":[{"id":0,"x":6,"y":3,"width":21,"height":29},{"id":1,"x":6,"y":2,"width":21,"height":29},{"id":2,"x":6,"y":3,"width":21,"height":29},{"id":3,"x":6,"y":3,"width":21,"height":29},{"id":4,"x":6,"y":2,"width":21,"height":29},{"id":5,"x":6,"y":3,"width":21,"height":29},{"id":6,"x":6,"y":3,"width":21,"height":29},{"id":7,"x":6,"y":2,"width":21,"height":29},{"id":8,"x":6,"y":3,"width":21,"height":29},{"id":9,"x":6,"y":3,"width":21,"height":29},{"id":10,"x":6,"y":2,"width":21,"height":29},{"id":11,"x":6,"y":3,"width":21,"height":29}],
	"char/pipo-nekonin022.png":[{"id":0,"x":6,"y":5,"width":21,"height":27},{"id":1,"x":6,"y":4,"width":21,"height":27},{"id":2,"x":6,"y":5,"width":21,"height":27},{"id":3,"x":6,"y":5,"width":21,"height":27},{"id":4,"x":6,"y":4,"width":21,"height":27},{"id":5,"x":6,"y":5,"width":22,"height":27},{"id":6,"x":5,"y":5,"width":22,"height":27},{"id":7,"x":6,"y":4,"width":21,"height":27},{"id":8,"x":6,"y":5,"width":21,"height":27},{"id":9,"x":6,"y":5,"width":21,"height":27},{"id":10,"x":6,"y":4,"width":21,"height":27},{"id":11,"x":6,"y":5,"width":21,"height":27}],
	"char/pipo-nekonin023.png":[{"id":0,"x":6,"y":5,"width":21,"height":27},{"id":1,"x":6,"y":4,"width":21,"height":27},{"id":2,"x":6,"y":5,"width":21,"height":27},{"id":3,"x":6,"y":5,"width":21,"height":27},{"id":4,"x":6,"y":4,"width":21,"height":27},{"id":5,"x":6,"y":5,"width":21,"height":27},{"id":6,"x":6,"y":5,"width":21,"height":27},{"id":7,"x":6,"y":4,"width":21,"height":27},{"id":8,"x":6,"y":5,"width":21,"height":27},{"id":9,"x":6,"y":5,"width":21,"height":27},{"id":10,"x":6,"y":4,"width":21,"height":27},{"id":11,"x":6,"y":5,"width":21,"height":27}],
	"char/pipo-nekonin024.png":[{"id":0,"x":7,"y":7,"width":19,"height":25},{"id":1,"x":7,"y":6,"width":19,"height":25},{"id":2,"x":7,"y":7,"width":20,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":18,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":7,"y":6,"width":18,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":19,"height":25},{"id":10,"x":7,"y":6,"width":19,"height":25},{"id":11,"x":6,"y":7,"width":20,"height":25}],
	"char/pipo-nekonin025.png":[{"id":0,"x":8,"y":7,"width":18,"height":25},{"id":1,"x":8,"y":6,"width":18,"height":25},{"id":2,"x":8,"y":7,"width":19,"height":25},{"id":3,"x":8,"y":7,"width":17,"height":25},{"id":4,"x":8,"y":6,"width":18,"height":25},{"id":5,"x":8,"y":7,"width":20,"height":25},{"id":6,"x":5,"y":7,"width":20,"height":25},{"id":7,"x":7,"y":6,"width":18,"height":25},{"id":8,"x":8,"y":7,"width":17,"height":25},{"id":9,"x":7,"y":7,"width":18,"height":25},{"id":10,"x":7,"y":6,"width":18,"height":25},{"id":11,"x":6,"y":7,"width":19,"height":25}],
	"char/pipo-nekonin026.png":[{"id":0,"x":1,"y":1,"width":31,"height":31},{"id":1,"x":1,"y":0,"width":31,"height":31},{"id":2,"x":1,"y":1,"width":31,"height":31},{"id":3,"x":8,"y":1,"width":23,"height":31},{"id":4,"x":8,"y":0,"width":23,"height":31},{"id":5,"x":8,"y":1,"width":23,"height":31},{"id":6,"x":2,"y":1,"width":23,"height":31},{"id":7,"x":2,"y":0,"width":23,"height":31},{"id":8,"x":2,"y":1,"width":23,"height":31},{"id":9,"x":1,"y":1,"width":31,"height":31},{"id":10,"x":1,"y":0,"width":31,"height":31},{"id":11,"x":1,"y":1,"width":31,"height":31}],
	"char/pipo-nekonin027.png":[{"id":0,"x":1,"y":5,"width":31,"height":27},{"id":1,"x":1,"y":4,"width":31,"height":27},{"id":2,"x":1,"y":5,"width":31,"height":27},{"id":3,"x":8,"y":5,"width":23,"height":27},{"id":4,"x":8,"y":4,"width":23,"height":27},{"id":5,"x":8,"y":5,"width":23,"height":27},{"id":6,"x":2,"y":5,"width":23,"height":27},{"id":7,"x":2,"y":4,"width":23,"height":27},{"id":8,"x":2,"y":5,"width":23,"height":27},{"id":9,"x":1,"y":5,"width":31,"height":27},{"id":10,"x":1,"y":4,"width":31,"height":27},{"id":11,"x":1,"y":5,"width":31,"height":27}],
	"char/pipo-nekonin028.png":[{"id":0,"x":6,"y":7,"width":21,"height":25},{"id":1,"x":6,"y":6,"width":21,"height":25},{"id":2,"x":6,"y":7,"width":21,"height":25},{"id":3,"x":6,"y":7,"width":21,"height":25},{"id":4,"x":6,"y":6,"width":21,"height":25},{"id":5,"x":6,"y":7,"width":22,"height":25},{"id":6,"x":5,"y":7,"width":22,"height":25},{"id":7,"x":6,"y":6,"width":21,"height":25},{"id":8,"x":6,"y":7,"width":21,"height":25},{"id":9,"x":6,"y":7,"width":21,"height":25},{"id":10,"x":6,"y":6,"width":21,"height":25},{"id":11,"x":6,"y":7,"width":21,"height":25}],
	"char/pipo-nekonin029.png":[{"id":0,"x":7,"y":4,"width":19,"height":28},{"id":1,"x":7,"y":3,"width":19,"height":28},{"id":2,"x":7,"y":4,"width":19,"height":28},{"id":3,"x":7,"y":4,"width":19,"height":28},{"id":4,"x":7,"y":3,"width":19,"height":28},{"id":5,"x":7,"y":4,"width":19,"height":28},{"id":6,"x":7,"y":4,"width":19,"height":28},{"id":7,"x":7,"y":3,"width":19,"height":28},{"id":8,"x":7,"y":4,"width":19,"height":28},{"id":9,"x":7,"y":4,"width":19,"height":28},{"id":10,"x":7,"y":3,"width":19,"height":28},{"id":11,"x":7,"y":4,"width":19,"height":28}],
	"char/pipo-nekonin030.png":[{"id":0,"x":1,"y":1,"width":31,"height":31},{"id":1,"x":1,"y":0,"width":31,"height":31},{"id":2,"x":1,"y":1,"width":31,"height":31},{"id":3,"x":3,"y":1,"width":28,"height":31},{"id":4,"x":3,"y":0,"width":28,"height":31},{"id":5,"x":3,"y":1,"width":28,"height":31},{"id":6,"x":2,"y":1,"width":28,"height":31},{"id":7,"x":2,"y":0,"width":28,"height":31},{"id":8,"x":2,"y":1,"width":28,"height":31},{"id":9,"x":1,"y":1,"width":31,"height":31},{"id":10,"x":1,"y":0,"width":31,"height":31},{"id":11,"x":1,"y":1,"width":31,"height":31}],
	"char/pipo-nekonin031.png":[{"id":0,"x":6,"y":2,"width":21,"height":30},{"id":1,"x":6,"y":1,"width":21,"height":30},{"id":2,"x":6,"y":2,"width":21,"height":30},{"id":3,"x":6,"y":2,"width":21,"height":30},{"id":4,"x":6,"y":1,"width":21,"height":30},{"id":5,"x":6,"y":2,"width":22,"height":30},{"id":6,"x":5,"y":2,"width":22,"height":30},{"id":7,"x":6,"y":1,"width":21,"height":30},{"id":8,"x":6,"y":2,"width":21,"height":30},{"id":9,"x":6,"y":2,"width":21,"height":30},{"id":10,"x":6,"y":1,"width":21,"height":30},{"id":11,"x":6,"y":2,"width":21,"height":30}],
	"char/pipo-nekonin032.png":[{"id":0,"x":5,"y":4,"width":23,"height":28},{"id":1,"x":5,"y":3,"width":23,"height":28},{"id":2,"x":5,"y":4,"width":23,"height":28},{"id":3,"x":5,"y":4,"width":23,"height":28},{"id":4,"x":5,"y":3,"width":23,"height":28},{"id":5,"x":5,"y":4,"width":23,"height":28},{"id":6,"x":5,"y":4,"width":23,"height":28},{"id":7,"x":5,"y":3,"width":23,"height":28},{"id":8,"x":5,"y":4,"width":23,"height":28},{"id":9,"x":5,"y":4,"width":23,"height":28},{"id":10,"x":5,"y":3,"width":23,"height":28},{"id":11,"x":5,"y":4,"width":23,"height":28}],
}
const CHARACTERS_STATE_FRAMES = {
    "idle": {
        "maxFrame": 1,
        "frames": [1]
    },
    "idle_up": {
        "maxFrame": 1,
        "frames": [10]
    },
    "idle_down": {
        "maxFrame": 1,
        "frames": [1]
    },
    "idle_left": {
        "maxFrame": 1,
        "frames": [4]
    },
    "idle_right": {
        "maxFrame": 1,
        "frames": [7]
    },
    "move_up": {
        "maxFrame": 3,
        "frames": [9, 10, 11]
    },
    "move_down": {
        "maxFrame": 3,
        "frames": [0, 1, 2]
    },
    "move_left": {
        "maxFrame": 3,
        "frames": [3, 4, 5]
    },
    "move_right": {
        "maxFrame": 3,
        "frames": [6, 7, 8]
    },
}