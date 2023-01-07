package dev.boom.common.game;

public class SudokuFunc {
	
	private static final int BOARD_SIZE = 9;
	private static final int BLOCK_HOR_SIZE = 3;
	private static final int BLOCK_VER_SIZE = 3;
	
	public static String getCnfFormula(int[][] board) {
		if (board == null || board.length != BOARD_SIZE || board.length != board[0].length) {
			return null;
		}
		StringBuilder cnf = new StringBuilder();
		int count = 0;
		int[][][] arrayNumber = new int[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
		// generate all available numbers.
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				for (int k = 0; k < BOARD_SIZE; k++) {
					arrayNumber[i][j][k] = ++count;
				}
			}
		}
		// make cnf formula.
		StringBuilder sub = new StringBuilder();
		int total = 0;
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				int val = board[i][j];
				if (val >= 1 && val <= 9) {
					cnf.append(arrayNumber[i][j][val - 1]).append(" 0\n");
					total++;
					for (int k = 0; k < BOARD_SIZE; k++) {
						if (val != k + 1) {
							cnf.append("-").append(arrayNumber[i][j][k]).append(" 0\n");
							total++;
						}
					}
				} else {
					sub.setLength(0);
					for (int m = 0; m < BOARD_SIZE; m++) {
						sub.append(arrayNumber[i][j][m]).append(" ");
						for (int n = m + 1; n < BOARD_SIZE; n++) {
							cnf.append("-").append(arrayNumber[i][j][m]).append(" -").append(arrayNumber[i][j][n]).append(" 0\n");
							total++;
						}
					}
					cnf.append(sub).append("0\n");
					total++;
				}
			}
		}
		// cnf for horizontal + vertical lines
		for (int m = 0; m < BOARD_SIZE; m++) { // row - col
			for (int n = 0; n < BOARD_SIZE; n++) { //number
				for (int i = 0; i < BOARD_SIZE; i++) { // col - row
					for (int j = i + 1; j < BOARD_SIZE; j++) { // next col - next row
						cnf.append("-").append(arrayNumber[m][i][n]).append(" -").append(arrayNumber[m][j][n]).append(" 0\n");
						total++;
						cnf.append("-").append(arrayNumber[i][m][n]).append(" -").append(arrayNumber[j][m][n]).append(" 0\n");
						total++;
					}
				}
			}
		}
		int nBlockPerRow = BOARD_SIZE / BLOCK_HOR_SIZE;
		// cnf for each 3x3 block
		for (int m = 0; m < BOARD_SIZE; m++) { // block
			for (int n = 0; n < BOARD_SIZE; n++) { //number
				for (int i = 0; i < BOARD_SIZE; i++) { // cell
					for (int j = i + 1; j < BOARD_SIZE; j++) { // next cell
						int c_row = (m / nBlockPerRow) * BLOCK_VER_SIZE + (i / BLOCK_HOR_SIZE);
						int c_col = (m % nBlockPerRow) * BLOCK_HOR_SIZE + (i % BLOCK_HOR_SIZE);
						int n_row = (m / nBlockPerRow) * BLOCK_VER_SIZE + (j / BLOCK_HOR_SIZE);
						int n_col = (m % nBlockPerRow) * BLOCK_HOR_SIZE + (j % BLOCK_HOR_SIZE);
						cnf.append("-").append(arrayNumber[c_row][c_col][n]).append(" -").append(arrayNumber[n_row][n_col][n]).append(" 0\n");
						total++;
					}
				}
			}
		}
		cnf.insert(0, "p cnf " + count + " " + total + "\n");
		return cnf.toString();
	}
	
	public static int[][] getSudokuBoard(String data) {
		if (data == null || data.isEmpty()) {
			return null;
		}
		String lines[] = data.split(";");
		if (lines.length == 0) {
			return null;
		}
		int col = lines[0].length();
		if (col == 0) {
			return null;
		}
		int ret[][] = new int[lines.length][col];
		for (int i = 0; i < lines.length; i++) {
			char[] nums = lines[i].toCharArray();
			if (nums.length != col) {
				return null;
			}
			for (int j = 0; j < col; j++) {
				if (nums[j] >= '1' && nums[j] <= '9') {
					ret[i][j] = Integer.parseInt(String.valueOf(nums[j]));
				}
			}
		}
		
		return ret;
	}
	
}
