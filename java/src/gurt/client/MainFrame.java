/**
 * FILE: MainFrame.java
 * DATE: November 7th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.client;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import gurt.client.screens.Game;
import gurt.client.screens.Menu;
import gurt.client.screens.Options;
import gurt.client.screens.Screen;
import gurt.client.sound.AudioOutput;

/**
 * The window for running the application. There is only one window for the
 * application, so all rendering must be done inside of it. This window also
 * brings in user input as well as times the frame updates.
 * 
 * Most user options are stored as static parameters to in this class. <br/>
 * TODO move user options to a serializable, instantiable GameOptions class.<br/>
 * TODO auto load/save user options
 * 
 * @author stm
 * 
 */
public class MainFrame extends Frame implements WindowListener, KeyListener,
		MouseMotionListener, MouseListener, Runnable
{
	private static final long	serialVersionUID	= 5716330318651588082L;
	public static Object		AA_OPTION			= RenderingHints.VALUE_ANTIALIAS_ON;
	public static Object		AA_TEXT_OPTION		= RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
	public static boolean		SOUND_ON			= true;
	private long				lastTime, startTime, frameCount;
	private Thread				thread;
	private boolean				running, allDone;
	private Screen[]			screens;
	private Screen				curScreen;
	private AudioOutput			audio;
	private BufferStrategy		bs;
	public static MainFrame		instance;
	private static final String	HELP				= "An asteroidish game that will eventually feature MMO play.\n\n"
															+ "CONTROLS:\n"
															+ "\t(left/right arrow keys) -- turn ship\n"
															+ "\t(up/down arrow keys) -- thrust fore/aft\n"
															+ "\tM -- Map\n"
															+ "\tEsc -- Exit current screen to previous screen (exit game on main menu)";

	/**
	 * creates a new window for the game to run in.
	 */
	public MainFrame()
	{
		super("Gurt");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ss = tk.getScreenSize();
		this.setSize(ss.width, ss.height);
		Screen.setSize(this.getWidth(), this.getHeight());
		
		this.setUndecorated(true);
		this.addWindowListener(this);
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		turnOffCursor();
		// get resources
		screens = new Screen[GameState.values().length];
		screens[GameState.Menu.ordinal()] = new Menu();
		Game game = new Game();
		screens[GameState.Game.ordinal()] = game;
		screens[GameState.Map.ordinal()] = game.makeMap();
		screens[GameState.Options.ordinal()] = new Options();
		curScreen = screens[GameState.Menu.ordinal()];
		loadSounds();
		// start the application
		running = true;
		thread = new Thread(this);
		lastTime = startTime = System.currentTimeMillis();
		frameCount = 0;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		if(gd.isFullScreenSupported())
		{
			gd.setFullScreenWindow(this);
		}
		this.setVisible(true);
		this.createBufferStrategy(2);
		this.bs = this.getBufferStrategy();
		thread.start();
	}

	private void loadSounds()
	{
		audio = new AudioOutput(AudioOutput.findOutputMixers()[0]);
        audio.addSoundFile("dat/snd/shot.wav");
		audio.addSoundFile("dat/snd/hit1.wav");
		audio.addSoundFile("dat/snd/hit2.wav");
		audio.addSoundFile("dat/snd/hit3.wav");
		audio.addSoundFile("dat/snd/hit4.wav");
		audio.addSoundFile("dat/snd/thud1.wav");
		audio.addSoundFile("dat/snd/thud2.wav");
		audio.addSoundFile("dat/snd/song1.wav");
		audio.addSoundFile("dat/snd/thrust.wav");
                
	}

	/**
	 * replaces the current cursor with a completely transparent one.
	 */
	private void turnOffCursor()
	{
		BufferedImage cursor = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_ARGB);
		Cursor transCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursor, new Point(0, 0), "transCurs");
		this.setCursor(transCursor);
	}

	/**
	 * the main game loop, running in a thread for better user experience
	 */
	public void run()
	{
		allDone = false;
		// TODO use proper thread synchronization
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		while (running)
		{
			long now = System.currentTimeMillis();
			long delta = now - lastTime;
			checkState();
			frameCount++;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, AA_OPTION);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					AA_TEXT_OPTION);
			curScreen.doInput();
			curScreen.update(delta);
			curScreen.render(g);
			curScreen.sound(audio);
			bs.show();
			lastTime = now;
		}
		g.dispose();
		// TODO use proper thread synchronization
		allDone = true;
	}

	/**
	 * handle control messages to change current game state.
	 */
	private void checkState()
	{
		if (curScreen.isStateChanged())
		{
			GameState next = curScreen.getNextState();
			if (next == GameState.Exit)
			{
				windowClosing(null);
			} else
			{
				curScreen = screens[next.ordinal()];
				curScreen.start();
			}
		}
	}

	/**
	 * shuts down any running resources before exiting the application.
	 */
	public void windowClosing(WindowEvent arg0)
	{
		System.out
				.println("Average FPS: "
						+ (1000 * frameCount / (System.currentTimeMillis() - startTime)));
		final Frame f = this;
		new Thread()
		{
			public void run()
			{
				running = false;
				while (!allDone)
					;
				f.setVisible(false);
				f.dispose();
				System.exit(0);
			}
		}.start();
	}

	/**
	 * catch the mouse event and set the mouse button state
	 */
	public void mousePressed(MouseEvent arg0)
	{
		Screen.setMouseButton(arg0.getButton(), 1);
	}

	/**
	 * catch the mouse event and set the mouse button state
	 */
	public void mouseReleased(MouseEvent arg0)
	{
		Screen.setMouseButton(arg0.getButton(), 0);
	}

	/**
	 * catch the key event and set the key state
	 */
	public void keyPressed(KeyEvent arg0)
	{
		Screen.setKey(arg0.getKeyCode(), 1);
		Screen.shift = arg0.isShiftDown();
	}

	/**
	 * catch the key event and set the key state
	 */
	public void keyReleased(KeyEvent arg0)
	{
		Screen.setKey(arg0.getKeyCode(), 0);
		Screen.shift = arg0.isShiftDown();
	}

	/**
	 * catch the mouse event and set the mouse location
	 */
	public void mouseMoved(MouseEvent arg0)
	{
		Screen.setMouseLocation(arg0.getX(), arg0.getY());
	}

	/**
	 * unused
	 */
	public void keyTyped(KeyEvent arg0)
	{
		// empty
	}

	/**
	 * unused
	 */
	public void mouseClicked(MouseEvent arg0)
	{
		// empty
	}

	/**
	 * unused
	 */
	public void mouseDragged(MouseEvent arg0)
	{
		// empty
	}

	/**
	 * unused
	 */
	public void windowActivated(WindowEvent arg0)
	{
		// empty
	}

	/**
	 * unused
	 */
	public void windowClosed(WindowEvent arg0)
	{
		// empty
	}

	/**
	 * unused
	 */
	public void windowDeactivated(WindowEvent arg0)
	{
		// empty
	}

	/**
	 * unused
	 */
	public void windowDeiconified(WindowEvent arg0)
	{
		// empty
	}

	/**
	 * unused
	 */
	public void windowIconified(WindowEvent arg0)
	{
		// empty
	}

	/**
	 * unused
	 */
	public void windowOpened(WindowEvent arg0)
	{
		// empty
	}

	/**
	 * unused
	 */
	public void mouseEntered(MouseEvent arg0)
	{
		// empty
	}

	/**
	 * unused
	 */
	public void mouseExited(MouseEvent arg0)
	{
		// empty
	}

	/**
	 * main program entry point
	 * 
	 * start the application with command line switch "-Dsun.java2d.opengl=True"
	 * for hardware accelerated OpenGL pipeline.
	 * 
	 * @param args
	 *            command line arguments (unused)
	 */
	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			if (args[0].equals("?"))
			{
				System.out.println(HELP);
			}
		} else
		{
			instance = new MainFrame();
		}
	}
}
