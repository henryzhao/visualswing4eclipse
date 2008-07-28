/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.wizards;

import javax.swing.JFrame;
/**
 * 
 * NewFrameCreationWizard
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class NewFrameCreationWizard extends NewVisualComponentVizard {
	public NewFrameCreationWizard() {
		super(JFrame.class.getName());
	}

	@Override
	protected NewComponentPage createPage() {
		return new NewFramePage();
	}
}
