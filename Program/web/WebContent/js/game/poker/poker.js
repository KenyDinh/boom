function getButtonLabel(type) {
	switch (type)  {
	case 1:
		return 'Bet';
	case 2:
		return 'Raise';
	case 3:
		return 'Check';
	case 4:
		return 'Call';
	case 5:
		return 'All In';
	case 6:
		return 'Fold';
	default:
		return '';
	}
}
class Sprite {
	constructor(ctx, x, y, imgSrc, width, height, {sx = 0, sy = 0, sw, sh, frameMax = 1, frameHoldMax = 1, initRotateDeg = 0, rotate = false, rotateSpeed = 0, loop = true, loopEndFunc = ()=>{this.currentFrame = this.frameMax - 1}} = {}) {
		this.ctx = ctx;
		this.x = x;
		this.y = y;
		this.imgSrc = imgSrc;
		this.image = getImage(this.imgSrc);
		this.width = width;
		this.height = height;
		this.sx = sx;
		this.sy = sy;
		this.sw = sw ?? this.width;
		this.sh = sh ?? this.height;
		this.currentFrame = 0;
		this.frameMax = frameMax;
		this.frameHold = 0;
		this.frameHoldMax = frameHoldMax;
		this.initRotateDeg = initRotateDeg;
		this.rotate = rotate;
		this.rotateDeg = Math.min(10, Math.max(1, 10 - rotateSpeed + 1)) * (Math.PI / 180);
		this.rotateDegTotal = 0;
		this.loop = loop;
		this.loopEndFunc = loopEndFunc;
		this.alive = true;
		this.syncVer = 0;
	}
	
	onMouseEvent() {
	}
	
	checkMouseEvent(e) {
		const cx = e.offsetX;
		const cy = e.offsetY;
		if (cx >= this.x && cx <= this.x + this.width && cy >= this.y && cy <= this.y + this.height) {
			this.onMouseEvent();
		}
	}

	checkSyncVersion(syncVer) {
		return (this.syncVer == syncVer);
	}

	syncVersion(syncVer) {
		this.syncVer = syncVer;
	}

	sync(data) {
		this.x = data.x ?? this.x;
		this.y = data.y ?? this.y;
		if (data.imgSrc && this.imgSrc != data.imgSrc) {
			this.imgSrc = data.imgSrc;
			this.image = getImage(this.imgSrc);
			this.currentFrame = 0;
			this.frameHold = 0;
		}
		this.width = data.w ?? this.width;
		this.height = data.h ?? this.height;
	}

	update() {
		if (!this.alive) {
			return;
		}
		if (this.frameMax <= 1) {
			this.frameHold++;
			this.draw();
			return;
		}
		if (this.frameHold > 0 && this.frameHold % this.frameHoldMax == 0) {
			this.currentFrame++;
		}
		if (this.currentFrame >= this.frameMax) {
			if (this.loop) {
				this.currentFrame = 0;
			} else {
				this.loopEndFunc();
			}
			
		}
		this.frameHold++;
		this.draw();
	}

	draw() {
		if (!this.imgSrc || !this.image) {
			return;
		}
		if (this.initRotateDeg != 0 || this.rotate) {
			this.ctx.save();
			this.ctx.translate(this.x + this.width/2, this.y + this.height/2);
			if (this.initRotateDeg != 0) {
				this.ctx.rotate(this.initRotateDeg);
			} else if (this.rotate) {
				this.ctx.rotate(this.rotateDeg * (this.frameHold % 360));
				this.rotateDegTotal += this.rotateDeg;
				if (!this.loop && this.rotateDegTotal >= 2 * Math.PI) {
					this.loopEndFunc();
				}
			}
			this.ctx.translate(-(this.x + this.width/2), -(this.y + this.height/2));
		}
		this.ctx.drawImage(
				this.image,
				this.sx + (this.sw * this.currentFrame),
				this.sy,
				this.sw,
				this.sh,
				this.x, 
				this.y, 
				this.width, 
				this.height);
		if (this.initRotateDeg != 0 || this.rotate) {
			this.ctx.restore();
		}
	}
}

