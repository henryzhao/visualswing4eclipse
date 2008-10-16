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
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
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

import org.dyno.visual.swing.lnfs.windowsxp.ClassicJAppletValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJButtonValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJCheckBoxMenuItemValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJCheckBoxValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJComboBoxValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJDesktopPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJDialogValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJEditorPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJFormattedTextFieldValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJFrameValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJInternalFrameValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJLabelValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJListValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJMenuBarValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJMenuItemValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJMenuValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJPanelValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJPasswordFieldValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJProgressBarValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJRadioButtonMenuItemValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJRadioButtonValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJScrollBarValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJScrollPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJSeparatorValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJSliderValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJSpinnerValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJSplitPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJTabbedPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJTableValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJTextAreaValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJTextFieldValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJTextPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJToggleButtonValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJToolBarValue;
import org.dyno.visual.swing.lnfs.windowsxp.ClassicJTreeValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJAppletValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJButtonValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJCheckBoxMenuItemValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJCheckBoxValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJComboBoxValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJDesktopPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJDialogValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJEditorPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJFormattedTextFieldValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJFrameValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJInternalFrameValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJLabelValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJListValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJMenuBarValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJMenuItemValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJMenuValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJPanelValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJPasswordFieldValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJProgressBarValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJRadioButtonMenuItemValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJRadioButtonValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJScrollBarValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJScrollPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJSeparatorValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJSliderValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJSpinnerValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJSplitPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJTabbedPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJTableValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJTextAreaValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJTextFieldValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJTextPaneValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJToggleButtonValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJToolBarValue;
import org.dyno.visual.swing.lnfs.windowsxp.XpJTreeValue;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;

public class WindowsLookAndFeelAdapter implements ILookAndFeelAdapter {
	@SuppressWarnings("unchecked")
	private HashMap<Class, WidgetValue> xpValues;
	@SuppressWarnings("unchecked")
	private HashMap<Class, WidgetValue> classicValues;

