/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.designborder;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * 
 * @author William Chen
 */
public class FrameBorder implements Border {

	private static int ICON_PAD = 2;
	private static int ICON_TEXT_PAD = 4;
	private static int OUTER_PAD = 4;
	private static int TITLE_HEIGHT = 27;
	private static Image TL_CORNER;
	private static Image TOP_RIGHT;
	private static Image BOTTOM_LEFT;
	private static Image BOTTOM_RIGHT;
	private static Image TOP_BORDER;
	private static Image LEFT_BORDER;
	private static Image BOTTOM_BORDER;
	private static Image RIGHT_BORDER;
	private static Image TITLE_PANE;
	private static Image CONTROL_CLOSE;
	private static Image CONTROL_MIN;
	private static Image CONTROL_MAX;
	private static Image JAVA_LOGO;
	private static Color BEGIN_COLOR;
	private static Color END_COLOR;
	private static Color CONTROL_COLOR;

	private static boolean isXP() {
		Boolean xp = (Boolean) Toolkit.getDefaultToolkit().getDesktopProperty(
				"win.xpstyle.themeActive");
		return xp != null && xp.booleanValue();
	}

	static {
		boolean isXP = isXP();
		String ext = "";
		if (isXP) {
			TITLE_HEIGHT = 27;
		} else {
			TITLE_HEIGHT = 19;
			ext = "_";
			BEGIN_COLOR = new Color(10, 36, 106);
			END_COLOR = new Color(166, 202, 240);
			CONTROL_COLOR = new Color(212, 208, 200);
		}
		MediaTracker tracker = new MediaTracker(new JFrame());
		TOP_BORDER = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("top_border" + ext + ".png"));
		tracker.addImage(TOP_BORDER, 0);
		LEFT_BORDER = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("left_border" + ext + ".png"));
		tracker.addImage(LEFT_BORDER, 1);
		BOTTOM_BORDER = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("bottom_border" + ext + ".png"));
		tracker.addImage(BOTTOM_BORDER, 2);
		RIGHT_BORDER = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("right_border" + ext + ".png"));
		tracker.addImage(RIGHT_BORDER, 3);
		TL_CORNER = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("top_left" + ext + ".png"));
		tracker.addImage(TL_CORNER, 4);
		TOP_RIGHT = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("top_right" + ext + ".png"));
		tracker.addImage(TOP_RIGHT, 5);
		BOTTOM_LEFT = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("bottom_left" + ext + ".png"));
		tracker.addImage(BOTTOM_LEFT, 6);
		BOTTOM_RIGHT = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("bottom_right" + ext + ".png"));
		tracker.addImage(BOTTOM_RIGHT, 7);

		TITLE_PANE = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("title_pane.png"));
		tracker.addImage(TITLE_PANE, 8);
		CONTROL_CLOSE = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("control_close" + ext + ".png"));
		tracker.addImage(CONTROL_CLOSE, 10);
		CONTROL_MIN = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("control_min" + ext + ".png"));
		tracker.addImage(CONTROL_MIN, 11);
		CONTROL_MAX = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("control_max" + ext + ".png"));
		tracker.addImage(CONTROL_MAX, 12);
		JAVA_LOGO = Toolkit.getDefaultToolkit().getImage(
				FrameBorder.class.getResource("java_logo.png"));
		tracker.addImage(JAVA_LOGO, 13);
		while (!tracker.checkAll()) {
			try {
				tracker.waitForAll();
			} catch (Exception e) {
			}
		}
	}
	private JFrame frame;

	public FrameBorder(JFrame frame) {
		this.frame = frame;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		int gx = x + OUTER_PAD;
		int gy = y;
		g.drawImage(TOP_BORDER, gx, gy, width - 2 * OUTER_PAD, OUTER_PAD, c);
		gx = x;
		gy = y + OUTER_PAD;
		g.drawImage(LEFT_BORDER, gx, gy, OUTER_PAD, height - 2 * OUTER_PAD, c);
		gx = x + OUTER_PAD;
		gy = y + height - OUTER_PAD;
		g.drawImage(BOTTOM_BORDER, gx, gy, width - 2 * OUTER_PAD, OUTER_PAD, c);
		gx = x + width - OUTER_PAD;
		gy = y + OUTER_PAD;
		g.drawImage(RIGHT_BORDER, gx, gy, OUTER_PAD, height - 2 * OUTER_PAD, c);

		gx = x;
		gy = y;
		g.drawImage(TL_CORNER, gx, gy, c);
		gx = x;
		gy = y + height - OUTER_PAD;
		g.drawImage(BOTTOM_LEFT, gx, gy, c);
		gx = x + width - OUTER_PAD;
		gy = y + height - OUTER_PAD;
		g.drawImage(BOTTOM_RIGHT, gx, gy, c);
		gx = x + width - OUTER_PAD;
		gy = y;
		g.drawImage(TOP_RIGHT, gx, gy, c);

		gx = x + OUTER_PAD;
		gy = y + OUTER_PAD;
		if (isXP()) {
			g.drawImage(TITLE_PANE, gx, gy, width - 2 * OUTER_PAD,
					TITLE_HEIGHT, c);
		} else {
			Graphics2D g2d = (Graphics2D) g;
			GradientPaint gp = new GradientPaint(gx, gy, BEGIN_COLOR, gx
					+ width - 2 * OUTER_PAD, gy, END_COLOR);
			Paint p = g2d.getPaint();
			g2d.setPaint(gp);
			g2d.fillRect(gx, gy, width - 2 * OUTER_PAD, TITLE_HEIGHT - 1);
			g2d.setPaint(p);
			g2d.setColor(CONTROL_COLOR);
			g2d.drawLine(gx, gy + TITLE_HEIGHT - 1, gx + width - 2 * OUTER_PAD,
					gy + TITLE_HEIGHT - 1);
		}
		Image icon = frame.getIconImage();
		if (icon == null) {
			Window win = SwingUtilities.getWindowAncestor(c);
			if (win instanceof Frame) {
				icon = ((Frame) win).getIconImage();
			}
		}
		if (icon == null)
			icon = JAVA_LOGO;
		if (icon != null) {
			gx = x + OUTER_PAD + ICON_PAD;
			gy = y + OUTER_PAD + (TITLE_HEIGHT - icon.getHeight(c)) / 2;
			g.drawImage(icon, gx, gy, c);
		}
		String title = frame.getTitle();
		if (title != null && title.trim().length() > 0) {
			Font font = frame.getFont();
			if(font==null)
				font = new Font("Dialog", Font.BOLD, 12);
			else
				font = new Font(font.getFamily(), Font.BOLD, font.getSize());
			g.setFont(font);
			int image_width = icon == null ? 0 : icon.getWidth(c);
			gx = x + OUTER_PAD + ICON_PAD + image_width + ICON_TEXT_PAD;
			FontMetrics fm = g.getFontMetrics();
			gy = y + OUTER_PAD + (TITLE_HEIGHT - fm.getHeight()) / 2
					+ fm.getAscent();
			g.drawString(title, gx, gy);
		}

		int w = CONTROL_CLOSE.getWidth(c);
		// int h = CONTROLS.getHeight(designer);
		gx = x + width - OUTER_PAD - w - 2;
		gy = y + OUTER_PAD + 2;
		g.drawImage(CONTROL_CLOSE, gx, gy, c);
		w = CONTROL_MAX.getWidth(c);
		gx = gx - w - 2;
		g.drawImage(CONTROL_MAX, gx, gy, c);
		w = CONTROL_MIN.getWidth(c);
		if (isXP()) {
			gx = gx - w - 2;
		} else {
			gx = gx - w;
		}
		g.drawImage(CONTROL_MIN, gx, gy, c);
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(OUTER_PAD + TITLE_HEIGHT, OUTER_PAD, OUTER_PAD,
				OUTER_PAD);
	}

	public boolean isBorderOpaque() {
		return true;
	}
}