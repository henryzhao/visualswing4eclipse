package org.dyno.visual.swing.widgets.delegate;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.eclipse.core.runtime.IAdaptable;

public class JMenuItemMouseDelegate extends MouseInputAdapter implements IAdaptableContext{
	@Override
	public void mousePressed(MouseEvent e) {
		if(e!=null){
			e.setSource(null);
		}
	}
	@Override
	public void setAdaptable(IAdaptable adaptable) {
	}
}
