/*
 * ComboBoxModelWrapper.java
 *
 * Created on 2007-8-28, 0:16:54
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.types.endec;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class ComboBoxModelWrapper implements ICodeGen {

	private ICodeGen elementEncoder;
	private ICodeGen elementDecoder;

	public ComboBoxModelWrapper() {
	}

	public ComboBoxModelWrapper(ICodeGen eEncoder, ICodeGen eDecoder) {
		this.elementEncoder = eEncoder;
		this.elementDecoder = eDecoder;
	}

	public ICodeGen getElementEncoder() {
		return elementEncoder;
	}

	public ICodeGen getElementDecoder() {
		return elementDecoder;
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}