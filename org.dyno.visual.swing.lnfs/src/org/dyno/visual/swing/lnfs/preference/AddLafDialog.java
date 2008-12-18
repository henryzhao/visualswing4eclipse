package org.dyno.visual.swing.lnfs.preference;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddLafDialog extends Dialog {
	public AddLafDialog(Shell parentShell) {
		super(parentShell);
	}
	private void newJarSrc(){
		JarSrcDialog jsDialog = new JarSrcDialog(getShell());
		jsDialog.open();
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("New Look And Feel");
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		main.setLayout(layout);
		Label label = new Label(main, SWT.WRAP);
		label.setText("Name:");
		Text text = new Text(main, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData();
		data.widthHint = 215;
		data.horizontalSpan = 2;
		text.setLayoutData(data);
		Composite right = new Composite(main, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		right.setLayout(gridLayout);
		Button btnAdd = new Button(right, SWT.PUSH);		
		btnAdd.setText("New ... ");
		btnAdd.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				newJarSrc();
			}
		});
		Button btnDel = new Button(right, SWT.PUSH);
		btnDel.setText("Remove");
		int style = SWT.H_SCROLL | SWT.V_SCROLL	| SWT.SINGLE | SWT.BORDER;
		ListViewer viewer = new ListViewer(main, style);
		data = new GridData();
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.heightHint = 100;
		viewer.getList().setLayoutData(data);
		viewer.setContentProvider(new JarSrcContProv());
		viewer.setInput(new JarSrcInput());
		viewer.setLabelProvider(new JarSrcLabelProvider());
		label = new Label(main, SWT.NONE);
		label.setText("Classname:");
		text = new Text(main, SWT.SINGLE|SWT.BORDER);
		data = new GridData();
		data.widthHint = 150;
		text.setLayoutData(data);
		Button btnBrowse = new Button(main, SWT.PUSH);
		btnBrowse.setText("Browse...");
		return main;
	}

	private List<JarSrc> jarSrcs;

	private class JarSrcLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			// TODO Auto-generated method stub
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element == null)
				return "";
			else if (element instanceof JarSrc) {
				JarSrc jarSrc = (JarSrc) element;
				return jarSrc.getJar()+"["+jarSrc.getSrc()+"]";
			} else
				return super.getText(element);
		}

	}

	private class JarSrcInput {
	}

	private class JarSrc {
		private String jar;
		private String src;

		public JarSrc(String jar, String src) {
			this.jar = jar;
			this.src = src;
		}

		public String getJar() {
			return jar;
		}

		public String getSrc() {
			return src;
		}
	}

	private class JarSrcContProv implements IStructuredContentProvider {
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return jarSrcs == null ? new Object[] { new JarSrc("jar1", "src1"),
					new JarSrc("jar2", "src2") } : jarSrcs.toArray();
		}
	}
}
