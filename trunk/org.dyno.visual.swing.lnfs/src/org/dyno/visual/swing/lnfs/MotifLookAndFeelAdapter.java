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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
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

import org.dyno.visual.swing.lnfs.motif.JButtonValue;
import org.dyno.visual.swing.lnfs.motif.JCheckBoxValue;
import org.dyno.visual.swing.lnfs.motif.JComboBoxValue;
import org.dyno.visual.swing.lnfs.motif.JDesktopPaneValue;
import org.dyno.visual.swing.lnfs.motif.JEditorPaneValue;
import org.dyno.visual.swing.lnfs.motif.JFormattedTextFieldValue;
import org.dyno.visual.swing.lnfs.motif.JFrameValue;
import org.dyno.visual.swing.lnfs.motif.JInternalFrameValue;
import org.dyno.visual.swing.lnfs.motif.JLabelValue;
import org.dyno.visual.swing.lnfs.motif.JListValue;
import org.dyno.visual.swing.lnfs.motif.JPanelValue;
import org.dyno.visual.swing.lnfs.motif.JPasswordFieldValue;
import org.dyno.visual.swing.lnfs.motif.JProgressBarValue;
import org.dyno.visual.swing.lnfs.motif.JRadioButtonValue;
import org.dyno.visual.swing.lnfs.motif.JScrollBarValue;
import org.dyno.visual.swing.lnfs.motif.JScrollPaneValue;
import org.dyno.visual.swing.lnfs.motif.JSeparatorValue;
import org.dyno.visual.swing.lnfs.motif.JSliderValue;
import org.dyno.visual.swing.lnfs.motif.JSpinnerValue;
import org.dyno.visual.swing.lnfs.motif.JSplitPaneValue;
import org.dyno.visual.swing.lnfs.motif.JTabbedPaneValue;
import org.dyno.visual.swing.lnfs.motif.JTableValue;
import org.dyno.visual.swing.lnfs.motif.JTextAreaValue;
import org.dyno.visual.swing.lnfs.motif.JTextFieldValue;
import org.dyno.visual.swing.lnfs.motif.JTextPaneValue;
import org.dyno.visual.swing.lnfs.motif.JToggleButtonValue;
import org.dyno.visual.swing.lnfs.motif.JToolBarValue;
import org.dyno.visual.swing.lnfs.motif.JTreeValue;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;

@SuppressWarnings("unchecked")
public class MotifLookAndFeelAdapter extends HashMap<Class, WidgetValue> implements ILookAndFeelAdapter {
	private static final long serialVersionUID = 1L;

	public MotifLookAndFeelAdapter() {
		put(JPanel.class, new JPanelValue());
		put(JFrame.class, new JFrameValue());
		put(JButton.class, new JButtonValue());
		put(JLabel.class, new JLabelValue());
		put(JToggleButton.class, new JToggleButtonValue());
		put(JCheckBox.class, new JCheckBoxValue());
		put(JRadioButton.class, new JRadioButtonValue());
		put(JComboBox.class, new JComboBoxValue());
		put(JList.class, new JListValue());
		put(JTextField.class, new JTextFieldValue());
		put(JScrollBar.class, new JScrollBarValue());
		put(JSlider.class, new JSliderValue());
		put(JProgressBar.class, new JProgressBarValue());
		put(JFormattedTextField.class, new JFormattedTextFieldValue());
		put(JPasswordField.class, new JPasswordFieldValue());
		put(JSeparator.class, new JSeparatorValue());
		put(JScrollPane.class, new JScrollPaneValue());
		put(JTextArea.class, new JTextAreaValue());
		put(JTextPane.class, new JTextPaneValue());
		put(JEditorPane.class, new JEditorPaneValue());
		put(JTree.class, new JTreeValue());
		put(JTabbedPane.class, new JTabbedPaneValue());
		put(JSplitPane.class, new JSplitPaneValue());
		put(JTable.class, new JTableValue());
		put(JSpinner.class, new JSpinnerValue());
		put(JToolBar.class, new JToolBarValue());
		put(JDesktopPane.class, new JDesktopPaneValue());
		put(JInternalFrame.class, new JInternalFrameValue());
	}

	@Override
	public Object getDefaultValue(Class beanClass, String propertyName) {
		WidgetValue widget = get(beanClass);
		if (widget == null && beanClass != Component.class) {
			return getDefaultValue(beanClass.getSuperclass(), propertyName);
		} else if (widget != null) {
			return widget.getDefaultValue(propertyName);
		} else
			return null;
	}
	private LookAndFeel metalLnf;
	@Override
	public LookAndFeel getLookAndFeelInstance() {
		if(metalLnf==null){
			try {
				metalLnf=(LookAndFeel) Class.forName("com.sun.java.swing.plaf.motif.MotifLookAndFeel").newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return metalLnf;
	}	
}
