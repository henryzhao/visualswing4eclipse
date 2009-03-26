package org.dyno.visual.swing.widgets.menucontext;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.widgets.Messages;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.Window;

public class JTabbedPaneMenuContext extends JComponentMenuContext {

	@Override
	public void fillConstraintsAction(MenuManager menu, Component child) {
		Action tabPropAction = new TabPropertiesAction(child);
		menu.add(tabPropAction);
	}
	class TabPropertiesAction extends Action{
		private Component child;
		TabPropertiesAction(Component child){
			super(Messages.JTabbedPaneMenuContext_Tab_Properties);
			this.child = child;
		}
	
		@Override
		public void run() {
			JTabbedPane jtp=(JTabbedPane) adaptable.getWidget();
			int index = jtp.indexOfComponent(child);
			if(index!=-1){
				TabPropertiesDialog tpd = new TabPropertiesDialog(JavaUtil.getEclipseShell());
				tpd.setTitle(jtp.getTitleAt(index));
				tpd.setTooltip(jtp.getToolTipTextAt(index));
				Icon icon = jtp.getIconAt(index);
				IFile imgFile = JavaUtil.getIconFile(icon);
				tpd.setIcon(imgFile);
				if(tpd.open()==Window.OK){
					jtp.setTitleAt(index, tpd.getTitle());
					jtp.setToolTipTextAt(index, tpd.getTooltip());
					icon = JavaUtil.getIconFromFile(tpd.getIcon());
					jtp.setIconAt(index, icon);
					adaptable.setDirty(true);
					adaptable.repaintDesigner();
				}
			}
		}		
	}
}
