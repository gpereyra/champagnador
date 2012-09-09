package com.tufiestapesta.champagnador.model.excel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public abstract class SheetProcessor {
	Map<String, Integer> headers = new HashMap<String, Integer>();
	
	abstract void processRow(Row row);
	
	void postProcess(){
	}

	protected void processSheet(Sheet sheet){
		for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
			Row row = rit.next();
			if (row.getRowNum() >= getHeaderRowNum()){
				if (row.getRowNum() == getHeaderRowNum()){
					for (Cell cell : row) {
						headers.put(cell.getStringCellValue(), cell.getColumnIndex());
					}
				} else {
					processRow(row);
				}
			}
		}
		postProcess();
	}

	protected int getHeaderRowNum() {
		return 0;
	}

	protected String asString(Row r, String col) {
		Integer colNum = headers.get(col);
		return asString(r, colNum);
	}

	protected String asString(Row r, Integer colNum) {
		Cell cell = r.getCell(colNum);
		String value;
		if (cell != null){
			switch(cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					value = cell.getRichStringCellValue().getString();
					break;
				case Cell.CELL_TYPE_FORMULA:
					value = String.valueOf(cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					value = String.valueOf(cell.getNumericCellValue());
					break;
				default:
					value = null;
			}
		} else {
			value = null;
		}
		return value != null ? value.trim() : value;
	}
}

