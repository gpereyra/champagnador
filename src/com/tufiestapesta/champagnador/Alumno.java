package com.tufiestapesta.champagnador;

public class Alumno {
	private Sexo sexo;
	private String nombres;
	private String apellido;
	private String id;
	private Curso curso;
	
	public Alumno(String id, String nombre, String apellido, Sexo sexo) {
		setId(id.contains(".") ? id.split("\\.")[0] : id);
		setNombres(nombre);
		setApellido(apellido);
		setSexo(sexo);
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}
	
	
	
}