class Card extends Sprite {
	constructor(ctx, x, y, num, type, width, height, {bnum = 0, btype = 0} = {}) {
		const cardImg = getCardImg(num, type);
		if (cardImg) {
			super(ctx, x, y, cardImg.src, width, height, {sx:cardImg.x, sy:cardImg.y, sw:cardImg.w, sh:cardImg.h});
			this.num = num;
			this.type = type;
			this.bnum = bnum;
			this.btype = btype;
			this.flip = false;
		}
	}
	
	onMouseEvent() {
		if (this.num == this.bnum && this.type == this.btype) {
			return;
		}
		this.flip = !this.flip;
		const cardImg = this.flip ? getCardImg(this.bnum, this.btype) : getCardImg(this.num, this.type);
		this.sx = cardImg.x;
		this.sy = cardImg.y;
		this.sw = cardImg.w;
		this.sh = cardImg.h;
	}
}

class ShareCard extends Sprite {
	constructor(ctx, pwidth, pheight, cards) {
		super(ctx, 0, 0, '', 0, 0);
		this.cardList = [];
		this.maxCard = 5;
		this.cardWidth = 60;
		this.cardHeight = 75;
		const sx = (pwidth / 2 - this.maxCard * this.cardWidth / 2);
		const sy = (pheight / 2 - this.cardHeight / 2);
		for (let i = 0; i < cards.length; i++) {
			const card = cards[i];
			this.cardList.push(new Card(this.ctx, sx + i * this.cardWidth, sy, card.num, card.type, this.cardWidth, this.cardHeight, {bnum:card.num, btype:card.type}));
		}
	}
	
	update() {
		for (const card of this.cardList) {
			card.update();
		}
	}
	
}

class Button extends Sprite {
	constructor(ctx, x, y, width, height, type) {
		super(ctx, x, y, BTN_IMAGE[type], width, height);
		this.sw = this.image.width;
		this.sh = this.image.height;
		this.label = getButtonLabel(type);
	}
	
	draw() {
		super.draw();
		
	}
}

class Player extends Sprite {
	constructor(ctx, x, y, width, height, index, avatar, name, coin, holdCards = []) {
		super(ctx, x, y, '', width, height);
		this.index = index;
		this.imgSrc = avatar;
		this.image = new Image();
		this.image.src = avatar;
		this.image.onload = () => {
			this.sw = this.image.width;
			this.sh = this.image.height;
		}
		this.maxCard = 2;
		this.cardWidth = 40;
		this.cardHeight = 50;
		this.cardList = [];
		const scale = (this.index == 0 ? 1.5 : 1.0);
		const sx = this.x + this.width / 2 - (this.maxCard * this.cardWidth * scale) / 2;
		const sy = this.y + (Math.floor((this.index - 1) / 3) == 1 ? this.height : -(this.cardHeight * scale));
		for (let i = 0; i < holdCards.length; i++) {
			const card = holdCards[i];
			this.cardList.push(new Card(this.ctx, sx + i * this.cardWidth * scale, sy, card.num, card.type, this.cardWidth * scale, this.cardHeight * scale));
		}
		if (this.index == 7 || this.index == 8) {
			this.x = this.x - this.width / 2 + (this.cardWidth * scale);
		} else {
			this.x = this.x + this.width / 2 - (this.cardWidth * scale);
		}
	}
	
	checkMouseEvent(e) {
		if (this.index != 0) {
			return;
		}
		for (const card of this.cardList) {
			card.checkMouseEvent(e);
		}
	}
	
	update() {
		super.update();
		for (const card of this.cardList) {
			card.update();
		}
	}
}

class TablePlay extends Sprite {
	constructor(ctx, pwidth, pheight, twidth, theight, tscale, players) {
		super(ctx, 0, 0, '', 0, 0);
		this.pw = pwidth;
		this.ph = pheight;
		this.tw = twidth;
		this.th = theight;
		this.ts = tscale;
		this.width = 48;
		this.height = 48;
		this.margin = 5;
		this.playerList = [];
		this.actionList = [];
		this.initlialize(players);
	}
	
