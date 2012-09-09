package com.tufiestapesta.champagnador.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.tufiestapesta.champagnador.Alumno;
import com.tufiestapesta.champagnador.Curso;
import com.tufiestapesta.champagnador.Sexo;
import com.tufiestapesta.champagnador.ilp.Condition;
import com.tufiestapesta.champagnador.ilp.Functional;
import com.tufiestapesta.champagnador.ilp.Goal;
import com.tufiestapesta.champagnador.ilp.LinearModel;
import com.tufiestapesta.champagnador.ilp.Restriction;
import com.tufiestapesta.champagnador.ilp.Term;
import com.tufiestapesta.champagnador.ilp.Variable;
import com.tufiestapesta.champagnador.ilp.VariableType;

public class AlumnosCursosModel {
	private static final String _SEPARATOR_ = "__";
	private static final String CANT_ALUMNOS_CURSO__ = "CANT_ALUMNOS_CURSO" + _SEPARATOR_;
	private static final String ALUMNO_CURSO__ = "ALUMNO_CURSO" + _SEPARATOR_;
	private static final String PREF_OK_CURSO__ = "PREF_OK_CURSO" + _SEPARATOR_;
	private static final String PREF_OK__ = "PREF_OK" + _SEPARATOR_;
	private static final String VETO_ALUMNOS_CURSO__ = "VETO_ALUMNO_CURSO" + _SEPARATOR_;
	
	private static final Integer MAX_DIF_ENTRE_CURSOS = 4;
	private static AlumnosCursosModel instance;
	private Set<Alumno> alumnos;
	private Set<Curso> cursos;
	private Set<Veto> vetos;
	private Set<Preferencia> preferencias;
	
	private AlumnosCursosModel() {
	}
	
	public static AlumnosCursosModel getInstance() {
		if (instance == null){
			instance = new AlumnosCursosModel();
		}
		return instance;
	}

	public Set<Alumno> getAlumnos() {
		if (alumnos == null){
			alumnos = new LinkedHashSet<Alumno>();
		}
		return alumnos;
	}

	public void setAlumnos(Set<Alumno> alumnos) {
		this.alumnos = alumnos;
	}
	
	public Set<Curso> getCursos() {
		if (cursos == null){
			cursos = new LinkedHashSet<Curso>();
		}
		return cursos;
	}
	
	public void setCursos(Set<Curso> cursos) {
		this.cursos = cursos;
	}

	public void addAlumno(Alumno alumno) {
		getAlumnos().add(alumno);
	}

	public void addCurso(Curso Curso) {
		getCursos().add(Curso);
	}
	
	public Set<Veto> getVetos() {
		if (vetos == null){
			vetos = new LinkedHashSet<Veto>();
		}
		return vetos;
	}

	public void setVetos(Set<Veto> vetos) {
		this.vetos = vetos;
	}

	public void addVeto(Veto Veto) {
		getVetos().add(Veto);
	}
	
	public Set<Preferencia> getPreferencias() {
		if (preferencias == null){
			preferencias = new LinkedHashSet<Preferencia>();
		}
		return preferencias;
	}

	public void setPreferencias(Set<Preferencia> preferencias) {
		this.preferencias = preferencias;
	}

	public void addPreferencia(Preferencia Preferencia) {
		getPreferencias().add(Preferencia);
	}

	public LinearModel toLinearModel() {
		LinearModel lm = new LinearModel();
		for (Alumno a : this.getAlumnos()) {
			//Solo una vez por alumno
			unaVezPorAlumno(lm, a);
		}
		Set<Term> todosAlumnosAsignadosTerms = new LinkedHashSet<Term>();
		for (Curso c : this.getCursos()) {
			//Por curso
			porCadaCurso(lm, c);
			for (Alumno a : this.getAlumnos()) {
				todosAlumnosAsignadosTerms.add(alumnoEnCursoVar(lm, a, c).asTerm());
			}
		}
		lm.addRestriction(new Restriction("Todos asig", Condition.EQUALS, BigDecimal.valueOf(this.getAlumnos().size()), todosAlumnosAsignadosTerms));
		for (Veto v : getVetos()) {
			porCadaVeto(lm, v);
		}
		Set<Term> prefTerms = new LinkedHashSet<Term>();
		for (Preferencia p : getPreferencias()) {
			prefTerms.add(porCadaPreferencia(lm, p));
		}
		cursosParejos(lm);
		lm.setFunctional(new Functional(prefTerms, Goal.MAXIMIZE));
		return lm;
	}
	
