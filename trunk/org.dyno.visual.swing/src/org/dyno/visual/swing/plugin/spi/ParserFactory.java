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

package org.dyno.visual.swing.plugin.spi;

import java.beans.EventSetDescriptor;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
/**
 * 
 * ParserFactory
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class ParserFactory {
	private static final String SOURCE_PARSER_FACTORY_EXTENSION = "org.dyno.visual.swing.sourceParserFactory";
	private static ParserFactory DEFAULT;

	private static void parseParserExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(SOURCE_PARSER_FACTORY_EXTENSION);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseParserExtension(extensions[i]);
				}
			}
		}
	}

	private static void parseParserExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("factory")) {
					setFactory(configs[i]);
				}
			}
		}
	}

	private static void setFactory(IConfigurationElement config) {
		try {
			DEFAULT = (ParserFactory) config.createExecutableExtension("class");
		} catch (CoreException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	public abstract ISourceParser newParser();

	public static ParserFactory getDefaultParserFactory() {
		if (DEFAULT == null)
			parseParserExtensions();
		return DEFAULT;
	}

	public abstract IEventListenerModel newModel(WidgetAdapter adapter, EventSetDescriptor eventSet);
}

