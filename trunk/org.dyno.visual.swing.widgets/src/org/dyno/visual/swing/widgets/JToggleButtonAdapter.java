
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

package org.dyno.visual.swing.widgets;

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JToggleButton;
import javax.swing.JToggleButton.ToggleButtonModel;

import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;

public class JToggleButtonAdapter extends TextWidgetAdapter {

	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JToggleButton.class;
	}
	@Override
	public IAdapter getParent() {
		JToggleButton jb=(JToggleButton)getWidget();
		ToggleButtonModel dbm=(ToggleButtonModel) jb.getModel();
		ButtonGroup bg=dbm.getGroup();
		if (bg != null) {
			for (InvisibleAdapter invisible : getRootAdapter().getInvisibles()) {
				if (invisible instanceof ButtonGroupAdapter) {
					if (bg == ((ButtonGroupAdapter) invisible).getButtonGroup())
						return invisible;
				}
			}
		}
		return super.getParent();
	}
	@Override
	public void deleteNotify() {
		JToggleButton jb = (JToggleButton) getWidget();
		DefaultButtonModel dbm = (DefaultButtonModel) jb.getModel();
		ButtonGroup bg = dbm.getGroup();
		if(bg!=null){
			bg.remove(jb);
		}
	}		
}

