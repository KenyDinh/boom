$j(document).ready(function() {
	initTable();
	$j('table#sudoku td').click(function() {
		removeEditingStateAll();
		highlightCellRelate($j(this));
		if ($j(this).hasClass('init-num') && $j('#init-number').is(':checked') == false) {
			return;
		}
		if ($j(this).hasClass('editing')) {
			return;
		}
		$j(this).addClass('editing');
		const value = textToNumber($j(this).text());
		if (value > 0) {
			$j(this).html('<input class="n-input" type="text" value="' + value + '" />');
		} else {
			$j(this).html('<input class="n-input" type="text" value="" />');
		}
		$j(this).find('input.n-input').focus();
		$j(this).find('input.n-input').select();
		
	});
	$j('table#sudoku').on('keypress','input.n-input', function(e) {
		const code = (e.keyCode || e.which);
		if (code == 13) { // enter
			removeEditingStateAll();
			e.preventDefault();
		}
	});
	$j('body').click(function(event) {
		if ($j(event.target).closest('table#sudoku').length == 0) {
			removeEditingStateAll();
		}
	});
	$j('body').on('keyup', function(e) {
		let row = $j('table#sudoku').data('row'); 
		let col = $j('table#sudoku').data('col');
		if (typeof row !== "number" || typeof col !== "number") {
			return;
		}
		if (row < 0 || col < 0 || row >= 9 || col >= 9) {
			return;
		}
		const code = (e.keyCode || e.which);
		switch (code) {
			case 37: //left
				col--;
				break;
			case 38: //up
				row--;
				break;
			case 39: //right
				col++;
				break;
			case 40: //down
				row++;
				break;
			default:
				return;
		}
		let nextCell = $j('td#block-' + row + '-' + col);
		if (nextCell.length) {
			e.preventDefault();
			nextCell.trigger('click');
		}
	})
	$j('#btn-load-puzzle').click(function() {
		const level = $j('#level-select').val();
		$j('#init-number').prop('checked', false);
		loadProblem(level);
		
	});
	$j('#btn-generate-puzzle').click(function() {
		const level = textToNumber($j('#level-generate').val());
		if (level > qqwing.Difficulty.EXPERT) {
			level = qqwing.Difficulty.EXPERT;
		}
		generatePuzzle(level);
	});
});

function initTable() {
	let html = '';
	for (let row = 0; row < 9; row++) {
		html += '<tr>';
		for (let col = 0; col < 9; col++) {
			let id = "block-" + row + "-" + col;
			let style = "border-bottom:1px solid var(--tbn);border-right:1px solid var(--tbn);";
			if ((row + 1) % 3 == 0 && (col + 1) % 3 == 0) {
				style = "border-bottom:2px solid var(--tbb);border-right:2px solid var(--tbb);";
			} else if ((row + 1) % 3 == 0) {
				style = "border-bottom:2px solid var(--tbb);border-right:1px solid var(--tbn);";
			} else if ((col + 1) % 3 == 0) {
				style = "border-bottom:1px solid var(--tbn);border-right:2px solid var(--tbb);";
			}
			html += '<td id="' + id + '" data-row="' + row + '" data-col="' + col + '" style="' + style + '">';
			html += '-';
			html += '</td>';
		}
		html += '</tr>';
	}
	$j('table#sudoku').html(html);
}
function highlightCellRelate(target) {
	const row = target.data('row');
	const col = target.data('col');
	$j('table#sudoku').data('row', row).data('col', col);
	for (let i = 0; i < 9; i++) {
		const h_cell = $j('td#block-' + row + '-' + i);
		if (h_cell.length) {
			h_cell.addClass('cell-relate');
		}
		const v_cell = $j('td#block-' + i + '-' + col);
		if (v_cell.length) {
			v_cell.addClass('cell-relate');
		}
		const r_i = parseInt(row / 3) * 3 + parseInt(i / 3);
		const c_i = parseInt(col / 3) * 3 + parseInt(i % 3);
		const b_cell = $j('td#block-' + r_i + '-' + c_i);
		if (b_cell.length) {
			b_cell.addClass('cell-relate');
		}
	}
}

