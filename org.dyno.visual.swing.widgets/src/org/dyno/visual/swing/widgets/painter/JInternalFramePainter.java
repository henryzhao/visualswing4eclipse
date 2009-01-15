package org.dyno.visual.swing.widgets.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.design.JInternalFrameDesignOperation;

public class JInternalFramePainter extends RootPaneContainerPainter {

	private Rectangle getContentBounds() {
		Component panel = adaptable.getContentPane();
		int w = panel.getWidth();
		int h = panel.getHeight();
		Rectangle bounds = new Rectangle(0, 0, w, h);
		return SwingUtilities.convertRectangle(panel, bounds, adaptable.getWidget());
	}
	
	protected boolean isDroppingMenuBar(){
		List<WidgetAdapter> target = adaptable.getDropWidget();
		if(target==null)
			return false;
		if(target.size()!=1)
			return false;
		Component drop =  target.get(0).getWidget();
		return drop instanceof JMenuBar;
	}
	@Override
	public void paintHovered(Graphics g) {
		 if (isDroppingMenuBar()) {
			JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
			if (jif.getJMenuBar() == null) {
				Rectangle rect = getContentBounds();
				Graphics clipg = g.create(rect.x, rect.y, rect.width,
						rect.height);
				clipg.setColor(Color.GREEN);
				int h = adaptable.getDropWidget().get(0).getWidget().getHeight();
				clipg.drawRect(0, 0, rect.width, h);
				clipg.dispose();
			} 
		}
	}
	protected boolean isDroppingMenuItem(){
		List<WidgetAdapter> target = adaptable.getDropWidget();
		if(target==null)
			return false;
		if(target.size()!=1)
			return false;
		Component drop = target.get(0).getWidget();
		return drop instanceof JMenuItem;
	}
	public IPainter getContentPainter(){
		WidgetAdapter contentAdapter=((RootPaneContainerAdapter)adaptable).getContentAdapter();
		return (IPainter) contentAdapter.getAdapter(IPainter.class);
	}	
	@Override
	public void paintHint(Graphics g) {
		if (!isDroppingMenuBar() && !isDroppingMenuItem()) {
			JInternalFrameDesignOperation operation=(JInternalFrameDesignOperation) adaptable.getAdapter(IDesignOperation.class);
			if (operation.isInContent()) {
				Rectangle rect = getContentBounds();
				Graphics clipg = g.create(rect.x, rect.y, rect.width,
						rect.height);
				getContentPainter().paintHint(clipg);
				clipg.dispose();
			}
		}else if(isDroppingMenuItem()){
			paintForbiddenMascot(g);
		}else if(isDroppingMenuBar()){
			JInternalFrame jif = (JInternalFrame) adaptable.getWidget();
			if (jif.getJMenuBar() != null) {
				paintForbiddenMascot(g);
			}
		}
	}
	public void paintGrid(Graphics clipg) {
		Rectangle bounds = getContentBounds();
		clipg = clipg.create(bounds.x, bounds.y, bounds.width, bounds.height);
		getContentPainter().paintGrid(clipg);
		clipg.dispose();
	}	
	public void paintAnchor(Graphics g) {
		Rectangle bounds = getContentBounds();
		g = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
		getContentPainter().paintAnchor(g);
		g.dispose();
	}		
}
