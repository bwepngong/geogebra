package geogebra.web.euclidian;

import geogebra.common.awt.GPoint;
import geogebra.common.euclidian.EnvironmentStyle;
import geogebra.common.euclidian.EuclidianView;
import geogebra.common.euclidian.event.AbstractEvent;
import geogebra.common.kernel.Kernel;
import geogebra.common.kernel.arithmetic.MyDouble;
import geogebra.common.kernel.geos.GeoList;
import geogebra.common.main.App;
import geogebra.common.util.debug.GeoGebraProfiler;
import geogebra.common.util.debug.Log;
import geogebra.html5.euclidian.EuclidianViewWeb;
import geogebra.html5.gui.inputfield.AutoCompleteTextFieldW;
import geogebra.web.euclidian.event.HasOffsets;
import geogebra.web.euclidian.event.MouseEventW;
import geogebra.web.euclidian.event.TouchEvent;
import geogebra.web.gui.GuiManagerW;
import geogebra.web.gui.tooltip.ToolTipManagerW;
import geogebra.web.main.AppW;

import java.util.LinkedList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.GestureChangeEvent;
import com.google.gwt.event.dom.client.GestureChangeHandler;
import com.google.gwt.event.dom.client.GestureEndEvent;
import com.google.gwt.event.dom.client.GestureEndHandler;
import com.google.gwt.event.dom.client.GestureStartEvent;
import com.google.gwt.event.dom.client.GestureStartHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class EuclidianControllerW extends geogebra.common.euclidian.EuclidianController implements MouseDownHandler, MouseUpHandler, 
MouseMoveHandler, MouseOutHandler, MouseOverHandler, MouseWheelHandler, ClickHandler, DoubleClickHandler, TouchStartHandler, TouchEndHandler, 
TouchMoveHandler, TouchCancelHandler, GestureStartHandler, GestureEndHandler, GestureChangeHandler, HasOffsets {

	private long lastMoveEvent = 0;
	private AbstractEvent waitingTouchMove = null;
	private AbstractEvent waitingMouseMove = null;
	
	public class EnvironmentStyleW extends EnvironmentStyle{
		
		private float widthScale;
		private float heightScale;
		private float scaleX;
		private float scaleY;
		private int xOffset;
		private int yOffset;
		private int scrollLeft;
		private int scrollTop;
		
		
		public EnvironmentStyleW() {
	    }
		/**
		 * @return the widthScale
		 */
        public float getWidthScale() {
	        return widthScale;
        }
		/**
		 * @param widthScale the widthScale to set
		 */
        public void setWidthScale(float widthScale) {
	        this.widthScale = widthScale;
        }
		/**
		 * @return the heightScale
		 */
        public float getHeightScale() {
	        return heightScale;
        }
		/**
		 * @param heightScale the heightScale to set
		 */
        public void setHeightScale(float heightScale) {
	        this.heightScale = heightScale;
        }
		/**
		 * @return the scaleX
		 */
        public float getScaleX() {
	        return scaleX;
        }
		/**
		 * @param scaleX the scaleX to set
		 */
        public void setScaleX(float scaleX) {
	        this.scaleX = scaleX;
        }
		/**
		 * @return the scaleY
		 */
        public float getScaleY() {
	        return scaleY;
        }
		/**
		 * @param scaleY the scaleY to set
		 */
        public void setScaleY(float scaleY) {
	        this.scaleY = scaleY;
        }
		/**
		 * @return the xOffset
		 */
        public int getxOffset() {
	        return xOffset;
        }
		/**
		 * @param xOffset the xOffset to set
		 */
        public void setxOffset(int xOffset) {
	        this.xOffset = xOffset;
        }
		/**
		 * @return the yOffset
		 */
        public int getyOffset() {
	        return yOffset;
        }
		/**
		 * @param yOffset the yOffset to set
		 */
        public void setyOffset(int yOffset) {
	        this.yOffset = yOffset;
        }
		/**
		 * @return the scrollLeft
		 */
        public int getScrollLeft() {
	        return scrollLeft;
        }
		/**
		 * @param scrollLeft the scrollLeft to set
		 */
        public void setScrollLeft(int scrollLeft) {
	        this.scrollLeft = scrollLeft;
        }
		/**
		 * @return the scrollTop
		 */
        public int getScrollTop() {
	        return scrollTop;
        }
		/**
		 * @param scrollTop the scrollTop to set
		 */
        public void setScrollTop(int scrollTop) {
	        this.scrollTop = scrollTop;
        }
        
        @Override
        public String toString() {
        	return 	" ws: " + this.widthScale +
        			", hs: " + this.heightScale +
        			", xo: " + this.xOffset +
        			", yo: " + this.yOffset +
        			", sx: " + this.scaleX +
        			", sy: " + this.scaleY +
         			", scrollL: " + this.scrollLeft +
        			", scrollt: " + this.scrollTop;
        }
        
		/**
		 * @return the multiplier that must be used on native event coordinates
		 */
		public float getScaleXMultiplier() {
	        return (1 / getScaleX());
        }
		
		/**
		 * @return the multiplier that must be used on native event coordinates
		 */
		public float getScaleYMultiplier() {
	        return (1 / getScaleY());
        }
		
	}
	
	public EnvironmentStyleW style; 
	
	
	public EnvironmentStyleW getEnvironmentStyle () {
		return style;
	}
	
	

	/**
	 * recalculates cached styles concerning browser environment
	 */
	@Override
	public void calculateEnvironment() {
	    style = new EnvironmentStyleW();
	    style.setWidthScale(getEnvWidthScale());
	    style.setHeightScale(getEnvHeightScale());
	    style.setxOffset(getEnvXoffset());
	    style.setyOffset(getEnvYoffset());
	    style.setScaleX(((AppW) app).getArticleElement().getScaleX());
	    style.setScaleY(((AppW) app).getArticleElement().getScaleY());
	    style.setScrollLeft(Window.getScrollLeft());
	    style.setScrollTop(Window.getScrollTop());
    }



	private float getEnvWidthScale() {
		EuclidianViewW v  = (EuclidianViewW) view;
		if (v.g2p.getOffsetWidth() != 0) {
			return v.g2p.getCoordinateSpaceWidth() / v.g2p.getOffsetWidth();
		}
		return 0;
	}
	
	private float getEnvHeightScale() {
		EuclidianViewW v = (EuclidianViewW) view;
		if (v.g2p.getOffsetHeight() != 0) {
			return v.g2p.getCoordinateSpaceHeight() / v.g2p.getOffsetHeight();
		}
		return 0;
	}
	
	private int getEnvXoffset(){
		//return EuclidianViewXOffset;
		//the former solution doesn't update on scrolling
		return Math.round((((EuclidianViewW) view).getAbsoluteLeft() - Window.getScrollLeft()));
				
	}
	
	
	//private int EuclidianViewXOffset;
	
	//private int EuclidianViewYOffset;
	/**
	 * @return offset to get correct getY() in mouseEvents
	 */
	private int getEnvYoffset(){
		//return EuclidianViewYOffset;
		//the former solution doesn't update on scrolling
		return ((EuclidianViewW) view).getAbsoluteTop() - Window.getScrollTop();
	}
	
	
	

	private boolean EuclidianOffsetsInited = false;
	
	public boolean isOffsetsUpToDate(){
		return EuclidianOffsetsInited;
	}
	
	public void updateOffsets(){
		//EuclidianViewXOffset = ((EuclidianViewW) view).getAbsoluteLeft() + Window.getScrollLeft();
		//EuclidianViewYOffset = ((EuclidianViewW) view).getAbsoluteTop() + Window.getScrollTop();	
	}
	
	private Timer repaintTimer = new Timer() {
		@Override
		public void run() {
			moveIfWaiting();
		}
	};
	
	protected void moveIfWaiting(){
		long time = System.currentTimeMillis();
		if(this.waitingMouseMove != null){
			GeoGebraProfiler.moveEventsIgnored--;
			this.onMouseMoveNow(waitingMouseMove, time);	
			return;
		}
		if(this.waitingTouchMove != null){
			GeoGebraProfiler.moveEventsIgnored--;
			this.onTouchMoveNow(waitingTouchMove, time);
		}
		
	}
	
	public EuclidianControllerW(Kernel kernel) {
		super(kernel.getApplication());
		setKernel(kernel);
		Window.addResizeHandler(new ResizeHandler() {
			
			public void onResize(ResizeEvent event) {
				calculateEnvironment();
			}
		});
		tempNum = new MyDouble(kernel);
	}
	
	public  void setView(EuclidianView view) {
		this.view = view;
	}

	public void onGestureChange(GestureChangeEvent event) {
		 //AbstractEvent e = geogebra.web.euclidian.event.TouchEvent.wrapEvent(event.getNativeEvent());
		//to not move the canvas (later some sophisticated handling must be find out)
				//event.preventDefault();
				//event.stopPropagation();
	}

	public void onGestureEnd(GestureEndEvent event) {
		 //AbstractEvent e = geogebra.web.euclidian.event.TouchEvent.wrapEvent(event.getNativeEvent());
		//to not move the canvas (later some sophisticated handling must be find out)
				//event.preventDefault();
				//event.stopPropagation();
	}

	public void onGestureStart(GestureStartEvent event) {
		 //AbstractEvent e = geogebra.web.euclidian.event.TouchEvent.wrapEvent(event.getNativeEvent());
		//to not move the canvas (later some sophisticated handling must be find out)
				//event.preventDefault();
				//event.stopPropagation();
	}

	public void onTouchCancel(TouchCancelEvent event) {
		 //AbstractEvent e = geogebra.web.euclidian.event.TouchEvent.wrapEvent(event.getNativeEvent());
		 Log.debug(event.getAssociatedType().getName());
	}

	public void onTouchMove(TouchMoveEvent event) {
		GeoGebraProfiler.drags++;
		long time = System.currentTimeMillis();
		JsArray<Touch> targets = event.getTargetTouches();
		for (int i = 0; i < targets.length(); i++) {
			 if (targets.length() == 1) {
				 event.stopPropagation();
				 event.preventDefault();
			 }
		}
		if(time < this.lastMoveEvent + EuclidianViewWeb.DELAY_BETWEEN_MOVE_EVENTS){
			AbstractEvent e = geogebra.web.euclidian.event.TouchEvent.wrapEvent(targets.get(targets.length()-1),this);
			boolean wasWaiting = waitingTouchMove != null || waitingMouseMove !=null;
			this.waitingTouchMove = e;
			this.waitingMouseMove = null;
			GeoGebraProfiler.moveEventsIgnored++;
			if(wasWaiting){
				this.repaintTimer.schedule(EuclidianViewWeb.DELAY_UNTIL_MOVE_FINISH);
			}
			return;
		}
		AbstractEvent e = geogebra.web.euclidian.event.TouchEvent.wrapEvent(targets.get(targets.length()-1),this);
		onTouchMoveNow(e, time);
	}

	private void onTouchMoveNow(AbstractEvent event,long time) {
		this.lastMoveEvent = time;
		wrapMouseDragged(event);
		
	    this.waitingTouchMove = null;
	    this.waitingMouseMove = null;
	    int dragTime = (int) (System.currentTimeMillis()-time);
	    GeoGebraProfiler.dragTime += dragTime;
	    if(dragTime > EuclidianViewWeb.DELAY_UNTIL_MOVE_FINISH){
	    	EuclidianViewWeb.DELAY_UNTIL_MOVE_FINISH = dragTime + 10;
	    }
	    
    }

	public void onTouchEnd(TouchEndEvent event) {
		this.moveIfWaiting();
		EuclidianViewWeb.resetDelay();
		JsArray<Touch> targets = event.getTargetTouches();
		for (int i = 0; i < targets.length(); i++) {
			 AbstractEvent e = geogebra.web.euclidian.event.TouchEvent.wrapEvent(targets.get(i),this);
			 e.release();
			 if (targets.length() == 1) {
				 event.stopPropagation();
				 event.preventDefault();
			 }
			 //should be substracted the event just ended, and call mouseevent for that.
			 //later :-)
		}		
	}

	public void onTouchStart(TouchStartEvent event) {
		JsArray<Touch> targets = event.getTargetTouches();
		for (int i = 0; i < targets.length(); i++) {
			AbstractEvent e = geogebra.web.euclidian.event.TouchEvent.wrapEvent(targets.get(i),this);
			wrapMousePressed(e);
			e.release();
			 if (targets.length() == 1) {
				 event.stopPropagation();
				 event.preventDefault();
			 }
			
		}
	}
	
	private static boolean DRAGMODE_MUST_BE_SELECTED = false;

	public void onDoubleClick(DoubleClickEvent event) {

		if (app.getGuiManager() != null)
			((GuiManagerW)app.getGuiManager()).setActiveToolbarId(App.VIEW_EUCLIDIAN);

		 AbstractEvent e = geogebra.web.euclidian.event.MouseEventW.wrapEvent(event.getNativeEvent(),this);
		 wrapMouseclicked(e);
		 e.release();
	}

	public void onClick(ClickEvent event) {

		if (app.getGuiManager() != null)
			((GuiManagerW)app.getGuiManager()).setActiveToolbarId(App.VIEW_EUCLIDIAN);

		 AbstractEvent e = geogebra.web.euclidian.event.MouseEventW.wrapEvent(event.getNativeEvent(),this);
		 wrapMouseclicked(e);
		 e.release();
	}

	public void onMouseWheel(MouseWheelEvent event) {
		//don't want to roll the scrollbar
		 event.preventDefault();
		 AbstractEvent e = geogebra.web.euclidian.event.MouseEventW.wrapEvent(event.getNativeEvent(),event.getDeltaY(),this);
		 wrapMouseWheelMoved(e);
		 e.release();
	}

	public void onMouseOver(MouseOverEvent event) {
		 wrapMouseEntered();
	}

	public void onMouseOut(MouseOutEvent event) {
		// hide dialogs if they are open
		int x = event.getClientX();
		int y = event.getClientY();
		int ex = ((EuclidianViewW) view).getAbsoluteLeft();
		int ey = ((EuclidianViewW) view).getAbsoluteTop();
		int eWidth = ((EuclidianViewW) view).getWidth();
		int eHeight = ((EuclidianViewW) view).getHeight();
		if ((x < ex || x > ex + eWidth) || (y < ey || y > ey + eHeight)) {
			ToolTipManagerW.sharedInstance().hideToolTip();
		}

		AbstractEvent e = geogebra.web.euclidian.event.MouseEventW.wrapEvent(event.getNativeEvent(),this);
		wrapMouseExited(e);
		e.release();
	}


	public void onMouseMove(MouseMoveEvent event) {
		GeoGebraProfiler.drags++;
		long time = System.currentTimeMillis();
		event.preventDefault();
		AbstractEvent e = geogebra.web.euclidian.event.MouseEventW.wrapEvent(event.getNativeEvent(),this);
		if(time < this.lastMoveEvent + EuclidianViewWeb.DELAY_BETWEEN_MOVE_EVENTS){
			boolean wasWaiting = waitingTouchMove != null || waitingMouseMove !=null;
			this.waitingMouseMove = e;
			this.waitingTouchMove = null;
			GeoGebraProfiler.moveEventsIgnored++;
			if(wasWaiting){
				this.repaintTimer.schedule(EuclidianViewWeb.DELAY_UNTIL_MOVE_FINISH);
			}
			return;
		}
		
		onMouseMoveNow(e,time);
	}
	
	public void onMouseMoveNow(AbstractEvent event,long time) {
		this.lastMoveEvent = time;
		 if (!DRAGMODE_MUST_BE_SELECTED) {
			 wrapMouseMoved(event);
		 } else {
			 wrapMouseDragged(event);
		 }
		 event.release();
		 this.waitingMouseMove = null;
		 this.waitingTouchMove = null;
		 int dragTime = (int) (System.currentTimeMillis()-time);
		GeoGebraProfiler.dragTime += dragTime;
		if(dragTime > EuclidianViewWeb.DELAY_UNTIL_MOVE_FINISH){
			EuclidianViewWeb.DELAY_UNTIL_MOVE_FINISH = dragTime + 10;
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		this.moveIfWaiting();
		EuclidianViewWeb.resetDelay();
		DRAGMODE_MUST_BE_SELECTED = false;
		event.preventDefault();	

		//hide dialogs if they are open
		if (app.getGuiManager() != null)
			((GuiManagerW)app.getGuiManager()).removePopup();

		AbstractEvent e = geogebra.web.euclidian.event.MouseEventW.wrapEvent(event.getNativeEvent(),this);
		wrapMouseReleased(e);
		e.release();
	}

	public void onMouseDown(MouseDownEvent event) {

		if (app.getGuiManager() != null)
			((GuiManagerW)app.getGuiManager()).setActiveToolbarId(App.VIEW_EUCLIDIAN);

		if ((!AutoCompleteTextFieldW.showSymbolButtonFocused)&&(!isTextfieldHasFocus())){
			DRAGMODE_MUST_BE_SELECTED = true;
		}
			
		if((!isTextfieldHasFocus())&&(!(view.getHits().size()>0))){
			
			//Call preventDefault only if no GeoList object clicked.
			boolean noGeolist = true;
			int i=0;
			while (noGeolist && i<view.getHits().size()){
				if (view.getHits().get(i++) instanceof GeoList) noGeolist = false;
			}			
			if (noGeolist){
				event.preventDefault();
			}
		}
			
		AbstractEvent e = geogebra.web.euclidian.event.MouseEventW.wrapEvent(event.getNativeEvent(),this);
		wrapMousePressed(e);
		//hide PopUp if no hits was found.
		if (view.getHits().isEmpty()) {
			if (EuclidianStyleBarW.CURRENT_POP_UP != null) {
				EuclidianStyleBarW.CURRENT_POP_UP.hide();
			}
		}
		e.release();
	}
	
	@Override
	protected void initToolTipManager() {
		// set tooltip manager
		ToolTipManagerW ttm = ToolTipManagerW.sharedInstance();
		//ttm.setInitialDelay(defaultInitialDelay / 2);
		//ttm.setEnabled((AppW.getAllowToolTips());
		
	}



	@Override
	protected void resetToolTipManager() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean hitResetIcon() {
		return app.showResetIcon()
				&& ((mouseLoc.y < 20) && (mouseLoc.x > (view.getViewWidth() - 18)));
	}
	private LinkedList<MouseEventW> mousePool = new LinkedList<MouseEventW>();
	public LinkedList<MouseEventW> getMouseEventPool() {
	    return mousePool;
    }
	private LinkedList<TouchEvent> touchPool = new LinkedList<TouchEvent>();
	public LinkedList<TouchEvent> getTouchEventPool() {
	    return touchPool;
    }

	@Override
	protected boolean textfieldJustFocusedW(GPoint p) {
		return view.textfieldClicked(p);
	}

	public int getXoffset() {
	   return style.getxOffset();
    }

	public int getYoffset() {
	    return style.getyOffset();
    }

	public float getWidthScale() {
	   return style.getWidthScale();
    }

	public float getHeightScale() {
	  return style.getHeightScale();
    }

	public float getScaleX() {
	  return style.getScaleX();
    }

	public float getScaleY() {
	   return style.getScaleY();
    }



	/**
	 * @return the multiplier that must be used to multiply the native event coordinates
	 */
	public float getScaleXMultiplier() {
	    return style.getScaleXMultiplier();
    }
	
	/**
	 * @return the multiplier that must be used to multiply the native event coordinates
	 */
	public float getScaleYMultiplier() {
		return style.getScaleYMultiplier();
	}

}

