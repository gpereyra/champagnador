package com.tufiestapesta.champagnador.ilp;


public interface LinearModelSolver {

	public abstract LinearModelSolver init(LinearModel m);

	public abstract void solveModel();

}