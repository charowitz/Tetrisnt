package tetrisnt;

import javax.swing.JFrame;

public class Frame extends JFrame{ //this class will always look like this
	public Frame() {
		this.add(new Panel()); //same as initializing instance of GamePanel, then passing it in. same deal
		this.setTitle("Tetrisn't");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
