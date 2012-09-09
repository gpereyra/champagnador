package com.tufiestapesta.champagnador;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tufiestapesta.champagnador.model.AlumnosCursosModel;
import com.tufiestapesta.champagnador.model.Preferencia;

public class XlsxModelReporter {
	private XSSFWorkbook report;
	private String reportFilename;

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
	public void setup() {
		//Output Report
		try {
			report = new XSSFWorkbook();
			reportFilename = "c:\\CursosFinales_" + System.currentTimeMillis() + ".xlsx";
			//flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("File not found");
		}
	}

	private void flush() {
		try {
			FileOutputStream out = new FileOutputStream(reportFilename);
			report.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public void report(AlumnosCursosModel m) {
		int rowNum;
		for (Curso c : m.getCursos()) {
			printCurso(c);
		}
		printPreferencias();
		writeReport();
	}
	private void printCurso(Curso c) {
		int rowNum;
		XSSFSheet sheet = report.createSheet("Curso " + c.getNombre() + (c.getIsMixto() ? "(Mixto)" : "(No Mixto)"));
		rowNum = 0;
		for (Alumno a : c.getAlumnos()) {
			XSSFRow row = sheet.createRow(rowNum++);
			int cellNum = 0;
			if (rowNum == 1){
				XSSFCell cell = row.createCell(cellNum++);
				cell.setCellValue("Id");
				cell = row.createCell(cellNum++);
				cell.setCellValue("Apellido");
				cell = row.createCell(cellNum++);
				cell.setCellValue("Nombre");
				cell = row.createCell(cellNum++);
				cellNum = 0;
				row = sheet.createRow(rowNum++);
			}
			XSSFCell cell = row.createCell(cellNum++);
			cell.setCellValue(a.getId());
			cell = row.createCell(cellNum++);
			cell.setCellValue(a.getApellido());
			cell = row.createCell(cellNum++);
			cell.setCellValue(a.getNombres());
			cell = row.createCell(cellNum++);
		}
	}
	

	private void printPreferencias() {
		int rowNum;
		XSSFSheet sheet = report.createSheet("Preferencias");
		rowNum = 0;
		for (Preferencia p : AlumnosCursosModel.getInstance().getPreferencias()) {
			XSSFRow row = sheet.createRow(rowNum++);
			int cellNum = 0;
			if (rowNum == 1){
				XSSFCell cell = row.createCell(cellNum++);
				cell.setCellValue("Id 1");
				cell = row.createCell(cellNum++);
				cell.setCellValue("Id 2");
				cell = row.createCell(cellNum++);
				cell.setCellValue("Satisfecha?");
				cell = row.createCell(cellNum++);
				cellNum = 0;
				row = sheet.createRow(rowNum++);
			}
			XSSFCell cell = row.createCell(cellNum++);
			cell.setCellValue(p.getAlumno1().getId());
			cell = row.createCell(cellNum++);
			cell.setCellValue(p.getAlumno2().getId());
			cell = row.createCell(cellNum++);
			cell.setCellValue(p.getIsSatisfecha().toString());
			cell = row.createCell(cellNum++);
		}
	}
	
	private void writeReport() {
		try {
			//flush();
			evaluateWorkbook(report);
			FileOutputStream fos = new FileOutputStream(reportFilename);
			report.write(fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
