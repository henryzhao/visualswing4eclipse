package org.dyno.visual.swing.wizards;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class NewVisualSwingExtensionLibraryWizard extends WizardPage implements
		IClasspathContainerPage, IClasspathContainerPageExtension {
	public NewVisualSwingExtensionLibraryWizard() {
		super("Visual Swing Extension Libarary Page",
				"Visual Swing Extension Libarary", null);
	}

	@Override
	public boolean finish() {
		ISelection sel=viewer.getSelection();
		if(sel==null||sel.isEmpty())
			return false;
		if(sel instanceof IStructuredSelection){
			IClasspathContainer icc = (IClasspathContainer) ((IStructuredSelection) sel).getFirstElement();
			this.toBeEdited = JavaCore.newContainerEntry(icc.getPath(), false);
			return true;
		}else
			return false;
	}

	@Override
	public IClasspathEntry getSelection() {
		for (IClasspathEntry entry : currentEntries) {
			if (entry.getPath().equals(this.toBeEdited))
				return null;
		}
		return this.toBeEdited;
	}

	@Override
	public void setSelection(IClasspathEntry containerEntry) {
		this.toBeEdited = containerEntry;
	}

	@Override
	public void createControl(Composite parent) {
		viewer = new ListViewer(parent, SWT.SINGLE | SWT.BORDER);
		viewer.setContentProvider(new LibContProv());
		viewer.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				if(element!=null&&element instanceof IClasspathContainer){
					return ((IClasspathContainer)element).getDescription();
				}
				return super.getText(element);
			}
			
		});
		viewer.setInput(new LibInput());
		if (toBeEdited != null) {
			IPath ipath=toBeEdited.getPath();
			if(ipath.equals(JavaUtil.VS_LAYOUTEXT)){
				viewer.setSelection(new StructuredSelection(new Object[]{new LayoutExtensionLibrary()}));
			}else{
				List<ILibraryExtension> libExts = ExtensionRegistry.getLibExtensions();
				for(ILibraryExtension libExt:libExts){
					IClasspathContainer lib = libExt.createLibExt(ipath);
					if(lib!=null){
						viewer.setSelection(new StructuredSelection(new Object[]{lib}));
						break;
					}
				}
			}
		}else{
			setPageComplete(false);
		}
		viewer.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection sel=viewer.getSelection();
				setPageComplete(sel!=null&&!sel.isEmpty());
			}});
		setControl(viewer.getControl());
	}



	private class LibInput {
	};

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
	public void initialize(IJavaProject project,
			IClasspathEntry[] currentEntries) {
		this.project = project;
		this.currentEntries = currentEntries;
	}

	private ListViewer viewer;
	private IJavaProject project;
	private IClasspathEntry[] currentEntries;
	private IClasspathEntry toBeEdited;
}
