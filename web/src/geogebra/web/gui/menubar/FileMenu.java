package geogebra.web.gui.menubar;

import geogebra.common.GeoGebraConstants;
import geogebra.common.main.AbstractApplication;
import geogebra.web.Web;
import geogebra.web.asyncservices.HandleGoogleDriveService;
import geogebra.web.asyncservices.HandleGoogleDriveServiceAsync;
import geogebra.web.gui.images.AppResources;
import geogebra.web.helper.GoogleApiCallback;
import geogebra.web.helper.MyGoogleApis;
import geogebra.web.main.Application;

import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class FileMenu extends MenuBar {
	/*Bunny, until getBase64() fixed*/
	public static String temp_base64_BUNNY = "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";

	private AbstractApplication app;
	
	public FileMenu(AbstractApplication app, boolean enabled) {
	    super(true);
	    this.app = app;
	    addStyleName("GeoGebraMenuBar");
	    initActions(enabled);
		update();
	}

	public FileMenu(AbstractApplication app) {
		this(app, true);
    }

	private void update() {
	    // TODO Auto-generated method stub
	    
    }

	private void initActions(boolean enabled) {

		// this is enabled always
		addItem(GeoGebraMenubar.getMenuBarHtml(AppResources.INSTANCE.empty().getSafeUri().asString(),app.getMenu("New")),true,new Command() {

			public void execute() {
				app.setWaitCursor();
				app.fileNew();
				app.setDefaultCursor();
			}
		});

	    /*addItem(GeoGebraMenubar.getMenuBarHtml(AppResources.INSTANCE.document_open().getSafeUri().asString(), app.getMenu("Load")), true, new Command() {
			
			public void execute() {
				
			}
				
		});*/
	    
		// this is enabled always
	    addItem(GeoGebraMenubar.getMenuBarHtml(AppResources.INSTANCE.document_open().getSafeUri().asString(),app.getMenu("OpenWebpage")),true,new Command() {
	    	public void execute() {
	    		app.getGuiManager().openURL();
	    	}
	    });
		
		if (enabled)
			addItem(GeoGebraMenubar.getMenuBarHtml(AppResources.INSTANCE.document_save().getSafeUri().asString(), app.getMenu("SaveAs")),true,new Command() {
			
				public void execute() {
					app.getGuiManager().save();
				}
			});
		else
			addItem(GeoGebraMenubar.getMenuBarHtmlGrayout(AppResources.INSTANCE.document_save().getSafeUri().asString(), app.getMenu("SaveAs")),true,new Command() {
				public void execute() {	}
			});

		// this is enabled always
	    addItem(GeoGebraMenubar.getMenuBarHtml(AppResources.INSTANCE.export_small().getSafeUri().asString(),app.getMenu("Share")),true,new Command() {
	    	public void execute() {
	    		app.uploadToGeoGebraTube();
	    	}
	    });
    }

}
