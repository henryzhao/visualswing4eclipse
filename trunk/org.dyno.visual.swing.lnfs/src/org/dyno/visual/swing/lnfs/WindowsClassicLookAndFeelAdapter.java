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

import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJButtonValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJCheckBoxValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJComboBoxValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJDesktopPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJEditorPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJFormattedTextFieldValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJInternalFrameValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJLabelValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJListValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJPanelValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJPasswordFieldValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJProgressBarValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJRadioButtonValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJScrollBarValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJScrollPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJSeparatorValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJSliderValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJSpinnerValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJSplitPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJTabbedPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJTableValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJTextAreaValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJTextFieldValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJTextPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJToggleButtonValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJToolBarValue;
import org.dyno.visual.swing.lnfs.windowsclassic.ClassicJTreeValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJButtonValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJCheckBoxValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJComboBoxValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJDesktopPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJEditorPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJFormattedTextFieldValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJInternalFrameValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJLabelValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJListValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJPanelValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJPasswordFieldValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJProgressBarValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJRadioButtonValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJScrollBarValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJScrollPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJSeparatorValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJSliderValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJSpinnerValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJSplitPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJTabbedPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJTableValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJTextAreaValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJTextFieldValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJTextPaneValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJToggleButtonValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJToolBarValue;
import org.dyno.visual.swing.lnfs.windowsclassic.XpJTreeValue;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;

public class WindowsClassicLookAndFeelAdapter implements ILookAndFeelAdapter {
	@SuppressWarnings("unchecked")
	private HashMap<Class, WidgetValue> xpValues;
	@SuppressWarnings("unchecked")
	private HashMap<Class, WidgetValue> classicValues;

	@SuppressWarnings("unchecked")
	public WindowsClassicLookAndFeelAdapter() {
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
		
		classicValues.put(JLabel.class, new ClassicJLabelValue());
		classicValues.put(JPanel.class, new ClassicJPanelValue());
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
			try {
				widgetValue=widgetValue.getClass().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
}
