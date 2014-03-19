/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

*/

/*
 * AlgoAnglePoints.java
 *
 * Created on 30. August 2001, 21:37
 */

package geogebra.common.geogebra3D.kernel3D.algos;

import geogebra.common.kernel.Construction;
import geogebra.common.kernel.StringTemplate;
import geogebra.common.kernel.algos.AlgoAnglePoints;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoPolygon;
import geogebra.common.kernel.kernelND.GeoDirectionND;


/**
 *
 * @author  mathieu
 */
public class AlgoAnglePolygon3DOrientation extends AlgoAnglePolygon3D{


	private GeoDirectionND orientation;
	
	/**
	 * @param cons construction
	 * @param labels labels
	 * @param poly polygon
	 */
	public AlgoAnglePolygon3DOrientation(Construction cons, String[] labels, GeoPolygon poly, GeoDirectionND orientation) {
		super(cons, labels, poly, orientation);
	}
	
    @Override
	protected AlgoAnglePoints newAlgoAnglePoints(Construction cons1){
    	return new AlgoAnglePoints3DOrientation(cons1, orientation);
    }
	
	
    @Override
	protected void setPolyAndOrientation(GeoPolygon p, GeoDirectionND orientation){
    	super.setPolyAndOrientation(p, orientation);
    	this.orientation = orientation;
    }
    
    @Override
	protected void setInputOutput() {
        input = new GeoElement[2];
        input[0] = poly;
        input[1] = (GeoElement) orientation;
               
        setDependencies();        
    }
    
    @Override
	public String toString(StringTemplate tpl) {

		return loc.getPlain("AngleOfAOrientedByB", poly.getLabel(tpl), orientation.getLabel(tpl));
	}
    

}