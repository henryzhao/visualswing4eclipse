package org.dyno.visual.swing.plugin.spi;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.ui.IEditorPart;

public interface IEventMethod {
	String getDisplayName();
	void editCode(IEditorPart editor);
	String createEventMethod(IType type, ImportRewrite imports);
	String createAddListenerCode();
}
