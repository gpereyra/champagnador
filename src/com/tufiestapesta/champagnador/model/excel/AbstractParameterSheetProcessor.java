package com.tufiestapesta.champagnador.model.excel;

import org.apache.poi.ss.usermodel.Row;

public abstract class AbstractParameterSheetProcessor extends SheetProcessor {

	public AbstractParameterSheetProcessor() {
		super();
	}

	protected abstract void processParameter(String param, String value);

	@Override
	protected void processRow(Row r) {
		processParameter(asString(r, XlsxModelFactory.COLUMN__PARAMETER), asString(r, XlsxModelFactory.COLUMN__VALUE));
	}

}