package com.tufiestapesta.champagnador.ilp.javailp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.javailp.Constraint;
import net.sf.javailp.Linear;
import net.sf.javailp.Operator;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.VarType;

import com.tufiestapesta.champagnador.ilp.Condition;
import com.tufiestapesta.champagnador.ilp.Goal;
import com.tufiestapesta.champagnador.ilp.LinearModel;
import com.tufiestapesta.champagnador.ilp.LinearModelSolver;
import com.tufiestapesta.champagnador.ilp.Restriction;
import com.tufiestapesta.champagnador.ilp.Term;
import com.tufiestapesta.champagnador.ilp.Variable;
import com.tufiestapesta.champagnador.ilp.VariableType;
import com.tufiestapesta.champagnador.model.AlumnosCursosModel;

public abstract class JavaILPMapper implements LinearModelSolver {
	private static Map<Condition, Operator> operator = new HashMap<Condition, Operator>();
	private static Map<Goal, OptType> goal = new HashMap<Goal, OptType>();
	private static Map<VariableType, VarType> varType = new HashMap<VariableType, VarType>();
	
	static {
		operator.put(Condition.EQUALS, Operator.EQ);
		operator.put(Condition.LESS_THAN, Operator.LE);
		operator.put(Condition.GREATER_THAN, Operator.GE);
		
		goal.put(Goal.MAXIMIZE, OptType.MAX);		
		goal.put(Goal.MINIMIZE, OptType.MIN);

		varType.put(VariableType.BOOLEAN, VarType.BOOL);
		varType.put(VariableType.CONTINOUS, VarType.REAL);
		varType.put(VariableType.INTEGER, VarType.INT);
	}

	private LinearModel linearModel;
	private Problem problem;

	@Override
	public LinearModelSolver init(LinearModel m) {
		this.linearModel = m;
		
		problem = new Problem();
		for (Restriction r : m.getRestrictions()) {
			Linear l = new Linear();
			for (Term t : r.getTerms()) {
				Variable v = t.getVar();
				if (v != null){
					l.add(t.getCoef(), v.getName());
					problem.setVarType(v.getName(), varType.get(v.getVarType()));
					//FIXME
					if (VariableType.INTEGER == v.getVarType()){
						problem.setVarUpperBound(v.getName(), AlumnosCursosModel.getInstance().getAlumnos().size());
					} else if (VariableType.BOOLEAN == v.getVarType()){
						problem.setVarLowerBound(v.getName(), 0);
						problem.setVarUpperBound(v.getName(), 1);
					}
					
				}
			}
			System.out.println("Adding Constraint: " + r.toString());
			Constraint c = new Constraint(r.getName(), l, operator.get(r.getCondition()), r.getRightHand());
			problem.add(c);
		}
		Linear obj = new Linear();
		for (Term t : m.getFunctional().getTerms()) {
			obj.add(t.getCoef(), t.getVar().getName());
		}
		problem.setObjective(obj, goal.get(m.getFunctional().getGoal()));
		System.out.println("Adding Functional: " + m.getFunctional().toString());
		System.out.println(problem);
		
		
		
		return this;
	}

	@Override
	public void solveModel() {
		SolverFactory sf = getSolverFactory();
		sf.setParameter(Solver.TIMEOUT, 720);
		Solver s = sf.get();
		Result result = s.solve(problem);
		if (result != null){
			System.out.println(result.toString());
			for (Variable v : linearModel.getVariables()) {
				if (v != null){
					if (v.getVarType() != VariableType.CONTINOUS){
						v.setIntegerValue(result.get(v.getName()).longValue());
					} else {
						v.setValue(BigDecimal.valueOf(result.get(v.getName()).doubleValue()));
					}
				}
			}
		}
		BigDecimal.ZERO.negate();
		
	}

	protected abstract SolverFactory getSolverFactory();
	
}
