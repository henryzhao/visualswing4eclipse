package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;
import java.awt.Font;

import javax.swing.event.ChangeListener;

public interface Editor {
	void validateValue() throws Exception;

	void setFocus();

	Object getValue();

	void setValue(Object v);

	Component getComponent();

	void addChangeListener(ChangeListener l);

	void removeChangeListener(ChangeListener l);
	
	void setFont(Font f);
}
