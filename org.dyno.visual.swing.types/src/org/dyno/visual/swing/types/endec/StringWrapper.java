
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

package org.dyno.visual.swing.types.endec;

import java.io.BufferedReader;
import java.io.StringReader;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class StringWrapper implements ICodeGen {
	
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

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}

