/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package geogebra.common.kernel.algos;

import geogebra.common.kernel.Construction;
import geogebra.common.kernel.arithmetic.ExpressionNode;
import geogebra.common.kernel.arithmetic.Function;
import geogebra.common.kernel.arithmetic.FunctionVariable;
import geogebra.common.kernel.geos.GeoFunction;

/**
 * 
 * 
 * @author Michael
 */
public class AlgoRootsPolynomialInterval extends AlgoRootsPolynomial {



	private double[] bounds;
	private Function intervalFun;

	/**
	 * @param cons cons
	 * @param labels labels
	 * @param f function
	 */
	public AlgoRootsPolynomialInterval(Construction cons, String[] labels,
			GeoFunction f) {
		super(cons, labels, f);
	}

	@Override
	public void compute() {

		
		
		super.compute();
		
		if (bounds == null) {
			bounds = new double[2];
		}
		
		// remove points outside the domain
		f.getInterval(bounds);
		
		for (int i = 0; i < rootPoints.length; i++) {
			double xCoord = rootPoints[i].getInhomX();
			if ( xCoord < bounds[0] || xCoord > bounds[1]) {
				rootPoints[i].setUndefined();
			}
		}
	}
	
	protected void computeRoots() {
		if (f.isDefined()) {
			if (intervalFun == null) {
				FunctionVariable fVar = f.getFunction().getFunctionVariable();
				// extract poly from If[0<x<10, poly]
				intervalFun = new Function((ExpressionNode) f.getFunctionExpression().getRight(), fVar);   

			}
			// get polynomial factors and calc roots
			calcRoots(intervalFun, 0);
		} else {
			curRealRoots = 0;
		}
	}



}