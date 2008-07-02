/*
 * InsetsRenderer.java
 * 
 * Created on 2007-8-27, 16:25:14
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.types.renderer;

import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * 
 * @author William Chen
 */
public class CharRenderer implements ILabelProviderFactory {
	private CharLabelProvider provider;
	@Override
	public ILabelProvider getLabelProvider() {
		if(provider==null)
			provider = new CharLabelProvider();
		return provider;
	}
}