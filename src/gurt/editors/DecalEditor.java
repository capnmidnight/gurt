package gurt.editors;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gurt.util.Decal;
import gurt.util.ErrorHandler;

/**
 * A user friendly means for building decals, instead of entering them manually
 * with integer arrays.
 * 
 * @author smcbeth
 * 
 */
public class DecalEditor extends JFrame implements MouseListener {
	private static final long serialVersionUID = 6594282883536936797L;

	private ArrayList<Point> points;

	private ArrayList<Decal> subDecals;

	private BasicStroke curStroke;

	private Color curColorStrk, curColorFill;

	private boolean drawGrid, snapToGrid, symmetrical;

	private int gridSize;

	private float scaleFactor;

	private String[] caps = new String[3];
	{
		caps[BasicStroke.CAP_BUTT] = "butt";
		caps[BasicStroke.CAP_ROUND] = "round";
		caps[BasicStroke.CAP_SQUARE] = "square";
	}

	private String[] joins = new String[3];
	{
		joins[BasicStroke.JOIN_BEVEL] = "bevel";
		joins[BasicStroke.JOIN_MITER] = "miter";
		joins[BasicStroke.JOIN_ROUND] = "round";
	}

	private JPanel editArea, controlArea;

	private JComboBox comboStrkCap, comboStrkJoin;;

	private JTextField txtScale, txtStroke;

	private JComboBox comboSubDecal;

	private JFileChooser jfc;

	private JColorChoserModal jcc;

	private BasicStroke s1, s2;

	private JCheckBox chkSymmetric;

	/**
	 * Displays the Decal Editor window
	 * 
	 */
	public DecalEditor() {
		super("Decal Editor");
		subDecals = new ArrayList<Decal>();
		points = new ArrayList<Point>();
		jfc = new JFileChooser();
		jcc = new JColorChoserModal(this);
		initializeOptions();
		initializeDrawArea();
		initializeControlArea();
		initializeWindow();
	}

