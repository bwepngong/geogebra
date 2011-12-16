package geogebra.kernel.commands;

import geogebra.common.kernel.AbstractKernel;
import geogebra.common.kernel.arithmetic.Command;
import geogebra.common.kernel.arithmetic.NumberValue;
import geogebra.common.kernel.commands.CommandProcessor;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoPoint2;
import geogebra.common.main.MyError;

/**
 *DynamicCoordinates
 */
class CmdDynamicCoordinates extends CommandProcessor {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdDynamicCoordinates(AbstractKernel kernel) {
		super(kernel);
	}

	final public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		GeoElement[] arg;
		arg = resArgs(c);

		switch (n) {
		case 3:
			boolean[] ok = new boolean[2];
			if ((ok[0] = (arg[0].isGeoPoint() && arg[0].isMoveable()))
					&& (ok[1] = arg[1].isNumberValue())
					&& (arg[2].isNumberValue())) {
				GeoElement[] ret = { kernelA.DynamicCoordinates(c.getLabel(),
						(GeoPoint2) arg[0], (NumberValue) arg[1],
						(NumberValue) arg[2]) };
				return ret;
			} else if (!ok[0])
				throw argErr(app, c.getName(), arg[0]);
			else if (!ok[1])
				throw argErr(app, c.getName(), arg[1]);
			else
				throw argErr(app, c.getName(), arg[2]);

			// more than one argument
		default:
			throw argNumErr(app, c.getName(), n);
		}
	}
}
