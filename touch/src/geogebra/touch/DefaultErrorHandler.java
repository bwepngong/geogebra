package geogebra.touch;

import geogebra.common.main.Localization;
import geogebra.html5.gui.ResizeListener;
import geogebra.touch.gui.laf.LookAndFeel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;

class DefaultErrorHandler implements ErrorHandler {

	private ErrorPopup errorPopup;
	private final Localization loc;

	DefaultErrorHandler(final Localization loc) {
		this.loc = loc;
	}

	private class ErrorPopup extends PopupPanel implements ResizeListener {
		private final FlowPanel dialogPanel;
		private final FlowPanel contentPanel = new FlowPanel();

		private final FlowPanel titlePanel = new FlowPanel();
		private final Label title;

		private final HorizontalPanel textPanel;
		private final ImageResource iconWarning;
		private final Label infoText;

		private HorizontalPanel buttonContainer;
		private final Button okButton;

		private final LookAndFeel laf;

		public ErrorPopup() {
			super(true, true);
			this.dialogPanel = new FlowPanel();
			this.laf = TouchEntryPoint.getLookAndFeel();
			this.iconWarning = this.laf.getIcons().icon_warning();

			this.title = new Label();
			this.addLabel();

			this.contentPanel.setStyleName("contentPanel");

			this.dialogPanel.add(this.contentPanel);

			this.textPanel = new HorizontalPanel();
			this.infoText = new Label();
			this.addText();

			this.okButton = new Button();
			initOKButton();

			this.add(this.dialogPanel);
			this.setStyleName("inputDialog");
			this.addStyleName("errorDialog");
			TouchEntryPoint.touchGUI.addResizeListener(this);
		}

		// only used for Win
		@Override
		public void setPopupPosition(final int left, final int top) {
			super.setPopupPosition(left, top);
			centerContent();
		}

		// only used for Win
		private void centerContent() {
			if (this.title != null && this.contentPanel != null) {
				this.laf.center(this.title);
				this.laf.center(this.contentPanel);
			}
		}

		@Override
		public void onResize() {
			if (this.isVisible() && this.isShowing()) {
				this.laf.setPopupCenter(this);
			}
		}

		private void addLabel() {
			this.title.setStyleName("title");
			this.titlePanel.add(this.title);
			this.titlePanel.setStyleName("titlePanel");
			this.dialogPanel.add(this.titlePanel);
		}

		private void addText() {
			final Panel iconPanel = new LayoutPanel();
			final String html = "<img src=\""
					+ this.iconWarning.getSafeUri().asString() + "\" />";
			iconPanel.getElement().setInnerHTML(html);
			iconPanel.setStyleName("iconPanel");
			this.textPanel.add(iconPanel);

			this.textPanel
					.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			this.textPanel.add(this.infoText);

			this.textPanel.setStyleName("textPanel");
			this.contentPanel.add(this.textPanel);
		}

		private void initOKButton() {
			this.buttonContainer = new HorizontalPanel();
			this.buttonContainer
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			this.buttonContainer.setStyleName("buttonPanel");

			this.okButton.addDomHandler(new ClickHandler() {

				@Override
				public void onClick(final ClickEvent event) {
					ErrorPopup.this.hide();
				}
			}, ClickEvent.getType());
			this.okButton.addStyleName("last");
			this.buttonContainer.add(this.okButton);
			this.contentPanel.add(this.buttonContainer);
		}

		public void setLabels(final Localization loc) {
			this.title.setText(loc.getError("Error"));
			this.okButton.setText(loc.getPlain("OK"));
		}

		public void setText(final String text) {
			this.infoText.setText(text);
		}
	}

	@Override
	public void showError(final String error) {
		if (this.errorPopup == null) {
			this.errorPopup = new ErrorPopup();
			this.errorPopup.setGlassEnabled(true);
			this.errorPopup.center();
			this.errorPopup.setLabels(this.loc);
		}
		this.errorPopup.setText(error);
		this.errorPopup.show();
		this.errorPopup.onResize();

	}

}
