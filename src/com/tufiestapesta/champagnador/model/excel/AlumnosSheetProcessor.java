package com.tufiestapesta.champagnador.model.excel;

import org.apache.poi.ss.usermodel.Row;

import com.tufiestapesta.champagnador.Alumno;
import com.tufiestapesta.champagnador.Sexo;
import com.tufiestapesta.champagnador.model.AlumnosCursosModel;


class AlumnosSheetProcessor extends SheetProcessor {

	private static final String COLUMN_NOMBRE = "Nombre";
	private static final String COLUMN_APELLIDO = "Apellido";
	private static final String COLUMN_ID = "ID";
	private static final String COLUMN_SEXO = "Sexo";

	@Override
	void processRow(Row r) {
		String nombre = asString(r, COLUMN_NOMBRE);
		if (nombre != null && !"".equals(nombre)){
			String apellido = asString(r, COLUMN_APELLIDO);
			String id = asString(r, COLUMN_ID);
			String sexo = asString(r, COLUMN_SEXO);
			AlumnosCursosModel.getInstance().addAlumno(new Alumno(id, nombre, apellido, toSexo(sexo)));
		}
	}

	private Sexo toSexo(String sexo) {
		return "M".equals(sexo) ? Sexo.MASCULINO : Sexo.FEMENINO;
	}

}
