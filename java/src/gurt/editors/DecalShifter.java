package gurt.editors;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DecalShifter extends JPanel implements ActionListener {
	private static final long serialVersionUID = 8060958683976329025L;

	private DecalEditor mover;

	private JButton up, dn, lf, rt, ul, ur, dl, dr;

	private JTextField txtAmt;

	DecalShifter() {
		super(true);
		GridLayout layout = new GridLayout(3, 3);
		this.setLayout(layout);
		up = new JButton("UP");
		up.addActionListener(this);
		dn = new JButton("DN");
		dn.addActionListener(this);
		lf = new JButton("LF");
		lf.addActionListener(this);
		rt = new JButton("RT");
		rt.addActionListener(this);
		ul = new JButton("UL");
		ul.addActionListener(this);
		ur = new JButton("UR");
		ur.addActionListener(this);
		dl = new JButton("DL");
		dl.addActionListener(this);
		dr = new JButton("DR");
		dr.addActionListener(this);
		txtAmt = new JTextField("1");
		this.add(ul);
		this.add(up);
		this.add(ur);
		this.add(lf);
		this.add(txtAmt);
		this.add(rt);
		this.add(dl);
		this.add(dn);
		this.add(dr);
	}

	public void setMover(DecalEditor mover) {
		this.mover = mover;
	}

	public void actionPerformed(ActionEvent e) {
		int amt = 0;
		try {
			amt = Integer.parseInt(txtAmt.getText());
		} catch (Exception exp) {
			amt = 1;
			txtAmt.setText("1");
		}
		int dx = 0, dy = 0;
		Object o = e.getSource();

		if (o == ul || o == lf || o == dl) {
			dx = -amt; // move left
		}
		if (o == ur || o == rt || o == dr) {
			dx = amt; // move right
		}
		if (o == ul || o == up || o == ur) {
			dy = -amt; // move up
		}
		if (o == dl || o == dn || o == dr) {
			dy = amt; // move down
		}
		mover.shiftPolygon(dx, dy);
	}

}
