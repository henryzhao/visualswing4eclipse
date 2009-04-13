package org.dyno.visual.swing.widgets.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.dyno.visual.swing.base.ImageSelectionDialog;
import org.dyno.visual.swing.base.ResourceIcon;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class TabIconEditor extends JComponent implements IEditor, ActionListener {
	private static final long serialVersionUID = -4403435758517308113L;
	private ArrayList<ChangeListener> listeners;
	private JButton button;
	public TabIconEditor() {
		listeners = new ArrayList<ChangeListener>();
		button = new JButton("icon");
		setLayout(new BorderLayout());		
		add(button, BorderLayout.CENTER);
		button.addActionListener(this);
	}

	private void fireValueChanged() {
		ChangeEvent ce = new ChangeEvent(this);
		for(ChangeListener l:listeners){
			l.stateChanged(ce);
		}
	}
	
	public void addChangeListener(ChangeListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	
	public Component getComponent() {
		return this;
	}

	
	public Object getOldValue() {
		return old;
	}

	
	public Object getValue() {
		return button.getIcon();
	}

	
	public void removeChangeListener(ChangeListener l) {
		if (listeners.contains(l))
			listeners.remove(l);
	}

	
	public void setFocus() {
	}

	private Object old;

	
	public void setValue(Object v) {
		if (v == null)
			button.setIcon(null);
		else
			button.setIcon((Icon) v);
	}

	
	public void validateValue() throws Exception {
	}

	
	public void actionPerformed(ActionEvent evt) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if(workbench==null)
			return;
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		IWorkbenchWindow window=null;
		if(windows!=null&&windows.length>0)
			window=windows[0];
		if(window==null)
			return;
		final Shell shell = window.getShell();
		shell.getDisplay().asyncExec(new Runnable() {
			
			public void run() {
				openDialog(shell);
			}
		});
	}
	private void openDialog(Shell shell){
		ImageSelectionDialog isd = new ImageSelectionDialog(shell);
		if (isd.open() == Window.OK) {
			IFile file = isd.getImageFile();
			IPath location = file.getFullPath();
			location = location.removeFirstSegments(2);
			String path = location.toString();
			if (!path.startsWith("/"))
				path += "/" + path;
			try {
				ImageIcon icon = new ImageIcon(location.toFile()
						.toURI().toURL());
				final ResourceIcon srcIcon = new ResourceIcon(icon, path);
				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {
						setIconAndFire(srcIcon);
					}});
			} catch (Exception e) {
			}
		}else{
			SwingUtilities.invokeLater(new Runnable(){
				
				public void run() {
					fireValueChanged();
				}});
		}
	}
	private void setIconAndFire(Icon icon){
		button.setIcon(icon);
		fireValueChanged();
	}
}
