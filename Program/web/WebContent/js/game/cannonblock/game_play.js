var $j = jQuery.noConflict();
var count = 1;
var firstPos, secondPos;
var firstColor;
var gameOver = false;

$j(function() {
	$j(document).ready(function() {
		initGameData();
	});
});

function loadCell() {
	$j(".cell").unbind("click");
	$j(".cell").click(function() {
		if (gameOver) {
			return;
		}
		$j(this).css('transform', "scale(1.2)");
		$j(this).css('box-shadow', "0px 0px 150px #000000");
		if (count % 2 == 1) {
			firstPos = $j(this).attr('value');
		} else {
			secondPos = $j(this).attr('value');
			postData(firstPos, secondPos);

		}

		if (count == 2) {
			count = 1;
		} else {
			count++;
		}

	});
}

function swapCell(firstPos, secondPos) {
	var firstLeft = $j('#cell-' + firstPos).offset().left;
	var firstTop = $j('#cell-' + firstPos).offset().top;
	var secondLeft = $j('#cell-' + secondPos).offset().left;
	var secondTop = $j('#cell-' + secondPos).offset().top;

	var a = firstLeft - secondLeft;
	var b = secondLeft - firstLeft;
	var c = firstTop - secondTop;
	var d = secondTop - firstTop;
	$j('#cell-' + firstPos).css('transform',
			"translate(" + b + "px," + d + "px)");
	$j('#cell-' + firstPos).css('transform', "transition: all .3s");

	$j('#cell-' + secondPos).css('transform',
			"translate(" + a + "px," + c + "px)");
	$j('#cell-' + secondPos).css('transform', "transition: all .3s");

}

function draw(userBoard, aiBoard, userHP, aiHP, gameStatus, firstColor) {
	document.getElementById("user-hp").innerHTML = userHP;
	document.getElementById("ai-hp").innerHTML = aiHP;
	var strResult = '';
	var str = '';
	var array = [];
	str += '<table>';
	str += '<div id="player" ><div id="player-progess" style="width: '+ userHP +'%;"></div></div>';


	if (firstColor == 1) {
		var gun11 = '<div id="';
		var gun12 = '" class ="triangle-right-blue"></div>';
		var gun21 = '<div id="';
		var gun22 = '" class ="triangle-right-green"></div>';
	} else {
		var gun11 = '<div id="';
		var gun12 = '" class ="triangle-right-green"></div>';
		var gun21 = '<div id="';
		var gun22 = '" class ="triangle-right-blue"></div>';
	}

	for (i = 1; i <= userBoard.length; i++) {
		if (i % 4 == 1) {
			str += '<tr>';
		}
		if (userBoard.charAt(i - 1) == 1) {
			str += '<td><div id="cell-' + i + '" class="cell cloudy" value="'
					+ i + '"></div></td>';
		} else if (userBoard.charAt(i - 1) == 2) {
			str += '<td><div id="cell-' + i + '" class="cell breezy" value="'
					+ i + '"></div></td>';
		} else if (userBoard.charAt(i - 1) == 3) {
			str += '<td><div id="cell-' + i + '" class="cell hot" value="' + i
					+ '"></div></td>';
		}
		if (i % 4 == 0) {
			if ((i / 4) % 2 == 1) {
				str += '<td>' + gun11 + '' + i + '' + gun12 + '</td>';
			} else {
				str += '<td>' + gun21 + '' + i + '' + gun22 + '</td>';
			}
			str += '</tr>';
		}
	}
	str += '</table >';

	strAI = '';
	var k = 0;
	strAI += '<table id="table">';
	strAI += '<div id="bot1" ><div id="bot" style="width: '+ aiHP +'%;"></div></div>';
	for (i = 1; i <= 7; i++) {
		strAI += '<tr><td><div id="explosion-'+ i + '"></td><td><div id="ai-cannon-' + i +  '" class="triangle-left"></div></td>';
		for (j = 1; j <= 4; j++) {
			if (j <= aiBoard.charAt(i - 1)) {
				strAI += '<td><div id="cell-ai-' + k + '" class="cell-ai black"></div></td>';
			} else {
				strAI += '<td><div id="cell-ai-' + k + '" class="cell-ai hot"></div></td>';
			}
			k++;
		}
		strAI += '</tr>';
	}

	if (gameStatus == 2) {
		strResult += '<h1>HUMAN WON!</h1>';
		gameOver = true;
		$j(".cell").unbind("click");
		document.getElementById("fireButton").setAttribute("id", "home");
	} else if (gameStatus == 3) {
		strResult += '<h1>BOT WON!</h1>';
		gameOver = true;
		$j(".cell").unbind("click");
		document.getElementById("fireButton").setAttribute("id", "home");
	} else {
		document.getElementById("ai-board").innerHTML = strAI;
		document.getElementById("user-board").innerHTML = str;
		loadCell();
	}
	document.getElementById("game-result").innerHTML = strResult;

}

