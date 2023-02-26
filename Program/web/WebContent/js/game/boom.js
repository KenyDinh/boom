GAME_STATUS = {
	INIT : 0,
	PREPARING: 1,
	PLAYING : 2,
	PAUSED : 3,
	RESUME : 4,
	FINISHED : 5
}
BOMB_EFFECT_ANIMATE = {
	NONE : 0,
	ROTATE : 1
}
// class
class Sprite {
	constructor(ctx, x, y, imgSrc, width, height, {sx = 0, sy = 0, sw, sh, gbAlpha = 1.0, frameMax = 1, frameHoldMax = 1, initRotateDeg = 0, rotate = false, rotateSpeed = 0, loop = true, loopEndFunc = ()=>{this.currentFrame = this.frameMax - 1}} = {}) {
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
		this.gbAlpha = gbAlpha;
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
		if (this.gbAlpha != 1.0) {
			this.ctx.globalAlpha = this.gbAlpha;
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
		if (this.gbAlpha != 1.0) {
			this.ctx.globalAlpha = 1.0;
		}
		if (this.initRotateDeg != 0 || this.rotate) {
			this.ctx.restore();
		}
	}
}

class Bomb extends Sprite {
	constructor(ctx, x, y, imgSrc, width, height, effects = {}) {
		super(ctx, x, y, imgSrc, width, height);
		this.effects = effects;
		this.initEffect();
	}

	initEffect() {
		this.effectList = [];
		for (const eff of this.effects) {
			const _sx = ((eff.id % ITM_CNT) * 32);
			const _sy = (parseInt(eff.id / ITM_CNT) * 32);
			const _rt = (eff.tp == BOMB_EFFECT_ANIMATE.ROTATE);
			this.effectList.push(new Sprite(this.ctx, this.x, this.y, BOMB_ITEM[0], 16, 16, {sx:_sx, sy:_sy, sw:32, sh:32, rotate:_rt, rotateSpeed:9}));
		}
	}

	update() {
		super.update();
		let sx = this.x + this.width;
		for (const effect of this.effectList) {
			if (!effect.alive) {
				continue;
			}
			sx = sx + 2 - effect.width;
			effect.x = sx;
			effect.y = this.y - 2;
			effect.update();
		}
	}
}

class Item extends Sprite {
	constructor(ctx, x, y, width, height, imageID) {
		const _sx = ((imageID % ITM_CNT) * 32);
		const _sy = (parseInt(imageID / ITM_CNT) * 32);
		super(ctx, x, y, BOMB_ITEM[0], width, height, {sx:_sx, sy:_sy, sw:32, sh:32, });
	}
}

class SpItem extends Sprite {
	constructor(ctx, x, y, width, height, imageID) {
		let shiftX, shiftY;
		shiftX = (width <= 32) ? 0 : (width - 32)/2;
		shiftY = (height <= 32) ? 0 : (height - 32)/2;
		const _sx = ((imageID % ITM_CNT) * 32);
		const _sy = (parseInt(imageID / ITM_CNT) * 32);
		super(ctx, x + shiftX, y + shiftY, BOMB_ITEM[0], Math.min(width, 32), Math.min(height, 32), {sx:_sx, sy:_sy, sw:32, sh:32});
		shiftX = (width > 64) ? 0 : (64 - width)/2;
		shiftY = (height > 64) ? 0 : (64 - height)/2;
		this.shiningEffect = new Sprite(this.ctx, x - shiftX, y - shiftY, REVIVAL_IMAGE[2], Math.max(64,width), Math.max(64,height), {sw:96, sh:96, frameMax: 15, frameHoldMax: 8})
	}
	
	sync(data) {
		
	}
	
	update() {
		super.update();
		this.shiningEffect.update();
	}
	
}

class Tree extends Sprite {
	constructor(ctx, x, y, width, height, imageID) {
		imageID = Math.max(0,imageID-1);
		const _sx = ((imageID % TREE_CNT) * 48);
		const _sy = (parseInt(imageID / TREE_CNT) * 48);
		super(ctx, x, y, TREES_IMAGE[0], width, height, {sx:_sx, sy:_sy, sw:48, sh:48, });
	}
}

class Ghost extends Sprite {
	constructor(ctx, x, y, imgSrc, width, height, revive) {
		super(ctx, x, y, imgSrc, width, height, {sw:32, sh:32, frameMax: 8, frameHoldMax: 2, loop: false});
		this.revive = revive;//revival gauge
		let shiftX = (width > 64) ? 0 : (64 - width)/2;
		let shiftY = (height > 64) ? 0 : (64 - height)/2;
		this.revivalEffect = new Sprite(this.ctx, this.x - shiftX, this.y - shiftY, REVIVAL_IMAGE[0], Math.max(64,width), Math.max(64,height), {sw:96, sh:96, frameMax: 10, frameHoldMax: 2})
	}
	
	setAttention() {
		this.attentionEffect = new Sprite(this.ctx, this.x, this.y, REVIVAL_IMAGE[1], this.width, this.height, {sw:48, sh:48, frameMax: 10, frameHoldMax: 2});
	}
	
	sync(data) {
		super.sync(data);
		if (data.revive) {
			this.revive = data.revive;
		} else {
			this.revive = 0;
		}
	}
	
	update() {
		super.update();
		if (this.revive > 0) {
			this.revivalEffect.update();
		}
		if (this.attentionEffect) {
			this.attentionEffect.update();
		}
	}
	
	draw() {
		super.draw();
		if (this.revive > 0) {
			this.ctx.fillStyle = "#333333";
			this.ctx.fillRect(this.x, this.y - 16, this.width, 10);
			this.ctx.fillStyle = "#28B463";
			this.ctx.fillRect(this.x + 1, this.y - 16 + 1, (this.revive / 1000) * (this.width - 2), 8);
		}
	}
	
