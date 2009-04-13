
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

package org.dyno.visual.swing.borders;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.border.MatteBorder;

import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.borders.action.MatteBorderSwitchAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;

@SuppressWarnings("unchecked")
public class MatteBorderAdapter extends BorderAdapter {

	
	public Class getBorderClass() {
		return MatteBorder.class;
	}

	
	public String getBorderName() {
		return "MatteBorder";
	}

	
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		InsetsProperty insetsProperty = new InsetsProperty(){
			
			protected Class getBorderClass() {
				return MatteBorder.class;
			}};
		FieldProperty colorProperty = new FieldProperty("color", "color", MatteBorder.class);
		FieldProperty tileIconProperty = new FieldProperty("tileIcon", "tileIcon", MatteBorder.class);
		return new IWidgetPropertyDescriptor[] { insetsProperty, colorProperty, tileIconProperty };
	}


	
	public IAction getContextAction(JComponent widget) {
		return new MatteBorderSwitchAction(widget);
	}

	
	public Object newInstance(Object bean) {
		return BorderFactory.createMatteBorder(0, 0, 0, 0, Color.black);
	}

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "null";
		String strBf=imports.addImport("javax.swing.BorderFactory");
		MatteBorder border = (MatteBorder)value;
		Insets insets = border.getBorderInsets();
		Color matteColor = border.getMatteColor();
		Icon tileIcon = border.getTileIcon();
		StringBuilder builder = new StringBuilder();
		builder.append(strBf+".createMatteBorder(");
		builder.append(insets.top);
		builder.append(", ");
		builder.append(insets.left);
		builder.append(", ");
		builder.append(insets.bottom);
		builder.append(", ");
		builder.append(insets.right);
		builder.append(", ");
		if(tileIcon!=null)
			builder.append(encodeValue(tileIcon, imports));
		else if(matteColor!=null)
			builder.append(encodeValue(matteColor, imports));
		else
			builder.append(encodeValue(Color.black, imports));		
		builder.append(")");
		return builder.toString();
	}

}

