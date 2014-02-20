	package geogebra.common.geogebra3D.euclidian3D.draw;




import geogebra.common.euclidian.Previewable;
import geogebra.common.geogebra3D.euclidian3D.EuclidianView3D;
import geogebra.common.geogebra3D.euclidian3D.PolygonTriangulation;
import geogebra.common.geogebra3D.euclidian3D.PolygonTriangulation.Convexity;
import geogebra.common.geogebra3D.euclidian3D.PolygonTriangulation.TriangleFan;
import geogebra.common.geogebra3D.euclidian3D.openGL.PlotterBrush;
import geogebra.common.geogebra3D.euclidian3D.openGL.Renderer;
import geogebra.common.geogebra3D.euclidian3D.openGL.Renderer.PickingType;
import geogebra.common.geogebra3D.kernel3D.Kernel3D;
import geogebra.common.geogebra3D.kernel3D.geos.GeoPolygon3D;
import geogebra.common.kernel.ConstructionDefaults;
import geogebra.common.kernel.Matrix.Coords;
import geogebra.common.kernel.geos.FromMeta;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoPolygon;
import geogebra.common.kernel.kernelND.GeoPointND;
import geogebra.common.main.App;

import java.util.ArrayList;
import java.util.Iterator;






/**
 * Class for drawing 3D polygons.
 * @author matthieu
 *
 */
public class DrawPolygon3D extends Drawable3DSurfaces implements Previewable {


	
	
	/**
	 * Common constructor
	 * @param a_view3D
	 * @param polygon
	 */
	public DrawPolygon3D(EuclidianView3D a_view3D, GeoPolygon polygon){
		
		super(a_view3D, polygon);
		
		
		
		

		
	}
	

	
	//drawing

	@Override
	public void drawGeometry(Renderer renderer) {

		renderer.setLayer(getLayer()); //+0f for z-fighting with planes
		renderer.getGeometryManager().draw(getGeometryIndex());	
		renderer.setLayer(0);

	}
	
	
	@Override
	public void drawOutline(Renderer renderer) {
		
		if(isVisible()){

			setHighlightingColor();

			renderer.getTextures().setDashFromLineType(getGeoElement().getLineType());
			drawGeometry(renderer);
		}

		drawTracesOutline(renderer);

	}
	

	@Override
	public void drawGeometryHiding(Renderer renderer) {
		drawSurfaceGeometry(renderer);
	}
	
	
	@Override
	public void drawGeometryHidden(Renderer renderer){
		drawGeometry(renderer);
	}
	
    @Override
    protected void drawGeometryForPicking(Renderer renderer, PickingType type){
    	if (type==PickingType.POINT_OR_CURVE){
    		drawGeometry(renderer);
    	}else{
    		if(getAlpha()>0){ //surface is pickable only if not totally transparent
    			drawSurfaceGeometry(renderer);
    		}
    	}
	}
	

	@Override
	protected void drawSurfaceGeometry(Renderer renderer){

		renderer.setLayer(getLayer()); //+0f to avoid z-fighting with planes
		renderer.getGeometryManager().draw(getSurfaceIndex());
		renderer.setLayer(0);

	}
	
	
	@Override
	public int getPickOrder(){
		/*
		Application.debug(alpha<1);
		if (alpha<1)
		*/
			return DRAW_PICK_ORDER_2D; //when transparent
			/*
		else
			return DRAW_PICK_ORDER_1D; //when not
			*/
	}	
	
	


	@Override
	public void addToDrawable3DLists(Drawable3DLists lists){
		if (((GeoPolygon) getGeoElement()).isPartOfClosedSurface())
			addToDrawable3DLists(lists,DRAW_TYPE_CLOSED_SURFACES_NOT_CURVED);
		else
			addToDrawable3DLists(lists,DRAW_TYPE_SURFACES);
		
		if(!((GeoPolygon) getGeoElement()).wasInitLabelsCalled()){ // no labels for segments
			addToDrawable3DLists(lists,DRAW_TYPE_CURVES);
		}
		
	}
    
