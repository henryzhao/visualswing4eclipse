package org.dyno.visual.swing.borders;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.eclipse.jface.action.Action;

class TitledBorderSwitchAction extends Action {
	private JComponent target;

	public TitledBorderSwitchAction(JComponent w) {
		super("Bevel Border", AS_RADIO_BUTTON);
		target = w;
		setId("bevel_border_switch_action");
		Border border = w.getBorder();
		setChecked(border != null && border.getClass() == BevelBorder.class);
	}

	@Override
	public void run() {
		Border border = target.getBorder();
		if (border == null || border.getClass() != BevelBorder.class) {
			target.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			target.repaint();
		}
		setChecked(true);
	}
}
