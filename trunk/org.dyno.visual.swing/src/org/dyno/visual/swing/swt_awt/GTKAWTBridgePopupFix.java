/************************************************************************************
 * Copyright (c) 2008 xuxinjie@apusic.com.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     xuxinjie@apusic.com - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.swt_awt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

public class GTKAWTBridgePopupFix {
	private static final int MAX_ATTEMPTS = 200;
	private static final int MAX_RETRIES = 5;

	public static void showMenu(final Menu menu) {
		showMenu(menu, MAX_RETRIES);
	}

	public static void showMenu(final Menu menu, final int retriesLeft) {
		if (retriesLeft <= 0)
			return;
		final Display display = Display.getCurrent();

		final Shell active = display.getActiveShell();
		final Shell useForPopups = new Shell(display, SWT.NO_TRIM
				| SWT.NO_FOCUS | SWT.ON_TOP);

		Point l = display.getCursorLocation();
		l.x -= 2;
		l.y -= 2;
		useForPopups.setLocation(l);
		useForPopups.setSize(4, 4);
		useForPopups.open();
		final int[] count = new int[1];
		Runnable r = new Runnable() {
			public void run() {
				useForPopups.setActive();
				menu.addListener(SWT.Hide, new Listener() {
					public void handleEvent(Event e) {
						useForPopups.dispose();
						active.setActive();
					}
				});
				menu.addListener(SWT.Show, new Listener() {
					public void handleEvent(Event e) {
						count[0]++;
						if (!menu.isVisible() && count[0] > MAX_ATTEMPTS) {
							Runnable r = new Runnable() {
								public void run() {
									menu.setVisible(false);
									menu.dispose();
									useForPopups.dispose();
									showMenu(menu, retriesLeft - 1);
								}
							};
							display.asyncExec(r);
							return;
						}

						Runnable r = new Runnable() {
							public void run() {
								if (!menu.isVisible())
									menu.setVisible(true);
							}
						};
						display.asyncExec(r);
					}
				});

				menu.setVisible(true);
			}
		};
		display.asyncExec(r);

	}
}