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
 * Finds all local extrema of a polynomial wrapped in If[]
 * eg If[0  <  x  <  10,3x³ - 48x² + 162x + 300]
 * 
 * @author Michael
 */
public class AlgoExtremumPolynomialInterval extends AlgoExtremumPolynomial {
	
	private double[] bounds;

	/**
	 * @param cons cons
	 * @param labels labels
	 * @param f function
	 */
	public AlgoExtremumPolynomialInterval(
			Construction cons,
			String[] labels,
			GeoFunction f) {
		super(cons, labels, f);
	}

	@Override
	public final void compute() {
		if (f.isDefined()) {

			if (yValFunction == null) {
				FunctionVariable fVar = f.getFunction().getFunctionVariable();

				// extract poly from If[0<x<10, poly]
				yValFunction = new Function((ExpressionNode) f.getFunctionExpression().getRight(), fVar);   

			}

			// roots of first derivative 
			//(roots without change of sign are removed)
			calcRoots(yValFunction, 1);             
		} else {
			curRealRoots = 0;
		}

		setRootPoints(curRoots, curRealRoots);
		
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


}