	isReviving() {
		return (this.revive > 0);
	}

}

class Explosion extends Sprite {
	constructor(ctx, x, y, imgSrc, width, height) {
		super(ctx, x, y, imgSrc, width, height, {sw:48, sh:48, frameMax:10, frameHoldMax: 2, loop:false, loopEndFunc: ()=>{this.alive = false}});
	}
}

class AbilityEffect extends Sprite {
	constructor(ctx, x, y, width, height, id, direction = 0) {
		let _initRotDeg = 0;
		switch (direction) {
			case 0:
				_initRotDeg = -(Math.PI / 2);
				break;	
			case 1:
				_initRotDeg = (Math.PI / 2);
				break;
			case 2:
				_initRotDeg = Math.PI;
				break;
			case 3:
				_initRotDeg = 0;
				break;
			break;
		}
		const imageSrc = EFFECT_IMAGE[id - 1];
		const effectFrame = EFFECT_IMAGE_FRAMES[imageSrc];
		super(ctx, x, y, imageSrc, width, height, {sw: 48, sh:48, frameMax:effectFrame.maxFrame, frameHoldMax: effectFrame.maxFrameHold, initRotateDeg: _initRotDeg, loop:false, loopEndFunc: ()=>{this.alive = false}});
	}
}

class SkillEffect extends Sprite {
	constructor(ctx, x, y, width, height, id, direction = 0) {
		let _initRotDeg = 0;
		switch (direction) {
			case 0:
				_initRotDeg = -(Math.PI / 2);
				break;	
			case 1:
				_initRotDeg = (Math.PI / 2);
				break;
			case 2:
				_initRotDeg = Math.PI;
				break;
			case 3:
				_initRotDeg = 0;
				break;
			break;
		}
		const imageSrc = EFFECT_IMAGE[id - 1];
		const effectFrame = EFFECT_IMAGE_FRAMES[imageSrc];
		super(ctx, x, y, imageSrc, width, height, {sw: 48, sh:48, frameMax:effectFrame.maxFrame, frameHoldMax: effectFrame.maxFrameHold, initRotateDeg: _initRotDeg});
	}
}

class Player extends Sprite {
	constructor(ctx, name, x, y, width, height, imgId, state, hp, maxHP, effect = [], invisible = false, isAlly = false, gauge = 0) {
		super(ctx, x, y, CHARACTERS_IMAGE[imgId - 1], width, height, {frameHoldMax: 4});
		this.imgId = imgId;
		this.name = name;
		this.effectImage = getImage(BOMB_ITEM[0]);
		this.state = state;
		this.hp = hp;
		this.maxHP = maxHP;
		this.invisible = invisible;
		this.isAlly = isAlly;
		this.effect = effect;
		this.gauge = gauge;
		this.cPut = CHARACTERS_IMAGE_PUT[CHARACTERS_IMAGE[this.imgId - 1]];
		this.cFrame = CHARACTERS_STATE_FRAMES[this.state];
	}

	sync(data) {
		super.sync(data);
		this.name = data.name ?? this.name;
		this.state = data.state ?? this.state;
		this.hp = data.hp ?? this.hp;
		this.maxHP = data.maxHP ?? this.maxHP;
		this.effect = data.effect ?? this.effect;
		this.invisible = data.invisible ?? this.invisible;
		this.isAlly = data.isAlly ?? this.isAlly;
		if (data.gauge) {
			this.gauge = data.gauge;
		} else {
			this.gauge = 0;
		}
		if (data.img && data.img != this.imgId) {
			this.imgId = data.img;
			this.image = getImage(CHARACTERS_IMAGE[this.imgId - 1]);
			this.cPut = CHARACTERS_IMAGE_PUT[CHARACTERS_IMAGE[this.imgId - 1]];
		}
		this.cFrame = CHARACTERS_STATE_FRAMES[this.state];
		if (this.currentFrame >= this.cFrame.maxFrame) {
			this.currentFrame = 0;
			this.frameHold = 0;
		}
	}

	update() {
		this.frameMax = this.cFrame.maxFrame;
		super.update();
	}

	draw() {
		if (this.invisible) {
			return;
		}
		this.ctx.drawImage(
			this.image, 
			(this.cPut[this.cFrame.frames[this.currentFrame]].x  + (this.cFrame.frames[this.currentFrame] % 3) * CHAR_IMG_SIZE), 
			(this.cPut[this.cFrame.frames[this.currentFrame]].y + parseInt(this.cFrame.frames[this.currentFrame] / 3) * CHAR_IMG_SIZE), 
			(this.cPut[this.cFrame.frames[this.currentFrame]].width), 
			(this.cPut[this.cFrame.frames[this.currentFrame]].height), 
			this.x, 
			this.y, 
			this.width, 
			this.height
		);
		this.ctx.font = "12px Comic Sans MS";
		this.ctx.fillStyle = "#2145b0";
		this.ctx.textAlign = "center";
		this.ctx.fillText(this.name, this.x + this.width / 2, this.y - 16 - 1);
		this.ctx.fillStyle = "#0d0d0d";
		this.ctx.fillRect(this.x, this.y - 16, this.width, 8);
		this.ctx.fillStyle = "#262626";
		this.ctx.fillRect(this.x + 1, this.y - 16 + 1, this.width - 2, 8 - 2);
		this.ctx.fillStyle = (this.isAlly ? "#1fa155" : "#b02121");
		this.ctx.fillRect(this.x + 1, this.y - 16 + 1, (this.hp / this.maxHP) * (this.width - 2), 8 - 2);
		//gauge
		if (this.gauge > 0) {
			this.ctx.fillStyle = "#FCB40E";
			this.ctx.fillRect(this.x + 1, this.y - 16 + 1 + 4, (this.gauge / 1000) * (this.width - 2), 2);
		}
		if (this.effect.length) {
			let sx = this.x;
			for (const eff of this.effect) {
				const id = eff.id;
				if (id < 0) {
					continue;
				}
				if (!eff.blk || (this.frameHold % 8 < 4)) {
					this.ctx.drawImage(
							this.effectImage,
							((id % ITM_CNT) * 32),
							(parseInt(id / ITM_CNT) * 32),
							32,
							32,
							sx,
							this.y - 10,
							16,
							16
					);
					if (eff.stk) {
						this.ctx.font = "8px Comic Sans MS";
						this.ctx.fillStyle = "#FFFFFF";
						this.ctx.textAlign = "center";
						this.ctx.fillText(eff.stk, sx + 12, this.y - 2);
					}
				}
				sx += 14;
			}
		}
	}
}
class BoomGame {
	constructor(socket, width = 864, height = 672) {
		this.width = width;
		this.height = height;
		this.status = GAME_STATUS.INIT;
		this.mapID = 0;
		this.foregroundID = 0;
		this.unitSize = 0;
		this.socket = socket;
		this.syncVersion = 0;
		this.gameObjects = new Map();
		this.currentPID = 0;
		this.currentGID = 0;
		this.rvStatus = 0;
		this.mode = 0;
		this.preparingTime = 0;
		this.totalFrame = 0;
		this.path = new Path2D();
		this.keyStack = [];
		this.hasSeExplosion = false;
		this.mapPidNameGroup = new Map();
		this.init();
	}

