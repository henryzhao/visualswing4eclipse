/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.parser;

import java.beans.EventSetDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.ParserFactory;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
/**
 * 
 * DefaultParserFactory
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class DefaultParserFactory extends ParserFactory {
	private static final String EVENT_LISTENER_MODEL_EXTENSION_ID="org.dyno.visual.swing.parser.listenerModel";
	private Map<String, IConfigurationElement> listenerModels;
	private String currentListenerID;
	public DefaultParserFactory(){
		listenerModels = new HashMap<String, IConfigurationElement>();
		parseListenerModels();
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
					if(i==0)
						currentListenerID = id;
				}
			}
		}
	}	
	@Override
	public ISourceParser newParser() {
		return new DefaultSourceParser(this);
	}
	public void setCurrentListenerID(String id){
		currentListenerID = id;
	}
	@Override
	public IEventListenerModel newModel(WidgetAdapter adapter, EventSetDescriptor eventSet) {
		return newModel(currentListenerID, adapter, eventSet);
	}
	private IEventListenerModel newModel(String id, WidgetAdapter adapter, EventSetDescriptor eventSet){
		IConfigurationElement config = listenerModels.get(id);
		try {
			IEventListenerModel model = (IEventListenerModel)config.createExecutableExtension("class");
			model.init(adapter, eventSet);
			return model;
		} catch (CoreException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Iterable<IEventListenerModel> enumerate(final WidgetAdapter adapter, final EventSetDescriptor eventSet) {		
		return new Iterable<IEventListenerModel>(){
			@Override
			public Iterator<IEventListenerModel> iterator() {
				return new ModelIterator(adapter, eventSet);
			}};
	}
	class ModelIterator implements Iterator<IEventListenerModel>{
		private Iterator<String> ids;
		private WidgetAdapter adapter;
		private EventSetDescriptor eventSet;
		public ModelIterator(WidgetAdapter adapter, EventSetDescriptor eventSet){
			ids=listenerModels.keySet().iterator();
			this.adapter = adapter;
			this.eventSet = eventSet;
		}
		@Override
		public boolean hasNext() {
			return ids.hasNext();
		}

		@Override
		public IEventListenerModel next() {
			String id = ids.next();
			return newModel(id, adapter, eventSet);
		}

		@Override
		public void remove() {
		}
	}
}
