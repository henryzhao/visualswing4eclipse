package org.dyno.visual.swing.widgets.baseline;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IBaselineAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class WidgetBaseline implements IBaselineAdapter, IAdaptableContext {
	protected WidgetAdapter adaptable;

	public int getBaseline() {
		return adaptable.getWidget().getHeight() / 2;
	}

	public int getBaseline(int h) {
		return h / 2;
	}

	public int getHeightByBaseline(int baseline) {
		return 2 * baseline;
	}
	public int getHeightByDescent(int descent) {
		return 2 * descent;
	}

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}
}