	init() {
		const canvas = document.querySelector('#canvas');
		this.ctx = canvas.getContext('2d');
		canvas.width = this.width;
		canvas.height = this.height;
		canvas.addEventListener('click', (e) => {
			if (this.status == GAME_STATUS.PLAYING) {
				return;
			}
			if (this.ctx.isPointInPath(this.path, e.offsetX, e.offsetY)) {
				if (this.status == GAME_STATUS.INIT) {
					this.socket.sendMessage('start')
				} else if (this.status == GAME_STATUS.FINISHED) {
					window.location.reload();
				}
			}
		});
		this.gameObjects.set('maps', []);
		this.gameObjects.set('foregrounds', []);
		this.gameObjects.set('portals', new Map());
		this.gameObjects.set('trees', []);
		this.gameObjects.set('bombs', new Map());
		this.gameObjects.set('items', new Map());
		this.gameObjects.set('spitems', new Map());
		this.gameObjects.set('explosions', []);
		this.gameObjects.set('abilities', []);
		this.gameObjects.set('skills', new Map());
		this.gameObjects.set('alive_players', new Map());
		this.gameObjects.set('deadth_players', new Map());
		this.gameObjects.set('fire_walls', new Map());
		this.socket.init({
			onmessage: this.sync.bind(this)
		});
		this.keys = {
			a: {
				name: 'a',
				pressed: false
			},
			s: {
				name: 's',
				pressed: false
			},
			d: {
				name: 'd',
				pressed: false
			},
			w: {
				name: 'w',
				pressed: false
			},
		}
		window.addEventListener('keydown', (event) => {
			switch (event.key) {
				case 'a':
				case 'ArrowLeft':
					this.keys.a.pressed = true;
					this.stackKey('a');
					break;
				case 's':
				case 'ArrowDown':
					this.keys.s.pressed = true;
					this.stackKey('s');
					break;
				case 'w':
				case 'ArrowUp':
					this.keys.w.pressed = true;
					this.stackKey('w');
					break;
				case 'd':
				case 'ArrowRight':
					this.keys.d.pressed = true;
					this.stackKey('d');
					break;
			}
		});
		window.addEventListener('keyup', (event) => {
			switch (event.key) {
				case 'a':
				case 'ArrowLeft':
					this.keys.a.pressed = false;
					this.removeKey('a');
					break;
				case 's':
				case 'ArrowDown':
					this.keys.s.pressed = false;
					this.removeKey('s');
					break;
				case 'w':
				case 'ArrowUp':
					this.keys.w.pressed = false;
					this.removeKey('w');
					break;
				case 'd':
				case 'ArrowRight':
					this.keys.d.pressed = false;
					this.removeKey('d');
					break;
				case ' ':
					this.socket.sendMessage('create_boom');
					break;
				case 'f':
					this.socket.sendMessage('use_ability');
					break;
			}
		});
		//
		$j('.exit-game > button').click(() => {
			if (this.socket && this.socket.isOpen()) {
				this.socket.sendMessage('stop')
				$j('.exit-game').hide();
			}
		});
		$j('.force-exit-game > button').click(() => {
			let cnf = confirm("Force finish this game?");
			if (!cnf) {
				return;
			}
			if (this.socket && this.socket.isOpen()) {
				this.socket.sendMessage('stop')
				$j('.force-exit-game').hide();
			}
		});
		this.seExplosion = document.getElementById('se-explosion');
		this.seExplosion.volume = 0.5;
	}
	
	updateSoundTrack(id) {
		if (!id) {
			return;
		}
		if (this.seId && this.seId == id) {
			return;
		}
		this.seId = id;
		if ($j('.se-track[data-id=' + this.seId + ']').length <= 0) {
			return;
		}
		if (this.seTrack) {
			this.seTrack.pause();
			this.seTrack = null;
		}
		this.seTrack = $j('.se-track[data-id=' + this.seId + ']')[0];
		this.seTrack.loop = true;
		this.seTrack.volume = 0.5;
		if (this.seExplosion) {
			this.seExplosion.volume = 0.3;
		}
		this.seTrack.play();
	}

	removeKey(key) {
		for (let i = 0; i < this.keyStack.length; i++) {
			if (this.keyStack[i] == key) {
				this.keyStack.splice(i, 1);
				break;
			}
		}
	}

	stackKey(key) {
		this.removeKey(key);
		this.keyStack.push(key);
	}

	getLastKey() {
		if (this.keyStack.length) {
			return this.keyStack[this.keyStack.length - 1];
		}
		return '';
	}

	checkKey() {
		if (this.keys.a.pressed && this.getLastKey() === this.keys.a.name) {
			this.socket.sendMessage('move_left');
		} else if (this.keys.s.pressed && this.getLastKey() === this.keys.s.name) {
			this.socket.sendMessage('move_down');
		} else if (this.keys.d.pressed && this.getLastKey() === this.keys.d.name) {
			this.socket.sendMessage('move_right');
		} else if (this.keys.w.pressed && this.getLastKey() === this.keys.w.name) {
			this.socket.sendMessage('move_up');
		} else {
			this.socket.sendMessage('move_stop');
		}
	}

	run() {
		window.requestAnimationFrame(() => this.run());
		this.update();
	}
	
	calculateTotalScore(playerObj) {
		let total = 0;
		for (const name in playerObj) {
			if (playerObj.hasOwnProperty(name)) {
				const rScore = playerObj[name];
				for (const round in rScore) {
					if (rScore.hasOwnProperty(round)) {
						total += rScore[round];
					}
				}
				break;
			}
		}
		return total;
	}
	
