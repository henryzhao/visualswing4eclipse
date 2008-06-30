package org.dyno.visual.swing.plugin.spi;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public interface ISourceParser {
	void setSource(ICompilationUnit source);
	
	void setImportWrite(ImportRewrite imports);
	
	boolean parse();

	WidgetAdapter getResult();

	void setRootAdapter(WidgetAdapter root);
	
	void setLnfChanged(boolean b);
	
	boolean genCode(IProgressMonitor monitor);
}
