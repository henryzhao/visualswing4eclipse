/*
 * FontCellRenderer.java
 *
 * Created on 2007年8月13日, 下午7:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.dyno.visual.swing.types.renderer;

import java.awt.Dimension;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * 
 * @author William Chen
 */
public class DimensionLabelProvider extends LabelProvider {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		if (element instanceof Dimension) {
			Dimension dim = (Dimension) element;
			return "("+dim.width+", "+dim.height+")";
		}
		return element.toString();
	}
}
