
/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.lnfs;

import java.awt.Component;
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
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.dyno.visual.swing.lnfs.nimbus.JAppletValue;
import org.dyno.visual.swing.lnfs.nimbus.JButtonValue;
import org.dyno.visual.swing.lnfs.nimbus.JCheckBoxMenuItemValue;
import org.dyno.visual.swing.lnfs.nimbus.JCheckBoxValue;
import org.dyno.visual.swing.lnfs.nimbus.JComboBoxValue;
import org.dyno.visual.swing.lnfs.nimbus.JDesktopPaneValue;
import org.dyno.visual.swing.lnfs.nimbus.JDialogValue;
import org.dyno.visual.swing.lnfs.nimbus.JEditorPaneValue;
import org.dyno.visual.swing.lnfs.nimbus.JFormattedTextFieldValue;
import org.dyno.visual.swing.lnfs.nimbus.JFrameValue;
import org.dyno.visual.swing.lnfs.nimbus.JInternalFrameValue;
import org.dyno.visual.swing.lnfs.nimbus.JLabelValue;
import org.dyno.visual.swing.lnfs.nimbus.JListValue;
import org.dyno.visual.swing.lnfs.nimbus.JMenuBarValue;
import org.dyno.visual.swing.lnfs.nimbus.JMenuItemValue;
import org.dyno.visual.swing.lnfs.nimbus.JMenuValue;
import org.dyno.visual.swing.lnfs.nimbus.JPanelValue;
import org.dyno.visual.swing.lnfs.nimbus.JPasswordFieldValue;
import org.dyno.visual.swing.lnfs.nimbus.JProgressBarValue;
import org.dyno.visual.swing.lnfs.nimbus.JRadioButtonMenuItemValue;
import org.dyno.visual.swing.lnfs.nimbus.JRadioButtonValue;
import org.dyno.visual.swing.lnfs.nimbus.JScrollBarValue;
import org.dyno.visual.swing.lnfs.nimbus.JScrollPaneValue;
import org.dyno.visual.swing.lnfs.nimbus.JSeparatorValue;
import org.dyno.visual.swing.lnfs.nimbus.JSliderValue;
import org.dyno.visual.swing.lnfs.nimbus.JSpinnerValue;
import org.dyno.visual.swing.lnfs.nimbus.JSplitPaneValue;
import org.dyno.visual.swing.lnfs.nimbus.JTabbedPaneValue;
import org.dyno.visual.swing.lnfs.nimbus.JTableValue;
import org.dyno.visual.swing.lnfs.nimbus.JTextAreaValue;
import org.dyno.visual.swing.lnfs.nimbus.JTextFieldValue;
import org.dyno.visual.swing.lnfs.nimbus.JTextPaneValue;
import org.dyno.visual.swing.lnfs.nimbus.JToggleButtonValue;
import org.dyno.visual.swing.lnfs.nimbus.JToolBarValue;
import org.dyno.visual.swing.lnfs.nimbus.JTreeValue;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;

@SuppressWarnings("unchecked")
public class NimbusLookAndFeelAdapter extends HashMap<Class, WidgetValue>
		implements ILookAndFeelAdapter {
	private static final long serialVersionUID = 1L;

	public NimbusLookAndFeelAdapter() {
		put(JPanel.class, new JPanelValue());
		put(JApplet.class, new JAppletValue());
		put(JFrame.class, new JFrameValue());
		put(JDialog.class, new JDialogValue());
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
		put(JMenuBar.class, new JMenuBarValue());
		put(JMenu.class, new JMenuValue());
		put(JMenuItem.class, new JMenuItemValue());
		put(JCheckBoxMenuItem.class, new JCheckBoxMenuItemValue());
		put(JRadioButtonMenuItem.class, new JRadioButtonMenuItemValue());		
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
		if (metalLnf == null) {
			try {
				LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
				for (LookAndFeelInfo info : infos) {
					if (info.getClassName().equals(
							"com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")) {
						metalLnf = (LookAndFeel) Class
								.forName(
										"com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")
								.newInstance();
						return metalLnf;
					}
				}
			} catch (Exception e) {
			}
		}
		return metalLnf;
	}
}

