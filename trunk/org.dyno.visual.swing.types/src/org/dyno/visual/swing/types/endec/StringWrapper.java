/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.endec;

import java.io.BufferedReader;
import java.io.StringReader;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class StringWrapper implements ICodeGen {
	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		else {
			String str = (String) value;
			str = str.replaceAll("\\\\", "\\\\\\\\");
			String content = null;
			try {
				BufferedReader br = new BufferedReader(new StringReader(str));
				String line;
				while ((line = br.readLine()) != null) {
					if (content == null)
						content = line;
					else {
						content += "\\n" + line;
					}
				}
				br.close();
			} catch (Exception e) {
				content = str;
			}
			return "\"" + content + "\"";
		}
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}
