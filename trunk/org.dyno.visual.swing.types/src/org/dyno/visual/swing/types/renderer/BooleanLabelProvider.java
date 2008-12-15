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

package org.dyno.visual.swing.types.renderer;

import java.awt.Toolkit;

import org.dyno.visual.swing.types.TypePlugin;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class BooleanLabelProvider extends LabelProvider {
	private static final String CHECKED_ICON = "/icons/checked.png";
	private static final String XP_CHECKED_ICON = "/icons/xpchecked.png";
	private static final String UNCHECKED_ICON = "/icons/unchecked.png";
	private static final String XP_UNCHECKED_ICON = "/icons/xpunchecked.png";

	private static boolean isXP() {
		Boolean xp = (Boolean) Toolkit.getDefaultToolkit().getDesktopProperty(
				"win.xpstyle.themeActive");
		return xp != null && xp.booleanValue();
	}

	@Override
	public Image getImage(Object element) {

		if (element != null && element instanceof Boolean) {
			boolean checked = ((Boolean) element).booleanValue();
			String iconurl;
			if (isXP()) {
				if (checked)
					iconurl = XP_CHECKED_ICON;
				else
					iconurl = XP_UNCHECKED_ICON;
			} else {
				if (checked)
					iconurl = CHECKED_ICON;
				else
					iconurl = UNCHECKED_ICON;
			}
			return TypePlugin.getSharedImage(iconurl);
		} else
			return TypePlugin.getSharedImage(isXP() ? XP_UNCHECKED_ICON
					: UNCHECKED_ICON);
	}

	@Override
	public String getText(Object element) {
		return null;
	}
}
