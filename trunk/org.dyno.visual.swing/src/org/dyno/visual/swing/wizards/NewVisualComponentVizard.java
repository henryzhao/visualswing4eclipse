package org.dyno.visual.swing.wizards;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;

public class NewVisualComponentVizard extends NewElementWizard {
	private static String NEWCLASS_WIZ_ICON = "/icons/newclass_wiz.png";
	private NewComponentPage fPage;
	private boolean fOpenEditorOnFinish;
	private String superClassName;

	public NewVisualComponentVizard(String superClassName) {
		this.superClassName = superClassName;
		setDefaultPageImageDescriptor(VisualSwingPlugin.getSharedDescriptor(NEWCLASS_WIZ_ICON));
		setDialogSettings(getDialogSettings());
		setWindowTitle("New Visual Component");
		fOpenEditorOnFinish = true;
	}

	public void addPages() {
		super.addPages();
		if (fPage == null) {
			fPage = new NewComponentPage();
			fPage.setWizard(this);
			fPage.init(getSelection());
		}
		addPage(fPage);
	}

	@Override
	public IDialogSettings getDialogSettings() {
		IDialogSettings settings = VisualSwingPlugin.getDefault().getDialogSettings();
		IDialogSettings section = settings.getSection(NewComponentPage.PAGE_NAME);
		if (section == null) {
			section = new DialogSettings(NewComponentPage.PAGE_NAME);
			settings.addSection(section);
		}
		section.put(NewComponentPage.SETTINGS_CREATECONSTR, false);
		section.put(NewComponentPage.SETTINGS_CREATEUNIMPLEMENTED, true);
		section.put(NewComponentPage.SETTINGS_CREATEMAIN, true);
		section.put(NewComponentPage.SETTINGS_SUPERCLASSNAME, superClassName);
		return settings;
	}

	protected boolean canRunForked() {
		return !fPage.isEnclosingTypeSelected();
	}

	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		fPage.createType(monitor);
		IResource resource = fPage.getModifiedResource();
		setLayoutLib(resource, monitor);
	}

	public boolean performFinish() {
		warnAboutTypeCommentDeprecation();
		boolean res = super.performFinish();
		if (res) {
			IResource resource = fPage.getModifiedResource();
			if (resource != null) {
				selectAndReveal(resource);
				if (fOpenEditorOnFinish) {
					openResource((IFile) resource);
				}
			}
		}
		return res;
	}

	private void setLayoutLib(IResource resource, IProgressMonitor monitor) {
		try {
			IProject project = resource.getProject();
			IJavaProject javaProject = JavaCore.create(project);
			IClasspathEntry[] classpath = javaProject.getRawClasspath();
			boolean layout_exists = false;
			for (IClasspathEntry path : classpath) {
				String sPath = path.getPath().toString();
				if (sPath.equals("VS_LAYOUT"))
					layout_exists = true;
			}
			if (!layout_exists) {
				IClasspathEntry varEntry = JavaCore.newContainerEntry(new Path("VS_LAYOUT"), false);
				IClasspathEntry[] newClasspath = new IClasspathEntry[classpath.length + 1];
				System.arraycopy(classpath, 0, newClasspath, 0, classpath.length);
				newClasspath[classpath.length] = varEntry;
				javaProject.setRawClasspath(newClasspath, monitor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public IJavaElement getCreatedElement() {
		return fPage.getCreatedType();
	}
}
