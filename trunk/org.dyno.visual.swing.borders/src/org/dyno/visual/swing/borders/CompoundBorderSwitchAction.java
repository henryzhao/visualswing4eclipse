package org.dyno.visual.swing.borders;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.eclipse.jface.action.Action;

class CompoundBorderSwitchAction extends Action {
	private JComponent target;

	public CompoundBorderSwitchAction(JComponent w) {
		super("Compound Border", AS_RADIO_BUTTON);
		target = w;
		setId("compound_border_switch_action");
		Border border = w.getBorder();
		setChecked(border != null && border.getClass() == CompoundBorder.class);
	}

	@Override
	public void run() {
		Border border = target.getBorder();
		if (border == null || border.getClass() != CompoundBorder.class) {
			target.setBorder(BorderFactory.createCompoundBorder());
			target.repaint();
		}
		setChecked(true);
	}
}
