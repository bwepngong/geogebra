package geogebra.common.kernel.scripting;

import geogebra.common.kernel.Kernel;
import geogebra.common.kernel.arithmetic.Command;
import geogebra.common.kernel.commands.CmdScripting;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoNumeric;
import geogebra.common.kernel.geos.GeoText;
import geogebra.common.main.MyError;
import geogebra.common.plugin.UDPLogger;

public class CmdStartLogging extends CmdScripting {
	public CmdStartLogging(Kernel kernel) {
		super(kernel);
	}

	@Override
	protected void perform(Command c) {
		
		int n = c.getArgumentNumber();
		
		// ignore last parameter if odd
		n = (n / 2) * 2;
		
		arg = resArgs(c);
		
		boolean success = false;

		UDPLogger logger = app.getUDPLogger();
		if (logger != null) {
			
			logger.stopLogging();

			GeoElement text;
			GeoElement number = null;
			
			
			for (int i = 0 ; i < n ; i += 2) {
				
				if ((text = arg[i]) instanceof GeoText && (number = arg[i + 1]) instanceof GeoNumeric) {
					logger.registerGeo(((GeoText) text).getTextString(), (GeoNumeric) number);
				} else {
					throw argErr(app, c.getName(), (text instanceof GeoText) ? number : text);
				}
			}
			
			
			
			success = logger.startLogging();
		} 
		
		
		if (!success) {
			throw new MyError(loc, loc.getError("NoLogging"));
		}

	}



}