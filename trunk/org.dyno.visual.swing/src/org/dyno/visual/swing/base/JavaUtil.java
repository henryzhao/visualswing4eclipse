/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.base;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.RewriteSessionEditProcessor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * JavaUtil
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
@SuppressWarnings("unchecked")
public class JavaUtil {
	private static CodeFormatter codeFormatter;

	public static void layoutContainer(Container container) {
		container.doLayout();
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component child = container.getComponent(i);
			if (child instanceof Container) {
				layoutContainer((Container) child);
			}
		}
	}

	public static ClassLoader getProjectClassLoader(IJavaProject java_project) {
		try {
			IProject project = java_project.getProject();
			project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
			String[] classPath = JavaRuntime.computeDefaultRuntimeClassPath(java_project);
			URL[] urls = new URL[classPath.length];
			for (int i = 0; i < classPath.length; i++) {
				File cp = new File(classPath[i]);
				if (cp.exists())
					urls[i] = cp.toURI().toURL();
			}
			return new URLClassLoader(urls, JavaUtil.class.getClassLoader());
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
			return null;
		}
	}

	public static IFile getIconFile(Icon icon) {
		if (icon != null && icon instanceof ResourceIcon) {
			String text = ((ResourceIcon) icon).toString();
			if (text != null && text.trim().length() > 0) {
				IJavaProject javaProject = VisualSwingPlugin.getCurrentProject();
				if (javaProject != null) {
					IPackageFragmentRoot src_root = getSourceRoot(javaProject);
					if (src_root != null) {
						String srcName = src_root.getElementName();
						IProject prj = javaProject.getProject();
						IFile file = prj.getFolder(srcName).getFile(text);
						if (file != null && file.exists())
							return file;
					}
				}
			}
		}
		return null;
	}

	public static Icon getIconFromFile(IFile file) {
		if (file != null) {
			IPath location = file.getFullPath();
			location = location.removeFirstSegments(2);
			String path = location.toString();
			if (!path.startsWith("/"))
				path = "/" + path;
			ImageIcon imgIcon;
			try {
				imgIcon = new ImageIcon(file.getRawLocationURI().toURL());
				ResourceIcon ri = new ResourceIcon(imgIcon, path);
				return ri;
			} catch (MalformedURLException e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
		return null;
	}

	private static IPackageFragmentRoot getSourceRoot(IJavaProject prj) {
		try {
			IJavaElement[] children = prj.getChildren();
			for (IJavaElement child : children) {
				if (child instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot childRoot = (IPackageFragmentRoot) child;
					if (!childRoot.isArchive())
						return childRoot;
				}
			}
		} catch (JavaModelException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		return null;
	}

	public static IWorkbenchWindow getEclipseWindow() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
			if (windows != null && windows.length > 0)
				return windows[0];
			else
				return null;
		} else
			return window;
	}

	public static Display getEclipseDisplay() {
		return PlatformUI.getWorkbench().getDisplay();
	}

	public static Shell getEclipseShell() {
		IWorkbenchWindow window = getEclipseWindow();
		if (window != null)
			return window.getShell();
		else
			return Display.getDefault().getActiveShell();
	}

	public static void applyEdit(ICompilationUnit cu, TextEdit edit, boolean save, IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		monitor.beginTask(Messages.JAVA_UTIL_APPLYING_CHANGES, 3);

		try {
			IDocument document = null;
			try {
				document = aquireDocument(cu, new SubProgressMonitor(monitor, 1));
				if (save) {
					commitDocument(cu, document, edit, new SubProgressMonitor(monitor, 1));
				} else {
					new RewriteSessionEditProcessor(document, edit, TextEdit.UPDATE_REGIONS).performEdits();
				}
			} catch (BadLocationException e) {
				VisualSwingPlugin.getLogger().error(e);
			} finally {
				releaseDocument(cu, document, new SubProgressMonitor(monitor, 1));
			}
		} finally {
			monitor.done();
		}
	}

	public static void hideMenu() {
		Stack<MenuElement> stack = MenuSelectionManager.defaultManager().getSelectionStack();
		while (!stack.isEmpty()) {
			MenuElement me = stack.pop();
			if (me instanceof JMenu) {
				JMenu jme = (JMenu) me;
				jme.setPopupMenuVisible(false);
				jme.setSelected(false);
			}else if (me instanceof JPopupMenu) {
				JPopupMenu pop = (JPopupMenu) me;
				pop.setVisible(false);
			}
		}
	}

	private static void releaseDocument(ICompilationUnit cu, IDocument document, IProgressMonitor monitor) throws CoreException {
		if (cu.getOwner() == null) {
			IFile file = (IFile) cu.getResource();
			if (file.exists()) {
				ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
				bufferManager.disconnect(file.getFullPath(), LocationKind.IFILE, monitor);
				return;
			}
		}
		cu.getBuffer().setContents(document.get());
		monitor.done();
	}

	private static IDocument aquireDocument(ICompilationUnit cu, IProgressMonitor monitor) throws CoreException {
		if (cu.getOwner() == null) {
			IFile file = (IFile) cu.getResource();
			if (file.exists()) {
				ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
				IPath path = cu.getPath();
				bufferManager.connect(path, LocationKind.IFILE, monitor);
				return bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();
			}
		}
		monitor.done();
		return new Document(cu.getSource());
	}

	private static void commitDocument(ICompilationUnit cu, IDocument document, TextEdit edit, IProgressMonitor monitor) throws CoreException, MalformedTreeException, BadLocationException {
		if (cu.getOwner() == null) {
			IFile file = (IFile) cu.getResource();
			if (file.exists()) {
				IStatus status = makeCommittable(new IResource[] { file }, null);
				if (!status.isOK()) {
					throw new CoreException(status);
				}
				new RewriteSessionEditProcessor(document, edit, TextEdit.UPDATE_REGIONS).performEdits(); // apply
				ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
				bufferManager.getTextFileBuffer(file.getFullPath(), LocationKind.IFILE).commit(monitor, true);
				return;
			}
		}
		new RewriteSessionEditProcessor(document, edit, TextEdit.UPDATE_REGIONS).performEdits();
	}

	private static IStatus makeCommittable(IResource[] resources, Object context) {
		List<IResource> readOnlyFiles = new ArrayList<IResource>();
		for (int i = 0; i < resources.length; i++) {
			IResource resource = resources[i];
			if (resource.getType() == IResource.FILE && isReadOnly(resource))
				readOnlyFiles.add(resource);
		}
		if (readOnlyFiles.size() == 0)
			return new Status(IStatus.OK, VisualSwingPlugin.getPluginID(), IStatus.OK, "", null); //$NON-NLS-1$

		Map<IFile, Long> oldTimeStamps = createModificationStampMap(readOnlyFiles);
		IStatus status = ResourcesPlugin.getWorkspace().validateEdit(readOnlyFiles.toArray(new IFile[readOnlyFiles.size()]), context);
		if (!status.isOK())
			return status;

		IStatus modified = null;
		Map<IFile, Long> newTimeStamps = createModificationStampMap(readOnlyFiles);
		for (Iterator<IFile> iter = oldTimeStamps.keySet().iterator(); iter.hasNext();) {
			IFile file = iter.next();
			if (!oldTimeStamps.get(file).equals(newTimeStamps.get(file)))
				modified = addModified(modified, file);
		}
		if (modified != null)
			return modified;
		return new Status(IStatus.OK, VisualSwingPlugin.getPluginID(), IStatus.OK, "", null); //$NON-NLS-1$
	}

	private static final int VALIDATE_EDIT_CHANGED_CONTENT = 10003;

	private static IStatus addModified(IStatus status, IFile file) {
		String message = Messages.JAVA_UTIL_FILE + file.getFullPath().toString() + Messages.JAVA_UTIL_MODIFIED;
		IStatus entry = new Status(VALIDATE_EDIT_CHANGED_CONTENT, message, null);
		if (status == null) {
			return entry;
		} else if (status.isMultiStatus()) {
			((MultiStatus) status).add(entry);
			return status;
		} else {
			MultiStatus result = new MultiStatus(VisualSwingPlugin.getPluginID(), VALIDATE_EDIT_CHANGED_CONTENT, Messages.JAVA_UTIL_MODIFIED_RESOURCE, null);
			result.add(status);
			result.add(entry);
			return result;
		}
	}

	private static Map<IFile, Long> createModificationStampMap(List<IResource> files) {
		Map<IFile, Long> map = new HashMap<IFile, Long>();
		for (Iterator<IResource> iter = files.iterator(); iter.hasNext();) {
			IFile file = (IFile) iter.next();
			map.put(file, new Long(file.getModificationStamp()));
		}
		return map;
	}

	private static boolean isReadOnly(IResource resource) {
		ResourceAttributes resourceAttributes = resource.getResourceAttributes();
		if (resourceAttributes == null)
			return false;
		return resourceAttributes.isReadOnly();
	}

	private static CodeFormatter getCodeFormatter() {
		if (codeFormatter == null) {
			Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
			options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
			options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
			options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
			options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ENUM_CONSTANTS, DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
					DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
			options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "160"); //$NON-NLS-1$
			codeFormatter = ToolFactory.createCodeFormatter(options);
		}
		return codeFormatter;
	}

	public static String formatCode(String source) {
		TextEdit edit = getCodeFormatter().format(CodeFormatter.K_UNKNOWN, source, 0, source.length(), 0, System.getProperty("line.separator")); //$NON-NLS-1$
		if (edit != null) {
			IDocument document = new Document(source);
			try {
				edit.apply(document);
				return document.get();
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
		return source;
	}

	public static boolean setupLayoutLib(IProgressMonitor monitor) {
		return setupLayoutLib(VisualSwingPlugin.getCurrentProject(), monitor);
	}

	public static boolean setupLayoutLib(IJavaProject javaProject, IProgressMonitor monitor) {
		if (javaProject != null) {
			try {
				IClasspathEntry[] entries = javaProject.getRawClasspath();
				boolean layout_exists = false;
				for (IClasspathEntry entry : entries) {
					if (entry.getPath().equals(VS_LAYOUTEXT))
						layout_exists = true;
				}
				if (!layout_exists) {
					IClasspathEntry varEntry = JavaCore.newContainerEntry(VS_LAYOUTEXT, false);
					IClasspathEntry[] newClasspath = new IClasspathEntry[entries.length + 1];
					System.arraycopy(entries, 0, newClasspath, 0, entries.length);
					newClasspath[entries.length] = varEntry;
					javaProject.setRawClasspath(newClasspath, monitor);
				}
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
				return false;
			}
		}
		return true;
	}

	public static final IPath VS_LIBRARY = new Path(VisualSwingPlugin.PLUGIN_ID + ".VS_LIBRARY"); //$NON-NLS-1$
	public static final IPath VS_LAYOUTEXT = VS_LIBRARY.append("layoutext"); //$NON-NLS-1$
}
