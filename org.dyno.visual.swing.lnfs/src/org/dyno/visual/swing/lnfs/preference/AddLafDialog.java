package org.dyno.visual.swing.lnfs.preference;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
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

import javax.swing.JFrame;
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
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
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

@SuppressWarnings("unchecked")
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
		getShell().setText(Messages.AddLafDialog_New_Laf);
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		main.setLayout(layout);
		Label label = new Label(main, SWT.WRAP);
		label.setText(Messages.AddLafDialog_Name);
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
		data.heightHint = 16;
		txtName.setLayoutData(data);
		Composite right = new Composite(main, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		right.setLayout(gridLayout);
		Button btnAdd = new Button(right, SWT.PUSH);
		btnAdd.setText(Messages.AddLafDialog_New);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newJarSrc();
			}
		});
		btnDel = new Button(right, SWT.PUSH);
		btnDel.setText(Messages.AddLafDialog_Remove);
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
		label.setText(Messages.AddLafDialog_Classname);
		txtClassname = new Text(main, SWT.SINGLE | SWT.BORDER);
		data = new GridData();
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.heightHint = 16;
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
		try {
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
				if (createLnf(monitor))
					lnfCreationDone(monitor);
				}
			};
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell()); 
			dialog.run(true, true, runnable);
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
		PrintWriter pw = null;
		try {
			IPath path = LookAndFeelLib.getLafLibDir();
			String hintID = translateDir(lafName);
			path = path.append(hintID);
			File folder = path.toFile();
			if (!folder.exists())
				folder.mkdirs();
			File lafFile = new File(folder, "laf.xml");			 //$NON-NLS-1$
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(lafFile),"UTF-8")); //$NON-NLS-1$
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); //$NON-NLS-1$
			pw.println("<lookandfeel name=\"" + lafName + "\" class=\"" + lafClassname + "\">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int count = jarSrcs.size();
			List<URL> urls = new ArrayList<URL>();
			for (int i = 0; i < count; i++) {
				JarSrc jarsrc = jarSrcs.get(i);
				File jarFile = new File(jarsrc.getJar());
				copyFile(jarFile, folder);
				urls.add(jarFile.toURI().toURL());
				pw.print("\t<classpath jar=\"" + jarFile.getName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
				if (jarsrc.getSrc() != null) {
					File srcFile = new File(jarsrc.getSrc());
					copyFile(srcFile, folder);
					pw.print(" src=\"" + srcFile.getName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
				}
				pw.println("/>"); //$NON-NLS-1$
			}
			if(!createDefCompValue(urls, monitor, pw)){
				if(monitor.isCanceled()){
					pw.close();
					deleteDirTree(folder);
					return null;
				}
			}
			pw.println("</lookandfeel>"); //$NON-NLS-1$
			pw.flush();
			pw.close();
			return hintID;
		} catch (Exception e) {
			LnfPlugin.getLogger().error(e);
			return null;
		} finally {
			if(pw!=null){
				pw.close();
			}
		}
	}
	private void deleteDirTree(File folder) {
		if (folder.isFile())
			folder.delete();
		else if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files)
					deleteDirTree(file);
			}
			folder.delete();
		}
	}
	private boolean createDefCompValue(List<URL> list, IProgressMonitor monitor, final PrintWriter pw) {
		try {
			LookAndFeel current = UIManager.getLookAndFeel();
			LookAndFeel newLnf = createNewLnf(list);
			UIManager.setLookAndFeel(newLnf);
			monitor.beginTask(Messages.AddLafDialog_Create_Default_Values, BeanCreator.COMPONENTS.length);
			final JFrame jframe = new JFrame();
			jframe.setSize(100, 100);
			Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			jframe.setLocation(size.width+1, size.height+1);
			jframe.setVisible(true);
			for(int i=0;i<BeanCreator.COMPONENTS.length;i++){
				if(monitor.isCanceled()){
					jframe.setVisible(false);
					return false;
				}
				final BeanCreator comp=BeanCreator.COMPONENTS[i];
				pw.print("\t<component class=\""); //$NON-NLS-1$
				pw.print(comp.getBeanClass().getName());
				pw.print("\">\n"); //$NON-NLS-1$
				SwingUtilities.invokeAndWait(new Runnable(){
					@Override
					public void run() {
						createDefaultXml(jframe, comp, pw);
					}});
				pw.print("\t</component>\n"); //$NON-NLS-1$
				monitor.worked(1);
			}
			jframe.setVisible(false);
			monitor.done();
			UIManager.setLookAndFeel(current);
			return true;
		} catch (Exception e) {
			LnfPlugin.getLogger().error(e);
			return false;
		}
	}
	private void createDefaultXml(JFrame jframe, BeanCreator comp, PrintWriter pw){
		Component component = comp.createComponent(jframe);
		WidgetAdapter adapter=ExtensionRegistry.createAdapterFor(component);
		ArrayList<IWidgetPropertyDescriptor> properties = adapter.getPropertyDescriptors();
		for (IWidgetPropertyDescriptor property : properties) {
			String name = property.getDisplayName();
			if (property.isGencode()&&
					!name.equals("minimumSize")&& //$NON-NLS-1$
					!name.equals("maximumSize")&& //$NON-NLS-1$
					!name.equals("preferredSize")) { //$NON-NLS-1$
				Class type=property.getPropertyType();
				Object value = property.getFieldValue(adapter.getWidget());
				TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
				String strValue=null;
				if(value==null)
					strValue="null"; //$NON-NLS-1$
				else if(Border.class.isAssignableFrom(type)){
					strValue="SYSTEM_VALUE"; //$NON-NLS-1$
				}else if(value instanceof UIResource){
					strValue="SYSTEM_VALUE"; //$NON-NLS-1$
				}
				if(ta!=null&&ta.getEndec()!=null){
					strValue=ta.getEndec().encode(value);
				}
				if(strValue==null)
					strValue=value.toString();
				pw.print("\t\t<property name=\""); //$NON-NLS-1$
				pw.print(property.getId());
				pw.print("\" default=\""); //$NON-NLS-1$
				pw.print(strValue);
				pw.print("\"/>\n"); //$NON-NLS-1$
			}
		}
		comp.dispose();
	}
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
		MessageDialog.openError(getShell(), Messages.AddLafDialog_Error, lnfClassname
				+ Messages.AddLafDialog_Not_Subclass_Of_Lnf);
	}

	private void showNoSuchClassError(String className) {
		MessageDialog.openError(getShell(), Messages.AddLafDialog_Error, Messages.AddLafDialog_Cannot_Find_Class
				+ className + Messages.AddLafDialog_In_These_Archives);
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
			return jarFile.getName() + "[" + srcFile.getName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
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
