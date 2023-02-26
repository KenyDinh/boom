var ADJACENT_CELLS_COOR = {
	"l": -1,
	"r": +1,
	"u": -9,
	"d": +9,
}
var board = [];
var cage_list = [];
var map_cage_id = {}; // dsu

class KillerSudoku {
	/*
	Design a killer sudoku board.
	*/ 
	constructor(board, cage_list_str) {
		this.board = board;
		this.cage_list = cage_list_str.split(";");
	}
}

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
		let block_inner = $j(this).children(".block-inner")[0];
		const value = textToNumber($j(block_inner).html());
		$j(block_inner).html(value && value > 0 ? value : "");
	});
	$j('table#sudoku').on('keypress', '.block-inner', function(e) {
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
		if ($j(event.target).is("div.block-inner")) {
			highlightCellRelate($j(event.target))
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
		if ((49 <= code && code <= 57) || (1 <= e.key && e.key <= 9)) {
			// 1-9
			let block_inner = $j('td#block-' + row + '-' + col).children(".block-inner")[0];
			$j(block_inner).html(e.key - 0);
			checkNewCheckedCell(row, col);
			return;
		}
		else if (code === 46) {
			// delete
			let block_inner = $j('td#block-' + row + '-' + col).children(".block-inner")[0];
			if ($j(block_inner).hasClass("init-num")) {
				return;
			}
			$j(block_inner).html("");
			checkNewCheckedCell(row, col);
			return;
		}
		else if (code === 37) {
			// left
			col--;
		}
		else if (code === 38) {
			// up
			row--;
		}
		else if (code === 39) {
			// right
			col++;
		}
		else if (code === 40) {
			// down
			row++;
		} else {
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
			html += '<td id="' + id + '" data-row="' + row + '" data-col="' + col + '"></td>';
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
			/*
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
			*/
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

function getCellValue(row, col) {
	let cell = $j('td#block-' + row + '-' + col).children(".block-inner")[0];
	if (!cell || cell.length <= 0) {
		return 0;
	}
	return textToNumber($j(cell).text());
}

function checkCage(cage_id) {
	let cage_sum, cage_cells;
	[cage_sum, cage_cells] = cage_list[cage_id]
	let cells_value = cage_cells.map(
		_1d_coor => getCellValue(parseInt(_1d_coor / 9), _1d_coor % 9)
	)
	let cage_set = 0;
	for (let v of cells_value) {
		if (1 <= v && v <= 9) {
			if ((cage_set & (1 << (v - 1))) == 0) {
				cage_set |= (1 << (v - 1));
			} else {
				return false;
			}
		}
	}
	let cells_value_sum = cells_value.reduce((s, v) => typeof(s) !== "undefined" && 1 <= v && v <= 9 ? s + v : undefined, 0)
	// console.log(cage_id, cage_sum, cage_cells, cage_cells.map(_1d_coor => getCellValue(parseInt(_1d_coor / 9), _1d_coor % 9)), cells_value_sum)
	return typeof(cells_value_sum) === "undefined" || cells_value_sum == cage_sum;
}

function checkNewCheckedCell(row, col) {
	let valid = true;
	let hor_line_set = 0, ver_line_set = 0, block_set = 0;
	let cell_value = getCellValue(row, col)
	for (let x = 0; x < 9; x++) {
		// horizontal
		let cell_r_x_value = getCellValue(row, x);
		if (x !== col && 1 <= cell_r_x_value && cell_r_x_value <= 9) {
			hor_line_set |= (1 << (cell_r_x_value - 1));
		}
		// vertical
		let cell_x_c_value = getCellValue(x, col);
		if (x !== row && 1 <= cell_x_c_value && cell_x_c_value <= 9) {
			ver_line_set |= (1 << (cell_x_c_value - 1));
		}
		// block 3x3
		let cell_rx_cx_value = getCellValue(row - row % 3 + parseInt(x / 3), col - col % 3 + x % 3);
		if (row - row % 3 + parseInt(x / 3) !== row && col - col % 3 + x % 3 !== col && 1 <= cell_rx_cx_value && cell_rx_cx_value <= 9) {
			block_set |= (1 << (cell_rx_cx_value - 1));
		}
	}
	if (1 <= cell_value && cell_value <= 9) {
		if (
			(hor_line_set & (1 << (cell_value - 1))) > 0 ||
			(ver_line_set & (1 << (cell_value - 1))) > 0 ||
			(block_set    & (1 << (cell_value - 1))) > 0 ||
			!checkCage(map_cage_id[row * 9 + col])
		) {
			valid = false;
		}
	}
	const cell = $j('td#block-' + row + '-' + col).children(".block-inner")[0];
	if (valid) {
		$j(cell).removeClass('invalid');
	} else {
		$j(cell).addClass('invalid');
	}
	return valid;
}

function checkSudokuBoard() {
	const CORRECT_VALUE = 0b111111111;
	const HOR_LINES = [0,0,0,0,0,0,0,0,0];
	const VER_LINES = [0,0,0,0,0,0,0,0,0];
	const BLOCKS    = [0,0,0,0,0,0,0,0,0];
	for (let row = 0; row < 9; row++) {
		for (let col = 0; col < 9; col++) {
			const cell = $j('td#block-' + row + '-' + col);
			if (cell.length <= 0) {
				continue;
			}
			let valid = true;
			const val = textToNumber(cell.html());
			if (1 <= val && val <= 9) {
				// horizontal
				if ((HOR_LINES[row] & (1 << (val - 1))) == 0) {
					HOR_LINES[row] |= (1 << (val - 1));
				} else {
					valid = false;
				}
				// vertical
				if ((VER_LINES[col] & (1 << (val - 1))) == 0) {
					VER_LINES[col] |= (1 << (val - 1));
				} else {
					valid = false;
				}
				// block 3x3
				const n_block = parseInt(row / 3) * 3 + parseInt(col / 3);
				if ((BLOCKS[n_block] & (1 << (val - 1))) == 0) {
					BLOCKS[n_block] |= (1 << (val - 1));
				} else {
					valid = false;
				}
				// cage
				
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
		$j(this).removeClass(
			['cell-relate', 'init-num', 'editing', 'invalid']
		).removeClass(
			Object.keys(ADJACENT_CELLS_COOR).map(d => "cage_edge_" + d)
		).text('');
	});
}

function loadProblem(level=1) {
	level = textToNumber(level);
	if (level === 1) {
		board = [
			[0, 4, 0, 1, 3, 5, 0, 0, 7],
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 1, 0, 0, 0, 5, 0, 4],
			[7, 0, 4, 8, 0, 2, 0, 6, 0],
			[8, 0, 3, 0, 9, 4, 0, 0, 0],
			[0, 6, 9, 3, 0, 0, 2, 4, 0],
			[4, 0, 0, 0, 8, 1, 7, 0, 0],
			[0, 0, 7, 0, 6, 0, 0, 0, 0],
			[0, 0, 0, 4, 7, 0, 0, 0, 2],
		]
		cage_list = [
			[21, [0, 1, 2]],
			[10, [3, 12]],
			[7, [4, 13]],
			[11, [5, 14]],
			[22, [6, 7, 15, 16, 24]],
			[10, [8, 17]],
			[15, [9, 10, 18]],
			[6, [11, 20]],
			[23, [19, 27, 28, 36]],
			[15, [21, 30]],
			[11, [22, 23, 31]],
			[19, [25, 26, 34]],
			[13, [29, 38, 39]],
			[5, [32, 33]],
			[9, [35]],
			[8, [37, 46]],
			[14, [40, 41, 42]],
			[12, [43, 44]],
			[5, [45, 54]],
			[19, [47, 48, 56, 57]],
			[13, [49, 58]],
			[9, [50, 51]],
			[12, [52, 53]],
			[24, [55, 64, 65]],
			[13, [59, 68, 77]],
			[20, [60, 69, 78]],
			[3, [61]],
			[9, [62, 71, 80]],
			[8, [63, 72]],
			[15, [66, 67, 76]],
			[13, [70, 79]],
			[11, [73, 74, 75]],
		]
	}
	else if (level === 2) {
		board = [
			[0, 1, 0, 0, 0, 7, 5, 6, 0],
			[7, 0, 0, 9, 1, 5, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 9, 0],
			[9, 0, 0, 0, 0, 0, 0, 0, 5],
			[0, 6, 0, 5, 0, 8, 7, 0, 4],
			[8, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 1, 0, 2, 6],
			[6, 0, 0, 0, 4, 0, 0, 0, 0],
			[0, 9, 0, 2, 0, 6, 0, 0, 3],
		]
		cage_list = [
			[22, [0, 9, 10, 18]],
			[14, [1, 2, 3]],
			[20, [4, 5, 6, 7]],
			[8, [8]],
			[8, [11, 20]],
			[18, [12, 13, 22]],
			[8, [14, 15]],
			[13, [16, 25]],
			[14, [17, 26, 35]],
			[27, [19, 27, 28, 37]],
			[6, [21]],
			[4, [23, 24]],
			[5, [29, 30]],
			[18, [31, 38, 39, 40]],
			[16, [32, 33, 41]],
			[15, [34, 43, 44]],
			[10, [42, 51, 52]],
			[10, [36, 45]],
			[18, [46, 54, 55, 63]],
			[8, [47, 56]],
			[15, [48, 57]],
			[17, [49, 50, 58]],
			[16, [53, 62, 71]],
			[10, [59, 68]],
			[27, [60, 69, 77, 78]],
			[7, [61, 70]],
			[9, [64, 65]],
			[6, [66, 75]],
			[9, [67, 76]],
			[1, [72]],
			[17, [73, 74]],
			[10, [79, 80]],
		]
	}
	else if (level === 3) {
		board = [
			[0, 0, 0, 3, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 3, 0],
			[0, 3, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 8, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 5, 0, 0, 0, 0, 0, 0, 2],
			[2, 0, 0, 0, 0, 0, 0, 0, 4],
			[0, 0, 0, 0, 0, 4, 0, 0, 0],
			[0, 0, 0, 0, 6, 0, 0, 0, 0],
		]
		cage_list = [
			[22, [0, 1, 9, 10]],
			[17, [2, 3, 4, 13]],
			[8, [5, 14]],
			[13, [6, 7]],
			[13, [8, 17]],
			[15, [11, 20, 29]],
			[11, [12, 21]],
			[11, [15, 16, 25]],
			[7, [18, 27]],
			[12, [19, 28]],
			[13, [22, 30, 31]],
			[11, [23, 24]],
			[21, [26, 34, 35, 43]],
			[21, [32, 33, 42]],
			[8, [36]],
			[6, [37, 46]],
			[2, [38]],
			[16, [39, 40, 41]],
			[5, [44, 53]],
			[3, [45]],
			[23, [47, 48, 49, 50]],
			[9, [51, 60]],
			[13, [52, 61, 62]],
			[7, [54, 63]],
			[21, [55, 64, 73]],
			[24, [56, 57, 58]],
			[9, [59, 67, 68]],
			[4, [65, 74]],
			[9, [66]],
			[22, [69, 76, 77, 78]],
			[13, [70, 71]],
			[4, [72]],
			[1, [75]],
			[11, [79, 80]],
		]
	}
	else // if (level === 4)
	{
		board = [
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
			[0, 0, 0, 0, 0, 0, 0, 0, 0],
		]
		cage_list = [
			[32, [0, 1, 2, 10, 11]],
			[19, [3, 4, 5, 6]],
			[16, [7, 8, 16, 17]],
			[12, [9, 18, 19]],
			[23, [12, 13, 20, 21, 22]],
			[9, [14, 15]],
			[12, [23, 24]],
			[9, [25, 34]],
			[10, [26, 35]],
			[14, [27, 36]],
			[10, [28, 29, 30]],
			[20, [31, 40, 49]],
			[11, [32, 33]],
			[21, [37, 38, 39]],
			[7, [41, 42, 43]],
			[14, [44, 53]],
			[8, [45, 54]],
			[6, [46, 55]],
			[3, [47, 48]],
			[21, [50, 51, 52]],
			[17, [56, 57]],
			[26, [58, 59, 60, 67, 68]],
			[9, [61, 62, 71]],
			[20, [63, 64, 72, 73]],
			[7, [65, 66]],
			[32, [69, 70, 78, 79, 80]],
			[17, [74, 75, 76, 77]],
		]
	}
	clearBoard();
	for (let i = 0; i < board.length; i++) {
		for (let j = 0; j < board[i].length; j++) {
			const block_element = $j('td#block-' + i + '-' + j);
			const num = board[i][j];
			if (num > 0) {
				$j(block_element).html('<div class="block-inner init-num">' + board[i][j] + '</div>') 
			} else {
				$j(block_element).html('<div class="block-inner"></div>') 
			}
		}
	}
	map_cage_id = {}; // dsu
	let cage_id = 0;

	for (let [cage_sum, cage_cells] of cage_list) {
		cage_cells.sort((u, v) => u - v)
		min_coor = cage_cells[0];
		let min_row = parseInt(min_coor / 9), min_col = min_coor % 9;
		const cell = $j('td#block-' + min_row + '-' + min_col)
		cell.prepend('<span class="cage_sum">' + cage_sum + '</span>')
		
		let isCellInCage = (cage_cells, _1d_coor) => 0 <= _1d_coor && _1d_coor < 81 && cage_cells.findIndex(c => c === _1d_coor) >= 0
		let isHasAdjacentCellInCage = (cage_cells, _1d_coor, direction) => isCellInCage(cage_cells, _1d_coor + ADJACENT_CELLS_COOR[direction])

		for (let _1d_coor of cage_cells) {
			map_cage_id[_1d_coor] = cage_id;
			let row = parseInt(_1d_coor / 9), col = _1d_coor % 9;
			const cell = $j('td#block-' + row + '-' + col);
			$j(cell).addClass("cage_edge_u cage_edge_d cage_edge_l cage_edge_r");
			
			for (let direction in ADJACENT_CELLS_COOR) {
				if (isHasAdjacentCellInCage(cage_cells, _1d_coor, direction)) {
					$j(cell).removeClass("cage_edge_" + direction);
				}
			}
			for (let dy of "du") {
				for (let dx of "lr") {
					if (
						isHasAdjacentCellInCage(cage_cells, _1d_coor, dx) &&
						isHasAdjacentCellInCage(cage_cells, _1d_coor, dy) &&
						!isCellInCage(cage_cells, _1d_coor + ADJACENT_CELLS_COOR[dx] + ADJACENT_CELLS_COOR[dy])
					) {
						cell.append('<div class="cage_angle_' + dy + ' cage_angle_' + dx + '"></div>')
					}
				}
			}
		}
		cage_id++;
	}

	/*
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
	});*/

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