package com.tufiestapesta.champagnador.ilp;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class Restriction {
	private Set<Term> terms;
	private Condition condition;
	private String name;

	
	public Restriction(String name, Condition condition, Term... terms) {
		super();
		this.name = name;
		this.terms = new LinkedHashSet<Term>(Arrays.asList(terms));
		this.condition = condition;
	}

	public Restriction(String name, Condition condition, BigDecimal rightHandTerm, Term... terms) {
		this(name, condition,terms);
		this.terms.add(new Term(null, rightHandTerm).invert());
	}

	public Restriction(String name, Condition condition, BigDecimal rightHandTerm, Collection<Term> terms) {
		super();
		this.name = name;
		this.terms = new LinkedHashSet<Term>(terms);
		this.condition = condition;
		this.terms.add(new Term(null, rightHandTerm).invert());
	}
	

	public Restriction(String name) {
		this.name = name;
	}

	public Set<Term> getTerms() {
		if (terms == null){
			terms = new LinkedHashSet<Term>();
		}
		return terms;
	}

	public Condition getCondition() {
		return condition;
	}

	//TODO Mejorar implementación
	public BigDecimal getTermFor(Variable v) {
		for (Term term : terms) {
			if (term.getVar().equals(v)){
				return term.getCoef();
			}
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal getRightHand() {
		for (Term t : terms) {
			if (t.getVar() == null){
				return t.getCoef().negate();
			}
		}
		return BigDecimal.ZERO;
	}

	public Restriction setCondition(Condition condition) {
		this.condition = condition;
		return this;
	}

	public static Restriction oneVarOneRH(BigDecimal coef, Variable v,
			Condition c, BigDecimal rh) {
		return new Restriction(v.getName() + " Non-negative.")
			.addTerm(new Term(v, coef))
			.setCondition(c)
			.setRightHand(rh, null);
	}

	public Restriction setRightHand(BigDecimal rh, Variable v) {
		Term rightHandTerm = new Term(v, rh);
		this.terms.add(rightHandTerm.invert());
		return this;
	}

	private Restriction addTerm(Term t) {
		getTerms().add(t);
		return this;
	}

	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getName() + ": ");
		for (Term t : getTerms()) {
			sb.append(t.toString());
		}
		sb.append(" = 0");
		return sb.toString();
	}
}
