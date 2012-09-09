package com.tufiestapesta.champagnador.model;

import com.tufiestapesta.champagnador.Alumno;

public class Relacion {

	protected Alumno alumno1;
	protected Alumno alumno2;

	public Relacion(Alumno alumno1, Alumno alumno2) {
		this.alumno1 = alumno1;
		this.alumno2 = alumno2;
	}

	public Alumno getAlumno1() {
		return alumno1;
	}

	public Alumno getAlumno2() {
		return alumno2;
	}

}