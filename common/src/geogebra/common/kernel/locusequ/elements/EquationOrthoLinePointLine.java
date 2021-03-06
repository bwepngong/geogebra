/**
 * 
 */
package geogebra.common.kernel.locusequ.elements;

import geogebra.common.kernel.algos.AlgoOrthoLinePointLine;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoLine;
import geogebra.common.kernel.locusequ.EquationScope;

/**
 * @author sergio
 * EquationElement for {@link AlgoOrthoLinePointLine}
 */
public class EquationOrthoLinePointLine extends EquationGenericLine {

    private GeoLine lParam;
    private EquationGenericLine lParamEqu;

    /**
     * General constructor.
     * @param l {@link GeoElement}
     * @param scope {@link EquationScope}
     */
    public EquationOrthoLinePointLine(final GeoElement l, final EquationScope scope) {
        super(l, scope);
        AlgoOrthoLinePointLine algo = (AlgoOrthoLinePointLine) l.getParentAlgorithm();

        this.lParam = algo.getl();
        this.lParamEqu = (EquationGenericLine) this.getScope().getElement(this.lParam);
        
        this.setVector(this.lParamEqu.getVector().normal());
        this.setPoint(algo.getP());
    }
}
