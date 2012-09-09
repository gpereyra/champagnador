package com.tufiestapesta.champagnador;

import com.tufiestapesta.champagnador.ilp.LinearModel;
import com.tufiestapesta.champagnador.ilp.LinearModelSolver;
import com.tufiestapesta.champagnador.ilp.javailp.GLPKMapper;
import com.tufiestapesta.champagnador.model.AlumnosCursosModel;
import com.tufiestapesta.champagnador.model.excel.XlsxModelFactory;

public class Main {

	public static void main(String[] args) {
		XlsxModelFactory factory = new XlsxModelFactory();
		//factory.buildModel("C:\\Users\\Gustavo\\Mi Software\\champagnador\\Datos_v0_1.xlsx");//TODO args[0]);
		factory.buildModel("C:\\Users\\Gustavo\\Mi Software\\champagnador\\MEZCLADOR.xlsx");
		AlumnosCursosModel model = AlumnosCursosModel.getInstance();
		LinearModel linearModel = model.toLinearModel();
		LinearModelSolver solver = new GLPKMapper().init(linearModel);
		solver.solveModel();
		model.eatLinearModelResults(linearModel);
		XlsxModelReporter xlsxModelReporter = new XlsxModelReporter();
		xlsxModelReporter.setup();
		xlsxModelReporter.report(model);
		
	}
	
	
	/*public static void main(String[] args) {
		LinearModel m = new LinearModel();
		
		LinkedHashSet<Term> ft = new LinkedHashSet<Term>();
		Variable panDeCampo = new Variable("panDeCampo", Boolean.FALSE);
		Variable baguette = new Variable("baguette", Boolean.FALSE);
		//$2 el Pan de Campo
		ft.add(new Term(panDeCampo, BigDecimal.valueOf(143)));
		//$3 la baguette
		ft.add(new Term(baguette, BigDecimal.valueOf(60)));
		Functional f = new Functional(ft, Goal.MAXIMIZE);
		m.setFunctional(f);

		LinkedHashSet<Term> rAgua = new LinkedHashSet<Term>();
		//1l el Pan de Campo
		rAgua.add(new Term(panDeCampo, BigDecimal.valueOf(120)));
		//3l la baguette
		rAgua.add(new Term(baguette, BigDecimal.valueOf(210)));
		//500 stock agua
		Restriction resAgua = new Restriction("Agua", rAgua, Condition.LESS_THAN, BigDecimal.valueOf(15000));
		m.addRestriction(resAgua);
		
		LinkedHashSet<Term> rHarina = new LinkedHashSet<Term>();
		//2k el Pan de Campo
		rHarina.add(new Term(panDeCampo, BigDecimal.valueOf(110)));
		//3k la baguette
		rHarina.add(new Term(baguette, BigDecimal.valueOf(30)));
		//350 stock harina
		Restriction resHarina = new Restriction("Harina", rHarina, Condition.LESS_THAN, BigDecimal.valueOf(4000));
		m.addRestriction(resHarina);
		
		LinearModelSolver model = new LpSolveMapper().init(m);
		model.solveModel();
	}*/

}
