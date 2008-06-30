package org.dyno.visual.swing.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

public class VsLayoutContainer implements IClasspathContainer {
	private Path jar_path;
	private Path src_path;

	public VsLayoutContainer(Path jar, Path src) {
		this.jar_path = jar;
		this.src_path = src;
	}

	public IClasspathEntry[] getClasspathEntries() {
		IClasspathEntry layoutextlib = JavaCore.newLibraryEntry(jar_path, src_path, new Path("src"), true);
		return new IClasspathEntry[] { layoutextlib };
	}

	public String getDescription() {
		return "GroupLayout Extension Library";
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return new Path("VS_LAYOUT");
	}
}
