/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.cloner;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.TypeAdapter;
import org.dyno.visual.swing.plugin.spi.ICloner;

public class ComboBoxModelCloner implements ICloner {

	@Override
	public Object clone(Object object) {
		ComboBoxModel cbm = (ComboBoxModel) object;
		int count = cbm.getSize();
		Object[]elements = new Object[count];
		for(int i=0;i<count;i++){
			Object element = cbm.getElementAt(i);
			if(element == null){
				elements[i] = null;
			}else{
				Object current = element;
				TypeAdapter adapter = ExtensionRegistry.getTypeAdapter(element.getClass());
				if(adapter!=null&&adapter.getCloner()!=null)
					current = adapter.getCloner().clone(element);
				elements[i] = current;				
			}
		}		
		return new DefaultComboBoxModel(elements);
	}

}