	drawDynamicScoreBoard() {
		$j('#left-panel').hide();
		$j('#score-panel').show();
		if (this.scores) {
			const tableScore = $j('#player-score');
			tableScore.empty();
			const func = this.calculateTotalScore;
			if (this.mapPidNameGroup.size) {
				const groupArr = []; // {}
				for (const playerObj of this.scores) {
					for (const name in playerObj) {
						if (playerObj.hasOwnProperty(name)) {
							let grName = '';
							let total_point = func(playerObj);
							if (this.mapPidNameGroup.has(name)) {
								grName = this.mapPidNameGroup.get(name);
							}
							//
							let exist = false;
							if (grName != '') {
								for (const group of groupArr) {
									if (group.name == grName) {
										group.point += total_point;
										group.member.push({name:name,point:total_point});
										group.member.sort((a,b)=>(b.point - a.point));
										exist = true;
										break;
									}
								}
							}
							if (!exist) {
								groupArr.push({name:grName,point:total_point,member:[{name:name,point:total_point}]});
							}
							break;
						}
					}
				}
				groupArr.sort((a,b)=>(b.point - a.point));
				let html = '<div class="score-board-container">';
				for (const group of groupArr) {
					html += '<div class="score-board-group-container">';
					html += '<span>' + group.name + '</span>';
					html += '<span class="score-board-gp-score">' + group.point + '</span>';
					html += '</div>';
					for (const mem of group.member) {
						html += '<div class="score-board-player-container">';
						let avatar;
						if (this.playerAvas && this.playerAvas[mem.name]) {
							avatar = $j('.character[data-id=' + this.playerAvas[mem.name] + ']');
						}
						if (avatar && avatar.length) {
							html += avatar.clone().removeClass('selected').show().prop('outerHTML');
						} else {
							html += '<div class="character"></div>';
						}
						html += '<div>' + mem.name + '</div>';
						html += '<div class="score-board-gp-score">' + mem.point + '</div>';
						html += '</div>';
					}
				}
				html += '</div>';
				tableScore.append(html);
			} else {
				this.scores.sort((a,b)=>(func(b) - func(a)));
				const scoreBoard = $j('<div class="score-board-container"></div>')
				for (const playerObj of this.scores) {
					for (const name in playerObj) {
						if (playerObj.hasOwnProperty(name)) {
							const player_score = $j('<div class="score-board-player-container"></div>');
							let avatar;
							if (this.playerAvas && this.playerAvas[name]) {
								avatar = $j('.character[data-id=' + this.playerAvas[name] + ']');
							}
							if (avatar && avatar.length) {
								player_score.append(avatar.clone().removeClass('selected').show());
							} else {
								player_score.append('<div class="character"></div>');
							}
							player_score.append('<div>' + name + '</div>');
							player_score.append('<div class="score-board-gp-score">' + func(playerObj) + '</div>');
							scoreBoard.append(player_score);
							break;
						}
					}
				}
				tableScore.append(scoreBoard);
			}
		}
	}
	
	drawTableScore() {
		if (!this.scores) {
			return false;
		}
		if (!this.tableScore) {
			let roundMax = 0;
			for (const playerObj of this.scores) {
				for (const name in playerObj) {
					if (playerObj.hasOwnProperty(name)) {
						const rScore = playerObj[name];
						for (const round in rScore) {
							if (rScore.hasOwnProperty(round)) {
								const rIdx = round.replaceAll(/[^0-9]/g,"");
								if (rIdx > roundMax) {
									roundMax = rIdx;
								}
							}
						}
						break;
					}
				}
			}
			let table = '<table border="1" style="width:100%;height:100%;border-collapse:collapse;">';
			table += '<tr>';
			table += '<th>Player</th>';
			if (this.mapPidNameGroup.size) {
				table += '<th>Team</th>';
			}
			for (let i = 0; i < roundMax; i++) {
				table += '<th>Round ' + (i + 1) + '</th>';
			}
			table += '<th>Total</th>';
			table += '</tr>';
			const func = this.calculateTotalScore;
			this.scores.sort((a,b)=>(func(b) - func(a)));
			for (const playerObj of this.scores) {
				for (const name in playerObj) {
					if (playerObj.hasOwnProperty(name)) {
						table += '<tr>';
						table += '<td>' + name + '</td>';
						if (this.mapPidNameGroup.size) {
							if (this.mapPidNameGroup.has(name)) {
								table += '<td>' + this.mapPidNameGroup.get(name) + '</td>';
							} else {
								table += '<td></td>';
							}
						}
						const rScore = playerObj[name];
						let total = 0;
						for (let i = 0; i < roundMax; i++) {
							const key = 'r' + (i + 1);
							if (rScore.hasOwnProperty(key)) {
								table += '<td>' + rScore[key] + '</td>';
								total += rScore[key];
							} else {
								table += '<td>-</td>';
							}
						}
						table += '<td>' + total + '</td>';
						table += '</tr>';
						break;
					}
				}
			}
			table += '</table>';
			const maxWidth = 200 + ((parseInt(roundMax) + 1) * 80) + (this.mapPidNameGroup.size > 0 ? 100 : 0);
			const maxHeight = 60 + (this.scores.length * 40);
			let data = "";
			data += "<svg xmlns='http://www.w3.org/2000/svg' width='" + maxWidth + "' height='" + maxHeight + "'>";
			data += "<foreignObject width='100%' height='100%'>";
			data += "<div xmlns='http://www.w3.org/1999/xhtml' style='font-family:Comic Sans MS;font-size:20px;color:#FFF;text-align:left;'>";
			data += table;
			data += "</div>";
			data += "</foreignObject>";
			data += "</svg>";
			//console.log(data);
			
			const DOMURL = (self.URL || self.webkitURL || self);
			this.tableScore = new Image();
			const svg = new Blob([data], {type: "image/svg+xml;charset=utf-8"});
			const url = DOMURL.createObjectURL(svg);
			this.tableScore.onload = () => {
				this.tableScoreLoaded = true;
				DOMURL.revokeObjectURL(url);
			}
			this.tableScore.src = url;
		} else if (this.tableScoreLoaded) {
			this.path = new Path2D();
			this.path.rect(0, 0, canvas.width, canvas.height);
			this.ctx.fillStyle = "rgba(66, 73, 73, 0.9)";
			this.ctx.fill(this.path);
			const _w = this.tableScore.width;
			const _h = this.tableScore.height;
			this.ctx.font = "40px Comic Sans MS";
			this.ctx.fillStyle = "#FFF";
			this.ctx.textAlign = "center";
			this.ctx.fillText("Player Score", canvas.width/2, (canvas.height - _h) / 2 - 20);
			this.ctx.drawImage(this.tableScore, 0, 0, _w, _h, (canvas.width - _w) / 2, (canvas.height - _h) / 2, _w, _h);
			const flag = 20;
			const f = this.totalFrame % (flag * 2);
			let fs = "255,255,255,";
			if (f <= flag) {
				fs += Math.min(1, f/flag);
			} else {
				fs += Math.min(1, (2 * flag - f)/flag);
			}
			this.ctx.font = "24px Segoe UI";
			this.ctx.fillStyle = "rgba(" + fs + ")";
			this.ctx.fillText("touch to exit", canvas.width/2, canvas.height - 50);
			return true;
		}
		return false;
	}

