package org.dyno.visual.swing.lnfs;

import java.awt.Component;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
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

import org.dyno.visual.swing.lnfs.windows.XpJButtonValue;
import org.dyno.visual.swing.lnfs.windows.XpJCheckBoxValue;
import org.dyno.visual.swing.lnfs.windows.XpJComboBoxValue;
import org.dyno.visual.swing.lnfs.windows.XpJDesktopPaneValue;
import org.dyno.visual.swing.lnfs.windows.XpJEditorPaneValue;
import org.dyno.visual.swing.lnfs.windows.XpJFormattedTextFieldValue;
import org.dyno.visual.swing.lnfs.windows.XpJInternalFrameValue;
import org.dyno.visual.swing.lnfs.windows.XpJLabelValue;
import org.dyno.visual.swing.lnfs.windows.XpJListValue;
import org.dyno.visual.swing.lnfs.windows.XpJPanelValue;
import org.dyno.visual.swing.lnfs.windows.XpJPasswordFieldValue;
import org.dyno.visual.swing.lnfs.windows.XpJProgressBarValue;
import org.dyno.visual.swing.lnfs.windows.XpJRadioButtonValue;
import org.dyno.visual.swing.lnfs.windows.XpJScrollBarValue;
import org.dyno.visual.swing.lnfs.windows.XpJScrollPaneValue;
import org.dyno.visual.swing.lnfs.windows.XpJSeparatorValue;
import org.dyno.visual.swing.lnfs.windows.XpJSliderValue;
import org.dyno.visual.swing.lnfs.windows.XpJSpinnerValue;
import org.dyno.visual.swing.lnfs.windows.XpJSplitPaneValue;
import org.dyno.visual.swing.lnfs.windows.XpJTabbedPaneValue;
import org.dyno.visual.swing.lnfs.windows.XpJTableValue;
import org.dyno.visual.swing.lnfs.windows.XpJTextAreaValue;
import org.dyno.visual.swing.lnfs.windows.XpJTextFieldValue;
import org.dyno.visual.swing.lnfs.windows.XpJTextPaneValue;
import org.dyno.visual.swing.lnfs.windows.XpJToggleButtonValue;
import org.dyno.visual.swing.lnfs.windows.XpJToolBarValue;
import org.dyno.visual.swing.lnfs.windows.XpJTreeValue;
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
		xpValues.put(JPanel.class, new XpJPanelValue());
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
		if (widgetValue != null)
			return widgetValue.get(propertyName);
		else
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
}
