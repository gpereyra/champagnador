package com.tufiestapesta.champagnador.ilp;

import java.math.BigDecimal;

public class Term {
	private Variable var;
	private BigDecimal coef;
	
	public Term(Variable var, BigDecimal coef) {
		super();
		this.var = var;
		this.coef = coef;
	}

	public Term invert() {
		return new Term(getVar(), getCoef().negate());
	}

	public Variable getVar() {
		return var;
	}

	public BigDecimal getCoef() {
		return coef;
	}
	
	@Override
	public String toString() {
		return getCoef().toPlainString() + (getVar() != null ? " " + getVar().getName() : "");
	}
}
