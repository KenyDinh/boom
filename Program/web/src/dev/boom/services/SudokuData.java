package dev.boom.services;

import dev.boom.tbl.data.TblSudokuData;

public class SudokuData {
	
	private TblSudokuData sudokuData = null;

	public SudokuData() {
		sudokuData = new TblSudokuData();
	}

	public SudokuData(TblSudokuData sudokuData) {
		super();
		this.sudokuData = sudokuData;
	}

	public TblSudokuData getSudokuData() {
		return sudokuData;
	}

	public int getId() {
		return sudokuData.getId();
	}
	
	public byte getLevel() {
		return sudokuData.getLevel();
	}
	
	public String getData() {
		return sudokuData.getData();
	}
	
}
