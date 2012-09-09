package com.tufiestapesta.champagnador.ilp.lpsolve;

import java.math.BigDecimal;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

import com.tufiestapesta.champagnador.ilp.Condition;
import com.tufiestapesta.champagnador.ilp.Functional;
import com.tufiestapesta.champagnador.ilp.Goal;
import com.tufiestapesta.champagnador.ilp.LinearModel;
import com.tufiestapesta.champagnador.ilp.LinearModelSolver;
import com.tufiestapesta.champagnador.ilp.Restriction;
import com.tufiestapesta.champagnador.ilp.Result;
import com.tufiestapesta.champagnador.ilp.Variable;

public class LpSolveSolver implements LinearModelSolver {
	private LpSolve lpModel;
	private LinearModel linearModel;
	
	/* (non-Javadoc)
	 * @see com.tufiestapesta.champagnador.ilp.lpsolve.LinearModelSolver#init(com.tufiestapesta.champagnador.ilp.LinearModel)
	 */
	@Override
	public LinearModelSolver init(LinearModel m) {
		linearModel = m;
		mapModel();
		return this;
	}
	
	private LpSolve mapModel() {
		try {
			lpModel = LpSolve.makeLp(linearModel.getRestrictions().size(), linearModel.getVariables().size());
			lpModel.setObjFn(getFunctionalAsArray(linearModel.getFunctional()));
			//Goal.MAXIMIZE => true, Goal.MINIMIZE => false
			lpModel.setSense(Goal.MAXIMIZE == linearModel.getFunctional().getGoal());
			for (Restriction r : linearModel.getRestrictions()) {
				lpModel.addConstraint(getCoeficientsArray(r), getLpCondition(r.getCondition()), getConstant(r));
			}
			return lpModel;
		} catch (LpSolveException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to build LpSolve linear model.", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.tufiestapesta.champagnador.ilp.lpsolve.LinearModelSolver#solveModel()
	 */
	@Override
	public void solveModel() {
		try {
			lpModel.solve();
			double[] var = lpModel.getPtrVariables();
		      for (int i = 0; i < var.length; i++) {
		        System.out.println("Value of var[" + i + "] = " + var[i]);
		        linearModel.addResult(new Result(linearModel.getOrderedVariables().get(i), BigDecimal.valueOf(var[i])));
		      }
		} catch (LpSolveException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to solve LpSolve linear model.", e);
		}

	}

	private double[] getFunctionalAsArray(Functional f) {
		int varCount = linearModel.getOrderedVariables().size();
		double[] coefs = new double[varCount];
		for (int i = 0; i < varCount; i++) {
			Variable v = linearModel.getOrderedVariables().get(i);
			BigDecimal term = f.getTermFor(v);
			double coef;
			if (term != null){
				coef = term.doubleValue();
			} else {
				coef = BigDecimal.ZERO.doubleValue();
			}
			coefs[i] = coef;
		}
		return coefs;
	}

	private double getConstant(Restriction r) {
		return r.getRightHand().doubleValue();
	}

	private int getLpCondition(Condition c) {
		int lpCondition = -1;
		if (Condition.GREATER_THAN == c){
			lpCondition = LpSolve.GE;
		} else if (Condition.LESS_THAN == c){
			lpCondition = LpSolve.LE;
		} else {
			lpCondition = LpSolve.EQ;
		}
		return lpCondition;
	}

	private double[] getCoeficientsArray(Restriction r) {
		int varCount = linearModel.getOrderedVariables().size();
		double[] coefs = new double[varCount];
		for (int i = 0; i < varCount; i++) {
			Variable v = linearModel.getOrderedVariables().get(i);
			BigDecimal term = r.getTermFor(v);
			double coef;
			if (term != null){
				coef = term.doubleValue();
			} else {
				coef = BigDecimal.ZERO.doubleValue();
			}
			coefs[i] = coef;
		}
		return coefs;
	}
}
