/*
 * ColorCellRenderer.java
 *
 * Created on 2007年8月13日, 下午6:53
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
public class ColorRenderer implements ILabelProviderFactory {
	private ColorLabelProvider provider;
	@Override
	public ILabelProvider getLabelProvider() {
		if(provider==null)
			provider = new ColorLabelProvider();
		return provider;
	}
}