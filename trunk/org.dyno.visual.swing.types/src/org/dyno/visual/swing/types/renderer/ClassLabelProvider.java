package org.dyno.visual.swing.types.renderer;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ClassLabelProvider extends LabelProvider {

	public ClassLabelProvider() {
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		String name=element.getClass().getName();
		int dot = name.lastIndexOf(".");
		if(dot!=-1)
			name=name.substring(dot+1);
		return name;
	}
}
