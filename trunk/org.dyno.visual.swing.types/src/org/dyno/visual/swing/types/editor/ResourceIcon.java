package org.dyno.visual.swing.types.editor;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.dyno.visual.swing.plugin.spi.WhiteBoard;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class ResourceIcon implements Icon {
	private String path;
	private Icon icon;

	public ResourceIcon(String p) {
		this.path = p;
		IJavaProject prj = WhiteBoard.getCurrentProject();
		IProject project = prj.getProject();
		IResource resource = project.findMember(new Path(p));
		if (resource == null) {
			IPackageFragmentRoot[] roots;
			try {
				roots = prj.getPackageFragmentRoots();
				for (IPackageFragmentRoot root : roots) {
					if (!root.isArchive()) {
						String src = root.getElementName();
						src = "/" + src + p;
						resource = project.findMember(new Path(src));
						if (resource != null) {
							String ext = resource.getFileExtension();
							if (ext.equals("gif") || ext.equals("png") || ext.equals("jpg")) {
								IPath fullPath = project.getWorkspace().getRoot().getRawLocation().append(resource.getFullPath());
								String fullpath = fullPath.toString();
								Image image = Toolkit.getDefaultToolkit().getImage(fullpath);
								icon = new ImageIcon(image);
							} else {
								break;
							}
						}
					}
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getIconHeight() {
		return icon == null ? 0 : icon.getIconHeight();
	}

	@Override
	public int getIconWidth() {
		return icon == null ? 0 : icon.getIconWidth();
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (icon == null) {
		} else {
			icon.paintIcon(c, g, x, y);
		}
	}

	public String toString() {
		return path;
	}
}