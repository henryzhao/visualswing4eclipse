/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.dyno.visual.swing.lnfs.meta.MetaJAppletValue;
import org.dyno.visual.swing.lnfs.meta.MetaJButtonValue;
import org.dyno.visual.swing.lnfs.meta.MetaJCheckBoxValue;
import org.dyno.visual.swing.lnfs.meta.MetaJComboBoxValue;
import org.dyno.visual.swing.lnfs.meta.MetaJDesktopPaneValue;
import org.dyno.visual.swing.lnfs.meta.MetaJDialogValue;
import org.dyno.visual.swing.lnfs.meta.MetaJEditorPaneValue;
import org.dyno.visual.swing.lnfs.meta.MetaJFormattedTextFieldValue;
import org.dyno.visual.swing.lnfs.meta.MetaJFrameValue;
import org.dyno.visual.swing.lnfs.meta.MetaJInternalFrameValue;
import org.dyno.visual.swing.lnfs.meta.MetaJLabelValue;
import org.dyno.visual.swing.lnfs.meta.MetaJListValue;
import org.dyno.visual.swing.lnfs.meta.MetaJPanelValue;
import org.dyno.visual.swing.lnfs.meta.MetaJPasswordFieldValue;
import org.dyno.visual.swing.lnfs.meta.MetaJProgressBarValue;
import org.dyno.visual.swing.lnfs.meta.MetaJRadioButtonValue;
import org.dyno.visual.swing.lnfs.meta.MetaJScrollBarValue;
import org.dyno.visual.swing.lnfs.meta.MetaJScrollPaneValue;
import org.dyno.visual.swing.lnfs.meta.MetaJSeparatorValue;
import org.dyno.visual.swing.lnfs.meta.MetaJSliderValue;
import org.dyno.visual.swing.lnfs.meta.MetaJSpinnerValue;
import org.dyno.visual.swing.lnfs.meta.MetaJSplitPaneValue;
import org.dyno.visual.swing.lnfs.meta.MetaJTabbedPaneValue;
import org.dyno.visual.swing.lnfs.meta.MetaJTableValue;
import org.dyno.visual.swing.lnfs.meta.MetaJTextAreaValue;
import org.dyno.visual.swing.lnfs.meta.MetaJTextFieldValue;
import org.dyno.visual.swing.lnfs.meta.MetaJTextPaneValue;
import org.dyno.visual.swing.lnfs.meta.MetaJToggleButtonValue;
import org.dyno.visual.swing.lnfs.meta.MetaJToolBarValue;
import org.dyno.visual.swing.lnfs.meta.MetaJTreeValue;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;

@SuppressWarnings("unchecked")
public class MetaLookAndFeelAdapter extends HashMap<Class, WidgetValue> implements ILookAndFeelAdapter {
	private static final long serialVersionUID = 1L;

	public MetaLookAndFeelAdapter() {
		put(JPanel.class, new MetaJPanelValue());
		put(JApplet.class, new MetaJAppletValue());
		put(JButton.class, new MetaJButtonValue());
		put(JLabel.class, new MetaJLabelValue());
		put(JToggleButton.class, new MetaJToggleButtonValue());
		put(JCheckBox.class, new MetaJCheckBoxValue());
		put(JRadioButton.class, new MetaJRadioButtonValue());
		put(JComboBox.class, new MetaJComboBoxValue());
		put(JList.class, new MetaJListValue());
		put(JTextField.class, new MetaJTextFieldValue());
		put(JScrollBar.class, new MetaJScrollBarValue());
		put(JSlider.class, new MetaJSliderValue());
		put(JProgressBar.class, new MetaJProgressBarValue());
		put(JFormattedTextField.class, new MetaJFormattedTextFieldValue());
		put(JPasswordField.class, new MetaJPasswordFieldValue());
		put(JSeparator.class, new MetaJSeparatorValue());
		put(JScrollPane.class, new MetaJScrollPaneValue());
		put(JTextArea.class, new MetaJTextAreaValue());
		put(JTextPane.class, new MetaJTextPaneValue());
		put(JEditorPane.class, new MetaJEditorPaneValue());
		put(JTree.class, new MetaJTreeValue());
		put(JTabbedPane.class, new MetaJTabbedPaneValue());
		put(JSplitPane.class, new MetaJSplitPaneValue());
		put(JTable.class, new MetaJTableValue());
		put(JSpinner.class, new MetaJSpinnerValue());
		put(JToolBar.class, new MetaJToolBarValue());
		put(JDesktopPane.class, new MetaJDesktopPaneValue());
		put(JInternalFrame.class, new MetaJInternalFrameValue());
		put(JFrame.class, new MetaJFrameValue());
		put(JDialog.class, new MetaJDialogValue());
	}

	@Override
	public Object getDefaultValue(Class beanClass, String propertyName) {
		WidgetValue widget = get(beanClass);
		if (widget == null && beanClass != Component.class) {
			return getDefaultValue(beanClass.getSuperclass(), propertyName);
		} else if(widget!=null)
			return widget.getDefaultValue(propertyName);
		else
			return null;
	}
	private LookAndFeel metalLnf;
	@Override
	public LookAndFeel getLookAndFeelInstance() {
		if(metalLnf==null){
			metalLnf=new MetalLookAndFeel();
		}
		return metalLnf;
	}
}
