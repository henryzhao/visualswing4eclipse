package org.dyno.visual.swing.lnfs.preference;

import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.ILibraryExtension;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class LafPreference extends PreferencePage implements
		IWorkbenchPreferencePage {
	private ListViewer viewer;
	private Button btnAdd;
	private Button btnDel;
	private Button btnEdit;

	
	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		main.setLayout(layout);
		Label label = new Label(main, SWT.NONE);
		label.setText(Messages.LafPreference_Lnf_Conf);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		label.setLayoutData(data);
		viewer = new ListViewer(main, SWT.SINGLE | SWT.BORDER);
		viewer.setContentProvider(new LibContProv());
		viewer.setLabelProvider(new LabelProvider() {

			
			public String getText(Object element) {
				if (element == null)
					return ""; //$NON-NLS-1$
				else if (element instanceof IClasspathContainer) {
					return ((IClasspathContainer) element).getDescription();
				} else
					return super.getText(element);
			}

		});
		viewer.setInput(new LibInput());
		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.heightHint = 280;
		data.verticalIndent = 30;
		viewer.getControl().setLayoutData(data);
		viewer.getList().addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				updateButtonState();
			}
		});
		Composite right = new Composite(main, SWT.NONE);
		layout = new GridLayout();
		right.setLayout(layout);

		data = new GridData();
		data.widthHint = 90;

		right.setLayoutData(data);

		btnAdd = new Button(right, SWT.PUSH);
		btnAdd.setText(Messages.LafPreference_Add);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				btnAddSelected();
			}
		});

		btnEdit = new Button(right, SWT.PUSH);
		btnEdit.setText(Messages.LafPreference_Edit);

		btnDel = new Button(right, SWT.PUSH);
		btnDel.setText(Messages.LafPreference_Delete);

		updateButtonState();

		return main;
	}

	private void btnAddSelected() {
		Shell parent = btnAdd.getShell();
		AddLafDialog dialog = new AddLafDialog(parent);
		if(dialog.open()==Window.OK){
			viewer.refresh();
		}
	}

	private void updateButtonState() {
		ISelection sel = viewer.getSelection();
		boolean empty = sel == null || sel.isEmpty();
		btnEdit.setEnabled(!empty);
		btnDel.setEnabled(!empty);
	}

	private class LibInput {
	};

	private class LibContProv implements IStructuredContentProvider {
		
		public Object[] getElements(Object inputElement) {
			List<IClasspathContainer> paths = new ArrayList<IClasspathContainer>();
			List<ILibraryExtension> libExts = ExtensionRegistry
					.getLibExtensions();
			for (ILibraryExtension libExt : libExts) {
				IClasspathContainer[] libPaths = libExt.listLibPaths(true);
				if (libPaths != null) {
					for (IClasspathContainer libPath : libPaths)
						paths.add(libPath);
				}
			}
			return paths.toArray();
		}

		
		public void dispose() {
		}

		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	
	public void init(IWorkbench workbench) {
	}
}
