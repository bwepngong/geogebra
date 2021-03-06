package geogebra.touch.gui.algebra;

import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.main.App;
import geogebra.html5.gui.view.algebra.AlgebraViewWeb;
import geogebra.html5.main.AppWeb;
import geogebra.touch.TouchEntryPoint;
import geogebra.touch.controller.TouchController;
import geogebra.touch.gui.laf.DefaultResources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * AlgebraView with tree for free and dependent objects.
 * 
 * Taken from the web-project.
 * 
 */
class AlgebraViewT extends AlgebraViewWeb {

	private static DefaultResources getLafIcons() {
		return TouchEntryPoint.getLookAndFeel().getIcons();
	}

	private TouchController controller;
	private AlgebraViewPanel panel;
	
	private boolean editing = false;
	private boolean showing = true;

	/**
	 * Creates new AlgebraView.
	 * @param algebraViewPanel 
	 * 
	 * @param algCtrl
	 *            : AlgebraController
	 * 
	 */
	AlgebraViewT(TouchController ctr, AlgebraViewPanel algebraViewPanel) {
		super((AppWeb) ctr.getApplication());
		// this is the default value
		this.treeMode = SortMode.TYPE;

		this.controller = ctr;
		this.panel = algebraViewPanel;

		// initializes the tree model
		this.initModel();

		this.setLabels();

		this.getElement().setId("View_" + App.VIEW_ALGEBRA);
	}

	@Override
	public void cancelEditing() {
		this.editing = false;
		this.setAnimationEnabled(true);
	}

	@Override
	public GeoElement getGeoElementForLocation(Object tree, int x, int y) {
		// return getGeoElementForLocation((JTree)tree, x, y);
		return null;
	}

	// temporary proxies for the temporary implementation of AlgebraController
	// in
	// common
	@Override
	public GeoElement getGeoElementForPath(Object tp) {
		// return getGeoElementForPath((TreePath)tp);
		return null;
	}

	@Override
	public Object getPathBounds(Object tp) {
		// return getPathBounds((TreePath)tp);
		return null;
	}

	@Override
	public Object getPathForLocation(int x, int y) {
		return null;
	}

	// temporary proxies end

	@Override
	public boolean hasFocus() {
		return false;
	}

	@Override
	public boolean isEditing() {
		return this.editing;
	}

	@Override
	protected boolean isKeyboardNavigationEnabled(TreeItem ti) {
		// keys should move the geos in the EV
		// if (isEditing())
		return false;
		// return super.isKeyboardNavigationEnabled(ti);
	}

	@Override
	public boolean isRenderLaTeX() {
		return true;
	}

	/**
	 * FIXME: should return false if the panel is not extended!
	 */
	@Override
	public boolean isShowing() {
		return this.showing;
	}

	@Override
	public void onBrowserEvent(Event event) {
		if (!this.editing) {
			super.onBrowserEvent(event);
		}
	}

	public void setShowing(boolean flag) {
		this.showing = flag;
		if (flag) {
			this.repaint();
		}
	}

	@Override
	public void add(GeoElement geo) {
		super.add(geo);
		this.panel.onResize();
	}
	
	@Override
	public void setUserObject(TreeItem ti, final Object ob) {
		ti.setUserObject(ob);
		if (ob instanceof GeoElement) {
			ti.setWidget(new RadioButtonTreeItemT((GeoElement) ob,
					getLafIcons().algebra_shown().getSafeUri(),
					DefaultResources.INSTANCE.algebra_hidden().getSafeUri(),
					null, this.controller));
			ti.getElement().getStyle().setPadding(0, Unit.PX);
			ti.setStyleName("treeItemWrapper");
			// Workaround to make treeitem visual selection available
			DOM.setStyleAttribute((com.google.gwt.user.client.Element) ti
					.getElement().getFirstChildElement(), "display",
					"-moz-inline-box");
			DOM.setStyleAttribute((com.google.gwt.user.client.Element) ti
					.getElement().getFirstChildElement(), "display",
					"inline-block");
		} else {
			ti.setWidget(new GroupHeaderT(this.app.getSelectionManager(), ti, ob
					.toString(), DefaultResources.INSTANCE.triangle_left()
					.getSafeUri(), getLafIcons().triangle_left().getSafeUri(), 
					this.controller));
		}		
	}

	@Override
	public void startEditing(GeoElement geo, boolean shiftDown) {
		// TODO Auto-generated method stub
		
	}
}
