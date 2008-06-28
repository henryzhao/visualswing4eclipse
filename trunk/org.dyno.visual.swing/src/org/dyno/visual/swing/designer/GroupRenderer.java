/*
 * GroupRenderer.java
 *
 * Created on 2006��11��18��, ����6:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.dyno.visual.swing.designer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * @author William Chen
 */
public class GroupRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public GroupRenderer() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setOpaque(true);
		String text = value == null ? null : value.toString();
		if (text != null && text.trim().length() > 0) {
			if (text.startsWith("+")) {
				setIcon(PLUS_ICON);
				setText(text.substring(1));
			} else if (text.startsWith("-")) {
				setIcon(MINUS_ICON);
				setText(text.substring(1));
			} else {
				setIcon(null);
				setText(text);
			}
		} else {
			setIcon(null);
			setText(text);
		}
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}
		return this;
	}

	static final Icon PLUS_ICON = new ImageIcon(GroupRenderer.class.getClassLoader().getResource("icons/plus.png"));
	static final Icon MINUS_ICON = new ImageIcon(GroupRenderer.class.getClassLoader().getResource("icons/minus.png"));
}