	/**
	 * Set properties of the window
	 * 
	 */
	private void initializeWindow() {
		this.setSize(700, 525);
		this.setResizable(false);
		this.getContentPane().setLayout(null);
		this.getContentPane().add(editArea);
		this.getContentPane().add(controlArea);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * create the tool controls
	 * 
	 */
	private void initializeControlArea() {
		controlArea = new JPanel(true);
		controlArea.setSize(200, 500);
		controlArea.setLocation(500, 0);

		// display some directions for user
		controlArea.add(new JLabel("Left click to place points"));
		controlArea.add(new JLabel("Right click to delete last point"));

		// Option for drawing the snap-to grid
		JCheckBox chkDrawGrid = new JCheckBox("Draw background grid");
		chkDrawGrid.setSelected(drawGrid);
		chkDrawGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawGrid = !drawGrid;
				render();
			}
		});
		controlArea.add(chkDrawGrid);

		// option for snap-to
		JCheckBox chkSnapTo = new JCheckBox("Snap to grid");
		chkSnapTo.setSelected(snapToGrid);
		chkSnapTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				snapToGrid = !snapToGrid;
			}
		});
		controlArea.add(chkSnapTo);

		chkSymmetric = new JCheckBox("Symmetrical about X-axis");
		chkSymmetric.setSelected(symmetrical);
		chkSymmetric.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				symmetrical = !symmetrical;
				render();
			}
		});
		controlArea.add(chkSymmetric);

		// stroke width
		txtStroke = new JTextField(3);
		txtStroke.setText("1.0");
		txtStroke.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float f = 1;
				try {
					f = Float.parseFloat(txtStroke.getText());
				} catch (Exception exp) {
					txtStroke.setText("1.0");
				}
				curStroke = new BasicStroke(f, curStroke.getEndCap(), curStroke
						.getLineJoin());
				render();
			}
		});
		JPanel panStroke = new JPanel();
		panStroke.add(new JLabel("Stroke"));
		panStroke.add(txtStroke);
		controlArea.add(panStroke);

		// stroke cap
		comboStrkCap = new JComboBox(caps);
		comboStrkCap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				curStroke = new BasicStroke(curStroke.getLineWidth(),
						comboStrkCap.getSelectedIndex(), curStroke
								.getLineJoin());
				render();
			}
		});
		// stroke join
		comboStrkJoin = new JComboBox(joins);
		comboStrkJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				curStroke = new BasicStroke(curStroke.getLineWidth(), curStroke
						.getEndCap(), comboStrkJoin.getSelectedIndex());
				render();
			}
		});
		JPanel panStroke2 = new JPanel();
		panStroke2.add(comboStrkCap);
		panStroke2.add(comboStrkJoin);
		controlArea.add(panStroke2);

		JButton btnStrkColor = new JButton("Stroke Color");
		btnStrkColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jcc.setVisible(true);
				curColorStrk = jcc.getColor();
				render();
			}
		});
		controlArea.add(btnStrkColor);
		JButton btnFillColor = new JButton("Fill Color");
		btnFillColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jcc.setVisible(true);
				curColorFill = jcc.getColor();
				render();
			}
		});
		controlArea.add(btnFillColor);

		// option for the final scale factor. Scale factor shrinks or grows the
		// object before writing it to disk. Try using the decal preview to see
		// what happens.
		JPanel panScale = new JPanel();
		JLabel lblScale = new JLabel("Scale factor");
		txtScale = new JTextField(Float.toString(scaleFactor), 5);
		txtScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = txtScale.getText();
				try {
					scaleFactor = Float.parseFloat(input);
				} catch (Exception exp) {
					txtScale.setText(Float.toString(scaleFactor));
				}
			}
		});
		panScale.add(lblScale);
		panScale.add(txtScale);
		controlArea.add(panScale);

		// option for adding a list of decals that will be appended to this one
		JPanel panDecals = new JPanel();
		comboSubDecal = new JComboBox();
		JButton btnAdd = new JButton("Sub Decal");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSubDecal();
			}
		});
		panDecals.add(comboSubDecal);
		panDecals.add(btnAdd);
		controlArea.add(panDecals);
		// controlArea.add(btnAdd);

		// sometimes the display gets wiped out. It's a convenience issue to not
		// constantly redraw the display.
		JButton btnRedraw = new JButton("Redraw Decal");
		btnRedraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				render();
			}
		});
		controlArea.add(btnRedraw);

		// show the full composite decal in motion (rotating about the center of
		// the decal)
		JButton btnShow = new JButton("Preview Full Decal");
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showDecal();
			}
		});
		controlArea.add(btnShow);

		// serialize the full composite decal to disk
		this.setJMenuBar(new JMenuBar());

		JMenu file = new JMenu("file");
		this.getJMenuBar().add(file);

		JMenuItem save = new JMenuItem("save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveDecal();
				render();
			}
		});

		file.add(save);

		JMenuItem load = new JMenuItem("load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDecal();
				render();
			}

		});
		file.add(load);

		JMenuItem exit = new JMenuItem("exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}

		});
		file.add(exit);

		// shift the entire decal over by a specified number of pixels
		DecalShifter ctrlShifter = new DecalShifter();
		ctrlShifter.setMover(this);
		controlArea.add(ctrlShifter);
	}

	private void loadDecal() {
		int ret = jfc.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File fin = jfc.getSelectedFile();
			try {
				Decal d = Decal.loadDecal(new FileInputStream(fin));
				this.points = d.getPoints();
				this.curStroke = d.getStroke();
				this.subDecals = d.getSubDecals();
				this.curColorStrk = d.getStrokeColor();
				this.curColorFill = d.getFillColor();
				this.symmetrical = false;
				this.chkSymmetric.setSelected(false);
			} catch (Exception exp) {
				ErrorHandler.showErrorDialog(exp, "Couldn't load decal", this);
			}
		}
	}

	public void shiftPolygon(int dx, int dy) {
		for (Point p : points) {
			p.x += dx;
			p.y += dy;
		}
		render();
	}

	private void addSubDecal() {
		int ret = jfc.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File fin = jfc.getSelectedFile();
			try {
				Decal d = Decal.loadDecal(new FileInputStream(fin));
				subDecals.add(d);
				comboSubDecal.addItem(fin.getName());
			} catch (Exception exp) {
				ErrorHandler.showErrorDialog(exp, "Couldn't load decal", this);
			}
		}
	}

	private void showDecal() {
		if (points.size() > 0) {
			Decal d = makeDecal();
			new DecalViewer(this, d);
		}
	}

	private Decal makeDecal() {
		Polygon temp = makePolygon();
		Polygon p = new Polygon();
		for (int i = 0; i < temp.npoints; ++i) {
			p.addPoint((int) (temp.xpoints[i] * scaleFactor),
					(int) (temp.ypoints[i] * scaleFactor));
		}
		Decal[] arr = null;
		if (subDecals.size() > 0) {
			arr = new Decal[subDecals.size()];
			for (int i = 0; i < arr.length; ++i)
				arr[i] = subDecals.get(i);
		}
		return new Decal(p, curColorFill, curColorStrk, curStroke
				.getLineWidth(), curStroke.getEndCap(),
				curStroke.getLineJoin(), arr);
	}

	private void saveDecal() {
		if (points.size() > 0) {
			Decal d = makeDecal();

			int ret = jfc.showSaveDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File fout = jfc.getSelectedFile();
				try {
					d.save(fout);
				} catch (Exception e) {
					ErrorHandler
							.showErrorDialog(e, "Couldn't save decal", this);
				}
			}
		}
	}

	private void initializeOptions() {
		curColorStrk = Color.GREEN;
		curStroke = new BasicStroke(1, 0, 0);
		drawGrid = true;
		gridSize = 25;
		snapToGrid = true;
		scaleFactor = 1;
		symmetrical = true;
		s1 = new BasicStroke(1);
		s2 = new BasicStroke(2);
	}

	private void initializeDrawArea() {
		editArea = new JPanel(true);
		editArea.setSize(500, 500);
		editArea.setLocation(0, 0);
		editArea.setBackground(Color.BLACK);
		editArea.addMouseListener(this);
		editArea.setIgnoreRepaint(true);
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			Point p = e.getPoint();
			if (snapToGrid) {
				double temp = (double) p.x / gridSize;
				p.x = (int) (Math.round(temp) * gridSize);
				temp = (double) p.y / gridSize;
				p.y = (int) (Math.round(temp) * gridSize);
			}
			p.x -= editArea.getWidth() / 2;
			p.y -= editArea.getHeight() / 2;
			points.add(p);
			render();
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			deleteLastPoint();
		}
	}

	private void deleteLastPoint() {
		if (points.size() > 0) {
			points.remove(points.size() - 1);
			render();
		}
	}

	private void render() {

		Graphics2D g = (Graphics2D) editArea.getGraphics();
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, editArea.getWidth(), editArea.getHeight());
		int w = editArea.getWidth() / 2;
		int h = editArea.getHeight() / 2;
		// put the center of the decal painting area, the origin (0, 0), into
		// the center of the display area
		g.translate(w, h);
		drawGrid(g, w, h);

		if (points.size() > 0) {
			g.setStroke(curStroke);
			int m = symmetrical ? 2 : 1;

			if (points.size() * m == 1) {
				Point p = points.get(0);
				g.setColor(curColorStrk);
				g.drawLine(p.x, p.y - 5, p.x, p.y + 5);
				g.drawLine(p.x - 5, p.y, p.x + 5, p.y);
			} else if (points.size() * m == 2) {
				Point p1 = points.get(0);
				Point p2 = null;
				if (symmetrical) {
					p2 = new Point(p1.x, -p1.y);
				} else {
					p2 = points.get(1);
				}
				g.setColor(curColorStrk);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			} else {
				Polygon p = makePolygon();
				if (curColorFill != null) {
					g.setColor(curColorFill);
					g.fillPolygon(p);
				}
				g.setColor(curColorStrk);
				g.drawPolygon(p);
			}
		}
	}

	private void drawGrid(Graphics2D g, int w, int h) {
		if (drawGrid) {
			g.setStroke(s1);
			g.setColor(Color.GRAY);
			for (int i = -w; i <= w; i += gridSize) {
				g.drawLine(i, -h, i, h);
				g.drawLine(-w, i, w, i);
			}
			g.setColor(Color.WHITE);
			g.setStroke(s2);
			g.drawLine(-w, 0, w, 0);
			g.drawLine(0, -h, 0, h);
		}
	}

	private Polygon makePolygon() {
		int[] xs, ys;
		int m = 1;
		if (symmetrical)
			m *= 2;
		xs = new int[points.size() * m];
		ys = new int[points.size() * m];
		for (int i = 0; i < points.size(); ++i) {
			Point p = points.get(i);
			xs[i] = p.x;
			ys[i] = p.y;
		}
		if (symmetrical) {
			for (int i = points.size() - 1; i >= 0; --i) {
				Point p = points.get(i);
				xs[points.size() * 2 - i - 1] = p.x;
				ys[points.size() * 2 - i - 1] = -p.y;
			}
		}
		Polygon p = new Polygon(xs, ys, xs.length);
		return p;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public static void main(String[] args) {
		new DecalEditor();
	}
}

class JColorChoserModal extends JDialog {
	private static final long serialVersionUID = -881830357847829069L;

	JColorChooser jcc;

	JTextField txtAlpha;

	JColorChoserModal(JFrame owner) {
		super(owner, true);

		jcc = new JColorChooser(Color.GREEN);
		txtAlpha = new JTextField("255", 3);
		txtAlpha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getAlpha();
			}

		});
		JPanel pan = new JPanel();
		pan.add(jcc);
		pan.add(new JLabel("Alpha"));
		pan.add(txtAlpha);
		this.add(pan);
		this.pack();
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}

	public int getAlpha() {
		int alpha = 255;
		try {
			alpha = Integer.parseInt(txtAlpha.getText());
		} catch (Exception exp) {

		} finally {
			if (alpha < 0)
				alpha = 0;
			if (alpha > 255)
				alpha = 255;
			txtAlpha.setText(Integer.toString(alpha));
		}
		return alpha;
	}

	public Color getColor() {
		int alpha = getAlpha();
		Color c, temp = jcc.getColor();
		c = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), alpha);
		return c;
	}
}