    @Override
	public void removeFromDrawable3DLists(Drawable3DLists lists){
    	if (((GeoPolygon) getGeoElement()).isPartOfClosedSurface())
    		removeFromDrawable3DLists(lists,DRAW_TYPE_CLOSED_SURFACES_NOT_CURVED); 
    	else
    		removeFromDrawable3DLists(lists,DRAW_TYPE_SURFACES);


		if(!((GeoPolygon) getGeoElement()).wasInitLabelsCalled()){ // no labels for segments
			removeFromDrawable3DLists(lists,DRAW_TYPE_CURVES);
		}

    }
    
    
	
	
	@Override
	protected boolean updateForItSelf(){
		
		//super.updateForItSelf();
		
		//creates the polygon
		GeoPolygon polygon = (GeoPolygon) getGeoElement();
		
		
		
		
		int pointLength = polygon.getPointsLength();
		
		if (pointLength<3 /*|| Kernel.isZero(polygon.getArea())*/){ //no polygon
			setSurfaceIndex(-1);
			return true;
		}
		
		
		Renderer renderer = getView3D().getRenderer();
		
		Coords[] vertices = new Coords[pointLength];
		for(int i=0;i<pointLength;i++){
			vertices[i] = polygon.getPoint3D(i);
		}



		// outline
		if(!polygon.wasInitLabelsCalled()){ // no labels for segments
			updateOutline(renderer, vertices);
		}


		
		
		// surface
		int index = renderer.startPolygons();
		Coords n = polygon.getMainDirection();
		//App.debug(polygon+"\n"+n);
		/*
		if (polygon.getReverseNormalForDrawing()){
			n = n.mul(-1);
		}
		*/
		
		
		PolygonTriangulation pt = new PolygonTriangulation(polygon);
		try{
			// simplify the polygon and check if there are at least 3 points left
			if (pt.updatePoints() > 2){
				
				// check if the polygon is convex
				Convexity convexity = pt.checkIsConvex();
				if(convexity != Convexity.NOT){
					boolean reverse = polygon.getReverseNormalForDrawing() ^ (convexity == Convexity.CLOCKWISE);
					//App.debug(polygon+" : "+reverse+" = "+polygon.getReverseNormalForDrawing()+" , "+convexity);
					//reverse = false;
					renderer.getGeometryManager().drawPolygonConvex(n, vertices, reverse);
				}else{
					// set intersections (if needed) and divide the polygon into non self-intersecting polygons
					pt.setIntersections();

					// convert the set of polygons to triangle fans
					pt.triangulate();

					// compute 3D coords for intersections
					Coords[] verticesWithIntersections = pt.getCompleteVertices(vertices, polygon.getCoordSys());

					// draw the triangle fans
					for (TriangleFan triFan : pt.getTriangleFans()){
						renderer.getGeometryManager().drawTriangleFan(n, verticesWithIntersections, triFan);
					}
				}

			}
		}catch(Exception e){
			App.debug(e.getMessage());
			e.printStackTrace();
		}
		 

		
		renderer.endPolygons();
		
		setSurfaceIndex(index);				
		
		return true;
		
	}
	
	
	private void updateOutline(Renderer renderer, Coords[] vertices){

		PlotterBrush brush = renderer.getGeometryManager().getBrush();	
		brush.start(8);
		brush.setThickness(getGeoElement().getLineThickness(),(float) getView3D().getScale());
		for(int i=0;i<vertices.length-1;i++){
			brush.setAffineTexture(0.5f,  0.25f);
			brush.segment(vertices[i],vertices[i+1]);
		}
		brush.setAffineTexture(0.5f,  0.25f);
		brush.segment(vertices[vertices.length-1],vertices[0]);
		setGeometryIndex(brush.end());

	}
	

	@Override
	protected void updateForView(){

		if (getView3D().viewChangedByZoom()){
			
			if(!((GeoPolygon) getGeoElement()).wasInitLabelsCalled()){ // no labels for segments

				GeoPolygon polygon = (GeoPolygon) getGeoElement();
				int pointLength = polygon.getPointsLength();
				Renderer renderer = getView3D().getRenderer();

				Coords[] vertices = new Coords[pointLength];
				for(int i=0;i<pointLength;i++){
					vertices[i] = polygon.getPoint3D(i);
				}


				// outline
				updateOutline(renderer, vertices);
				
				recordTrace();
			}
		}
	}
	
	
	////////////////////////////////
	// Previewable interface 

	
	@SuppressWarnings("unchecked")
	private ArrayList selectedPoints;
	
	/** segments of the polygon preview */
	private ArrayList<DrawSegment3D> segments;
	
