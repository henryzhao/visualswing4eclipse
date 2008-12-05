package org.dyno.visual.swing.plugin.spi;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;

public interface ILibraryExtension {
	IClasspathContainer createLibExt(IPath containerPath);

	IPath[] listLibPaths();
}
