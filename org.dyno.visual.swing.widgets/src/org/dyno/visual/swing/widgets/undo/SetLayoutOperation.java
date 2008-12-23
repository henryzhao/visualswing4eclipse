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

package org.dyno.visual.swing.widgets.undo;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.widgets.JPanelAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class SetLayoutOperation extends AbstractOperation {
	private IConfigurationElement config;
	private JPanelAdapter jpaneladapter;
	private LayoutAdapter oldLayoutAdapter;
	private List<Pair> constraints;

	class Pair {
		Component child;
		Object constraints;
	}

	public SetLayoutOperation(IConfigurationElement config,
			JPanelAdapter jpaneladapter) {
		super("Set Layout");
		this.config = config;
		this.jpaneladapter = jpaneladapter;
		this.oldLayoutAdapter = jpaneladapter.getLayoutAdapter();
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		constraints = new ArrayList<Pair>();
		for (int i = 0; i < jpaneladapter.getChildCount(); i++) {
			Component child = jpaneladapter.getChild(i);
			Object cons = jpaneladapter.getChildConstraints(child);
			Pair pair = new Pair();
			pair.child = child;
			pair.constraints = cons;
			constraints.add(pair);
		}
		JPanel jpanel = (JPanel) jpaneladapter.getWidget();
		LayoutAdapter adapter = LayoutAdapter.createLayoutAdapter(config);
		adapter.initConainerLayout(jpanel, monitor);
		jpaneladapter.setLayoutAdapter(null);
		jpaneladapter.doLayout();
		jpanel.validate();
		jpaneladapter.setDirty(true);
		jpaneladapter.changeNotify();
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		JPanel jpanel = (JPanel) jpaneladapter.getWidget();
		jpanel.removeAll();
		if (oldLayoutAdapter != null)
			oldLayoutAdapter.initConainerLayout(jpanel, monitor);
		else
			jpanel.setLayout(null);
		for (Pair pair : constraints) {
			jpaneladapter.addChildByConstraints(pair.child, pair.constraints);
		}
		jpaneladapter.doLayout();
		jpaneladapter.setLayoutAdapter(null);
		jpanel.validate();
		jpaneladapter.setDirty(true);
		jpaneladapter.changeNotify();
		return Status.OK_STATUS;
	}

}

