package com.tufiestapesta.champagnador.ilp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class LinearModel {
	private List<Result> results;
	private Functional functional;
	private Map<String, Variable> variables = new LinkedHashMap<String, Variable>();
	private Map<String, Restriction> restrictions = new LinkedHashMap<String, Restriction>();
	
	
	public List<Result> getResults() {
		if (results == null){
			results = new ArrayList<Result>();
		}
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
	public Collection<Restriction> getRestrictions() {
		return restrictions.values();
	}
	public void addRestriction(Restriction r) {
		if (this.restrictions.containsKey(r.getName())){
			System.out.println("WARNING: Restriction " + r.getName() + " overriden.");
		}
		this.restrictions.put(r.getName(), r);
	}
	public Functional getFunctional() {
		return functional;
	}
	public void setFunctional(Functional functional) {
		this.functional = functional;
	}
	public Collection<Variable> getVariables() {
		return Collections.unmodifiableCollection(variables.values());
	}
	public void addResult(Result r) {
		getResults().add(r);
	}
	public Variable variableForName(String name, VariableType varType) {
		Variable v = variables.get(name);
		if (v == null){
			v = new Variable(name, varType);
			variables.put(name, v);
		}
		return v;
	}
	public Restriction restrictionForName(String name){
		return restrictions.get(name);
	}
	public List<Variable> getOrderedVariables() {
		ArrayList<Variable> variableList = new ArrayList<Variable>(getVariables());
		Collections.sort(variableList, new Comparator<Variable>() {
			@Override
			public int compare(Variable o1, Variable o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return variableList;
	}
}
