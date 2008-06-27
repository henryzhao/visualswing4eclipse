package org.dyno.visual.swing.editors;

import java.util.List;

import org.dyno.visual.swing.designer.VisualDesigner;

public class ComponentTreeInput {
	private VisualDesigner root;
	private List<?> invisibles;

	public ComponentTreeInput(VisualDesigner root) {
		assert root != null;
		this.root = root;
	}

	public ComponentTreeInput(VisualDesigner root, List<?> invisibles) {
		this.root = root;
		this.invisibles = invisibles;
	}

	public VisualDesigner getDesigner() {
		return root;
	}

	public List<?> getInvisibles() {
		return invisibles;
	}
}
