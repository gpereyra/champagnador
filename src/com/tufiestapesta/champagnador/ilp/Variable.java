package com.tufiestapesta.champagnador.ilp;

import java.math.BigDecimal;




public class Variable {
	private String name;
	private VariableType varType;
	private BigDecimal value;
	
	public Variable(String name, VariableType varType) {
		this.name = name;
		this.varType = varType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public Long getIntegerValue() {
		return value.longValue();
	}
	public void setIntegerValue(Long integerValue) {
		this.value = BigDecimal.valueOf(integerValue);
	}
	public VariableType getVarType() {
		return varType;
	}
	public void setVarType(VariableType varType) {
		this.varType = varType;
	}
	public Term asTerm() {
		return new Term(this,BigDecimal.ONE);
	}
	
	
}
