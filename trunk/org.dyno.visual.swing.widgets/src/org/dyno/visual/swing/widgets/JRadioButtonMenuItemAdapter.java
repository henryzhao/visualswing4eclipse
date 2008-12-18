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

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JRadioButtonMenuItemAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;
	public JRadioButtonMenuItemAdapter(){
		super("jRadioButtonMenuItem" + (VAR_INDEX++));
	}

	@Override
	protected Component createWidget() {
		JRadioButtonMenuItem jmi = new JRadioButtonMenuItem();
		jmi.setText("radio button item");
		jmi.setSize(jmi.getPreferredSize());
		jmi.doLayout();
		return jmi;
	}
	public boolean isMoveable() {
		return true;
	}
	public boolean isResizable() {
		return false;
	}
	@Override
	protected Component newWidget() {
		return new JRadioButtonMenuItem();
	}
	public CompositeAdapter getParentAdapter() {
		Component me = getWidget();
		JPopupMenu jpm = (JPopupMenu) me.getParent();
		Component parent = jpm.getInvoker();
		return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(parent);
	}
	@Override
	public Component cloneWidget() {
		JRadioButtonMenuItem jmi = (JRadioButtonMenuItem) super.cloneWidget();
		JRadioButtonMenuItem origin = (JRadioButtonMenuItem)getWidget();
		jmi.setText(origin.getText());
		jmi.setSelected(origin.isSelected());
		return jmi;
	}
	
	private LabelEditor editor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (editor == null) {
			editor = new LabelEditor();
		}
		return editor;
	}

	@Override
	public Object getWidgetValue() {
		Component me = getWidget();
		JMenuItem jmi = (JMenuItem)me;
		return jmi.getText();
	}

	@Override
	public void setWidgetValue(Object value) {
		Component me = getWidget();
		JMenuItem jmi = (JMenuItem)me;
		jmi.setText(value==null?"":value.toString());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		Component widget = getWidget();
		FontMetrics fm = widget.getFontMetrics(widget.getFont());
		int fh = fm.getHeight() + VER_TEXT_PAD;
		Component me = getWidget();
		JMenuItem jmi = (JMenuItem)me;
		int fw = fm.stringWidth(jmi.getText()) + HOR_TEXT_PAD;
		int fx = (w - fw) / 2;
		int fy = (h - fh) / 2;
		return new Rectangle(fx, fy, fw, fh);
	}
	private static final int HOR_TEXT_PAD = 20;
	private static final int VER_TEXT_PAD = 4;

	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JRadioButtonMenuItem.class;
	}
	@Override
	public IAdapter getParent() {
		JRadioButtonMenuItem jb = (JRadioButtonMenuItem) getWidget();		
		DefaultButtonModel dbm = (DefaultButtonModel) jb.getModel();
		ButtonGroup bg = dbm.getGroup();
		if (bg != null) {
			for (InvisibleAdapter invisible : getRootAdapter().getInvisibles()) {
				if (invisible instanceof ButtonGroupAdapter) {
					if (bg == ((ButtonGroupAdapter) invisible).getButtonGroup())
						return invisible;
				}
			}
		}
		return super.getParent();
	}
	@Override
	public void deleteNotify() {
		JRadioButtonMenuItem jb = (JRadioButtonMenuItem) getWidget();
		DefaultButtonModel dbm = (DefaultButtonModel) jb.getModel();
		ButtonGroup bg = dbm.getGroup();
		if(bg!=null){
			bg.remove(jb);
		}
	}	
}

