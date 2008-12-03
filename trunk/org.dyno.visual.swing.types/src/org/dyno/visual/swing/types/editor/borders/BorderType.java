
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

package org.dyno.visual.swing.types.editor.borders;

import java.util.ArrayList;
import java.util.List;

import javax.swing.border.Border;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

public class BorderType {
	private static BorderType[] border_types;
	static {
		initBorderTypes();
	}

	public static BorderType[] getBorderTypes() {
		return border_types;
	}

	private static void initBorderTypes() {
		List<BorderType> types = new ArrayList<BorderType>();
		for (BorderAdapter adapter : BorderAdapter.getBorderList()) {
			types.add(new BorderType(adapter.getBorderName(), adapter) {
			});
		}
		border_types = new BorderType[types.size()];
		types.toArray(border_types);
	}

	protected String name;
	protected BorderAdapter factory;

	private BorderType(String name, BorderAdapter factory) {
		this.name = name;
		this.factory = factory;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public String toString() {
		return name;
	}

	public static BorderType getBorderType(Border border) {
		
		for(int i=0;i<border_types.length;i++){
			BorderType type = border_types[i];
			if(border==null&&type.factory.getBorderClass()==null||border!=null&&type.factory.getBorderClass()==border.getClass())
				return type;
		}
		return null;
	}

	public IPropertySourceProvider getPropertySourceProvider(Border border) {
		if (border == null)
			return null;
		BorderAdapter adapter = BorderAdapter.getBorderAdapter(border.getClass());
		return new DelegateProvider(border, adapter);
	}

	class DelegateProvider implements IPropertySourceProvider {
		private Object object;
		private BorderAdapter adapter;

		public DelegateProvider(Object object, BorderAdapter adapter) {
			this.object = object;
			this.adapter = adapter;
		}

		@Override
		public IPropertySource getPropertySource(Object object) {
			if (this.object == object)
				return adapter.getPropertySource(object);
			else
				return null;
		}

	}

	public Border createBorder() {
		if (factory == null)
			return null;
		else
			return (Border) factory.newInstance(this);
	}
}

