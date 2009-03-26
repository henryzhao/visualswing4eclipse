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

package org.dyno.visual.swing.editors.actions;

import javax.swing.SwingUtilities;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.eclipse.jface.action.Action;

public class SetLnfAction extends Action {
	private String lnfClassname;
	private VisualDesigner designer;

	public SetLnfAction(VisualDesigner designer, String lnfName, String lnfClassname) {
		super(lnfName, AS_RADIO_BUTTON);
		this.lnfClassname = lnfClassname;
		this.designer = designer;
		String lnf = designer.getLnfClassname();
		setChecked(lnf != null && lnf.equals(lnfClassname));
	}

	@Override
	public void run() {
		if (isChecked())
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					changeLnf();
				}
			});
	}

	private void changeLnf() {
		String lnf = designer.getLnfClassname();
		if (lnf == null || !lnf.getClass().getName().equals(lnfClassname)) {
			try {
				designer.setLnfClassname(lnfClassname);
				designer.repaint();
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
	}
}
