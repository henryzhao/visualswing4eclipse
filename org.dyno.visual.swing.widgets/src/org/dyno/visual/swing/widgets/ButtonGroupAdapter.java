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

package org.dyno.visual.swing.widgets;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.swt.graphics.Image;

public class ButtonGroupAdapter extends InvisibleAdapter {
	private static final String BUTTON_GROUP_ICON = "/icons/button_group_16.png";
	static Image BUTTON_GROUP_SWT_ICON_IMAGE;
	static{
		BUTTON_GROUP_SWT_ICON_IMAGE = WidgetPlugin.getSharedImage(BUTTON_GROUP_ICON);
	}
	private ButtonGroup group;
	private WidgetAdapter root;
	public ButtonGroupAdapter(){}
	public ButtonGroupAdapter(WidgetAdapter root){
		this.root = root;
		group = new ButtonGroup();		
		setName(root.getNamespace().nextName(getBasename()));
	}
	public ButtonGroup getButtonGroup(){
		return group;
	}
	@Override
	public Image getIconImage() {
		return BUTTON_GROUP_SWT_ICON_IMAGE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getElements() {
		Enumeration<AbstractButton> elements = this.group.getElements();
		List list=new ArrayList();
		while(elements.hasMoreElements()){
			AbstractButton aButton = elements.nextElement();
			list.add(WidgetAdapter.getWidgetAdapter(aButton));
		}
		return list;
	}
	@Override
	public IAdapter getParent() {
		return root;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Class getObjectClass() {
		return ButtonGroup.class;
	}
	@Override
	public void init(WidgetAdapter rootAdapter, String name, Object object) {
		super.init(rootAdapter, name, object);
		this.group = (ButtonGroup) object;
		this.root = rootAdapter;
	}
	@Override
	public String getBasename() {
		return "buttonGroup";
	}
	@Override
	public void requestNewName() {
	}
}

