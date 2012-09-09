package com.tufiestapesta.champagnador.model.excel;

import org.apache.poi.ss.usermodel.Row;

import com.tufiestapesta.champagnador.model.AlumnosCursosModel;
import com.tufiestapesta.champagnador.model.Veto;

public class VetoSheetProcessor extends SheetProcessor {
	private static final int COLUMN_ALUMNO1_ID = 0;
	private static final int COLUMN_ALUMNO2_ID = 3;

	@Override
	void processRow(Row r) {
		String alumno1id = asString(r, COLUMN_ALUMNO1_ID);
		if (alumno1id != null && !"".equals(alumno1id)){
			String alumno2id = asString(r, COLUMN_ALUMNO2_ID);
			if(alumno2id != null && !"".equals(alumno2id)){
				AlumnosCursosModel.getInstance().addVeto(new Veto(AlumnosCursosModel.getInstance().alumnoForId(alumno1id), AlumnosCursosModel.getInstance().alumnoForId(alumno2id)));
			}
		}
	}
	
	@Override
	protected int getHeaderRowNum() {
		return 2;
	}

}