	update() {
		this.draw();
		this.checkKey();
		if (this.status != GAME_STATUS.INIT) {
			$j('#map-list').hide();
			$j('#game-mode').hide();
			if (this.status != GAME_STATUS.PREPARING) {
				$j('#character-list').hide();
				$j('#character-class').hide();
				this.seTrack.volume = 0.4;
			}
		} 
		if (this.status != GAME_STATUS.INIT && this.status != GAME_STATUS.PREPARING) {
			const cpid = this.currentPID;
			if (this.inspectors && this.inspectors.find((id)=>(id==cpid))) {
				this.drawDynamicScoreBoard();
			}
		} else {
			$j('#score-panel').hide();
		}
		if (this.gameObjects.get('alive_players').size + this.gameObjects.get('deadth_players').size == 1) {
			if ($j('.force-exit-game').is(':visible')) {
				$j('.exit-game').hide();
			} else {
				$j('.exit-game').show();
			}
		} else {
			$j('.exit-game').hide();
		}
		this.totalFrame++;
	}

	draw() {
		for (const map of this.gameObjects.get('maps')) {
			map.update();
		}
		for (const portal of this.gameObjects.get('portals').values()) {
			portal.update();
		}
		for (const tree of this.gameObjects.get('trees')) {
			tree.update();
		}
		for (const deadthPlayer of this.gameObjects.get('deadth_players').values()) {
			if (!deadthPlayer.isReviving()) {
				deadthPlayer.update();
			}
		}
		for (const firewall of this.gameObjects.get('fire_walls').values()) {
			firewall.update();
		}
		for (const bomb of this.gameObjects.get('bombs').values()) {
			bomb.update();
		}
		for (const item of this.gameObjects.get('items').values()) {
			item.update();
		}
		for (const item of this.gameObjects.get('spitems').values()) {
			item.update();
		}
		let _tPlayer = null;
		for (const [_id, _player] of this.gameObjects.get('alive_players')) {
			if (_id != this.currentPID) {
				_player.update();
			} else {
				_tPlayer = _player;
			}
		}
		if (_tPlayer != null) {
			_tPlayer.update();
		}
		for (const deadthPlayer of this.gameObjects.get('deadth_players').values()) {
			if (deadthPlayer.isReviving()) {
				deadthPlayer.update();
			}
		}
		for (const ability of this.gameObjects.get('abilities')) {
			ability.update();
		}
		for (const skill of this.gameObjects.get('skills').values()) {
			skill.update();
		}
		for (const explosion of this.gameObjects.get('explosions')) {
			explosion.update();
		}
		if (this.hasSeExplosion) {
			this.seExplosion.cloneNode(true).play();
		}
		//
		for (const foreground of this.gameObjects.get('foregrounds')) {
			foreground.update();
		}
		if (this.status == GAME_STATUS.INIT) {
			this.ctx.fillStyle = "rgba(66, 73, 73, 0.5)";
			this.ctx.fillRect(0, 0, canvas.width, canvas.height);
			this.path = new Path2D();
			this.path.moveTo(canvas.width / 3, canvas.height/4);
			this.path.lineTo(2 * canvas.width / 3, canvas.height/2);
			this.path.lineTo(canvas.width / 3, 3 *canvas.height/4);
			this.path.lineTo(canvas.width / 3, canvas.height/4);
			this.ctx.fillStyle = "rgba(255,255,255,0.8)";
			this.ctx.fill(this.path);
			const modeText = (this.mode == 0) ? "Tournament" : (this.mode == 2 ? "Party" : "Single");
			this.ctx.font = (this.mode == 0) ? "40px Comic Sans MS" : "60px Comic Sans MS";
			this.ctx.fillStyle = "#636363";
			this.ctx.textAlign = "center";
			this.ctx.fillText(modeText, canvas.width/2 - (this.mode == 0 ? 20 : 10), canvas.height/2 + (this.mode == 0 ? 15 : 23));
		} else if ((this.status == GAME_STATUS.PREPARING || this.status == GAME_STATUS.RESUME) && this.preparingTime) {
			this.ctx.fillStyle = "rgba(66, 73, 73, 0.3)";
			this.ctx.fillRect(0, 0, canvas.width, canvas.height);
			this.path = new Path2D();
			this.path.arc(canvas.width / 2, canvas.height / 2, canvas.height / 5, 0, 2 * Math.PI);
			this.ctx.fillStyle = "rgba(255,255,255,0.6)";
			this.ctx.fill(this.path);
			this.ctx.font = "80px Comic Sans MS";
			this.ctx.fillStyle = "#CB4335";
			this.ctx.textAlign = "center";
			this.ctx.fillText(this.preparingTime, canvas.width/2, canvas.height/2 + 40 - 10);
		} else if (this.status == GAME_STATUS.PAUSED) {
			this.ctx.fillStyle = "rgba(66, 73, 73, 0.3)";
			this.ctx.fillRect(0, 0, canvas.width, canvas.height);
			this.path = new Path2D();
			this.path.arc(canvas.width / 2, canvas.height / 2, canvas.height / 5, 0, 2 * Math.PI);
			this.ctx.fillStyle = "rgba(255,255,255,0.6)";
			this.ctx.fill(this.path);
			this.ctx.font = "40px Comic Sans MS";
			this.ctx.fillStyle = "#CB4335";
			this.ctx.textAlign = "center";
			this.ctx.fillText("paused", canvas.width/2, canvas.height/2 + 20 - 5);
		} else if (this.status == GAME_STATUS.FINISHED) {
			if (!this.drawTableScore()) {
				this.ctx.fillStyle = "rgba(66, 73, 73, 0.5)";
				this.ctx.fillRect(0, 0, canvas.width, canvas.height);
				this.path = new Path2D();
				this.path.arc(canvas.width / 2, canvas.height / 2, canvas.height / 4, 0, 2 * Math.PI);
				this.ctx.fillStyle = "rgba(255,255,255,0.8)";
				this.ctx.fill(this.path);
				this.ctx.font = "40px Comic Sans MS";
				this.ctx.fillStyle = "#CB4335";
				this.ctx.textAlign = "center";
				this.ctx.fillText("Game End", canvas.width/2, canvas.height/2 + 20);
			}
		}
	}

