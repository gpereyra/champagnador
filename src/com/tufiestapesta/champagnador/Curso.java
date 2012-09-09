package com.tufiestapesta.champagnador;

import java.util.LinkedHashSet;
import java.util.Set;

public class Curso {
	private String nombre;
	private Boolean isMixto;
	private Set<Alumno> alumnos;
	
	public Curso(String nombre, Boolean mixto) {
		this.nombre = nombre;
		this.isMixto = mixto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Boolean getIsMixto() {
		if (isMixto == null){
			isMixto = Boolean.FALSE;
		}
		return isMixto;
	}

	public void setIsMixto(Boolean isMixto) {
		this.isMixto = isMixto;
	}
	public Set<Alumno> getAlumnos() {
		if(alumnos == null){
			alumnos = new LinkedHashSet<Alumno>();
		}
		return alumnos;
	}

	public void setAlumnos(Set<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	public void addAlumno(Alumno a) {
		getAlumnos().add(a);
	}

	
}
