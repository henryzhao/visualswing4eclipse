package org.dyno.visual.swing.widgets.design;

import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.painter.JInternalFramePainter;

public class JInternalFrameDesignOperation extends
		RootPaneContainerDesignOperation {
	private IDesignOperation getContentOperation(){
		WidgetAdapter contentAdapter = ((RootPaneContainerAdapter)adaptable).getContentAdapter();
		return (IDesignOperation) contentAdapter.getAdapter(IDesignOperation.class);
	}
	private void setDropStatus(int dropStatus){
		JInternalFramePainter jap=(JInternalFramePainter) adaptable.getAdapter(IPainter.class);
		jap.setDropStatus(dropStatus);
	}	
	@Override
	public boolean dragOver(Point p) {
		if (isDroppingForbbiden()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JInternalFramePainter.DROPPING_FORBIDDEN);
			return true;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JInternalFramePainter.DROPPING_PERMITTED);
			return true;
		} else{
			if(hasMenuBar()){
				JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
				JRootPane rootPane = jif.getRootPane(); 
				p=SwingUtilities.convertPoint(rootPane, p, adaptable.getContentPane());
			}
			return getContentOperation().dragOver(p);
		}
	}

	@Override
	public boolean dragEnter(Point p) {
		if (isDroppingForbbiden()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JInternalFramePainter.DROPPING_FORBIDDEN);
			return true;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JInternalFramePainter.DROPPING_PERMITTED);
			return true;
		} else{
			if(hasMenuBar()){
				JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
				JRootPane rootPane = jif.getRootPane(); 
				p=SwingUtilities.convertPoint(rootPane, p, adaptable.getContentPane());
			}
			return getContentOperation().dragEnter(p);
		}
	}

	private boolean isDroppingForbbiden() {
		return isDroppingMenu()||isDroppingMenuItem()||isDroppingPopup() || isDroppingMenuBar() && hasMenuBar();
	}

	@Override
	public boolean dragExit(Point p) {
		if (isDroppingForbbiden()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JInternalFramePainter.NOOP);
			return true;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JInternalFramePainter.NOOP);
			return true;
		} else{
			if(hasMenuBar()){
				JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
				JRootPane rootPane = jif.getRootPane(); 
				p=SwingUtilities.convertPoint(rootPane, p, adaptable.getContentPane());
			}
			return getContentOperation().dragExit(p);
		}
	}

	@Override
	public boolean drop(Point p) {
		if (isDroppingForbbiden()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JInternalFramePainter.NOOP);
			Toolkit.getDefaultToolkit().beep();
			return false;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			WidgetAdapter target = adaptable.getDropWidget().get(0);
			JMenuBar jmb = (JMenuBar) target.getWidget();
			JInternalFrame jframe = (JInternalFrame) adaptable.getWidget();
			jframe.setJMenuBar(jmb);
			target.requestNewName();
			jframe.validate();
			adaptable.doLayout();
			adaptable.validateContent();
			adaptable.clearAllSelected();
			target.setSelected(true);
			adaptable.setDirty(true);
			adaptable.addNotify();
			adaptable.repaintDesigner();
			setDropStatus(JInternalFramePainter.NOOP);
			return true;
		} else{
			if(hasMenuBar()){
				JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
				JRootPane rootPane = jif.getRootPane(); 
				p=SwingUtilities.convertPoint(rootPane, p, adaptable.getContentPane());
			}
			return getContentOperation().drop(p);
		}
	}

	private boolean hasMenuBar() {
		JInternalFrame jframe = (JInternalFrame) adaptable.getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		return jmb != null;
	}
}
