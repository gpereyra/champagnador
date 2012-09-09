package com.tufiestapesta.champagnador.ilp.javailp;

import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryGLPK;

public class GLPKMapper extends JavaILPMapper {

	@Override
	protected SolverFactory getSolverFactory() {
		System.loadLibrary("glpk_4_47_java");
		return new SolverFactoryGLPK();
	}

}
