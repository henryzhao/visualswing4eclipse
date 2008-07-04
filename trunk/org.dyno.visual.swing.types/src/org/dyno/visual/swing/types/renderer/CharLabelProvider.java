/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.renderer;

import org.eclipse.jface.viewers.LabelProvider;

public class CharLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element == null)
			return "\\0";
		if (element instanceof Character) {
			Character character = (Character) element;
			return character.charValue()=='\0'?"\\0":character.toString();
		}else if(element instanceof String){
			String string = (String)element;
			if(string.trim().length()==0)
				return "\\0";
			else
				return string;
		}
		return element.toString();
	}
}
