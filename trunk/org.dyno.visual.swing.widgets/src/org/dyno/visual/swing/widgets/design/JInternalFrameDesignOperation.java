package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JInternalFrameDesignOperation extends
		RootPaneContainerDesignOperation {
	private boolean inContent;
	public boolean isInContent(){
		return inContent;
	}
	@Override
	public boolean dragEnter(Point p) {
		if (!isDroppingMenuBar() && !isDroppingMenuItem() && isInContentPane(p)) {
			inContent = true;
			Point cp = SwingUtilities.convertPoint(adaptable.getWidget(), p, adaptable.getContentPane());
			return getContentOperation().dragEnter(cp);
		}
		adaptable.setMascotLocation(p);
		return true;
	}


	@Override
	public boolean drop(Point p) {
		if (!isDroppingMenuBar() && !isDroppingMenuItem()) {
		inContent = false;
			if (isInContentPane(p)) {
				Point cp = SwingUtilities.convertPoint(adaptable.getWidget(), p,
						adaptable.getContentPane());
				return getContentOperation().drop(cp);
			} else {
				Toolkit.getDefaultToolkit().beep();
				return true;
			}
		}else if (isDroppingMenuBar()) {
			JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
			if(jif.getJMenuBar()==null){
				WidgetAdapter jmenuBarAdapter=adaptable.getDropWidget().get(0);
				JMenuBar jmb=(JMenuBar)jmenuBarAdapter.getWidget();
				jif.setJMenuBar(jmb);
				jmenuBarAdapter.requestNewName();
				adaptable.clearAllSelected();
				jmenuBarAdapter.setSelected(true);
				jmenuBarAdapter.addNotify();
				adaptable.setDirty(true);
			}else{
				Toolkit.getDefaultToolkit().beep();
				return false;
			}
		}
		adaptable.setMascotLocation(p);
		return true;
	}
	private boolean isInContentPane(Point p) {
		return getContentBounds().contains(p);
	}

	private Rectangle getContentBounds() {
		Component panel = adaptable.getContentPane();
		int w = panel.getWidth();
		int h = panel.getHeight();
		Rectangle bounds = new Rectangle(0, 0, w, h);
		return SwingUtilities.convertRectangle(panel, bounds, adaptable.getWidget());
	}

	@Override
	public boolean dragExit(Point p) {
		if (inContent) {
			inContent = false;
			Point cp = SwingUtilities.convertPoint(adaptable.getWidget(), p,
					adaptable.getContentPane());
			return getContentOperation().dragExit(cp);
		} else{
			adaptable.setMascotLocation(p);
			return true;
		}
	}
	private IDesignOperation getContentOperation(){
		WidgetAdapter contentAdapter = ((RootPaneContainerAdapter)adaptable).getContentAdapter();
		return (IDesignOperation) contentAdapter.getAdapter(IDesignOperation.class);
	}
	@Override
	public boolean dragOver(Point p) {
		if (!isDroppingMenuBar() && !isDroppingMenuItem()) {
			if (isInContentPane(p)) {
				if (!inContent) {
					inContent = true;
					Point cp = SwingUtilities.convertPoint(adaptable.getWidget(), p,
							adaptable.getContentPane());
					return getContentOperation().dragEnter(cp);
				} else {
					Point cp = SwingUtilities.convertPoint(adaptable.getWidget(), p,
							adaptable.getContentPane());
					return getContentOperation().dragOver(cp);
				}
			} else {
				if (inContent) {
					inContent = false;
					Point cp = SwingUtilities.convertPoint(adaptable.getWidget(), p,
							adaptable.getContentPane());
					return getContentOperation().dragExit(cp);
				} else {
					adaptable.setMascotLocation(p);
					return true;
				}
			}
		}
		adaptable.setMascotLocation(p);
		return true;
	}

}
