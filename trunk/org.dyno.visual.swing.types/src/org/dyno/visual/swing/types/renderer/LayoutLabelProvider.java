package org.dyno.visual.swing.types.renderer;

import java.awt.BorderLayout;

import org.eclipse.jface.viewers.LabelProvider;

public class LayoutLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if(element == null)
			return "null";
		if(element instanceof BorderLayout)
			return "BorderLayout";
		return "null";
	}
}
