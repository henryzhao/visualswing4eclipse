package org.dyno.visual.swing.borders;

import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import org.eclipse.jface.action.Action;

class SoftBevelBorderSwitchAction extends Action {
	private JComponent target;

	public SoftBevelBorderSwitchAction(JComponent w) {
		super("Soft Bevel Border", AS_RADIO_BUTTON);
		target = w;
		setId("soft_bevel_border_switch_action");
		Border border = w.getBorder();
		setChecked(border != null && border.getClass() == SoftBevelBorder.class);
	}

	@Override
	public void run() {
		Border border = target.getBorder();
		if (border == null || border.getClass() != SoftBevelBorder.class) {
			target.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
			target.repaint();
		}
		setChecked(true);
	}
}
