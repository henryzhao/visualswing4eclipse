package org.dyno.visual.swing.plugin.spi;

import java.util.Comparator;

public class TypeAdapter {
	private ICodeGen endec;
	@SuppressWarnings("unchecked")
	private Comparator comparator;
	private ILabelProviderFactory renderer;
	private ICellEditorFactory editor;
	private ICloner cloner;

	public ICodeGen getEndec() {
		if (endec != null)
			return endec;
		else
			return editor;
	}

	public void setEndec(ICodeGen endec) {
		this.endec = endec;
	}

	@SuppressWarnings("unchecked")
	public Comparator getComparator() {
		return comparator;
	}

	@SuppressWarnings("unchecked")
	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public ILabelProviderFactory getRenderer() {
		return renderer;
	}

	public void setRenderer(ILabelProviderFactory renderer) {
		this.renderer = renderer;
	}

	public ICellEditorFactory getEditor() {
		return editor;
	}

	public void setEditor(ICellEditorFactory editor) {
		this.editor = editor;
	}

	public void setCloner(ICloner cloner) {
		this.cloner = cloner;
	}

	public ICloner getCloner() {
		return cloner;
	}
}
