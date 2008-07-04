/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.items;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;

import org.dyno.visual.swing.base.FactoryItem;
import org.dyno.visual.swing.base.IFactory;
import org.dyno.visual.swing.base.IFactoryProvider;
import org.dyno.visual.swing.layouts.GroupLayout;

public class LayoutItems implements IFactoryProvider {
	private FactoryItem[] ITEMS = { 
		new FactoryItem("null", null, new IFactory(){
			@Override
			public Object newInstance(Object bean) {
				return null;
			}}),
		new FactoryItem("BorderLayout", BorderLayout.class, new IFactory() {
		@Override
		public Object newInstance(Object bean) {
			return new BorderLayout();
		}
	}), new FactoryItem("FlowLayout", FlowLayout.class, new IFactory() {
		@Override
		public Object newInstance(Object bean) {
			return new FlowLayout();
		}
	}), new FactoryItem("GridLayout", GridLayout.class, new IFactory() {
		@Override
		public Object newInstance(Object bean) {
			return new GridLayout();
		}
	}), new FactoryItem("CardLayout", CardLayout.class, new IFactory() {

		@Override
		public Object newInstance(Object bean) {
			return new CardLayout();
		}
	}), new FactoryItem("BoxLayout", BoxLayout.class, new IFactory() {

		@Override
		public Object newInstance(Object bean) {
			return new BoxLayout((Container) bean, BoxLayout.X_AXIS);
		}
	}), new FactoryItem("GridBagLayout", GridBagLayout.class, new IFactory() {

		@Override
		public Object newInstance(Object bean) {
			return new GridBagLayout();
		}
	}), new FactoryItem("GroupLayout", GroupLayout.class, new IFactory() {

		@Override
		public Object newInstance(Object bean) {
			return new GroupLayout();
		}
	}), };

	@Override
	public FactoryItem[] getItems() {
		return ITEMS;
	}

	@Override
	public boolean isSelected(FactoryItem item, Object value) {
		return item.isSelected(value);
	}
}
