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

package org.dyno.visual.swing.parser;

import java.beans.EventSetDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dyno.visual.swing.parser.listener.AbstractClassModel;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.ParserFactory;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * 
 * DefaultParserFactory
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class DefaultParserFactory extends ParserFactory {
	private static final String EVENT_LISTENER_MODEL_EXTENSION_ID = "org.dyno.visual.swing.parser.listenerModel";
	
	private Map<String, IConfigurationElement> listenerModels;
	private String currentListenerID;
	
	public DefaultParserFactory() {
		listenerModels = new HashMap<String, IConfigurationElement>();
		parseListenerModels();
	}
	
	@Override
	public ISourceParser newParser() {
		return new DefaultSourceParser(this);
	}
	@Override
	public IEventListenerModel newModel(WidgetAdapter adapter, EventSetDescriptor eventSet) {
		return newModel(currentListenerID, adapter, eventSet);
	}
	void parseEventListener(WidgetAdapter adapter, TypeDeclaration type, EventSetDescriptor esd) {
		Map<EventSetDescriptor, IEventListenerModel> map = adapter.getEventDescriptor();
		IEventListenerModel model = map.get(esd);
		if (model == null) {
			for (IEventListenerModel mod : enumerate(adapter, esd)) {
				if (mod instanceof AbstractClassModel) {
					AbstractClassModel classModel = (AbstractClassModel) mod;
					if (classModel.parse(type)) {
						map.put(esd, mod);
						break;
					}
				}
			}
		}
	}
	private Iterable<AbstractClassModel> enumerate(final WidgetAdapter adapter, final EventSetDescriptor eventSet) {
		return new Iterable<AbstractClassModel>() {
			@Override
			public Iterator<AbstractClassModel> iterator() {
				return new ModelIterator(adapter, eventSet);
			}
		};
	}
	private class ModelIterator implements Iterator<AbstractClassModel> {
		private Iterator<String> ids;
		private WidgetAdapter adapter;
		private EventSetDescriptor eventSet;

		public ModelIterator(WidgetAdapter adapter, EventSetDescriptor eventSet) {
			ids = listenerModels.keySet().iterator();
			this.adapter = adapter;
			this.eventSet = eventSet;
		}

		@Override
		public boolean hasNext() {
			return ids.hasNext();
		}

		@Override
		public AbstractClassModel next() {
			String id = ids.next();
			return newModel(id, adapter, eventSet);
		}

		@Override
		public void remove() {
		}
	}
	private void parseListenerModels() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(EVENT_LISTENER_MODEL_EXTENSION_ID);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseListenerModel(extensions[i]);
				}
			}
		}
	}
	private void parseListenerModel(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("model")) {
					String id = configs[i].getAttribute("id");
					listenerModels.put(id, configs[i]);
					if (i == 0)
						currentListenerID = id;
				}
			}
		}
	}
	private AbstractClassModel newModel(String id, WidgetAdapter adapter, EventSetDescriptor eventSet) {
		IConfigurationElement config = listenerModels.get(id);
		try {
			AbstractClassModel model = (AbstractClassModel) config.createExecutableExtension("class");
			model.init(adapter, eventSet);
			return model;
		} catch (CoreException e) {
			ParserPlugin.getLogger().error(e);
			return null;
		}
	}	
}

