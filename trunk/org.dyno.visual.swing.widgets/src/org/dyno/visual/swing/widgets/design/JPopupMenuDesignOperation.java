package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JMenuAdapter;

public class JPopupMenuDesignOperation extends CompositeDesignOperation {
	private int insert_y;
	public int getInsert_y() {
		return insert_y;
	}

	private int insert_index;
	private int calculateInsert(Point p){
		JPopupMenu popup=(JPopupMenu)adaptable.getWidget();
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
		adaptable.setMascotLocation(p);
		insert_y=calculateInsert(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		adaptable.setMascotLocation(p);
		return true;
	}
	@Override
	public boolean dragOver(Point p) {
		adaptable.setMascotLocation(p);
		insert_y=calculateInsert(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		adaptable.setMascotLocation(p);
		List<WidgetAdapter> targets = adaptable.getDropWidget();
		WidgetAdapter target = targets.get(0);
		if(targets.size()!=1)
			return false;
		if(insert_index==-1){
			JPopupMenu popup=(JPopupMenu)adaptable.getWidget();
			JMenu jmenu=(JMenu)popup.getInvoker();
			JMenuAdapter jma=(JMenuAdapter)WidgetAdapter.getWidgetAdapter(jmenu);
			jmenu.add((JMenuItem)target.getWidget());
			target.requestNewName();
			MouseInputListener l=(MouseInputListener) jma.getAdapter(MouseInputListener.class);
			if(l!=null){
				l.mousePressed(null);
				l.mousePressed(null);
			}
		}else{
			JPopupMenu popup=(JPopupMenu)adaptable.getWidget();
			JMenu jmenu=(JMenu)popup.getInvoker();
			JMenuAdapter jma=(JMenuAdapter)WidgetAdapter.getWidgetAdapter(jmenu);
			jmenu.add(target.getParentContainer(), insert_index);
			target.requestNewName();
			MouseInputListener l=(MouseInputListener) jma.getAdapter(MouseInputListener.class);
			if(l!=null){
				l.mousePressed(null);
				l.mousePressed(null);
			}
		}
		return true;
	}
}
