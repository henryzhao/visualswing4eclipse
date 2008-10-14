package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JMenuBarAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;
	public JMenuBarAdapter(){
		super("jMenuBar" + (VAR_INDEX++));
	}
	@Override
	public Component cloneWidget() {
		JMenuBar copy = (JMenuBar) super.cloneWidget();
		JMenuBar origin = (JMenuBar) getWidget();
		int count = origin.getMenuCount();
		for(int i=0;i<count;i++){
			Component child = getChild(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			copy.add(adapter.cloneWidget());
		}
		return copy;
	}
	public boolean isResizable() {
		return false;
	}
	
	@Override
	public boolean isMoveable() {
		return false;
	}
	@Override
	public int getChildCount() {
		JMenuBar origin = (JMenuBar) getWidget();
		return origin.getMenuCount();
	}

	@Override
	public Component getChild(int index) {
		JMenuBar origin = (JMenuBar) getWidget();
		return origin.getMenu(index);
	}

	@Override
	public void addChild(Component widget) {
		JMenuBar origin = (JMenuBar) getWidget();
		origin.add(widget);
	}

	@Override
	public void removeAllChild() {
		JMenuBar origin = (JMenuBar) getWidget();
		origin.removeAll();
	}

	@Override
	public boolean removeChild(Component child) {
		JMenuBar origin = (JMenuBar) getWidget();
		origin.remove(child);
		return true;
	}

	@Override
	protected Component createWidget() {
		JMenuBar jmb = new JMenuBar();
		WidgetAdapter menuAdapter = ExtensionRegistry.createWidgetAdapter(JMenu.class);
		JMenu jmenu=(JMenu)menuAdapter.getWidget();
		jmenu.setText("File");
		jmb.add(jmenu);
		jmb.setSize(100, 25);
		jmb.doLayout();
		return jmb;
	}

	@Override
	protected Component newWidget() {
		return new JMenuBar();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
	}
	@Override
	public Object getChildConstraints(Component child) {
		return null;
	}
	@Override
	public boolean dragEnter(Point p) {
		setMascotLocation(p);
		allow_dropping=isDroppingMenu();
		if(isDroppingMenu()){
		}else{
		}
		return true;
	}
	@Override
	public boolean dragExit(Point p) {
		setMascotLocation(p);
		allow_dropping=isDroppingMenu();
		if(isDroppingMenu()){
		}else{
		}
		return true;
	}
	@Override
	public boolean dragOver(Point p) {
		setMascotLocation(p);
		allow_dropping=isDroppingMenu();
		if(isDroppingMenu()){
		}else{
		}
		return true;
	}
	@Override
	public boolean drop(Point p) {
		setMascotLocation(p);
		allow_dropping=isDroppingMenu();
		if(isDroppingMenu()){
			WidgetAdapter menuAdapter = getDropWidget();
			JMenu jmenu=(JMenu)menuAdapter.getWidget();
			JMenuBar jmb=(JMenuBar)getWidget();
			jmb.add(jmenu);
			jmb.validate();
			jmb.doLayout();
			clearAllSelected();
			menuAdapter.setSelected(true);
			addNotify();
			repaintDesigner();
		}else{
		}
		return true;
	}
	private boolean allow_dropping;
	@Override
	public void paintFocused(Graphics clipg) {
		if(allow_dropping){
			
		}else{
			
		}
	}
	private boolean isDroppingMenu(){
		WidgetAdapter target = super.getDropWidget();
		Component drop = target.getWidget();
		return drop instanceof JMenu;
	}
	
}
