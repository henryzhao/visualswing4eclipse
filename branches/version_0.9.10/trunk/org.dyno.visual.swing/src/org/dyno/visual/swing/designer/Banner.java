package org.dyno.visual.swing.designer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

class Banner extends JLabel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private boolean error;
	public boolean isError(){
		return error;
	}
	public void setError(boolean v){
		error = v;
		if (error)
			setIcon(new ImageIcon(getClass().getResource("/icons/error.png")));
	}
	public Banner(){
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		Timer timer = new Timer(2000, this);
		timer.setRepeats(false);
		timer.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!error)
			setIcon(new ImageIcon(getClass().getResource("/icons/progress.gif")));
	}
}
