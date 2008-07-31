package org.dyno.visual.swing.undo;

import java.awt.Component;
import java.beans.PropertyDescriptor;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class SetValueOperation extends AbstractOperation {
	private Object bean;
	private Object old_value;
	private Object new_value;
	private PropertyDescriptor property;

	public SetValueOperation(Object bean, PropertyDescriptor property,
			Object new_value) {
		super("changing " + property.getDisplayName());
		this.bean = bean;
		this.property = property;
		try {
			this.old_value = property.getReadMethod().invoke(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.new_value = new_value;
	}

	private IStatus setValue(IProgressMonitor monitor, IAdaptable info,
			Object value) throws ExecutionException {
		try {
			property.getWriteMethod().invoke(bean, value);
			if (bean instanceof Component) {
				Component jcomp = (Component) bean;
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(jcomp);
				adapter.setDirty(true);
				if (adapter != null) {
					if (!adapter.isRoot()) {
						CompositeAdapter parent = adapter.getParentAdapter();
						Component widget = parent.getWidget();
						widget.doLayout();
						widget.validate();
					} else {
						Component widget = adapter.getWidget();
						widget.doLayout();
						widget.validate();
					}
					adapter.getDesigner().repaint();
				}
			}
		} catch (Exception e) {
			if (info != null) {
				Shell shell = (Shell) info.getAdapter(Shell.class);
				if (shell != null) {
					MessageDialog.openError(shell, "Error", "occurs while setting property:"+property.getDisplayName()+"!"+e.getMessage());
				}
			}
			throw new ExecutionException(e.getMessage());
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return setValue(monitor, info, new_value);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return setValue(monitor, info, old_value);
	}
}
