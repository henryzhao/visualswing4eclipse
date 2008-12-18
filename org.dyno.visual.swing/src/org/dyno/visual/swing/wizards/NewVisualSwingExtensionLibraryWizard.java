package org.dyno.visual.swing.wizards;

import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.ILibraryExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension2;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class NewVisualSwingExtensionLibraryWizard extends WizardPage implements
		IClasspathContainerPage, IClasspathContainerPageExtension,
		IClasspathContainerPageExtension2 {
	public NewVisualSwingExtensionLibraryWizard() {
		super("Visual Swing Extension Libarary Page",
				"Visual Swing Extension Libarary", null);
	}

	@Override
	public boolean finish() {
		Object[] sel = viewer.getCheckedElements();
		if (sel == null || sel.length == 0)
			return false;
		this.selected_containers = new IClasspathEntry[sel.length];
		for (int i = 0; i < sel.length; i++) {
			IClasspathContainer icc = (IClasspathContainer) sel[i];
			this.selected_containers[i] = JavaCore.newContainerEntry(icc
					.getPath(), false);
		}
		return true;
	}

	private IClasspathEntry[] selected_containers;

	@Override
	public void setSelection(IClasspathEntry containerEntry) {
		this.toBeEdited = containerEntry;
	}

	class CheckedTableLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return VisualSwingPlugin.getSharedImage("/icons/library.png");
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			return ((IClasspathContainer) element).getDescription();
		}
	}

	@Override
	public void createControl(Composite parent) {
		viewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
		viewer.setContentProvider(new LibContProv());
		viewer.setLabelProvider(new CheckedTableLabelProvider());
		viewer.setInput(new LibInput());
		if (toBeEdited != null) {
			IPath ipath = toBeEdited.getPath();
			IClasspathContainer lib = null;
			if (ipath.equals(JavaUtil.VS_LAYOUTEXT)) {
				lib = new LayoutExtensionLibrary();
			} else {
				List<ILibraryExtension> libExts = ExtensionRegistry
						.getLibExtensions();
				for (ILibraryExtension libExt : libExts) {
					lib = libExt.createLibExt(ipath);
					if (lib != null) {
						break;
					}
				}
			}
			if (lib != null) {
				viewer.setSelection(new StructuredSelection(new Object[] { lib }));
				viewer.setChecked(lib, true);
			}
		} 
		for(IClasspathEntry currentEntry:currentEntries){
			IPath ipath = currentEntry.getPath();
			IClasspathContainer lib = null;
			if (ipath.equals(JavaUtil.VS_LAYOUTEXT)) {
				lib = new LayoutExtensionLibrary();
			} else {
				List<ILibraryExtension> libExts = ExtensionRegistry
						.getLibExtensions();
				for (ILibraryExtension libExt : libExts) {
					lib = libExt.createLibExt(ipath);
					if (lib != null) {
						break;
					}
				}
			}
			if (lib != null)
				viewer.setGrayed(lib, lib == toBeEdited);
		}
		updateCheckState();
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateCheckState();
			}
		});
		setControl(viewer.getControl());
	}
	private void updateCheckState(){
		Object[] sel = viewer.getCheckedElements();
		setPageComplete(sel != null && sel.length > 0);
	}
	private class LibInput {};

	private class LibContProv implements IStructuredContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			List<IClasspathContainer> paths = new ArrayList<IClasspathContainer>();
			paths.add(new LayoutExtensionLibrary());
			List<ILibraryExtension> libExts = ExtensionRegistry
					.getLibExtensions();
			for (ILibraryExtension libExt : libExts) {
				IClasspathContainer[] libPaths = libExt.listLibPaths();
				if (libPaths != null) {
					for (IClasspathContainer libPath : libPaths)
						paths.add(libPath);
				}
			}
			return paths.toArray();
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	@Override
	public void initialize(IJavaProject project, IClasspathEntry[] currentEntries) {
		this.currentEntries = currentEntries;
	}
	private IClasspathEntry[]currentEntries;
	private CheckboxTableViewer viewer;
	private IClasspathEntry toBeEdited;

	@Override
	public IClasspathEntry getSelection() {
		return this.selected_containers == null ? null
				: (this.selected_containers.length == 0 ? null
						: this.selected_containers[0]);
	}

	@Override
	public IClasspathEntry[] getNewContainers() {
		return selected_containers;
	}
}
