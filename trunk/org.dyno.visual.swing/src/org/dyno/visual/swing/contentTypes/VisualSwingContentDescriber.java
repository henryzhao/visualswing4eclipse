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

package org.dyno.visual.swing.contentTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;
/**
 * 
 * VisualSwingContentDescriber
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class VisualSwingContentDescriber implements ITextContentDescriber {
	public static final String CONTENT_TYPE_ID_VS = "org.dyno.visual.swing.visualSwing";

	private static class PatternItem {
		public String type;
		public String value;

		public PatternItem(String type, String value) {
			this.type = type;
			this.value = value;
		}
	}
	public static final String VISUAL_SWING_MAGIC="VS4E";
	public static final String VISUAL_SWING_MAGIC_COMMENT="//VS4E";
	private static String VISUAL_SWING_CLASS_TAILER=".*//\\s*"+VISUAL_SWING_MAGIC+".*";
	private static QualifiedName COMPONENT_TYPE = new QualifiedName("component.type", "Component Type");
	private static QualifiedName[] QUALIFIERS = new QualifiedName[] { COMPONENT_TYPE };
	private static String CONTENT_TYPE_PATTERN_EXTENSION_POINT = "org.dyno.visual.swing.contentTypePattern";
	private static ArrayList<PatternItem> patterns;
	static {
		patterns = new ArrayList<PatternItem>();
		parsePatternExtensions();
	}

	private static void parsePatternExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(CONTENT_TYPE_PATTERN_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parsePatternExtension(extensions[i]);
				}
			}
		}
	}

	private static void parsePatternExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("pattern")) {
					addPattern(configs[i]);
				}
			}
		}
	}

	private static void addPattern(IConfigurationElement config) {
		String type = config.getAttribute("type");
		String value = config.getAttribute("value");
		patterns.add(new PatternItem(type, value));
	}

	@Override
	public int describe(Reader contents, IContentDescription description) throws IOException {
		BufferedReader buffer = new BufferedReader(contents);
		StringBuilder cat = new StringBuilder();
		String line;
		while ((line = buffer.readLine()) != null) {
			cat.append(line);
		}
		String result = cat.toString();
		if (result.matches(VISUAL_SWING_CLASS_TAILER)) {
			for (PatternItem pattern : patterns) {
				if (result.matches(pattern.value)) {
					if (description != null)
						description.setProperty(COMPONENT_TYPE, pattern.type);
					return VALID;
				}
			}
			if (description != null)
				description.setProperty(COMPONENT_TYPE, "Unknown");
			return VALID;
		}
		return INDETERMINATE;
	}

	@Override
	public int describe(InputStream contents, IContentDescription description) throws IOException {
		InputStreamReader reader = new InputStreamReader(contents);
		return describe(reader, description);
	}

	@Override
	public QualifiedName[] getSupportedOptions() {
		return QUALIFIERS;
	}

}

