/*
 * PointCellRenderer.java
 *
 * Created on August 14, 2007, 6:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.dyno.visual.swing.types.renderer;

import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * 
 * @author William Chen
 */
public class IconRenderer implements ILabelProviderFactory {
	private IconLabelProvider provider;
	@Override
	public ILabelProvider getLabelProvider() {
		if(provider==null)
			provider = new IconLabelProvider();
		return provider;
	}
}