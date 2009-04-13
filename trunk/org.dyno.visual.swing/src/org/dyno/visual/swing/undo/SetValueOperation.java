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

package org.dyno.visual.swing.undo;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ISystemValue;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("unchecked")
public class SetValueOperation extends AbstractOperation {
	private Object bean;
	private Object old_value;
	private Object new_value;
	private IWidgetPropertyDescriptor property;

	public SetValueOperation(Object bean, IWidgetPropertyDescriptor property,
			Object new_value) {
		super(Messages.SetValueOperation_Changing_Value + property.getDisplayName());
		this.bean = bean;
		this.property = property;
		try {
			this.old_value = property.getFieldValue(bean);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		this.new_value = new_value;
	}
	private class SetValueRunnable implements Runnable{
		private WidgetAdapter adapter;
		public SetValueRunnable(WidgetAdapter adapter){
			this.adapter = adapter;
		}
		
		public void run() {
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
			adapter.repaintDesigner();
		}
	}
	private static Map<Class, Object> DEFAULT_BEANS= new HashMap<Class, Object>();	
	private static Object getDefaultValue(Object bean, IWidgetPropertyDescriptor property){
		try {
			Class beanClass = bean.getClass();
			Object copy = DEFAULT_BEANS.get(beanClass);
			if (copy == null) {
				copy = beanClass.newInstance();
				DEFAULT_BEANS.put(beanClass, copy);
			}
			return property.getFieldValue(copy);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
			return null;
		}
	}
	private IStatus setValue(IProgressMonitor monitor, IAdaptable info,
			Object value) throws ExecutionException {
		try {
			if(value!=null&&value instanceof ISystemValue){
				value=getDefaultValue(bean, property);
			}
			property.setFieldValue(bean, value);
			if (bean instanceof Component) {
				Component jcomp = (Component) bean;
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(jcomp);
				adapter.setDirty(true);
				adapter.getEditingMap().put((String)property.getId(), Boolean.TRUE);
				if (adapter != null) {
					SwingUtilities.invokeLater(new SetValueRunnable(adapter));
				}
			}
		} catch (Exception e) {
			if (info != null) {
				Shell shell = (Shell) info.getAdapter(Shell.class);
				if (shell != null) {
					MessageDialog.openError(shell, Messages.SetValueOperation_Error, Messages.SetValueOperation_Error_Ocurrs_While_Setting_Property+property.getDisplayName()+"!"+e.getMessage()); //$NON-NLS-3$
				}
			}
			VisualSwingPlugin.getLogger().error(e);
			throw new ExecutionException(e.getMessage());
		}
		return Status.OK_STATUS;
	}

	
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return setValue(monitor, info, new_value);
	}

	
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return setValue(monitor, info, old_value);
	}
}

