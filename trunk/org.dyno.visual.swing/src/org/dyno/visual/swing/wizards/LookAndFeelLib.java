package org.dyno.visual.swing.wizards;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.JavaUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

public class LookAndFeelLib implements IClasspathContainer {
	private String lnfName;
	private IPath libPath;
	private IPath jar_path;
	private IPath src_path;
	public LookAndFeelLib(String lnfName){
		this.lnfName = lnfName;
		libPath = JavaUtil.VS_LIBRARY.append("lookandfeel").append(lnfName);
		IPath path = Platform.getLocation();
		path = path.append(".metadata");
		path = path.append(".plugins");
		path = path.append(VisualSwingPlugin.getPluginID());
		path = path.append("lookandfeel");
		path = path.append(lnfName);
		jar_path = path.append(lnfName+".jar");
		src_path = path.append(lnfName+".zip");
	}
	@Override
	public IClasspathEntry[] getClasspathEntries() {
		IClasspathEntry layoutextlib = JavaCore.newLibraryEntry(jar_path, src_path, new Path("src"), true);
		return new IClasspathEntry[] { layoutextlib };
	}

	@Override
	public String getDescription() {
		return lnfName+" Look And Feel";
	}

	@Override
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	@Override
	public IPath getPath() {
		return libPath;
	}
	@Override
	public boolean equals(Object o) {
		if(o==null)
			return false;
		if(o instanceof LookAndFeelLib){
			LookAndFeelLib lnfLib=(LookAndFeelLib)o;
			return lnfLib.lnfName.equals(lnfName);
		}else
			return false;
	}
}