	public void eatLinearModelResults(LinearModel lm){
		for (Alumno a : getAlumnos()) {
			for (Curso c : getCursos()) {
				Variable var = alumnoEnCursoVar(lm, a, c);
				if (BigDecimal.ONE.equals(lm.variableForName(var.getName(), var.getVarType()).getValue())){
					c.addAlumno(a);
					a.setCurso(c);
				}
			}
		}
		for (Preferencia p : getPreferencias()) {
			p.setIsSatisfecha(lm.variableForName(p.getDescription(), VariableType.BOOLEAN).getValue().intValue() == 1 ? Boolean.TRUE : Boolean.FALSE);
		}
	}

	private void porCadaVeto(LinearModel lm, Veto v) {
		for (Curso c : getCursos()) {
			Variable alumno1Curso = alumnoEnCursoVar(lm, v.getAlumno1(), c);
			Variable alumno2Curso = alumnoEnCursoVar(lm, v.getAlumno2(), c);
			
			lm.addRestriction(
					new Restriction(
							VETO_ALUMNOS_CURSO__ + v.getAlumno1().getId() + _SEPARATOR_ + v.getAlumno2().getId() + _SEPARATOR_ + c.getNombre(), 
							Condition.LESS_THAN, BigDecimal.ONE, alumno1Curso.asTerm(), alumno2Curso.asTerm()
					)
			);
		}
	}
	
	private Term porCadaPreferencia(LinearModel lm, Preferencia p) {
		/*
		 * PrefC1yC9aPt1: c1a + c9a <= 1 + p19a;
		 * PrefC1yC9aPt2: c1a + c9a >= 2 * p19a;
		 * */
		Term prefOk = lm.variableForName(p.getDescription(), VariableType.CONTINOUS).asTerm();
		Collection<Term> terms = new ArrayList<Term>();
		for (Curso c : getCursos()) {
			Variable prefOkEnCursoVar = lm.variableForName(PREF_OK_CURSO__ + p.getDescription() + _SEPARATOR_ + c.getNombre(), VariableType.BOOLEAN);
			Term prefOkEnCurso = prefOkEnCursoVar.asTerm();
			Term alumno1EnCurso = alumnoEnCursoVar(lm, p.getAlumno1(), c).asTerm();
			Term alumno2EnCurso = alumnoEnCursoVar(lm, p.getAlumno2(), c).asTerm();
			
			//PrefC1yC9aPt1: c1a + c9a <= 1 + p19a; ==> c1a + c9a - p19a <= 1
			lm.addRestriction(new Restriction("Up-" + p.getDescription() + _SEPARATOR_ + c.getNombre(), Condition.LESS_THAN, BigDecimal.ONE, alumno1EnCurso, alumno2EnCurso, prefOkEnCurso.invert()));
			
			//PrefC1yC9aPt2: c1a + c9a >= 2 * p19a;
			lm.addRestriction(new Restriction("Lw-" + p.getDescription() + _SEPARATOR_ + c.getNombre(), Condition.GREATER_THAN, alumno1EnCurso, alumno2EnCurso, new Term(prefOkEnCursoVar, BigDecimal.valueOf(-2))));
			
			terms.add(prefOkEnCurso);
		}
		terms.add(prefOk.invert());
		lm.addRestriction(new Restriction(PREF_OK__ + p.getDescription(), Condition.EQUALS, BigDecimal.ZERO, terms));
		return prefOk;
	}

	private Variable alumnoEnCursoVar(LinearModel lm, Alumno a, Curso c) {
		return lm.variableForName(ALUMNO_CURSO__ + a.getId() + _SEPARATOR_ + c.getNombre(), VariableType.BOOLEAN);
	}

