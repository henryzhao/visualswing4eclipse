package org.dyno.visual.swing.widgets;

import javax.swing.ButtonGroup;

import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.eclipse.swt.graphics.Image;

public class ButtonGroupAdapter implements InvisibleAdapter {
	private static final String BUTTON_GROUP_ICON = "/icons/button_group_16.png";
	private static int VAR_INDEX=0;
	private static Image iconImage;
	static{
		iconImage = WidgetPlugin.getSharedImage(BUTTON_GROUP_ICON);
	}
	private String name;
	private ButtonGroup group;
	public ButtonGroupAdapter(){
		name = "buttonGroup"+(VAR_INDEX++);
		group = new ButtonGroup();
		
	}
	public ButtonGroup getButtonGroup(){
		return group;
	}
	@Override
	public Image getIconImage() {
		return iconImage;
	}

	@Override
	public String getName() {
		return name;
	}

}
