package org.dyno.visual.swing.types.editor;

import javax.swing.ListModel;

interface ListModelAdapter {
	ListModel newModel();
	void addElement(ListModel model, Object element);
}
