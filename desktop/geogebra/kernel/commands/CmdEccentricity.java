package geogebra.kernel.commands;

import geogebra.common.kernel.AbstractKernel;
import geogebra.common.kernel.arithmetic.Command;
import geogebra.common.kernel.commands.CommandProcessor;
import geogebra.common.kernel.geos.GeoConic;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.main.MyError;

/**
 * Eccentricity[ <GeoConic> ]
 */
class CmdEccentricity extends CommandProcessor {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdEccentricity(AbstractKernel kernel) {
		super(kernel);
	}

	final public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		GeoElement[] arg;

		switch (n) {
		case 1:
			arg = resArgs(c);

			// asymptotes to conic
			if (arg[0].isGeoConic()) {
				GeoElement[] ret = { kernelA.Eccentricity(c.getLabel(),
						(GeoConic) arg[0]) };
				return ret;
			} else
				throw argErr(app, c.getName(), arg[0]);

		default:
			throw argNumErr(app, c.getName(), n);
		}
	}
}