	sync(event) {
		if (!event.data) {
			return;
		}
		//console.log(event.data);
		this.syncVersion++;
		const jsonObj = JSON.parse(event.data);
		const objData = jsonObj.data;
		this.currentPID = jsonObj.pid;
		this.inspectors = jsonObj.data.isp;
		this.preparingTime = jsonObj.data.prp;
		this.playerAvas = jsonObj.data.pava;
		this.scores = jsonObj.data.pscore;
		this.mapPidNameGroup.clear();
		if (jsonObj.data.stid) {
			this.updateSoundTrack(jsonObj.data.stid);
		}
		if (objData.map) {
			const mapArray = [];
			const foregroundArray = [];
			this.unitSize = objData.map.unitSize;
			this.status = objData.map.status;
			this.mode = objData.map.mode ?? 0;
			if (this.mapID != objData.map.id) {
				this.mapID = objData.map.id;
				this.foregroundID = objData.map.fid;
				this.width = objData.map.width;
				this.height = objData.map.height;
				triggerMapChange(this.mapID, this.width, this.height);// outer function
			}
			mapArray.push(new Sprite(this.ctx, 0, 0, MAPS_IMAGE[this.mapID - 1], objData.map.width, objData.map.height));
			this.gameObjects.set('maps', mapArray);
			if (this.foregroundID > 0) {
				foregroundArray.push(new Sprite(this.ctx, 0, 0, MAPS_FOREGROUND_IMAGE[this.foregroundID - 1], objData.map.width, objData.map.height, {gbAlpha: 0.6}));
			}
			this.gameObjects.set('foregrounds', foregroundArray);
		}
		if (objData.portal) {
			this.syncPortals(objData.portal);
		}
		if (objData.tree) {
			const treeArray = [];
			for (const tree of objData.tree) {
				treeArray.push(new Tree(this.ctx, tree.x, tree.y, this.unitSize, this.unitSize, tree.img));
			}
			this.gameObjects.set('trees', treeArray);
		}
		if (objData.bomb) {
			this.syncBombs(objData.bomb);
		}
		if (objData.item) {
			this.syncItems(objData.item);
		}
		if (objData.spitem) {
			this.syncSpItems(objData.spitem);
		}
		if (objData.player) {
			this.syncPlayers(objData.player);
		}
		if (objData.explosion) {
			const explosionArray = [];
			for (const expl of this.gameObjects.get('explosions')) {
				if (expl.alive) {
					explosionArray.push(expl);
				}
			};
			let cnt = 0;
			for (const explosion of objData.explosion) {
				explosionArray.push(new Explosion(this.ctx, explosion.x, explosion.y, BOMB_EXPLOSION_IMAGE[explosion.img - 1], explosion.w, explosion.h));
				cnt++;
			}
			if (cnt > 0) {
				this.hasSeExplosion = true;
			} else {
				this.hasSeExplosion = false;
			}
			this.gameObjects.set('explosions', explosionArray);
		}
		if (objData.ability) {
			const abilityArray = [];
			const skillArray = [];
			for (const abl of this.gameObjects.get('abilities')) {
				if (abl.alive) {
					abilityArray.push(abl);
				}
			};
			for (const ability of objData.ability) {
				if (ability.id > 0) {
					skillArray.push(ability);
				} else {
					abilityArray.push(new AbilityEffect(this.ctx, ability.x, ability.y, ability.w, ability.h, ability.img, ability.stt));
				}
			}
			this.gameObjects.set('abilities', abilityArray);
			this.syncSkills(skillArray);
		}
		if (objData.fire) {
			this.syncFireWall(objData.fire);
		}
	}
	syncBombs(bombs) {
		const mapBombs = this.gameObjects.get('bombs');
		for (const bomb of bombs) {
			if (mapBombs.has(bomb.id)) {
				mapBombs.get(bomb.id).sync(bomb);
			} else {
				mapBombs.set(bomb.id, new Bomb(this.ctx, bomb.x, bomb.y, BOMB_IMAGE[bomb.img - 1], this.unitSize, this.unitSize, bomb.effect));
			}
			mapBombs.get(bomb.id).syncVersion(this.syncVersion);
		}
		for (const [id, bomb] of mapBombs) {
			if (!bomb.checkSyncVersion(this.syncVersion)) {
				mapBombs.delete(id);
			}
		}
	}
	syncItems(items) {
		const mapItems = this.gameObjects.get('items');
		for (const item of items) {
			if (mapItems.has(item.id)) {
				mapItems.get(item.id).sync(item);
			} else {
				mapItems.set(item.id, new Item(this.ctx, item.x, item.y, this.unitSize, this.unitSize, item.img));
			}
			mapItems.get(item.id).syncVersion(this.syncVersion);
		}
		for (const [id, item] of mapItems) {
			if (!item.checkSyncVersion(this.syncVersion)) {
				mapItems.delete(id);
			}
		}
	}
	syncSpItems(items) {
		const mapItems = this.gameObjects.get('spitems');
		for (const item of items) {
			if (mapItems.has(item.id)) {
				mapItems.get(item.id).sync(item);
			} else {
				mapItems.set(item.id, new SpItem(this.ctx, item.x, item.y, this.unitSize, this.unitSize, item.img));
			}
			mapItems.get(item.id).syncVersion(this.syncVersion);
		}
		for (const [id, item] of mapItems) {
			if (!item.checkSyncVersion(this.syncVersion)) {
				mapItems.delete(id);
			}
		}
	}
	syncPlayers(players) {
		for (const player of players) {
			if (player.id == this.currentPID) {
				this.currentGID = player.gid;
				this.rvStatus = player.rv;
				break;
			}
		}
		const mapAlivePlayers = this.gameObjects.get('alive_players');
		const mapDeadthPlayers = this.gameObjects.get('deadth_players');
		for (const player of players) {
			player.invisible = false;
			const full_name = player.name;
			player.name = full_name.split('.')[0];
			if (player.id == this.currentPID || (this.currentGID > 0 && this.currentGID == player.gid)) {
				player.isAlly = true;
			}
			if (player.hide) {
				const cpid = this.currentPID;
				if (!player.isAlly && !this.inspectors.find((id)=>(id==cpid))) {
					player.invisible = true;
				}
			}
			if (player.hp <= 0) {
				if (!mapDeadthPlayers.has(player.id)) {
					const ghost = new Ghost(this.ctx, player.x, player.y, GHOST_DEADTH_IMAGE[0], this.unitSize, this.unitSize);
					if (player.isAlly && player.id != this.currentPID && this.rvStatus > 0 && player.rva) {
						ghost.setAttention();
					}
					mapDeadthPlayers.set(player.id, ghost);
				} else {
					mapDeadthPlayers.get(player.id).sync(player);
				}
				if (mapAlivePlayers.has(player.id)) {
					mapAlivePlayers.delete(player.id);
				}
			} else {
				if (!mapAlivePlayers.has(player.id)) {
					mapAlivePlayers.set(player.id, new Player(this.ctx, player.name, player.x, player.y, this.unitSize, this.unitSize, player.img, player.state, player.hp, player.maxHP, player.effect, player.invisible));
				} else {
					mapAlivePlayers.get(player.id).sync(player);
				}
				if (mapDeadthPlayers.has(player.id)) {
					mapDeadthPlayers.delete(player.id);
				}
			}
			// pid - name - group
			if (player.gid) {
				this.mapPidNameGroup.set(full_name, player.gname);
			}
		}
	}
	syncSkills(skills) {
		const mapSkills = this.gameObjects.get('skills');
		for (const skill of skills) {
			if (mapSkills.has(skill.id)) {
				mapSkills.get(skill.id).sync(skill);
			} else {
				mapSkills.set(skill.id, new SkillEffect(this.ctx, skill.x, skill.y, this.unitSize, this.unitSize, skill.img, skill.stt));
			}
			mapSkills.get(skill.id).syncVersion(this.syncVersion);
		}
		for (const [id, skill] of mapSkills) {
			if (!skill.checkSyncVersion(this.syncVersion)) {
				mapSkills.delete(id);
			}
		}
	}
	syncPortals(portals) {
		const portalUpdate = new Map();
		const mapPortals = this.gameObjects.get('portals');
		for (const portal of portals) {
			const key = this.makeGeneralKey(portal.x, portal.y);
			if (mapPortals.has(key)) {
				portal.imgSrc = TELEPORT_IMAGE[portal.img - 1];
				portalUpdate.set(key, mapPortals.get(key));
				portalUpdate.get(key).sync(portal);
			} else {
				portalUpdate.set(key, new Sprite(this.ctx, portal.x, portal.y, TELEPORT_IMAGE[portal.img - 1], this.unitSize, this.unitSize, {frameMax:12, frameHoldMax:10}));
			}
		}
		// overwrite
		this.gameObjects.set('portals', portalUpdate);
	}
	syncFireWall(fireWalls) {
		const mapFireWalls = this.gameObjects.get('fire_walls');
		if (fireWalls.length == 0) {
			mapFireWalls.clear();
			return;
		}
		for (const firewall of fireWalls) {
			const key = this.makeGeneralKey(firewall.x, firewall.y);
			if (mapFireWalls.has(key)) {
				firewall.imgSrc = FIRE_EFFECT[firewall.img - 1];
				mapFireWalls.get(key).sync(firewall);
			} else {
				mapFireWalls.set(key, new Sprite(this.ctx, firewall.x, firewall.y, FIRE_EFFECT[firewall.img - 1], this.unitSize, this.unitSize, {frameMax:8, frameHoldMax:5}));
			}
		}
	}
	makeGeneralKey(x, y) {
		return ("_" + x + "_" + y + "_");
	}
}

function triggerMapChange(id, width = 864, height = 672) {
	$j('.char-info-detail').hide();
	if (MAP_CHARS.get(id)) {
		$j('#character-class').show();
		$j('.char-class-select').trigger('change');
		const cid = $j('.character.selected').data('id') || 0;
		$j('.char-info-detail[data-id=' + cid + ']').show();
	} else {
		$j('#character-class').hide();
		$j('.character').show();
		$j('.char-info-detail[data-id=0]').show();
	}
	const items = MAP_ITEMS.get(id);
	$j('.game-item').css('display', 'none');
	for (const item of items) {
		$j('.game-item[data-id="' + item + '"]').css('display', 'flex');
	}
	const canvas = document.querySelector('#canvas');
	canvas.width = width;
	canvas.height = height;
}

function init(ret) {
	if (ret.error) {
		alert(ret.error);
		window.location.reload();
		return;
	}
	if (ret.game_url) {
		if (ret.game_id) {
			$j('.game-map').data('game',ret.game_id);
			$j('.game-mode-select').data('game',ret.game_id);
			$j('.character').removeClass('selected').data('game',ret.game_id);
			$j('.character[data-id="' + ret.avatar + '"]').addClass('selected');
			$j('#character-list').css('display','inline-flex');
			$j('.char-class-select[value="' + $j('.character[data-id="' + ret.avatar + '"]').data('type') + '"]').prop('checked', true);
			$j('.char-class-select').trigger('change');
			$j('.char-info-detail').hide();
			if (MAP_CHARS.get(ret.mid)) {
				$j('.char-info-detail[data-id=' + ret.avatar + ']').show();
			} else {
				$j('.char-info-detail[data-id=0]').show();
			}
			if (ret.host) {
				$j('#map-list').show();
				$j('#game-mode').show();
				$j('.game-map').removeClass('selected');
				$j('.game-map[data-id="' + ret.mid + '"]').addClass('selected');
			}
		}
		$j('#item-list').show();
		$j('#game-control').hide();
		$j('#game-board').css('display','flex');
		const boomGame = new BoomGame(new BoomSocket(ret.game_url), ret.width, ret.height);
		boomGame.run();
	}
}
// draw canvas
function refreshLobby() {
	if ($j('#room-list').is(':visible')) {
		$j.ajax({
			url : CONTEXT + "/game/json/boom_lobby.htm",
			type : "get",
			dataType : "html",
			success : function(ret) {
				$j('#room-list').html(ret);
			},
			error : function() {
				alert("Unexpected error occurred!");
				window.location.reload();
			}
		});
	}
}
$j(document).ready(function() {
	$j('#content').css('margin-top','');
	if ($j('#token').length) {
		const lobbySocket = new BoomSocket($j('#token').text());
		lobbySocket.init({
			onmessage: (event) => {
				if (event.data) {
					const obj = JSON.parse(event.data);
					if (obj.type == "update") {
						refreshLobby();
					}
				}
			}
		});
	}
	$j('#create-game').click(function() {
		const needConfirm = $j('#stage').length;
		const stage = needConfirm ? $j('#stage').val() : 0;
		const inspector = ($j('#inspector-mode').prop('checked')) ? 1 : 0;
		if (needConfirm) {
			const conf = confirm("Confirm create new game?");
			if (!conf) {
				return;
			}
		}
		$j.ajax({
			url : CONTEXT + "/game/json/boom_confirm.json",
			type : "post",
			data : {
				"type" : "create",
				"inspector" : inspector,
				"stage" : stage
			},
			success : init,
			error : function() {
				alert("Unexpected error occurred!");
				window.location.reload();
			}
		});
	});
	$j('#room-list').on('click', '.game-room', function() {
		const gameID = $j(this).data('id');
		const type = ($j('#inspector-mode').prop('checked')) ? "inspect" : "join";
		$j.ajax({
			url : CONTEXT + "/game/json/boom_confirm.json",
			type : "post",
			data : {
				"type" : type,
				"game_id" : gameID
			},
			success : init,
			error : function() {
				alert("Unexpected error occurred!");
				window.location.reload();
			}
		});
	});
	$j('.game-map').click(function() {
		const mapID = $j(this).data('id');
		const gameID = $j(this).data('game');
		$j.ajax({
			url : CONTEXT + "/game/json/boom_confirm.json",
			type : "post",
			data : {
				"type" : "map",
				"map_id" : mapID,
				"game_id" : gameID
			},
			success : function(ret) {
				$j('.game-map').removeClass('selected');
				$j('.game-map[data-id="' + mapID + '"]').addClass('selected');
				if (ret.error) {
					alert(ret.error);
					window.location.reload();
				}
			},
			error : function() {
				alert("Unexpected error occurred!");
				window.location.reload();
			}
		});
	});
	$j('#character-list > .character').click(function() {
		const avatarId = $j(this).data('id');
		const gameID = $j(this).data('game');
		$j.ajax({
			url : CONTEXT + "/game/json/boom_confirm.json",
			type : "post",
			data : {
				"type" : "avatar",
				"id" : avatarId,
				"game_id" : gameID
			},
			success : function(ret) {
				if (ret.error) {
					return;
				}
				$j('.character').removeClass('selected');
				$j('.character[data-id="' + ret.avatar + '"]').addClass('selected');
				$j('.char-info-detail').hide();
				if (MAP_CHARS.get(ret.mid)) {
					$j('.char-info-detail[data-id=' + ret.avatar + ']').show();
				} else {
					$j('.char-info-detail[data-id=0]').show();
				}
			},
			error : function() {
				alert("Unexpected error occurred!");
				window.location.reload();
			}
		});
	});
	$j('.game-mode-select').click(function() {
		const gameID = $j(this).data('game');
		const mode = $j(this).val();
		$j.ajax({
			url : CONTEXT + "/game/json/boom_confirm.json",
			type : "post",
			data : {
				"type" : "mode",
				"mode" : mode,
				"game_id" : gameID
			},
			success : function(ret) {
				if (ret.error) {
					alert(ret.error);
					window.location.reload();
				}
			},
			error : function() {
				alert("Unexpected error occurred!");
				window.location.reload();
			}
		});
	});
	$j('#scroll-items,#character-list').on('mousedown', function(e) {
		$j(this).data('x',e.clientX);
		$j(this).data('y',e.clientY);
		$j(this).data('left',this.scrollLeft);
		$j(this).data('top',this.scrollTop);
		$j(this).data('drag',1);
		$j(this).css('cursor','grabbing');
	});
	$j(document).on('mouseup', function(e) {
		$j('#scroll-items').data('drag',0);
		$j('#scroll-items').css('cursor','grab');
		$j('#character-list').data('drag',0);
		$j('#character-list').css('cursor','grab');
	});
	$j('#scroll-items').on('mousemove', function(e) {
		if (!$j(this).data('drag')) {
			return;
		}
		const dx = e.clientX - ($j(this).data('x') || 0);
	    const dy = e.clientY - ($j(this).data('y') || 0);
	    this.scrollTop = ($j(this).data('top') || 0) - dy;
	    this.scrollLeft = ($j(this).data('left') || 0) - dx;
	});
	$j('#character-list').on('mousemove', function(e) {
		if (!$j(this).data('drag')) {
			return;
		}
		const dx = e.clientX - ($j(this).data('x') || 0);
	    const dy = e.clientY - ($j(this).data('y') || 0);
	    this.scrollTop = ($j(this).data('top') || 0) - dy;
	    this.scrollLeft = ($j(this).data('left') || 0) - dx;
	});
	$j('.char-class-select').on('change', function() {
		if ($j(this).prop('checked')) {
			const type = $j(this).val();
			$j('.character').hide();
			$j('.character[data-type=' + type + ']').show();
			const len = $j('.character[data-type=' + type + ']').length;
			if (len > 0) {
				const idx = parseInt(Math.random() * len);
				if (idx >= 0 && idx < len) {
					$j('.character[data-type=' + type + ']').eq(idx).trigger('click');
				}
			}
		}
	});
});