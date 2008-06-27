package org.dyno.visual.swing.borders;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import org.eclipse.jface.action.Action;

class MatteBorderSwitchAction extends Action {
	private JComponent target;

	public MatteBorderSwitchAction(JComponent w) {
		super("Matte Border", AS_RADIO_BUTTON);
		target = w;
		setId("matte_border_switch_action");
		Border border = w.getBorder();
		setChecked(border != null && border.getClass() == MatteBorder.class);
	}

	@Override
	public void run() {
		Border border = target.getBorder();
		if (border == null || border.getClass() != MatteBorder.class) {
			target.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.black));
			target.repaint();
		}
		setChecked(true);
	}
}
