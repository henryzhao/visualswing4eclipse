package org.dyno.visual.swing.plugin.spi;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public interface IEventMethod {

	String getDisplayName();

	void editCode();

	String createEventMethod(IType type, ImportRewrite imports);

	String createAddListenerCode();
	

}
