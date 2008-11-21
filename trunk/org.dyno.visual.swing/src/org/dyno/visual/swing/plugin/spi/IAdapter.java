package org.dyno.visual.swing.plugin.spi;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.swt.graphics.Image;

public interface IAdapter {
	Image getIconImage();
	String getName();
	boolean generateCode(IType type, ImportRewrite imports, IProgressMonitor monitor);
	String getCreationMethodName();
}
