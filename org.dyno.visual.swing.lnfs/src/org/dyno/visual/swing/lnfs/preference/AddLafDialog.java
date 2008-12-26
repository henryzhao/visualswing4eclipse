package org.dyno.visual.swing.lnfs.preference;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.TypeAdapter;
import org.dyno.visual.swing.lnfs.LnfPlugin;
import org.dyno.visual.swing.lnfs.lib.DelegateLookAndFeel;
import org.dyno.visual.swing.lnfs.lib.LookAndFeelLib;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

public class AddLafDialog extends Dialog {
	
	private ListViewer viewer;
	private Button btnDel;
	private Text txtName;
	private Text txtClassname;
	private List<JarSrc> jarSrcs;

	public AddLafDialog(Shell parentShell) {
		super(parentShell);
	}

	private void newJarSrc() {
		JarSrcDialog jsDialog = new JarSrcDialog(getShell());
		int ret = jsDialog.open();
		if (ret == Window.OK) {
			addJarSrc(new JarSrc(jsDialog.getJarPath(), jsDialog.getSrcPath()));
		}
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
		txtName = new Text(main, SWT.SINGLE | SWT.BORDER);
		txtName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateButtonState();
			}
		});
		GridData data = new GridData();
		data.widthHint = 250;
		data.horizontalSpan = 2;
		txtName.setLayoutData(data);
		Composite right = new Composite(main, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		right.setLayout(gridLayout);
		Button btnAdd = new Button(right, SWT.PUSH);
		btnAdd.setText("New ... ");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newJarSrc();
			}
		});
		btnDel = new Button(right, SWT.PUSH);
		btnDel.setText("Remove");
		btnDel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSelected();
			}

		});
		int style = SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.BORDER;
		viewer = new ListViewer(main, style);
		data = new GridData();
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.heightHint = 100;
		viewer.getList().setLayoutData(data);
		viewer.setContentProvider(new JarSrcContProv());
		viewer.setInput(new JarSrcInput());
		viewer.setLabelProvider(new LabelProvider());
		viewer.getList().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateButtonState();
			}
		});
		label = new Label(main, SWT.NONE);
		label.setText("Classname:");
		txtClassname = new Text(main, SWT.SINGLE | SWT.BORDER);
		data = new GridData();
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		txtClassname.setLayoutData(data);
		txtClassname.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateButtonState();
			}
		});
		return main;
	}

	private String lafName;
	private String lafClassname;

	@Override
	protected void okPressed() {
		lafName = txtName.getText().trim();
		lafClassname = txtClassname.getText().trim();
		IProgressService progressService = PlatformUI.getWorkbench()
				.getProgressService();
		try {
			progressService.busyCursorWhile(new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					if (createLnf(monitor))
						lnfCreationDone(monitor);
				}
			});
		} catch (Exception e) {
			LnfPlugin.getLogger().error(e);
			return;
		}
	}

	private void asyncRun(Runnable run) {
		getShell().getDisplay().asyncExec(run);
	}

	protected boolean createLnf(IProgressMonitor monitor) {
		if (!checkClassname())
			return false;
		String hintID = copyAndConfig(monitor);
		if (hintID == null)
			return false;
		return true;
	}
	private String translateDir(String lafName){
		String result = lafName.replace(' ', '_');
		return result;
	}
	private String copyAndConfig(IProgressMonitor monitor) {
		try {
			IPath path = LookAndFeelLib.getLafLibDir();
			String hintID = translateDir(lafName);
			path = path.append(hintID);
			File folder = path.toFile();
			if (!folder.exists())
				folder.mkdirs();
			File lafFile = new File(folder, "laf.xml");
			
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(lafFile),"UTF-8"));
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			pw.println("<lookandfeel name=\"" + lafName + "\" class=\"" + lafClassname + "\">");
			int count = jarSrcs.size();
			List<URL> urls = new ArrayList<URL>();
			for (int i = 0; i < count; i++) {
				JarSrc jarsrc = jarSrcs.get(i);
				File jarFile = new File(jarsrc.getJar());
				copyFile(jarFile, folder);
				urls.add(jarFile.toURI().toURL());
				pw.print("\t<classpath jar=\"" + jarFile.getName() + "\"");
				if (jarsrc.getSrc() != null) {
					File srcFile = new File(jarsrc.getSrc());
					copyFile(srcFile, folder);
					pw.print(" src=\"" + srcFile.getName() + "\"");
				}
				pw.println("/>");
			}
			createDefCompValue(urls, monitor, pw);
			pw.println("</lookandfeel>");
			pw.flush();
			pw.close();
			return hintID;
		} catch (Exception e) {
			LnfPlugin.getLogger().error(e);
			return null;
		}
	}
	private void createDefCompValue(List<URL> list, IProgressMonitor monitor, final PrintWriter pw) {
		try {
			LookAndFeel current = UIManager.getLookAndFeel();
			LookAndFeel newLnf = createNewLnf(list);
			UIManager.setLookAndFeel(newLnf);
			monitor.beginTask("Creating default values...", BeanCreator.COMPONENTS.length);
			for(int i=0;i<BeanCreator.COMPONENTS.length;i++){
				final BeanCreator comp=BeanCreator.COMPONENTS[i];
				pw.print("\t<component class=\"");
				pw.print(comp.getBeanClass().getName());
				pw.print("\">\n");
				SwingUtilities.invokeAndWait(new Runnable(){
					@Override
					public void run() {
						createDefaultXml(comp, pw);
					}});
				pw.print("\t</component>\n");
				monitor.worked(1);
			}
			monitor.done();
			UIManager.setLookAndFeel(current);
		} catch (Exception e) {
			LnfPlugin.getLogger().error(e);
		}
	}
	@SuppressWarnings("unchecked")
	private void createDefaultXml(BeanCreator comp, PrintWriter pw){
		Component component = comp.createComponent();
		WidgetAdapter adapter=ExtensionRegistry.createAdapterFor(component);
		ArrayList<IWidgetPropertyDescriptor> properties = adapter.getPropertyDescriptors();
		for (IWidgetPropertyDescriptor property : properties) {
			String name = property.getDisplayName();
			if (property.isGencode()&&
					!name.equals("minimumSize")&&
					!name.equals("maximumSize")&&
					!name.equals("preferredSize")) {
				Object value = property.getFieldValue(adapter.getWidget());
				Class type=property.getObjectClass();
				TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
				String strValue=null;
				if(value==null)
					strValue="null";
				else if(Border.class.isAssignableFrom(type)){
					strValue="SYSTEM_VALUE";
				}else if(value instanceof UIResource){
					strValue="SYSTEM_VALUE";
				}
				if(ta!=null&&ta.getEndec()!=null){
					strValue=ta.getEndec().encode(value);
				}
				if(strValue==null)
					strValue=value.toString();
				pw.print("\t\t<property name=\"");
				pw.print(property.getId());
				pw.print("\" default=\"");
				pw.print(strValue);
				pw.print("\"/>\n");
			}
		}
		comp.dispose();
	}
	@SuppressWarnings("unchecked")
	private LookAndFeel createNewLnf(List<URL> list) {
		try {
			URL[] urls = new URL[list.size()];
			list.toArray(urls);
			URLClassLoader loader = new URLClassLoader(urls, getClass().getClassLoader());
			Class lafClass = loader.loadClass(lafClassname);
			return new DelegateLookAndFeel((LookAndFeel) lafClass.newInstance());
		} catch (Exception e) {
			LnfPlugin.getLogger().error(e);
			return null;
		}
	}

	private void copyFile(File jarFile, File folder) {
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(jarFile));
			BufferedOutputStream ps = new BufferedOutputStream(
					new FileOutputStream(new File(folder, jarFile.getName())));
			byte[] data = new byte[1024 * 10];
			int length;
			while ((length = bis.read(data)) > 0) {
				ps.write(data, 0, length);
				ps.flush();
			}
			ps.close();
			bis.close();
		} catch (Exception e) {
			LnfPlugin.getLogger().error(e);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean checkClassname() {
		URL[] urls = new URL[jarSrcs.size()];
		for (int i = 0; i < jarSrcs.size(); i++) {
			JarSrc jarsrc = jarSrcs.get(i);
			String jarpath = jarsrc.getJar();
			File jarFile = new File(jarpath);
			try {
				urls[i] = jarFile.toURI().toURL();
			} catch (Exception e) {
				LnfPlugin.getLogger().error(e);
				return false;
			}
		}
		URLClassLoader urlLoader = new URLClassLoader(urls, getClass()
				.getClassLoader());
		try {
			Class lnfClass = urlLoader.loadClass(lafClassname);
			if (!LookAndFeel.class.isAssignableFrom(lnfClass)) {
				asyncRun(new Runnable() {
					@Override
					public void run() {
						showNotALafClass(lafClassname);
					}
				});
			}
		} catch (ClassNotFoundException e) {
			asyncRun(new Runnable() {
				@Override
				public void run() {
					showNoSuchClassError(lafClassname);
				}
			});
			return false;
		}
		return true;
	}

	protected void showNotALafClass(String lnfClassname) {
		MessageDialog.openError(getShell(), "Error", lnfClassname
				+ " is not a subclass of LookAndFeel.");
	}

	private void showNoSuchClassError(String className) {
		MessageDialog.openError(getShell(), "Error", "Cannot find such class:"
				+ className + " in these archives");
	}

	private void lnfCreationDone(final IProgressMonitor monitor) {
		getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				finishCreation(monitor);
			}
		});
	}

	private void finishCreation(IProgressMonitor monitor) {
		super.okPressed();
	}

	private void updateButtonState() {
		ISelection sel = viewer.getSelection();
		boolean empty = sel == null || sel.isEmpty();
		btnDel.setEnabled(!empty);
		String txt = txtName.getText().trim();
		if (txt == null || txt.length() == 0) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}
		if (viewer.getList().getItemCount() == 0) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}
		txt = txtClassname.getText().trim();
		if (txt == null || txt.length() == 0) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			return;
		}
		getButton(IDialogConstants.OK_ID).setEnabled(true);
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

		public String toString() {
			File jarFile = new File(jar);
			if (src == null)
				return jarFile.getName();
			File srcFile = new File(src);
			return jarFile.getName() + "[" + srcFile.getName() + "]";
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
			return jarSrcs == null ? new Object[0] : jarSrcs.toArray();
		}
	}

	private void addJarSrc(JarSrc jarSrc) {
		if (jarSrcs == null)
			jarSrcs = new ArrayList<JarSrc>();
		jarSrcs.add(jarSrc);
		viewer.refresh();
	}

	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		updateButtonState();
		return control;
	}

	private void removeSelected() {
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		if (sel != null && !sel.isEmpty()) {
			JarSrc jarSrc = (JarSrc) sel.getFirstElement();
			jarSrcs.remove(jarSrc);
			viewer.refresh();
			updateButtonState();
		}
	}
}
