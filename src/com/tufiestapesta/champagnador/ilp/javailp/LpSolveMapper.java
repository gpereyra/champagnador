package com.tufiestapesta.champagnador.ilp.javailp;

import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

public class LpSolveMapper extends JavaILPMapper {

	@Override
	protected SolverFactory getSolverFactory() {
		System.loadLibrary("lpsolve55");
		System.loadLibrary("lpsolve55j");
		return new SolverFactoryLpSolve();
	}

}
