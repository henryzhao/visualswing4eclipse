package org.dyno.visual.swing.widgets.baseline;

import java.awt.Font;
import java.awt.FontMetrics;

public class TextWidgetBaseline extends WidgetBaseline {

	@Override
	public int getBaseline() {
		return getBaseline(adaptable.getWidget().getHeight());
	}

	private Font getButtonFont() {
		Font f = adaptable.getWidget().getFont();
		if (f == null)
			f = new Font("Dialog", 0, 12); //$NON-NLS-1$
		return f;
	}

	@Override
	public int getHeightByBaseline(int baseline) {
		FontMetrics fm = adaptable.getWidget().getFontMetrics(getButtonFont());
		return 2 * (baseline - fm.getAscent()) + fm.getHeight();
	}

	@Override
	public int getBaseline(int h) {
		FontMetrics fm = adaptable.getWidget().getFontMetrics(getButtonFont());
		return (h - fm.getHeight()) / 2 + fm.getAscent();
	}

	@Override
	public int getHeightByDescent(int descent) {
		FontMetrics fm = adaptable.getWidget().getFontMetrics(getButtonFont());
		return 2 * (descent + fm.getAscent())- fm.getHeight();
	}

}
