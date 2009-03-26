package org.dyno.visual.swing.lnfs.lib;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.LayoutStyle;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;

public class DelegateLookAndFeel extends LookAndFeel{
	private LookAndFeel instance;
	private ClassLoader loader;
	public DelegateLookAndFeel(LookAndFeel instance){
		this.instance = instance;
		this.loader = instance.getClass().getClassLoader();
	}
	@Override
	public UIDefaults getDefaults() {
		UIDefaults defaults = instance.getDefaults();
		defaults.put("ClassLoader", loader);
		return defaults;
	}

	@Override
	public Icon getDisabledIcon(JComponent component, Icon icon) {
		return instance.getDisabledIcon(component, icon);
	}

	@Override
	public Icon getDisabledSelectedIcon(JComponent component, Icon icon) {
		return instance.getDisabledSelectedIcon(component, icon);
	}

	@Override
	public LayoutStyle getLayoutStyle() {
		return instance.getLayoutStyle();
	}

	@Override
	public boolean getSupportsWindowDecorations() {
		return instance.getSupportsWindowDecorations();
	}

	@Override
	public void initialize() {
		instance.initialize();
	}

	@Override
	public void provideErrorFeedback(Component component) {
		instance.provideErrorFeedback(component);
	}

	@Override
	public String toString() {
		return instance.toString();
	}

	@Override
	public void uninitialize() {
		instance.uninitialize();
	}

	@Override
	public String getDescription() {
		return instance.getDescription();
	}

	@Override
	public String getID() {
		return instance.getID();
	}

	@Override
	public String getName() {
		return instance.getName();
	}

	@Override
	public boolean isNativeLookAndFeel() {
		return instance.isNativeLookAndFeel();
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		return instance.isSupportedLookAndFeel();
	}
	
}