function removeEditingStateAll() {
	const list = $j('table#sudoku td.editing');
	if (list.length) {
		list.each(function() {
			let cur_value = textToNumber($j(this).find('input.n-input').val());
			if ($j('#init-number').is(':checked') && cur_value > 0) {
				$j(this).addClass('init-num');
			} else {
				$j(this).removeClass('init-num');
			}
			if (cur_value >= 1 && cur_value <= 9) {
				$j(this).text(cur_value);
			} else {
				$j(this).text('-');
			}
			$j(this).removeClass('editing');
		});
		checkSudokuBoard();
	}
	$j('table#sudoku td.cell-relate').each(function() {
		$j(this).removeClass('cell-relate');
	});
}
function textToNumber(text) {
	if (typeof text === "undefined" || text === null) {
		return 0;
	}
	if (typeof text === "number") {
		return text;
	}
	text = text.toString();
	const m = (text.startsWith("-") ? -1 : 1);
	return m * Number(text.replace(/\D/g,""));
}

function checkSudokuBoard() {
	const CORRECT_VALUE = 511;
	const HOR_LINES = [0,0,0,0,0,0,0,0,0];
	const VER_LINES = [0,0,0,0,0,0,0,0,0];
	const BLOCKS 	= [0,0,0,0,0,0,0,0,0];
	for (let row = 0; row < 9; row++) {
		for (let col = 0; col < 9; col++) {
			const cell = $j('td#block-' + row + '-' + col);
			if (cell.length <= 0) {
				continue;
			}
			let valid = true;
			const val = textToNumber(cell.text());
			//horizontal
			if ((val >= 1) && (val <= 9)) {
				if (( HOR_LINES[row] & (1 << (val - 1)) ) == 0) {
					HOR_LINES[row] |= (1 << (val - 1));
				} else {
					valid = false;
				}
			} else {
			}
			//vertical
			if ((val >= 1) && (val <= 9)) {
				if (( VER_LINES[col] & (1 << (val - 1)) ) == 0) {
					VER_LINES[col] |= (1 << (val - 1));
				} else {
					valid = false;
				}
			} else {
			}
			//block 3x3
			const n_block = parseInt(row / 3) * 3 + parseInt(col / 3);
			if ((val >= 1) && (val <= 9)) {
				if (( BLOCKS[n_block] & (1 << (val - 1)) ) == 0) {
					BLOCKS[n_block] |= (1 << (val - 1));
				} else {
					valid = false;
				}
			} else {
			}
			if (valid) {
				cell.removeClass('invalid');
			} else {
				cell.addClass('invalid');
			}
		}
	}
	for (const value of HOR_LINES) {
		if (value != CORRECT_VALUE) {
			return false;
		}
	}
	for (const value of VER_LINES) {
		if (value != CORRECT_VALUE) {
			return false;
		}
	}
	for (const value of BLOCKS) {
		if (value != CORRECT_VALUE) {
			return false;
		}
	}
	
	return true;
}
function clearBoard() {
	$j('table#sudoku td').each(function() {
		$j(this).removeClass('cell-relate').removeClass('init-num').removeClass('editing').removeClass('invalid').text('-');
	});
}
function solveThePuzzle() {
	let board = "";
	for (let row = 0; row < 9; row++) {
		if (row > 0) {
			board += ";";
		}
		for (let col = 0; col < 9; col++) {
			const cell = $j('td#block-' + row + '-' + col);
			if (cell.length <= 0) {
				return;
			}
			const val = textToNumber(cell.text());
			if (val >= 1 && val <= 9 && cell.hasClass('init-num')) {
				board += val;
			} else {
				board += "-";
			}
		}
	}
	if (board.length <= 0) {
		return;
	}
	$j.ajax({
		url : CONTEXT + "/game/json/sudoku_loader.json",
		type : "POST",
		data : {"type" : 2, "board" : board},
		dataType : "json",
		success : function(ret) {
			if (ret.cnf) {
				executeCnfSolver(ret.cnf);
			}
		},
		error : function() {
		}
	});
}
function executeCnfSolver(cnf) {
	//console.log(cnf);
	// process cnf formula.
	const solve_string = Module.cwrap('solve_string', 'string', ['string', 'int']);
	const result = solve_string(cnf, cnf.length);
	//console.log(result);
	// check result
	if (result.startsWith("UNSAT")) {
		alert("Can not solve this puzzle!");
		return;
	}
	const ret = [];
	const r_arr = result.split(" ");
	//console.log(r_arr);
	for (const s_val of r_arr) {
		const sn = textToNumber(s_val);
		if (sn <= 0) {
			continue;
		}
		ret.push(sn);
	}
	//console.log(ret);
	// fill correct numbers.
	for (const val of ret) {
		const row = parseInt(parseInt((val - 1) / 9) / 9) + 1 - 1;
		const col = parseInt(parseInt((val - 1) / 9) % 9) + 1 - 1;
		const num = parseInt((val - 1) % 9) + 1;
		const cell = $j('td#block-' + row + '-' + col);
		if (cell.length <= 0) {
			continue;
		}
		cell.removeClass('editing').removeClass('invalid');
		if (cell.hasClass('init-num')) {
			continue;
		}
		cell.text(num);
	}
}
function loadProblem(level) {
	level = textToNumber(level);
	$j.ajax({
		url : CONTEXT + "/game/json/sudoku_loader.json",
		type : "POST",
		data : {"type" : 1, "level" : level},
		dataType : "json",
		success : function(ret) {
			if (ret.data) {
				parseSudokuProblem(ret.data);
			}
		},
		error : function() {
		}
	});
}
function parseSudokuProblem(data) {
	if (!data) {
		return;
	}
	let lines = null;
	if (data.indexOf(";") > 0) {
		lines = data.split(";");
	} else {
		lines = data.split("\n");
	}
	if (lines == null || lines.length <= 0) {
		return;
	}
	clearBoard();
	for (let i = 0; i < lines.length; i++) {
		const cd = lines[i].split("");
		if (cd.length <= 0) {
			return;
		}
		for (let j = 0; j < cd.length; j++) {
			const cell = $j('td#block-' + i + '-' + j);
			if (cell.length <= 0) {
				continue;
			}
			const num = textToNumber(cd[j]);
			if (num > 0) {
				cell.text(cd[j]);
				cell.addClass('init-num');
			} else {
				cell.text("-");
			}
		}
	}
}
function generatePuzzle(level) {
	//console.log('start generating puzzle!');
	const qq = new qqwing();
	qq.setRecordHistory((level > 0));
	qq.setPrintStyle(qqwing.PrintStyle.COMPACT);
	qq.generatePuzzle(qqwing.Symmetry.RANDOM);
	qq.solve();
	if (level > 0 && qq.getDifficulty() != level) {
		generatePuzzle(level);
	} else {
		parseSudokuProblem(qq.getPuzzleString());
		//console.log(qq.getPuzzleString());
	}
}
function showInstruction() {
	let puzzle = [];
	for (let row = 0; row < 9; row++) {
		for (let col = 0; col < 9; col++) {
			const cell = $j('td#block-' + row + '-' + col);
			let val = 0;
			if (cell.length > 0) {
				val = textToNumber(cell.text());
			}
			puzzle.push(val);
		}
	}
	const qq = new qqwing();
	qq.setRecordHistory(true);
	qq.setPuzzle(puzzle);
	qq.solve();
	//console.log(qq.getSolveInstructions());
	console.log(qq.getSolveInstructionsString());
}