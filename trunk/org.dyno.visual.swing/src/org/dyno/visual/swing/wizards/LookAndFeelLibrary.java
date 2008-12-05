package org.dyno.visual.swing.wizards;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.ILibraryExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathContainer;

public class LookAndFeelLibrary implements ILibraryExtension {

	@Override
	public IClasspathContainer createLibExt(IPath containerPath) {
		int count = containerPath.segmentCount();
		if (count > 0) {
			IPath firstPath = new Path(containerPath.segment(0));
			if (firstPath.equals(JavaUtil.VS_LIBRARY)) {
				if (count > 1) {
					String type = containerPath.segment(1);
					if (type.equals("lookandfeel")) {
						IPath path = Platform.getLocation();
						path = path.append(".metadata");
						path = path.append(".plugins");
						path = path.append(VisualSwingPlugin.getPluginID());
						path = path.append("lookandfeel");
						if (count > 2) {
							String lnfName = containerPath.segment(2);
							path = path.append(lnfName);
							File folder = path.toFile();
							if (folder.exists()) {
								return new LnfLib(lnfName);
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public IPath[] listLibPaths() {
		IPath libPath = JavaUtil.VS_LIBRARY.append("lookandfeel");
		IPath path = Platform.getLocation();
		path = path.append(".metadata");
		path = path.append(".plugins");
		path = path.append(VisualSwingPlugin.getPluginID());
		path = path.append("lookandfeel");
		File folder = path.toFile();
		if (folder.exists()) {
			File[] folders = folder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});
			if (folders != null && folders.length > 0) {
				List<IPath> paths = new ArrayList<IPath>();
				for (File dir : folders) {
					paths.add(libPath.append(dir.getName()));
				}
				IPath[] psArray = new IPath[paths.size()];
				return paths.toArray(psArray);
			}
		}
		return null;
	}

}