	@SuppressWarnings("unchecked")
	public WindowsLookAndFeelAdapter() {
		xpValues = new HashMap<Class, WidgetValue>();
		classicValues = new HashMap<Class, WidgetValue>();
		
		xpValues.put(JLabel.class, new XpJLabelValue());
		xpValues.put(JApplet.class, new XpJAppletValue());
		xpValues.put(JPanel.class, new XpJPanelValue());
		xpValues.put(JFrame.class, new XpJFrameValue());
		xpValues.put(JDialog.class, new XpJDialogValue());
		xpValues.put(JButton.class, new XpJButtonValue());
		xpValues.put(JToggleButton.class, new XpJToggleButtonValue());
		xpValues.put(JCheckBox.class, new XpJCheckBoxValue());
		xpValues.put(JRadioButton.class,new XpJRadioButtonValue());
		xpValues.put(JComboBox.class, new XpJComboBoxValue());
		xpValues.put(JList.class, new XpJListValue());
		xpValues.put(JTextField.class, new XpJTextFieldValue());
		xpValues.put(JTextArea.class, new XpJTextAreaValue());
		xpValues.put(JScrollBar.class, new XpJScrollBarValue());
		xpValues.put(JSlider.class, new XpJSliderValue());
		xpValues.put(JProgressBar.class, new XpJProgressBarValue());
		xpValues.put(JFormattedTextField.class, new XpJFormattedTextFieldValue());
		xpValues.put(JPasswordField.class, new XpJPasswordFieldValue());
		xpValues.put(JSpinner.class, new XpJSpinnerValue());
		xpValues.put(JSeparator.class, new XpJSeparatorValue());
		xpValues.put(JTextPane.class, new XpJTextPaneValue());
		xpValues.put(JEditorPane.class, new XpJEditorPaneValue());
		xpValues.put(JTree.class, new XpJTreeValue());
		xpValues.put(JTable.class, new XpJTableValue());
		xpValues.put(JTabbedPane.class, new XpJTabbedPaneValue());
		xpValues.put(JSplitPane.class, new XpJSplitPaneValue());
		xpValues.put(JToolBar.class, new XpJToolBarValue());
		xpValues.put(JDesktopPane.class, new XpJDesktopPaneValue());
		xpValues.put(JInternalFrame.class, new XpJInternalFrameValue());		
		xpValues.put(JScrollPane.class, new XpJScrollPaneValue());
		xpValues.put(JMenuBar.class, new XpJMenuBarValue());
		xpValues.put(JMenu.class, new XpJMenuValue());
		xpValues.put(JMenuItem.class, new XpJMenuItemValue());
		xpValues.put(JCheckBoxMenuItem.class, new XpJCheckBoxMenuItemValue());
		xpValues.put(JRadioButtonMenuItem.class, new XpJRadioButtonMenuItemValue());
		
		classicValues.put(JLabel.class, new ClassicJLabelValue());
		classicValues.put(JApplet.class, new ClassicJAppletValue());
		classicValues.put(JPanel.class, new ClassicJPanelValue());
		classicValues.put(JFrame.class, new ClassicJFrameValue());
		classicValues.put(JDialog.class, new ClassicJDialogValue());
		classicValues.put(JButton.class, new ClassicJButtonValue());
		classicValues.put(JToggleButton.class, new ClassicJToggleButtonValue());
		classicValues.put(JCheckBox.class, new ClassicJCheckBoxValue());
		classicValues.put(JRadioButton.class, new ClassicJRadioButtonValue());
		classicValues.put(JComboBox.class, new ClassicJComboBoxValue());
		classicValues.put(JList.class, new ClassicJListValue());
		classicValues.put(JTextField.class, new ClassicJTextFieldValue());
		classicValues.put(JTextArea.class, new ClassicJTextAreaValue());
		classicValues.put(JScrollBar.class, new ClassicJScrollBarValue());
		classicValues.put(JSlider.class, new ClassicJSliderValue());
		classicValues.put(JProgressBar.class, new ClassicJProgressBarValue());
		classicValues.put(JFormattedTextField.class, new ClassicJFormattedTextFieldValue());
		classicValues.put(JPasswordField.class, new ClassicJPasswordFieldValue());
		classicValues.put(JSpinner.class, new ClassicJSpinnerValue());
		classicValues.put(JSeparator.class, new ClassicJSeparatorValue());
		classicValues.put(JTextPane.class, new ClassicJTextPaneValue());
		classicValues.put(JEditorPane.class, new ClassicJEditorPaneValue());
		classicValues.put(JTree.class, new ClassicJTreeValue());
		classicValues.put(JTable.class, new ClassicJTableValue());
		classicValues.put(JTabbedPane.class, new ClassicJTabbedPaneValue());
		classicValues.put(JSplitPane.class, new ClassicJSplitPaneValue());
		classicValues.put(JToolBar.class, new ClassicJToolBarValue());
		classicValues.put(JDesktopPane.class, new ClassicJDesktopPaneValue());
		classicValues.put(JInternalFrame.class, new ClassicJInternalFrameValue());		
		classicValues.put(JScrollPane.class, new ClassicJScrollPaneValue());
		classicValues.put(JMenuBar.class, new ClassicJMenuBarValue());
		classicValues.put(JMenu.class, new ClassicJMenuValue());
		classicValues.put(JMenuItem.class, new ClassicJMenuItemValue());
		classicValues.put(JCheckBoxMenuItem.class, new ClassicJCheckBoxMenuItemValue());
		classicValues.put(JRadioButtonMenuItem.class, new ClassicJRadioButtonMenuItemValue());
	}

	private static boolean isXP() {
		Boolean xp = (Boolean) Toolkit.getDefaultToolkit().getDesktopProperty(
				"win.xpstyle.themeActive");
		return xp != null && xp.booleanValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getDefaultValue(Class widgetClass, String propertyName) {
		WidgetValue widgetValue = getWidgetValue(widgetClass);
		if (widgetValue != null){
			return widgetValue.get(propertyName);
		}else
			return null;
	}
	@SuppressWarnings("unchecked")
	private WidgetValue getWidgetValue(Class widgetClass){
		WidgetValue widgetValue;
		if (isXP()) {
			widgetValue = xpValues.get(widgetClass);
		} else {
			widgetValue = classicValues.get(widgetClass);
		}
		if(widgetValue==null&&widgetClass!=Component.class){
			return getWidgetValue(widgetClass.getSuperclass());
		}else{
			return widgetValue;
		}
	}
	private LookAndFeel metalLnf;
	@Override
	public LookAndFeel getLookAndFeelInstance() {
		if(metalLnf==null){
			try {
				metalLnf=(LookAndFeel) Class.forName("com.sun.java.swing.plaf.windows.WindowsLookAndFeel").newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return metalLnf;
	}		
}
