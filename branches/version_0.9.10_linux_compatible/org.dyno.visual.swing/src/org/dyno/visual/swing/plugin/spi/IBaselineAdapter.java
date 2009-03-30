package org.dyno.visual.swing.plugin.spi;

public interface IBaselineAdapter {
	int getBaseline();
	int getBaseline(int h);
	int getHeightByBaseline(int baseline);
	int getHeightByDescent(int descent);
}
