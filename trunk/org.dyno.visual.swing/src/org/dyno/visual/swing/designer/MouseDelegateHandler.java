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
package org.dyno.visual.swing.designer;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

class MouseDelegateHandler implements InvocationHandler {
	private VisualDesigner designer;

	MouseDelegateHandler(VisualDesigner designer) {
		this.designer = designer;
	}

	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		MouseEvent e = (MouseEvent) args[0];
		if (!e.isConsumed()) {
			Point point = e.getPoint();
			Component hovered = designer.componentAt(point, WidgetAdapter.ADHERE_PAD);
			if (hovered != null) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(hovered);
				if (adapter != null) {
					do {
						MouseEvent mEvent = SwingUtilities.convertMouseEvent(designer, e, adapter.getContentPane());
						Object l = adapter.getAdapter(MouseInputListener.class);
						if (l != null) {
							method.invoke(l, new Object[] { mEvent });
							if (mEvent.isConsumed()) {
								e.consume();
								return null;
							} else if (mEvent.getSource() == null) {
								return null;
							}
						}
						adapter = adapter.getParentAdapter();
					} while (adapter != null);
				}
			}
		}
		return null;
	}
}