	initlialize(players) {
		let selfIdx = 0;
		for (let i = 0; i < players.length; i++) {
			if (players[i].self) {
				selfIdx = i;
				break;
			}
		}
		for (let i = 0; i < players.length; i++) {
			const index = (i - selfIdx + 10) % 10;
			const pos = this.getPlayerPos(index);
			const avatar = 'http://localhost/friday/img/page/common-user.png';
			this.playerList.push(new Player(this.ctx, pos.x, pos.y, this.width, this.height, index, avatar, '', 0, players[i].hold_card));
			if (this.actionList.length <= 0 && players[i].action_list) {
				for (const action of players[i].action_list) {
					this.actionList.push(new Button(this.ctx, 0, 0, 100, 50, action.type));
				}
			}
		}
	}
	
	getPlayerPos(idx) {
		let x = 0, y = 0;
		switch (idx) {
		case 0:
			x = this.pw / 2 - this.width / 2;
			y = this.ph / 2 + (this.th * this.ts) / 2 - this.margin * this.ts + 20 * this.ts;//self
			break;
		case 1:
			x = this.pw / 2 - (this.tw * this.ts) / 2 + (this.tw * this.ts) / 5 - this.width / 2;
			y = this.ph / 2 + (this.th * this.ts) / 2 - this.margin * this.ts;
			break;
		case 2:
			x = this.pw / 2 - (this.tw * this.ts) / 2 - this.width / 2 - this.margin * this.ts;
			y = this.ph / 2 - (this.th * this.ts) / 2 + (this.th * this.ts * 2) / 3;
			break;
		case 3:
			x = this.pw / 2 - (this.tw * this.ts) / 2 - this.width / 2 - this.margin * this.ts;
			y = this.ph / 2 - (this.th * this.ts) / 2 + (this.th * this.ts) / 3 - this.height;
			break;
		case 4:
			x = this.pw / 2 - (this.tw * this.ts) / 2 + (this.tw * this.ts) / 5 - this.width / 2;
			y = this.ph / 2 - (this.th * this.ts) / 2 - this.height;
			break;
		case 5:
			x = this.pw / 2 - this.width / 2;
			y = this.ph / 2 - (this.th * this.ts) / 2 - this.height;
			break;
		case 6:
			x = this.pw / 2 - (this.tw * this.ts) / 2 + (this.tw * this.ts * 4) / 5 - this.width / 2;
			y = this.ph / 2 - (this.th * this.ts) / 2 - this.height;
			break;
		case 7:
			x = this.pw / 2 + (this.tw * this.ts) / 2 - this.width / 2;
			y = this.ph / 2 - (this.th * this.ts) / 2 + (this.th * this.ts) / 3 - this.height;
			break;
		case 8:
			x = this.pw / 2 + (this.tw * this.ts) / 2 - this.width / 2;
			y = this.ph / 2 - (this.th * this.ts) / 2 + (this.th * this.ts * 2) / 3;
			break;
		case 9:
			x = this.pw / 2 - (this.tw * this.ts) / 2 + (this.tw * this.ts * 4) / 5 - this.width / 2;
			y = this.ph / 2 + (this.th * this.ts) / 2 - this.margin * this.ts;
			break;
		default:
			break;
		}
		
		return {x,y};
	}
	
	checkMouseEvent(e) {
		for (const player of this.playerList) {
			player.checkMouseEvent(e);
		}
	}
	
	update() {
		for (const player of this.playerList) {
			player.update();
		}
		for (const button of this.actionList) {
			button.update();
		}
	}
}

class PokerGame {
	constructor(socket, width = 1000, height = 680) {
		this.width = width;
		this.height = height;
		this.tableWidth = 700;
		this.tableHeight = 327;
		this.tableScale = 1.25;
		this.status = 0;
		this.socket = socket;
		this.syncVersion = 0;
		this.gameObjects = new Map();
		this.currentPID = 0;
		this.totalFrame = 0;
		this.path = new Path2D();
		this.init();
	}

