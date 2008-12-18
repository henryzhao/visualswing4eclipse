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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JPopupMenuAdapter extends CompositeAdapter {
	public JPopupMenuAdapter(){
	}
	@Override
	public Component cloneWidget() {
		JPopupMenu copy = (JPopupMenu) super.cloneWidget();
		JPopupMenu origin = (JPopupMenu) getWidget();
		int count = origin.getComponentCount();
		for(int i=0;i<count;i++){
			Component child = origin.getComponent(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			copy.add(adapter.cloneWidget());
		}
		return copy;
	}

	@Override
	public Component getChild(int index) {
		JPopupMenu origin = (JPopupMenu) getWidget();
		return origin.getComponent(index);
	}

	@Override
	public int getChildCount() {
		JPopupMenu origin = (JPopupMenu) getWidget();
		return origin.getComponentCount();
	}

	@Override
	public int getIndexOfChild(Component child) {
		int count = getChildCount();
		for(int i=0;i<count;i++){
			if(getChild(i)==child)
				return i;
		}
		return -1;
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
	}

	@Override
	public Object getChildConstraints(Component child) {
		return null;
	}

	@Override
	protected Component createWidget() {
		JPopupMenu menu = new JPopupMenu();
		menu.setSize(new Dimension(72,100));
		return menu;
	}

	@Override
	protected Component newWidget() {
		return new JPopupMenu();
	}
	private int insert_y;
	private int insert_index;
	private int calculateInsert(Point p){
		JPopupMenu popup=(JPopupMenu)getWidget();
		JMenu jmenu=(JMenu)popup.getInvoker();
		int count=jmenu.getMenuComponentCount();
		int caly=0;
		for(int i=0;i<count;i++){
			Component jmu=jmenu.getMenuComponent(i);
			if(p.y>=caly-jmu.getHeight()/2&&p.y<caly+jmu.getHeight()/2){
				insert_index=i;
				insert_y=caly;
				return caly;
			}
			caly+=jmu.getHeight();
		}
		insert_index=-1;
		insert_y=caly;
		return caly;
	}
	@Override
	public boolean dragEnter(Point p) {
		setMascotLocation(p);
		insert_y=calculateInsert(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		setMascotLocation(p);
		return true;
	}
	public CompositeAdapter getParentAdapter() {
		JPopupMenu jpopup=(JPopupMenu)getWidget();
		Component parent=jpopup.getInvoker();
		return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(parent);
	}
	@Override
	public boolean dragOver(Point p) {
		setMascotLocation(p);
		insert_y=calculateInsert(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		setMascotLocation(p);
		List<WidgetAdapter> targets = getDropWidget();
		WidgetAdapter target = targets.get(0);
		if(targets.size()!=1)
			return false;
		if(insert_index==-1){
			JPopupMenu popup=(JPopupMenu)getWidget();
			JMenu jmenu=(JMenu)popup.getInvoker();
			JMenuAdapter jma=(JMenuAdapter)WidgetAdapter.getWidgetAdapter(jmenu);
			jmenu.add((JMenuItem)target.getWidget());			
			jma.widgetPressed(null);
			jma.widgetPressed(null);
		}else{
			JPopupMenu popup=(JPopupMenu)getWidget();
			JMenu jmenu=(JMenu)popup.getInvoker();
			JMenuAdapter jma=(JMenuAdapter)WidgetAdapter.getWidgetAdapter(jmenu);
			jmenu.add(target.getComponent(), insert_index);
			jma.widgetPressed(null);
			jma.widgetPressed(null);
		}
		return true;
	}

	@Override
	public void paintFocused(Graphics g) {
		JPopupMenu jpm=(JPopupMenu)getWidget();
		Rectangle bounds = jpm.getBounds();
		Point lt=convertToGlobal(new Point(0,0));
		bounds.x=lt.x;
		bounds.y=lt.y;
		Graphics clipg=g.create(bounds.x, bounds.y, bounds.width, bounds.height);
		Graphics2D g2d = (Graphics2D) clipg;
		g2d.setStroke(STROKE);
		g2d.setColor(GREEN_COLOR);
		g2d.drawLine(0,insert_y, jpm.getWidth(), insert_y);
		clipg.dispose();
	}
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;

	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 4 }, 0);
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JPopupMenu.class;
	}
	
}

