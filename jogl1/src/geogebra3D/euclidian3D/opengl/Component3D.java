package geogebra3D.euclidian3D.opengl;




import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import javax.media.opengl.GLEventListener;


/**
 * Simple class extending GL JPanel/Canvas
 * @author matthieu
 *
 */
public interface Component3D { 
	

	public void addGLEventListener(GLEventListener renderer);

	public void display();

	public Point getLocationOnScreen();

	public Component getParent();

	public Dimension getSize();
	
	
}
