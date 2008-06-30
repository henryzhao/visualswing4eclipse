/*
 * FontCellRenderer.java
 *
 * Created on 2007年8月13日, 下午7:26
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
public class FontRenderer implements ILabelProviderFactory {
	private static final long serialVersionUID = -4403435758517308113L;
	private FontLabelProvider provider;
	@Override
	public ILabelProvider getLabelProvider() {
		if(provider==null){
			provider = new FontLabelProvider();
		}
		return provider;
	}
}