	init() {
		const canvas = document.querySelector('#canvas');
		this.ctx = canvas.getContext('2d');
		canvas.width = this.width;
		canvas.height = this.height;
		this.ctx.fillStyle = "#000000";
		this.ctx.fillRect(0, 0, this.width, this.height);
		this.ctx.fillStyle = "#248D0F";
		
		this.gameObjects.set('backgrounds', []);
		this.gameObjects.set('shareCards', []);
		this.gameObjects.set('tablePlayers', []);
		this.socket.init({
			onmessage: this.sync.bind(this)
		});
		this.smapleDate();
		canvas.addEventListener('click', (e) => {
			this.checkMouseEvent(e);
		});
	}
	
	sync(event) {
		if (!event.data) {
			return;
		}
		console.log(event.data);
		//this.syncVersion++;
		//const jsonObj = JSON.parse(event.data);
		
	}
	
	smapleDate() {
		this.gameObjects.get('backgrounds').push(new Sprite(this.ctx, 0, 0, BG_IMAGE[0], this.width, this.height));
		this.gameObjects.get('backgrounds').push(new Sprite(this.ctx, this.width/2-this.tableWidth*this.tableScale/2, this.height/2-this.tableHeight*this.tableScale/2, TBL_IMAGE[0], this.tableWidth*this.tableScale, this.tableHeight*this.tableScale, {sw:this.tableWidth,sh:this.tableHeight}));
		
		this.gameObjects.get('shareCards').push(new ShareCard(this.ctx, this.width, this.height, [{num:1,type:2},{num:2,type:2},{num:3,type:2},{num:4,type:2},{num:5,type:2}]));
		//this.gameObjects.get('cards').push(new Card(this.ctx, 0, 0, 1, 3, 80, 100));
		const players = [];
		players.push({hold_card: [{num:0,type:0},{num:0,type:0}]});
		players.push({hold_card: [{num:0,type:0},{num:0,type:0}]});
		players.push({self:1,action_list: [{type:1},{type:2},{type:3}], hold_card: [{num:(Math.floor(Math.random() * 13)+ 1),type:Math.floor(Math.random() * 3)},{num:(Math.floor(Math.random() * 13)+ 1),type:Math.floor(Math.random() * 3)}]});
		players.push({hold_card: [{num:0,type:0},{num:0,type:0}]});
		players.push({hold_card: [{num:0,type:0},{num:0,type:0}]});
		players.push({hold_card: [{num:0,type:0},{num:0,type:0}]});
		players.push({hold_card: [{num:0,type:0},{num:0,type:0}]});
		players.push({hold_card: [{num:0,type:0},{num:0,type:0}]});
		players.push({hold_card: [{num:0,type:0},{num:0,type:0}]});
		players.push({hold_card: [{num:0,type:0},{num:0,type:0}]});
		
		this.gameObjects.get('tablePlayers').push(new TablePlay(this.ctx, this.width, this.height, this.tableWidth, this.tableHeight, this.tableScale, players));
	}
	
	checkMouseEvent(e) {
		for (const tbPlayer of this.gameObjects.get('tablePlayers')) {
			tbPlayer.checkMouseEvent(e);
		}
	}

	run() {
		window.requestAnimationFrame(() => this.run());
		this.update();
	}

	update() {
		this.draw();
		this.totalFrame++;
	}

	draw() {
		for (const bg of this.gameObjects.get('backgrounds')) {
			bg.update();
		}
		for (const card of this.gameObjects.get('shareCards')) {
			card.update();
		}
		for (const tbPlayer of this.gameObjects.get('tablePlayers')) {
			tbPlayer.update();
		}
	}

	sync(event) {
	}

}
// draw canvas
$j(document).ready(function() {
	const pokerGame = new PokerGame(new BoomSocket($j('#socket_url').text()));
	pokerGame.run();
});