package geogebra.web.gui.util;


import geogebra.common.awt.GColor;
import geogebra.common.awt.GRenderingHints;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.plugin.EuclidianStyleConstants;
import geogebra.html5.awt.GGraphics2DW;
import geogebra.html5.gui.util.BasicIcons;
import geogebra.web.css.GuiResources;
import geogebra.web.gui.images.StyleBarResources;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.resources.client.ImageResource;

public class GeoGebraIcon extends BasicIcons{



	/** creates LineStyle icon
	 * @param dashStyle
	 * @param thickness
	 * @param iconSize
	 * @param fgColor
	 * @param bgColor
	 * @return Canvas with icon drawn
	 */
	public static ImageOrText createLineStyleIcon(int dashStyle, int thickness, GColor fgColor, GColor bgColor) {
		ImageOrText ret = new ImageOrText();
		if(dashStyle >= lineStyleIcons.length){
			return ret;
		}
		ret.setUrl(lineStyleIcons[dashStyle].getSafeUri().asString());
		return ret;
    }

	private static StyleBarResources LafIcons = StyleBarResources.INSTANCE;
	private static ImageResource[] pointStyleIcons =  {
		(LafIcons.point_full()),
		(LafIcons.point_cross_diag()),
		(LafIcons.point_empty()),
		(LafIcons.point_cross()),
		(LafIcons.point_diamond()),
		(LafIcons.point_diamond_empty()),
		(LafIcons.point_up()),
		(LafIcons.point_down()),
		(LafIcons.point_right()),
		(LafIcons.point_left())};
	private static ImageResource[] gridStyleIcons =  {
		(LafIcons.stylingbar_empty()),
		(LafIcons.grid()),
		(LafIcons.polar_grid()),
		(LafIcons.isometric_grid())};
	private static ImageResource[] lineStyleIcons = {
		(LafIcons.line_solid()),
		(LafIcons.line_dashed_long()),
		(LafIcons.line_dashed_short()),
		(LafIcons.line_dotted()),
		(LafIcons.line_dash_dot()) };
	
	/**
	 * @param pointStyle
	 *            int
	 * @return {@link ImageOrText}
	 */
	public static ImageOrText createPointStyleIcon(int pointStyle) {
		ImageOrText ret = new ImageOrText();
		ret.setUrl(pointStyleIcons[pointStyle].getSafeUri().asString());
		return ret;
    }
	
	/**
	 * @param pointStyle
	 *            int
	 * @return {@link ImageOrText}
	 */
	public static ImageOrText createGridStyleIcon(int pointStyle) {
		ImageOrText ret = new ImageOrText();
		ret.setUrl(gridStyleIcons[pointStyle].getSafeUri().asString());
		return ret;
    }
	
	/**
	 * @param alpha
	 *            {@code float}
	 * @param fgColor
	 *            {@link GColor}
	 * @param bgColor
	 *            {@link GColor}
	 * @return {@link ImageOrText}
	 */
	public static ImageOrText createColorSwatchIcon(float alpha,
	        GColor fgColor, GColor bgColor) {
		ImageOrText ret = new ImageOrText();
		float[] rgb = new float[3];
		if(fgColor!=null){
			fgColor.getRGBColorComponents(rgb);
			ret.setFgColor(geogebra.common.factories.AwtFactory.prototype.newColor( rgb[0], rgb[1], rgb[2], alpha));
		}
		if(bgColor!=null){
			bgColor.getRGBColorComponents(rgb);
			ret.setBgColor(geogebra.common.factories.AwtFactory.prototype.newColor( rgb[0], rgb[1], rgb[2], alpha));
		}
		return ret;
	}

	/**
	 * 
	 * @param symbol
	 *            {@code String}
	 * @param fgColor
	 *            {@link GColor}
	 * @param bgColor
	 *            {@link GColor}
	 * @return {@link ImageOrText}
	 */
	public static ImageOrText createTextSymbolIcon(String symbol,
	        GColor fgColor, GColor bgColor) {
		ImageOrText ret = new ImageOrText();
		ret.setText(symbol);
		ret.setFgColor(fgColor);
		ret.setBgColor(bgColor);
		return ret;
	}
	
	/**
	 * @param width
	 *            {@code int}
	 * @param height
	 *            {@code int}
	 * @return {@link ImageOrText}
	 */
	public static ImageOrText createNullSymbolIcon(int width, int height){

		Canvas c = getTmpCanvas(width, height);

		GGraphics2DW g2 = new GGraphics2DW(c);
		g2.setRenderingHint(GRenderingHints.KEY_ANTIALIASING, GRenderingHints.VALUE_ANTIALIAS_ON);

		g2.setPaint(GColor.GRAY);
		// draw a rectangle with an x inside
		g2.drawRect(3, 3, width-6, height-6);
		int k = 7;
		g2.drawLine(k, k, width-k, height-k);
		g2.drawLine(k, height-k, width-k, k );
		return new ImageOrText();
	}

