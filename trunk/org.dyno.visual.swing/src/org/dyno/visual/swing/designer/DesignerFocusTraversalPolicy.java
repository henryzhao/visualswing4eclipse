package org.dyno.visual.swing.designer;

import java.awt.Component;
import java.awt.Container;

import javax.swing.LayoutFocusTraversalPolicy;

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
