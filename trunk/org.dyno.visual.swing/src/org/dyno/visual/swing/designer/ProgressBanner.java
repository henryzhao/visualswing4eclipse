package org.dyno.visual.swing.designer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

class ProgressBanner extends JLabel implements ActionListener {
	private static final long serialVersionUID = 1L;
	public ProgressBanner(){
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		Timer timer = new Timer(2000, this);
		timer.setRepeats(false);
		timer.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		setIcon(new ImageIcon(getClass().getResource("/icons/progress.gif")));
	}
}
