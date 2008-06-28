package org.dyno.visual.swing.types.editor;

import java.net.URL;

import javax.swing.ImageIcon;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.plugin.spi.WhiteBoard;
import org.dyno.visual.swing.types.endec.IconWrapper;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class IconEditor implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		CellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new IconCellEditorValidator());
		return editor;
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			return null;
		if (value.equals("null"))
			return null;
		String string = (String) value;
		return new ResourceIcon(string);
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return "null";
		if (value instanceof ImageIcon) {
			ImageIcon imageIcon = (ImageIcon) value;
			String filename = IconWrapper.getImageIconFilename(imageIcon);
			if (filename != null) {
				return filename;
			} else {
				URL location = IconWrapper.getImageIconLocation(imageIcon);
				if (location != null) {
					IProject prj = WhiteBoard.getCurrentProject().getProject();
					String root = prj.getWorkspace().getRoot().getRawLocation().toString();
					String path = location.getFile().toString();
					path = path.substring(1);
					path = path.substring(root.length());
					Path ipath = new Path(path);
					IPath rel = ipath.removeFirstSegments(2);
					return "/" + rel.toString();
				} else
					return "null";
			}
		} else
			return value.toString();
	}
}
