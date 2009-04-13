package org.dyno.visual.swing.lnfs.preference;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

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

import org.dyno.visual.swing.lnfs.LnfPlugin;


@SuppressWarnings("unchecked")
public interface BeanCreator {
	Class getBeanClass();
	Component createComponent(JFrame jframe);
	void dispose();
	abstract class AbstractScrollPaneCreator implements BeanCreator{
		private Component content;
		
		public Component createComponent(JFrame jframe) {
			jframe.getContentPane().removeAll();
			JScrollPane jsp = new JScrollPane();
			try {
				Class clazz = getBeanClass();
				content = (Component) clazz.newInstance();
				jsp.setViewportView(content);
			} catch (Exception e) {
				LnfPlugin.getLogger().error(e);
			}
			jframe.add(jsp);
			jframe.validate();
			return content;
		}
		
		public void dispose() {
		}
	}	
	abstract class AbstractBeanCreator implements BeanCreator{
		private Component content;
		
		public Component createComponent(JFrame jframe) {
			jframe.getContentPane().removeAll();
			try {
				Class clazz = getBeanClass();
				content = (Component) clazz.newInstance();
				jframe.add(content);
				jframe.validate();
			} catch (Exception e) {
				LnfPlugin.getLogger().error(e);
			}
			return content;
		}
		
		public void dispose() {
		}
	}
	BeanCreator[]COMPONENTS={
			new BeanCreator(){
				private JFrame frame;
				
				public Component createComponent(JFrame jframe) {
					frame = new JFrame();
					frame.setSize(100, 100);
					Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setLocation(size.width+1, size.height+1);
					return frame;
				}

				
				public void dispose() {
				}

				
				public Class getBeanClass() {
					return JFrame.class;
				}
			},
			new BeanCreator(){
				private JDialog jdialog;
				
				public Component createComponent(JFrame jframe) {
					jdialog = new JDialog(jframe);
					jdialog.setSize(100, 100);
					Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
					jdialog.setLocation(size.width+1, size.height+1);
					return jdialog;
				}

				
				public void dispose() {
					jdialog.setVisible(false);
				}

				
				public Class getBeanClass() {
					return JDialog.class;
				}
				
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JApplet.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JInternalFrame.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JPanel.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JButton.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JCheckBox.class;
				}
			},
			new BeanCreator(){
				
				public Class getBeanClass() {
					return JCheckBoxMenuItem.class;
				}

				
				public Component createComponent(JFrame jframe) {
					return new JCheckBoxMenuItem();
				}

				
				public void dispose() {
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JComboBox.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JDesktopPane.class;
				}
			},
			new AbstractScrollPaneCreator(){
				
				public Class getBeanClass() {
					return JEditorPane.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JFormattedTextField.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JLabel.class;
				}
			},
			new AbstractScrollPaneCreator(){
				
				public Class getBeanClass() {
					return JList.class;
				}
			},
			new BeanCreator(){
				
				public Class getBeanClass() {
					return JMenuBar.class;
				}

				
				public Component createComponent(JFrame jframe) {
					return new JMenuBar();
				}

				
				public void dispose() {
				}
			},
			new BeanCreator(){
				
				public Class getBeanClass() {
					return JMenuItem.class;
				}

				
				public Component createComponent(JFrame jframe) {
					return new JMenuItem();
				}

				
				public void dispose() {
				}
			},
			new BeanCreator(){
				
				public Class getBeanClass() {
					return JMenu.class;
				}

				
				public Component createComponent(JFrame jframe) {
					return new JMenu();
				}

				
				public void dispose() {
					
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JPasswordField.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JProgressBar.class;
				}
			},
			new BeanCreator(){
				
				public Class getBeanClass() {
					return JRadioButtonMenuItem.class;
				}

				
				public Component createComponent(JFrame jframe) {
					return new JRadioButtonMenuItem();
				}

				
				public void dispose() {
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JRadioButton.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JScrollBar.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JScrollPane.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JSeparator.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JSlider.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JSpinner.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JSplitPane.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JTabbedPane.class;
				}
			},
			new AbstractScrollPaneCreator(){
				
				public Class getBeanClass() {
					return JTextArea.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JTextField.class;
				}
			},
			new AbstractScrollPaneCreator(){
				
				public Class getBeanClass() {
					return JTextPane.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JToggleButton.class;
				}
			},
			new AbstractBeanCreator(){
				
				public Class getBeanClass() {
					return JToolBar.class;
				}
			},
			new AbstractScrollPaneCreator(){
				
				public Class getBeanClass() {
					return JTable.class;
				}
			},
			new AbstractScrollPaneCreator(){
				
				public Class getBeanClass() {
					return JTree.class;
				}
			},
	};
}
