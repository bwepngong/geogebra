package geogebra.gui.menubar;

import geogebra.gui.layout.DockPanel;
import geogebra.gui.layout.LayoutD;
import geogebra.main.AppD;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 * The "View" menu for the applet. For application use ViewMenuApplication class
 */
public class ViewMenuD extends BaseMenu {
	private static final long serialVersionUID = 1L;
	protected final LayoutD layout;
	protected AbstractAction refreshAction, recomputeAllViews;

	/**
	 * @param app
	 *            app
	 * @param layout
	 *            layout
	 */
	public ViewMenuD(AppD app, LayoutD layout) {
		super(app, app.getMenu("View"));

		this.layout = layout;

		// items are added to the menu when it's opened, see BaseMenu:
		// addMenuListener(this);
	}

	/**
	 * Initialize the menu items.
	 */
	@Override
	protected void initItems() {

		if (!initialized) {
			return;
		}

		JMenuItem mi;

		mi = add(refreshAction);
		setMenuShortCutAccelerator(mi, 'F');

		mi = add(recomputeAllViews);
		// F9 and Ctrl-R both work, but F9 doesn't on MacOS, so we must display
		// Ctrl-R
		setMenuShortCutAccelerator(mi, 'R');

		// support for right-to-left languages
		app.setComponentOrientation(this);

	}

	/**
	 * Initialize the actions, which used by applet and application too.
	 */
	@Override
	protected void initActions() {

		refreshAction = new AbstractAction(app.getMenu("Refresh"),
				new ImageIcon(app.getRefreshViewImage())) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				app.refreshViews();
			}
		};

		recomputeAllViews = new AbstractAction(
				app.getMenu("RecomputeAllViews"), app.getEmptyIcon()) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				app.getKernel().updateConstruction();
			}
		};
	}

	@Override
	public void update() {
		// do nothing
	}

	/**
	 * Tells if the 3D View is shown in the current window
	 * 
	 * @return whether 3D View is switched on
	 */
	public boolean is3DViewShown() {
		DockPanel[] dockPanels = layout.getDockManager().getPanels();
		for (DockPanel panel : dockPanels) {
			if (panel.isVisible() && panel.isEuclidianDockPanel3D())
				return true;
		}
		return false;
	}

}
