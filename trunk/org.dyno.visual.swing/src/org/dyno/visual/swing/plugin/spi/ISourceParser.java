/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.plugin.spi;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.swt.widgets.Shell;
/**
 * 
 * ISourceParser
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public interface ISourceParser {
	void setSource(ICompilationUnit source);
	
	void setImportWrite(ImportRewrite imports);
	
	boolean parse(Shell shell);

	WidgetAdapter getResult();

	void setRootAdapter(WidgetAdapter root);
	
	void setLnfChanged(boolean b);
	
	boolean genCode(IProgressMonitor monitor);
}
