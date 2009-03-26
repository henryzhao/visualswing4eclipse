package org.dyno.visual.swing.widgets.design;

import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JMenuBar;

import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.painter.JDialogPainter;

public class JDialogDesignOperation extends RootPaneContainerDesignOperation {
	private IDesignOperation getContentOperation(){
		WidgetAdapter contentAdapter = ((RootPaneContainerAdapter)adaptable).getContentAdapter();
		return (IDesignOperation) contentAdapter.getAdapter(IDesignOperation.class);
	}
	private void setDropStatus(int dropStatus){
		JDialogPainter jap=(JDialogPainter) adaptable.getAdapter(IPainter.class);
		jap.setDropStatus(dropStatus);
	}
	
	@Override
	public boolean dragOver(Point p) {
		if (isDroppingForbbiden()) {
			if (hasMenuBar())
				p.y += getJMenuBarHeight();
			adaptable.setMascotLocation(p);
			setDropStatus(JDialogPainter.DROPPING_FORBIDDEN);
			return true;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JDialogPainter.DROPPING_PERMITTED);
			return true;
		} else
			return getContentOperation().dragOver(p);
	}

	@Override
	public boolean dragEnter(Point p) {
		if (isDroppingForbbiden()) {
			if (hasMenuBar())
				p.y += getJMenuBarHeight();
			adaptable.setMascotLocation(p);
			setDropStatus(JDialogPainter.DROPPING_FORBIDDEN);
			return true;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JDialogPainter.DROPPING_PERMITTED);
			return true;
		} else
			return getContentOperation().dragEnter(p);
	}

	private int getJMenuBarHeight() {
		JDialog jdialog = (JDialog) adaptable.getWidget();
		JMenuBar jmb = jdialog.getJMenuBar();
		return jmb.getHeight();
	}

	private boolean isDroppingForbbiden() {
		return isDroppingMenu()||isDroppingMenuItem()||isDroppingPopup() || isDroppingMenuBar() && hasMenuBar();
	}

	@Override
	public boolean dragExit(Point p) {
		if (isDroppingForbbiden()) {
			if (hasMenuBar())
				p.y += getJMenuBarHeight();
			adaptable.setMascotLocation(p);
			setDropStatus(JDialogPainter.NOOP);
			return true;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JDialogPainter.NOOP);
			return true;
		} else
			return getContentOperation().dragExit(p);
	}

	@Override
	public boolean drop(Point p) {
		if (isDroppingForbbiden()) {
			if (hasMenuBar())
				p.y += getJMenuBarHeight();
			adaptable.setMascotLocation(p);
			setDropStatus(JDialogPainter.NOOP);
			Toolkit.getDefaultToolkit().beep();
			return false;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			WidgetAdapter target = adaptable.getDropWidget().get(0);
			JMenuBar jmb = (JMenuBar) target.getWidget();
			JDialog jdialog = (JDialog) adaptable.getWidget();
			jdialog.setJMenuBar(jmb);
			target.requestNewName();
			jdialog.validate();
			adaptable.doLayout();
			adaptable.validateContent();
			adaptable.clearAllSelected();
			target.setSelected(true);
			adaptable.setDirty(true);
			adaptable.addNotify();
			adaptable.repaintDesigner();
			setDropStatus(JDialogPainter.NOOP);
			return true;
		} else
			return getContentOperation().drop(p);
	}


	private boolean hasMenuBar() {
		JDialog jdialog = (JDialog) adaptable.getWidget();
		JMenuBar jmb = jdialog.getJMenuBar();
		return jmb != null;
	}
	
}
