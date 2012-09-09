package com.tufiestapesta.champagnador.model.excel;

import org.apache.poi.ss.usermodel.Row;

import com.tufiestapesta.champagnador.Curso;
import com.tufiestapesta.champagnador.model.AlumnosCursosModel;


class CursosSheetProcessor extends SheetProcessor {

	private static final String COLUMN_NOMBRE = "Nombre";
	private static final String COLUMN_MIXTO = "Mixto (S/N)";
	
	private static final String COLUMN_MIXTO__SI = "S";
	@SuppressWarnings("unused")
	private static final String COLUMN_MIXTO__NO = "N";

	@Override
	void processRow(Row r) {
		String nombre = asString(r, COLUMN_NOMBRE);
		if (nombre != null && !"".equals(nombre)){
			Boolean mixto = COLUMN_MIXTO__SI.equals(asString(r, COLUMN_MIXTO));
			AlumnosCursosModel.getInstance().addCurso(new Curso(nombre, mixto));
		}
	}

}