	@SuppressWarnings("unchecked")
	private ArrayList<ArrayList> segmentsPoints;
	

	/**
	 * Constructor for previewable
	 * @param a_view3D
	 * @param selectedPoints
	 */
	@SuppressWarnings("unchecked")
	public DrawPolygon3D(EuclidianView3D a_view3D, ArrayList selectedPoints){
		
		super(a_view3D);
		
		Kernel3D kernel = getView3D().getKernel();

		setGeoElement(new GeoPolygon3D(kernel.getConstruction(),null));
		
		getGeoElement().setObjColor(ConstructionDefaults.colPolygon);
		getGeoElement().setAlphaValue(ConstructionDefaults.DEFAULT_POLYGON_ALPHA);
		getGeoElement().setIsPickable(false);
		
		this.selectedPoints = selectedPoints;
		
		segments = new ArrayList<DrawSegment3D>();
		segmentsPoints = new ArrayList<ArrayList>();
		

		updatePreview();
		
	}	

	
	private boolean freezePreview = false;

	/**
	 * no more update for the preview
	 */
	public void freezePreview(){
		freezePreview = true;
	}







	public void updateMousePos(double xRW, double yRW) {	
		// TODO Auto-generated method stub
		
	}



	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updatePreview() {
		
		if (freezePreview)
			return;
		
		
		// intersection curve
		if (segmentsPoints == null){
			//App.debug(this);
			setWaitForUpdate();
			return;
		}
		

		int index =0;
		Iterator<ArrayList> spi = segmentsPoints.iterator();
		Iterator i = selectedPoints.iterator();
		GeoPointND point = null; // current point of the selected points
		ArrayList sp = null; // segment selected points
		
		// set points to existing segments points
		for (; i.hasNext() && spi.hasNext();){
			point = (GeoPointND) i.next();
			if (sp!=null)
				sp.add(point);	// add second point to precedent segment
			
			sp = spi.next(); 
			sp.clear();	
			sp.add(point);	// add first point to current segment			
		}
		
		// clear segments points if there are some more
		for (; spi.hasNext();){
			sp = spi.next(); 
			sp.clear();
		}
		
		
		// set points to new segments points
		for (; i.hasNext() ;){
			if (sp!=null && point!=null)
				sp.add(point);	// add second point to precedent segment
			
			sp = new ArrayList();
			segmentsPoints.add(sp);
			point = (GeoPointND) i.next();
			sp.add(point);
			DrawSegment3D s = new DrawSegment3D(getView3D(),sp);
			s.getGeoElement().setObjColor(ConstructionDefaults.colPolygon);
			segments.add(s);
			getView3D().addToDrawable3DLists(s);
		}
		
		// update segments
		for (Iterator<DrawSegment3D> s = segments.iterator(); s.hasNext();)
			s.next().updatePreview();
		
		
		
		//Application.debug("DrawList3D:\n"+getView3D().getDrawList3D().toString());
		
		
		// polygon itself
		
		if (selectedPoints.size()<2){
			getGeoElement().setEuclidianVisible(false);
			return;
		}
		

		
		getGeoElement().setEuclidianVisible(true);
		
		GeoPointND[] points = new GeoPointND[selectedPoints.size()+1];
		
		index =0;
		for (Iterator p = selectedPoints.iterator(); p.hasNext();){
			points[index]= (GeoPointND) p.next();
			index++;
		}
		
		points[index] = getView3D().getCursor3D();
			
		//sets the points of the polygon
		((GeoPolygon3D) getGeoElement()).setPoints(points,null,false);
		//check if all points are on the same plane
		((GeoPolygon3D) getGeoElement()).updateCoordSys();
		if (getGeoElement().isDefined())
			setWaitForUpdate();
		
		
	}
	
	@Override
	public void disposePreview() {
		super.disposePreview();
		
		// dispose segments
		if (segments != null){
			for (DrawSegment3D s : segments){
				s.disposePreview();
			}
		}

		
	}



	@Override
	public boolean doHighlighting(){

		//if the polygon depends on a polyhedron, look at the meta' highlighting
		
		if (getGeoElement().getMetasLength() > 0){
			for (GeoElement meta : ((FromMeta) getGeoElement()).getMetas()){
				if (meta!=null && meta.doHighlighting())
					return true;
			}
		}
		
		return super.doHighlighting();
	}

}