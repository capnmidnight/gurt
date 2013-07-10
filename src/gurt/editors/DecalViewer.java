package gurt.editors;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.Timer;

import gurt.util.Decal;

public class DecalViewer extends JDialog implements ActionListener {
	private static final long serialVersionUID = 5367198713541808288L;

	Decal d;

	double a;

	Timer t;

	private BufferStrategy bs;

	public DecalViewer(JFrame owner, Decal d) {
		super(owner);
		this.d = d;
		this.setBackground(Color.BLACK);
		a = 0;
		int[] b = d.getSize();
		int s = (int) Math.sqrt(b[0] * b[0] + b[1] * b[1]) + 100;
		this.setSize(s, s);
		this.setIgnoreRepaint(true);
		this.setVisible(true);
		this.createBufferStrategy(2);
		bs = this.getBufferStrategy();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		t = new Timer(1, this);
		t.start();
	}

	public void dispose() {
		t.stop();
		this.setVisible(false);
	}

	public void render() {
		a += Math.PI / 180;
		Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();
		g2.setBackground(Color.BLACK);
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());
		d.render(g2, this.getWidth() / 2, this.getHeight() / 2, a);
		bs.show();
	}

	public void actionPerformed(ActionEvent e) {
		this.render();
	}

}
