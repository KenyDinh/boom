package dev.boom.services;

import dev.boom.tbl.data.TblKillerSudokuData;

public class KillerSudokuData {
	
	private TblKillerSudokuData sudokuData = null;

	public KillerSudokuData() {
		sudokuData = new TblKillerSudokuData();
	}

	public KillerSudokuData(TblKillerSudokuData sudokuData) {
		super();
		this.sudokuData = sudokuData;
	}

	public TblKillerSudokuData getSudokuData() {
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
	
	public String getCageData() {
		return (String) sudokuData.Get("cage_data");
	}
	
}
