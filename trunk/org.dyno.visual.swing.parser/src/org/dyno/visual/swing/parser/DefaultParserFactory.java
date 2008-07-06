/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.parser;

import org.dyno.visual.swing.parser.listener.AnonymousInnerClassModel;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.ParserFactory;
/**
 * 
 * DefaultParserFactory
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class DefaultParserFactory extends ParserFactory {
	@Override
	public ISourceParser newParser() {
		return new DefaultSourceParser();
	}

	@Override
	public IEventListenerModel newDefaultListenerModel() {
		return new AnonymousInnerClassModel();
	}
}
