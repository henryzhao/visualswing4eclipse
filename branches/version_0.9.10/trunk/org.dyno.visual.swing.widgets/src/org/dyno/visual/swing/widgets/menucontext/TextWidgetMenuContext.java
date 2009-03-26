package org.dyno.visual.swing.widgets.menucontext;

import org.dyno.visual.swing.widgets.Messages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

public class TextWidgetMenuContext extends JComponentMenuContext {
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
