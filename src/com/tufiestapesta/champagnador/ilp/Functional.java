package com.tufiestapesta.champagnador.ilp;

import java.math.BigDecimal;
import java.util.Set;

public class Functional {
	private Set<Term> terms;
	private Goal goal;
	public Functional(Set<Term> terms, Goal goal) {
		super();
		this.terms = terms;
		this.goal = goal;
	}
	public Set<Term> getTerms() {
		return terms;
	}
	public Goal getGoal() {
		return goal;
	}

	//TODO Mejorar implementaci�n
	public BigDecimal getTermFor(Variable v) {
		for (Term term : terms) {
			if (term.getVar().equals(v)){
				return term.getCoef();
			}
		}
		return BigDecimal.ZERO;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Ganancia: ");
		for (Term t : getTerms()) {
			sb.append(t.toString());
		}
		sb.append(" = 0");
		return sb.toString();
	}
}