function initGameData() {
	// Make an ajax call to read game board data from server.
	cannonblock_ajax( {
		type : "GET",
		url : CONTEXT + "/game/json/cannon_swap_json.json",
		success : function(result) {
			if (typeof result != "undefined" && result != null) {
				var status = result.status;
				if (status != -1) {
					userBoard = result.userBoard;
					aiBoard = result.aiBoard;
					userHP = result.userHP;
					aiHP = result.aiHP;
					firstColor = result.firstColor;
					gameStatus = result.gameStatus;
					draw(userBoard, aiBoard, userHP, aiHP, gameStatus,
							firstColor);
				} else {
					returnMenu();
				}

			}
		}
	});
}

function cannonblock_ajax(options) {
	$j.ajax(options);
}
function postData(firstPos, secondPos) {
	// Make a post request to update our board column 1 and column 2 value in
	// our server.
	cannonblock_ajax( {
		type : "POST",
		url : CONTEXT + "/game/json/cannon_swap_json.json",
		data : "firstPos=" + firstPos + "&secondPos=" + secondPos,
		success : function(result) {
			if (typeof result != "undefined" && result != null) {
				var status = result.status;
				if (status != -1) {
					userBoard = result.userBoard;
					aiBoard = result.aiBoard;
					userHP = result.userHP;
					aiHP = result.aiHP;
					gameStatus = result.gameStatus;
					swapCell(firstPos, secondPos);
					drawHP(userHP, aiHP);
					setTimeout(function() {
						draw(userBoard, aiBoard, userHP, aiHP, gameStatus,
								firstColor);
					}, 400);

				} else {
					initGameData();
				}
			}
		}
	});
}
$j("#fireButton").click(function() {
	if (!gameOver) {
		firePostData();
	} else {
		returnMenu();
	}
});



function returnMenu() {
	location.href = CONTEXT + "/game/cannon_block/cannon_block_menu.htm";
}
function firePostData() {
	cannonblock_ajax( {
		type : "POST",
		url : CONTEXT + "/game/json/cannon_fire_json.json",
		success : function(result) {
			if (typeof result != "undefined" && result != null) {
				var status = result.status;
				if (status != -1) {
					userBoard = result.userBoard;
					aiBoard = result.aiBoard;
					userHP = result.userHP;
					aiHP = result.aiHP;
					gameStatus = result.gameStatus;
					cannonFire = result.cannonFire;
					checkCannon(cannonFire);
					drawHP(userHP, aiHP);
					setTimeout(function() {
						draw(userBoard, aiBoard, userHP, aiHP, gameStatus,
							firstColor);
					}, 600);
				} else {
					location.reload();
				}
			}
		}
	});
}

function drawHP(userHP, aiHP){
	$j('#player-progess').css("width",userHP +"%");
	$j('#bot').css("width", aiHP +"%");
}

function drawBullet(index, cannonFire) {
	var distance = $j('#ai-cannon-' + (index+1)).offset().left - $j('#' + (index + 1) * 4).offset().left;
	if (cannonFire[index] > 0) {
		$j('#' + (index + 1) * 4).css({"transform":"translate("+ distance + "px, 0)", "transition":"all .2s ease-in"});
		setTimeout(function(){
			$j('#' + (index + 1) * 4).removeClass();
			$j('#explosion-' + (index + 1)).addClass("explosion");
			$j('#table').css({"-webkit-animation":"shake 0.1s ease-in-out 0.1s infinite alternate","-ms-animation":"shake 0.1s ease-in-out 0.1s infinite alternate"});

		}, 200);

	}
}

function checkCannon(cannonFire){
	drawBullet(0, cannonFire);
	drawBullet(1, cannonFire);
	drawBullet(2, cannonFire);
	drawBullet(3, cannonFire);
	drawBullet(4, cannonFire);
	drawBullet(5, cannonFire);
	drawBullet(6, cannonFire);
}