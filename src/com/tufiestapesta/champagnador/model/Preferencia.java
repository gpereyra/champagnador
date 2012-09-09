package com.tufiestapesta.champagnador.model;

import com.tufiestapesta.champagnador.Alumno;

public class Preferencia extends Relacion {
	private Boolean isSatisfecha;

	public Preferencia(Alumno alumno1, Alumno alumno2) {
		super(alumno1, alumno2);
	}

	public String getDescription() {
		return "P_" + getAlumno1().getId() + "_" + getAlumno2().getId();
	}

	public Boolean getIsSatisfecha() {
		return isSatisfecha;
	}

	public void setIsSatisfecha(Boolean isSatisfecha) {
		this.isSatisfecha = isSatisfecha;
	}
	
}
