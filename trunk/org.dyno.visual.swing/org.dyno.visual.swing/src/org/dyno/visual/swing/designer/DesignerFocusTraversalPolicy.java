/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.designer;

import java.awt.Component;
import java.awt.Container;

import javax.swing.LayoutFocusTraversalPolicy;
/**
 * 
 * DesignerFocusTraversalPolicy
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class DesignerFocusTraversalPolicy extends LayoutFocusTraversalPolicy {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getComponentAfter(Container container, Component component) {
		if (container instanceof VisualDesigner) {
			VisualDesigner designer = (VisualDesigner) container;
			return designer.getGlass();
		} else
			return super.getComponentAfter(container, component);
	}

	@Override
	public Component getComponentBefore(Container container, Component component) {
		if (container instanceof VisualDesigner) {
			VisualDesigner designer = (VisualDesigner) container;
			return designer.getGlass();
		} else
			return super.getComponentBefore(container, component);
	}

	@Override
	public Component getDefaultComponent(Container container) {
		if (container instanceof VisualDesigner) {
			VisualDesigner designer = (VisualDesigner) container;
			return designer.getGlass();
		} else
			return super.getDefaultComponent(container);
	}

	@Override
	public Component getFirstComponent(Container container) {
		if (container instanceof VisualDesigner) {
			VisualDesigner designer = (VisualDesigner) container;
			return designer.getGlass();
		} else
			return super.getFirstComponent(container);
	}

	@Override
	public Component getLastComponent(Container container) {
		if (container instanceof VisualDesigner) {
			VisualDesigner designer = (VisualDesigner) container;
			return designer.getGlass();
		} else
			return super.getLastComponent(container);
	}

}