	private void porCadaCurso(LinearModel lm, Curso c) {
		//Cantidad de alumnos no negativa 
		Variable cantAlumnosCurso = cantAlumnosCursoVar(lm, c);
		initNonNegative(lm, cantAlumnosCurso);
		
		//Cantidad de alumnos
		Collection<Term> totalAlumnos = new ArrayList<Term>();
		
		//Cantidad de mujeres
		Collection<Term> totalMujeres = new ArrayList<Term>();
		for (Alumno a : getAlumnos()) {
			totalAlumnos.add(alumnoEnCursoVar(lm, a, c).asTerm());
			if (a.getSexo() == Sexo.FEMENINO){
				totalMujeres.add(alumnoEnCursoVar(lm, a, c).asTerm());
			}
		}
		totalAlumnos.add(new Term(cantAlumnosCurso, BigDecimal.ONE.negate()));
		lm.addRestriction(new Restriction(CANT_ALUMNOS_CURSO__ + c.getNombre(), Condition.EQUALS, BigDecimal.ZERO, totalAlumnos));
		Variable totalMujeresEnCurso = cantMujeresEnCursoVar(lm, c);
		totalMujeres.add(totalMujeresEnCurso.asTerm().invert());
		lm.addRestriction(new Restriction("CANT_MUJERES_CURSO__" + c.getNombre(), Condition.EQUALS, BigDecimal.ZERO, totalMujeres));
		if (!c.getIsMixto()){
			lm.addRestriction(new Restriction("MAX_CANT_MUJERES_CURSO__" + c.getNombre(), Condition.EQUALS, BigDecimal.ZERO, totalMujeresEnCurso.asTerm()));
		}
		
		lm.addRestriction(new Restriction("CANT_MAX_ALUMNOS_CURSO__" + c.getNombre(), Condition.LESS_THAN, BigDecimal.valueOf((int)((getAlumnos().size() / getCursos().size()) + MAX_DIF_ENTRE_CURSOS)), cantAlumnosCurso.asTerm()));
		
	}

	private Variable cantMujeresEnCursoVar(LinearModel lm, Curso c) {
		return lm.variableForName("CANT_MUJERES_CURSO__" + c.getNombre(), VariableType.CONTINOUS);
	}

	private Variable cantAlumnosCursoVar(LinearModel lm, Curso c) {
		return lm.variableForName(CANT_ALUMNOS_CURSO__ + c.getNombre(), VariableType.CONTINOUS);
	}

	private void cursosParejos(LinearModel lm) {
		for (Curso c1 : getCursos()) {
			for (Curso c2 : getCursos()) {
				if (c1 != c2){
					Set<Term> pocaDiferenciaTerms = new LinkedHashSet<Term>();
					pocaDiferenciaTerms.add(cantAlumnosCursoVar(lm, c1).asTerm());
					pocaDiferenciaTerms.add(cantAlumnosCursoVar(lm, c2).asTerm().invert());
					lm.addRestriction(new Restriction("Poca diferencia " + c1.getNombre() + "-" + c2.getNombre(), Condition.LESS_THAN, BigDecimal.valueOf(MAX_DIF_ENTRE_CURSOS), pocaDiferenciaTerms));
					if(c1.getIsMixto() && c2.getIsMixto()){
						Set<Term> pocaDiferenciaMujeresTerms = new LinkedHashSet<Term>();
						pocaDiferenciaMujeresTerms.add(cantMujeresEnCursoVar(lm, c1).asTerm());
						pocaDiferenciaMujeresTerms.add(cantMujeresEnCursoVar(lm, c2).asTerm().invert());
						lm.addRestriction(new Restriction("Poca diferencia (mujeres)" + c1.getNombre() + "-" + c2.getNombre(), Condition.LESS_THAN, BigDecimal.valueOf(MAX_DIF_ENTRE_CURSOS), pocaDiferenciaMujeresTerms));
					}
				}
			}
		}
	}

	private void unaVezPorAlumno(LinearModel lm, Alumno a) {
		Collection<Term> terms = new ArrayList<Term>();
		for (Curso c : getCursos()) {
			Variable alumnoEnCursoVar = alumnoEnCursoVar(lm, a, c);
			initNonNegative(lm, alumnoEnCursoVar);
			terms.add(alumnoEnCursoVar.asTerm());
		}
		lm.addRestriction(new Restriction("AlmAsig-" + a.getId(), Condition.EQUALS, BigDecimal.ONE, terms));
	}

	private void initNonNegative(LinearModel lm, Variable v) {
		lm.addRestriction(Restriction.oneVarOneRH(BigDecimal.ONE, v, Condition.GREATER_THAN, BigDecimal.ZERO));
	}

	public Alumno alumnoForId(String id) {
		String trimmed = id.contains(".") ? id.split("\\.")[0] : id;
		// TODO Mejorar implementación
		for (Alumno a : getAlumnos()) {
			if(trimmed.equals(a.getId())){
				return a;
			}
		}
		return null;
	}

	public Curso cursoForId(String nombre) {
		// TODO Mejorar implementación
		for (Curso c : getCursos()) {
			if(nombre.equals(c.getNombre())){
				return c;
			}
		}
		return null;
	}
	
	

}