	/**
	 * @param res
	 *            {@link ImageResource}
	 * @return {@link ImageOrText}
	 */
	public static ImageOrText createResourceImageIcon(ImageResource res) {
		ImageOrText ret = new ImageOrText();
		ret.setUrl(res.getSafeUri().asString());
	    return ret;
    }

	/**
	 * @param id
	 *            {@code int}
	 * @return {@link ImageOrText}
	 */
	public static ImageOrText createDecorAngleIcon(int id) {
		ImageOrText ret = new ImageOrText();
		ImageResource url = null;
		switch(id){
			case GeoElement.DECORATION_ANGLE_TWO_ARCS:
				url =  GuiResources.INSTANCE.deco_angle_2lines();
			break;
			case GeoElement.DECORATION_ANGLE_THREE_ARCS:
				url =  GuiResources.INSTANCE.deco_angle_3lines();
			break;
			case GeoElement.DECORATION_ANGLE_ONE_TICK:
				url =  GuiResources.INSTANCE.deco_angle_1stroke();
			break;
			case GeoElement.DECORATION_ANGLE_TWO_TICKS:
				url =  GuiResources.INSTANCE.deco_angle_2strokes();
			break;
			case GeoElement.DECORATION_ANGLE_THREE_TICKS:
				url =  GuiResources.INSTANCE.deco_angle_3strokes();
			break;			
//			 Michael Borcherds 2007-11-19 BEGIN
			case GeoElement.DECORATION_ANGLE_ARROW_ANTICLOCKWISE:
				url =  GuiResources.INSTANCE.deco_angle_arrow_down();
			break;
			case GeoElement.DECORATION_ANGLE_ARROW_CLOCKWISE:
				url =  GuiResources.INSTANCE.deco_angle_arrow_up();
			break;
			default:
				url =  GuiResources.INSTANCE.deco_angle_1line();
//			 Michael Borcherds 2007-11-19 END
		}
		ret.setUrl(url.getSafeUri().asString());
		return ret;

	}

	/**
	 * @param id
	 *            {@code int}
	 * @return {@link ImageOrText}
	 */
	public static ImageOrText createDecorSegmentIcon(int id) {
		ImageOrText ret = new ImageOrText();
		ImageResource url = null;

		switch (id) {		
		case GeoElement.DECORATION_SEGMENT_ONE_TICK:
			url =  GuiResources.INSTANCE.deco_segment_1stroke();
			break;
		case GeoElement.DECORATION_SEGMENT_TWO_TICKS:
			url =  GuiResources.INSTANCE.deco_segment_2strokes();
			break;
		case GeoElement.DECORATION_SEGMENT_THREE_TICKS:
			url =  GuiResources.INSTANCE.deco_segment_3strokes();
			break;
		// Michael Borcherds 20071006 start
		case GeoElement.DECORATION_SEGMENT_ONE_ARROW:
			url =  GuiResources.INSTANCE.deco_segment_1arrow();
			break;
		case GeoElement.DECORATION_SEGMENT_TWO_ARROWS:
			url =  GuiResources.INSTANCE.deco_segment_2arrows();
			break;
		case GeoElement.DECORATION_SEGMENT_THREE_ARROWS:
			url =  GuiResources.INSTANCE.deco_segment_3arrows();
			break;
		default:
			url =  GuiResources.INSTANCE.deco_segment_none();
			break;
		// Michael Borcherds 20071006 end
		}

		ret.setUrl(url.getSafeUri().asString());
		return ret;

    }

	/**
	 * @param id
	 *            {@code int}
	 * @return {@link ImageOrText}
	 */
	public static ImageOrText createAxesStyleIcon(int id) {
		ImageOrText ret = new ImageOrText();
		ImageResource url = null;
		switch(id){
			case EuclidianStyleConstants.AXES_LINE_TYPE_ARROW:
				url =  GuiResources.INSTANCE.deco_axes_arrow();
				break;
			case EuclidianStyleConstants.AXES_LINE_TYPE_TWO_ARROWS:
				url =  GuiResources.INSTANCE.deco_axes_arrows();
				break;
			case EuclidianStyleConstants.AXES_LINE_TYPE_ARROW_FILLED:
				url =  GuiResources.INSTANCE.deco_axes_arrow_filled();
				break;
			case EuclidianStyleConstants.AXES_LINE_TYPE_TWO_ARROWS_FILLED:
				url =  GuiResources.INSTANCE.deco_axes_arrows_filled();
				break;
			default:
				url =  GuiResources.INSTANCE.deco_axes_none();
		}
		ret.setUrl(url.getSafeUri().asString());
		return ret;
	}
}
