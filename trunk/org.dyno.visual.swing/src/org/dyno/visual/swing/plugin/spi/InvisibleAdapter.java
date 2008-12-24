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

package org.dyno.visual.swing.plugin.spi;

import java.util.List;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.eclipse.core.runtime.IConfigurationElement;

public abstract class InvisibleAdapter extends AbstractAdaptable implements IAdapter {
	protected String name;
	protected String lastName;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void init(String name, Object object) {
		this.name = name;
	}
	@SuppressWarnings("unchecked")
	public abstract List getElements();

	public static InvisibleAdapter createAdapter(String name, Object object) {
		String className = object.getClass().getName();
		IConfigurationElement config = ExtensionRegistry
				.getInvisibleConfig(className);
		if (config == null)
			return null;
		try {
			InvisibleAdapter invisible = (InvisibleAdapter) config
					.createExecutableExtension("class");
			invisible.init(name, object);
			return invisible;
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
			return null;
		}
	}

	public boolean isRenamed() {
		return lastName!=null&&!lastName.equals(name);
	}
}
