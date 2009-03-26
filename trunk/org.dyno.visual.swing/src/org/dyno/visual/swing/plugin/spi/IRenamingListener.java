package org.dyno.visual.swing.plugin.spi;

import org.eclipse.jdt.core.ICompilationUnit;

public interface IRenamingListener {
	void adapterRenamed(ICompilationUnit unit, IAdapter adapter);
}
