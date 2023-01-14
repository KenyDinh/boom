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
		return (Integer) sudokuData.Get("id");
	}
	
	public byte getLevel() {
		return (Byte) sudokuData.Get("level");
	}
	
	public String getData() {
		return (String) sudokuData.Get("data");
	}
	
}
