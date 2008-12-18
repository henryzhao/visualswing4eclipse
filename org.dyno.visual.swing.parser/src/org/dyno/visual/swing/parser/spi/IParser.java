package org.dyno.visual.swing.parser.spi;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public interface IParser {
	boolean generateCode(IType type, ImportRewrite imports, IProgressMonitor monitor);
}
