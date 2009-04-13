package org.dyno.visual.swing.lnfs.lib;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.ILibraryExtension;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;

public class LookAndFeelLibrary implements ILibraryExtension, IExecutableExtension {
	public static final String LOOK_AND_FEEL_EXT="lookandfeel";
	public static final String LOOK_AND_FEEL_LIB_DIR="lookandfeel";
	private IClasspathContainer[] lnfLibs;
	public LookAndFeelLibrary(){
	}
	public IClasspathContainer createLibExt(IPath containerPath) {
		int count = containerPath.segmentCount();
		if (count > 0) {
			IPath firstPath = new Path(containerPath.segment(0));
			if (firstPath.equals(JavaUtil.VS_LIBRARY)) {
				if (count > 1) {
					String type = containerPath.segment(1);
					if (type.equals(LOOK_AND_FEEL_EXT)) {
						IPath path = LookAndFeelLib.getLafLibDir();
						if (count > 2) {
							String libDir = containerPath.segment(2);
							path = path.append(libDir);
							File folder = path.toFile();
							if (folder.exists()) {
								return LookAndFeelLib.getLnfLib(libDir);
							}
						}
					}
				}
			}
		}
		return null;
	}
	public IClasspathContainer[] listLibPaths(boolean refresh) {
		if(refresh){
			initLafLibs();	
		}
		return lnfLibs;
		
	}
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		initLafLibs();		
	}
	private void initLafLibs() {
		IPath path = LookAndFeelLib.getLafLibDir();
		File folder = path.toFile();
		if (folder.exists()) {
			File[] folders = folder.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});
			if (folders != null && folders.length > 0) {
				List<IClasspathContainer> paths = new ArrayList<IClasspathContainer>();
				for (File dir : folders) {
					LookAndFeelLib lib = LookAndFeelLib.getLnfLib(dir.getName());
					ExtensionRegistry.registerLnfAdapter(lib.getClassname(), lib);
					paths.add(lib);
				}
				lnfLibs = new IClasspathContainer[paths.size()];
				lnfLibs = paths.toArray(lnfLibs);
			}
		}
	}
}
