package gurt.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import javax.swing.JFrame;

/**
 * Handles important events for a javax.swing.JFrame. The class is abstract, so
 * it cannot be used directly. Subclass it, and override the specific method
 * calls that you desire. Do not call the super method implementations, they do
 * not do anything.
 * 
 * @author Owner
 * 
 */
public abstract class JFrameEventHandler implements KeyListener,
		MouseMotionListener, MouseListener, MouseWheelListener, WindowListener,
		WindowStateListener, WindowFocusListener
{
	public static final int	SUPPRESS_MISSING_METHOD		= 1;
	public static final int	USE_KEY_LISTENER			= 2;
	public static final int	USE_MOUSE_LISTENER			= 4;
	public static final int	USE_MOUSE_MOTION_LISTENER	= 8;
	public static final int	USE_MOUSE_WHEEL_LISTENER	= 16;
	public static final int	USE_WINDOW_LISTENER			= 32;
	public static final int	USE_WINDOW_STATE_LISTENER	= 64;
	public static final int	USE_WINDOW_FOCUS_LISTENER	= 128;
	private int				state;
	private boolean			useMissing;

	/**
	 * Assigns all event listeners to the supplied JFrame.
	 * MethodMissingExceptions are supressed.
	 * 
	 * @param frame
	 *            a frame to add listeners to
	 */
	public JFrameEventHandler(JFrame frame)
	{
		useMissing = false;
		state = 255;
		init(frame);
	}

	/**
	 * Assigns all event listeners to the supplied JFrame.
	 * 
	 * @param frame
	 *            a frame to add listeners to
	 * @param suppressMethodMissing
	 *            if set to true, event handling methods that do not receive an
	 *            overriding implementation will not throw an exception.
	 */
	public JFrameEventHandler(JFrame frame, boolean suppressMethodMissing)
	{
		useMissing = !suppressMethodMissing;
		state = 255;
		init(frame);
	}

	/**
	 * Assigns the indicated event listeners to the supplied JFrame.
	 * MethodMissingExceptions are not supressed.
	 * 
	 * @param frame
	 *            a frame to add listeners to
	 * @param listenerState
	 *            a bit flag indicating which event listeners to assign to the
	 *            frame
	 */
	public JFrameEventHandler(JFrame frame, int listenerState)
	{
		useMissing = true;
		state = listenerState;
		init(frame);
	}

	/**
	 * Assigns the indicated event listeners to the supplied JFrame.
	 * 
	 * @param frame
	 *            a frame to add listeners to
	 * @param suppressMethodMissing
	 *            if set to true, event handling methods that do not receive an
	 *            overriding implementation will not throw an exception. *
	 * @param listenerState
	 *            a bit flag indicating which event listeners to assign to the
	 *            frame
	 */
	public JFrameEventHandler(JFrame frame, boolean suppressMethodMissing,
			int listenerState)
	{
		useMissing = !suppressMethodMissing;
		state = listenerState;
		init(frame);
	}

	private void init(JFrame frame)
	{
		if (check(USE_KEY_LISTENER))
			frame.addKeyListener(this);
		if (check(USE_MOUSE_LISTENER))
			frame.addMouseListener(this);
		if (check(USE_MOUSE_MOTION_LISTENER))
			frame.addMouseMotionListener(this);
		if (check(USE_MOUSE_WHEEL_LISTENER))
			frame.addMouseWheelListener(this);
		if (check(USE_WINDOW_LISTENER))
			frame.addWindowListener(this);
		if (check(USE_WINDOW_STATE_LISTENER))
			frame.addWindowStateListener(this);
		if (check(USE_WINDOW_FOCUS_LISTENER))
			frame.addWindowFocusListener(this);
	}

	private boolean check(int flag)
	{
		return (state & flag) != 0;
	}

	private void missing(String name)
	{
		if (useMissing)
			throw new MethodMissingException(name);
	}

	public void windowGainedFocus(WindowEvent e)
	{
		missing("windowGainedFocus");
	}

	public void windowLostFocus(WindowEvent e)
	{
		missing("windowLostFocus");
	}

	public void windowStateChanged(WindowEvent e)
	{
		missing("windowStateChanged");
	}

	public void windowActivated(WindowEvent e)
	{
		missing("windowActivated");
	}

	public void windowClosed(WindowEvent e)
	{
		missing("windowClosed");
	}

	public void windowClosing(WindowEvent e)
	{
		missing("windowClosing");
	}

	public void windowDeactivated(WindowEvent e)
	{
		missing("windowDeactivated");
	}

	public void windowDeiconified(WindowEvent e)
	{
		missing("windowDeiconified");
	}

	public void windowIconified(WindowEvent e)
	{
		missing("windowIconified");
	}

	public void windowOpened(WindowEvent e)
	{
		missing("windowOpened");
	}

	public void mouseClicked(MouseEvent e)
	{
		missing("mouseClicked");
	}

	public void mouseEntered(MouseEvent e)
	{
		missing("mouseEntered");
	}

	public void mouseExited(MouseEvent e)
	{
		missing("mouseExited");
	}

	public void mousePressed(MouseEvent e)
	{
		missing("mousePressed");
	}

	public void mouseReleased(MouseEvent e)
	{
		missing("mouseReleased");
	}

	public void keyPressed(KeyEvent e)
	{
		missing("keyPressed");
	}

	public void keyReleased(KeyEvent e)
	{
		missing("keyReleased");
	}

	public void keyTyped(KeyEvent e)
	{
		missing("keyTyped");
	}

	public void mouseDragged(MouseEvent e)
	{
		missing("mouseDragged");
	}

	public void mouseMoved(MouseEvent e)
	{
		missing("mouseMoved");
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		missing("mouseWheelMoved");
	}
}
