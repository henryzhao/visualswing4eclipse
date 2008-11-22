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
import org.eclipse.swt.SWT;

/**
 * 
 * @author William Chen
 */
public class FontLabelProvider extends LabelProvider {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		if (element instanceof java.awt.Font) {
			java.awt.Font font = (java.awt.Font) element;
			String styleString;
			int style = font.getStyle();
			if ((style & java.awt.Font.BOLD) != 0) {
				if ((style & java.awt.Font.ITALIC) != 0) {
					styleString = "BOLDITALIC";
				} else {
					styleString = "BOLD";
				}
			} else {
				if ((style & java.awt.Font.ITALIC) != 0) {
					styleString = "ITALIC";
				} else {
					styleString = "REGULAR";
				}
			}
			return "(" + font.getFamily() + ", " + styleString + ", " + font.getSize() + ")";
		} else if (element instanceof org.eclipse.swt.graphics.FontData) {
			org.eclipse.swt.graphics.FontData font = (org.eclipse.swt.graphics.FontData) element;
			return encodeFontData(font);
		}
		return element.toString();
	}

	private static String encodeFontData(org.eclipse.swt.graphics.FontData font) {
		int style = font.getStyle();
		String styleString;
		if ((style & SWT.BOLD) != 0) {
			if ((style & SWT.ITALIC) != 0) {
				styleString = "BOLDITALIC";
			} else {
				styleString = "BOLD";
			}
		} else {
			if ((style & SWT.ITALIC) != 0) {
				styleString = "ITALIC";
			} else {
				styleString = "REGULAR";
			}
		}
		return "(" + font.getName() + ", " + styleString + ", " + font.getHeight() + ")";
	}
}
