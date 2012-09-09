package com.tufiestapesta.champagnador.model.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class XlsxModelFactory {
	public static final String COLUMN__PARAMETER = "Parámetro";
	public static final String COLUMN__VALUE = "Valor";
	private Map<String, SheetProcessor> processors;
	
	public Map<String, SheetProcessor> getProcessors() {
		if (processors == null){
			processors = new LinkedHashMap<String, SheetProcessor>();
		}
		return processors;
	}

	public XlsxModelFactory() {
		getProcessors().put("Alumnos", new AlumnosSheetProcessor());
		getProcessors().put("Cursos", new CursosSheetProcessor());
		getProcessors().put("Preferencias", new PreferenciasSheetProcessor());
		getProcessors().put("Restricciones", new VetoSheetProcessor());
	}

	public void buildModel(String path){
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			Workbook wb = new XSSFWorkbook(fis);
			evaluateWorkbook(wb);
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i);
				SheetProcessor sheetProcessor = processors.get(sheet.getSheetName());
				if (sheetProcessor != null){
					sheetProcessor.processSheet(sheet);
				} else {
					System.out.println("Skipping sheet: " + sheet.getSheetName());
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("La planilla de datos: " + path + " no existe o no puede ser leída.",e);
		}
	}

	private void evaluateWorkbook(Workbook wb) {
		FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			Sheet s = wb.getSheetAt(i);
			for (Row row : s) {
				for (Cell cell : row) {
					eval.evaluateFormulaCell(cell);
				}
			}
		}
	}
}
