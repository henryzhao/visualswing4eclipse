package org.dyno.visual.swing.borders;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.eclipse.jface.action.Action;

class EmptyBorderSwitchAction extends Action {
	private JComponent target;

	public EmptyBorderSwitchAction(JComponent w) {
		super("Empty Border", AS_RADIO_BUTTON);
		target = w;
		setId("empty_border_switch_action");
		Border border = w.getBorder();
		setChecked(border != null && border.getClass() == EmptyBorder.class);
	}

	@Override
	public void run() {
		Border border = target.getBorder();
		if (border == null || border.getClass() != EmptyBorder.class) {
			target.setBorder(BorderFactory.createEmptyBorder());
			target.repaint();
		}
		setChecked(true);
	}
}
