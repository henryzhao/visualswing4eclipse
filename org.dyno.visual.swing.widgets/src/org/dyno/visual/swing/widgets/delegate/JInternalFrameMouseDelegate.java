package org.dyno.visual.swing.widgets.delegate;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.widgets.JInternalFrameAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class JInternalFrameMouseDelegate extends MouseAdapter implements IAdaptableContext{
	private JInternalFrameAdapter adapter;
	@Override
	public void mousePressed(MouseEvent e) {
		JInternalFrame jif = (JInternalFrame) adapter.getWidget();
		jif.toFront();
	}
	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adapter=(JInternalFrameAdapter) adaptable;
	}
}