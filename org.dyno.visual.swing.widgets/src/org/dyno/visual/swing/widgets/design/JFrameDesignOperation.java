package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.painter.JFramePainter;

public class JFrameDesignOperation extends RootPaneContainerDesignOperation {
	private IDesignOperation getContentOperation(){
		WidgetAdapter contentAdapter = ((RootPaneContainerAdapter)adaptable).getContentAdapter();
		return (IDesignOperation) contentAdapter.getAdapter(IDesignOperation.class);
	}
	private void setDropStatus(int dropStatus){
		JFramePainter jap=(JFramePainter) adaptable.getAdapter(IPainter.class);
		jap.setDropStatus(dropStatus);
	}	
	@Override
	public boolean dragOver(Point p) {
		if (isDroppingForbbiden()) {
			if (hasMenuBar())
				p.y += getJMenuBarHeight();
			adaptable.setMascotLocation(p);
			setDropStatus(JFramePainter.DROPPING_FORBIDDEN);
			return true;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JFramePainter.DROPPING_PERMITTED);
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
			setDropStatus(JFramePainter.DROPPING_FORBIDDEN);
			return true;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JFramePainter.DROPPING_PERMITTED);
			return true;
		} else
			return getContentOperation().dragEnter(p);
	}

	private int getJMenuBarHeight() {
		JFrame jframe = (JFrame) adaptable.getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		return jmb.getHeight();
	}

	private boolean isDroppingForbbiden() {
		return isDroppingMenu() || isDroppingMenuBar() && hasMenuBar();
	}

	@Override
	public boolean dragExit(Point p) {
		if (isDroppingForbbiden()) {
			if (hasMenuBar())
				p.y += getJMenuBarHeight();
			adaptable.setMascotLocation(p);
			setDropStatus(JFramePainter.NOOP);
			return true;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			setDropStatus(JFramePainter.NOOP);
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
			setDropStatus(JFramePainter.NOOP);
			Toolkit.getDefaultToolkit().beep();
			return true;
		} else if (isDroppingMenuBar()) {
			adaptable.setMascotLocation(p);
			WidgetAdapter target = adaptable.getDropWidget().get(0);
			JMenuBar jmb = (JMenuBar) target.getWidget();
			JFrame jframe = (JFrame) adaptable.getWidget();
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
			setDropStatus(JFramePainter.NOOP);
			return true;
		} else
			return getContentOperation().drop(p);
	}

	private boolean isDroppingMenu() {
		List<WidgetAdapter> targets = adaptable.getDropWidget();
		if(targets.size()!=1)
			return false;
		Component drop = targets.get(0).getWidget();
		return drop != null
				&& (drop instanceof JMenu || drop instanceof JMenuItem || drop instanceof JPopupMenu);
	}

	private boolean hasMenuBar() {
		JFrame jframe = (JFrame) adaptable.getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		return jmb != null;
	}

}
