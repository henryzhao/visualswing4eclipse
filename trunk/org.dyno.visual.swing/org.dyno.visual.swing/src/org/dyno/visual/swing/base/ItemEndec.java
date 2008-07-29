/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.base;

import javax.xml.bind.ValidationException;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * ItemEndec
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class ItemEndec implements ICodeGen {

	private Item[] items;

	public ItemEndec(ItemProvider provider) {
		this(provider.getItems());
	}

	public ItemEndec(Item[] items) {
		this.items = items;
	}

	public Object decode(String txt) {
		for (Item item : items) {
			if (txt.equals(item.getName())) {
				return item.getValue();
			}
		}
		return null;
	}

	public String encode(Object v) {
		for (Item item : items) {
			if (item.getValue().equals(v)) {
				return item.getName();
			}
		}
		return null;
	}

	public void validateValue(String txt) throws Exception {
		for (Item item : items) {
			if (txt.equals(item.getName())) {
				return;
			}
		}
		throw new ValidationException("No such element:" + txt);
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		for (Item item : items) {
			if (item.getValue().equals(value)) {
				return item.getCode(imports);
			}
		}
		return value.toString();
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}