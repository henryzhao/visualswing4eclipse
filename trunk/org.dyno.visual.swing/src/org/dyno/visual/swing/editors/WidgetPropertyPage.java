package org.dyno.visual.swing.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class WidgetPropertyPage extends PropertySheetPage {
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		final Tree tree = (Tree) getControl();
		tree.addListener(SWT.MeasureItem, new org.eclipse.swt.widgets.Listener(){
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				event.height = 20;
			}
		});
	}
}
