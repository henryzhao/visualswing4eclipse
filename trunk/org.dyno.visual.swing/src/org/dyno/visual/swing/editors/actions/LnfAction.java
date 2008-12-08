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
import javax.swing.UIManager.LookAndFeelInfo;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.eclipse.jface.action.Action;

public class LnfAction extends Action {
	private LookAndFeelInfo info;
	private VisualDesigner designer;
	public LnfAction(VisualDesigner designer, LookAndFeelInfo info) {
		super(info.getName(), AS_RADIO_BUTTON);
		this.info = info;
		this.designer = designer;
		String lnf = designer.getLnfClassname();
		setChecked(lnf != null && lnf.equals(info.getClassName()));
		System.out.println();
	}

	@Override
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				changeLnf();
			}
		});
	}

	private void changeLnf() {
		String lnf = designer.getLnfClassname();
		if (lnf==null||!lnf.getClass().getName().equals(info.getClassName())) {
			try {
				designer.setLnfClassname(info.getClassName());
				SwingUtilities.updateComponentTreeUI(designer);
				designer.repaint();
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}else{
			
		}
	}
}

