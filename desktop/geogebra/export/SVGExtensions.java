package geogebra.export;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * adds support for grouping objects in SVG files
 * 
 * also now adds support for exporting size in cm from suggestion here
 * http://forum.geogebra.org/viewtopic.php?f=8&t=38068
 * 
 * and returning null from getTransform() (ie Identity matrix)
 * 
 * needs this line changed in SVGGraphics2D.java (was private) protected
 * PrintWriter os;
 * 
 * @author Michael Borcherds
 */

public class SVGExtensions extends org.freehep.graphicsio.svg.SVGGraphics2D {

	private double cmWidth;
	private double cmHeight;

	public SVGExtensions(File file, Dimension size, double cmWidth,
			double cmHeight) throws IOException {
		super(file, size);
		this.cmWidth = cmWidth;
		this.cmHeight = cmHeight;
	}

	public void startGroup(String s) {
		os.println("<g id=\"" + s + "\">");
	}

	public void endGroup(String s) {
		os.println("</g><!-- " + s + " -->");
	}

	@Override
	public AffineTransform getTransform() {
		return null;
	}

	protected void writeSize(PrintWriter os) {

		if (cmWidth > 0 && cmHeight > 0) {
			// cm
			os.println("     width=\"" + cmWidth + "cm\"");
			os.println("     height=\"" + cmHeight + "cm\"");

		} else {
			super.writeSize(os);

		}

	}

}