package org.dyno.visual.swing.widgets.menucontext;

import org.dyno.visual.swing.base.WidgetMenuContext;
import org.dyno.visual.swing.widgets.Messages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

public class TextWidgetMenuContext extends WidgetMenuContext {
	class TextEditingAction extends Action {
		public TextEditingAction() {
			setText(Messages.TextWidgetAdapter_Edit_Text);
			setId("EditingTextId"); //$NON-NLS-1$
		}

		public void run() {
			adaptable.editValue();
		}
	}
	@Override
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		menu.add(new TextEditingAction());
	}
}
