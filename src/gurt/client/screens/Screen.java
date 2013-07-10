package gurt.client.screens;

import java.awt.Graphics2D;
import java.util.LinkedList;

import gurt.client.GameState;
import gurt.client.MainFrame;
import gurt.client.sound.AudioOutput;

public abstract class Screen
{
	protected GameState	nextState;
	protected boolean	stateChanged	= false;

	/**
	 * When this screen is done, indicates the next screen
	 * 
	 * @return the value to change after this screen is done
	 */
	public GameState getNextState()
	{
		return nextState;
	}

	/**
	 * Indicates if this screen is done and the game is ready to move to the
	 * next screen
	 * 
	 * @return true if it's time for the game state
	 */
	public boolean isStateChanged()
	{
		return stateChanged;
	}

	public void start()
	{
		this.stateChanged = false;
	}

	/**
	 * Renders the screen, duh.
	 * 
	 * @param dt
	 *            the number of milliseconds since the last update
	 * @param g
	 *            the graphics context to render to.
	 */
	public abstract void render(Graphics2D g);

	/**
	 * Play any sounds that were made last time
	 * 
	 * @param audio
	 */
	public abstract void sound(AudioOutput audio);

	/**
	 * do the effects of any user input
	 */
	public abstract void doInput();

	/**
	 * updates the state of the current screen
	 * 
	 * @param dt -
	 *            the number of milliseconds since the last update
	 */
	public void update(float dt)
	{
		if (keyQueueTimer > 0)
			keyQueueTimer -= dt;
	}

	/**
	 * Shift key modifier
	 */
	public static boolean				shift;
	private static LinkedList<String>	msgQueue;
	private static int[][]				input;
	protected static int				curKeyQueueLoc		= -1,
			keyQueueTimer = 0;
	/**
	 * The dimensions of the screen
	 */
	public static int					width, height;
	/**
	 * A number of indices into the input array
	 */
	private static final int			DELTA_KEY_TIME		= 50;
	public static final int				KEYBOARD			= 0;
	public static final int				KEYQUEUE			= 1;
	private static final int			MAX_KEYQUEUE_LEN	= 25;
	public static final int				MOUSEBUTTONS		= 2;
	public static final int				MOUSELOCATION		= 3;
	public static final int				MX					= 0;
	public static final int				MY					= 1;
	public static final int				MDX					= 2;
	public static final int				MDY					= 3;
	/**
	 * initialize the input block
	 */
	static
	{
		input = new int[4][];
		input[KEYBOARD] = new int[1024];
		input[KEYQUEUE] = new int[MAX_KEYQUEUE_LEN];
		input[MOUSEBUTTONS] = new int[3];
		input[MOUSELOCATION] = new int[4];
		msgQueue = new LinkedList<String>();
	}

	/**
	 * Pushes a new system message onto the queue. These messages are used to
	 * alter the overall application state.
	 * 
	 * @param msg
	 *            the name of the new state to enter
	 */
	public static void pushMsg(String msg)
	{
		msgQueue.addLast(msg);
	}

	/**
	 * remove a message from the queue.
	 * 
	 * @return the oldest message on the message quque
	 */
	public static String popMsg()
	{
		String ret = null;
		if (msgQueue.size() > 0)
			ret = msgQueue.removeFirst();
		return ret;
	}

	public static void setInput(int type, int key, int value)
	{
		input[type][key] = value;
	}

	public static int getInputState(int type, int key)
	{
		return input[type][key];
	}

	public static void setMouseLocation(int x, int y)
	{
		setInput(MOUSELOCATION, MDX, x - input[MOUSELOCATION][MX]);
		setInput(MOUSELOCATION, MDY, y - input[MOUSELOCATION][MY]);
		setInput(MOUSELOCATION, MX, x);
		setInput(MOUSELOCATION, MY, y);
	}

	public static void setMouseButton(int button, int value)
	{
		setInput(MOUSEBUTTONS, button, value);
	}

	public static void setKey(int keyCode, int value)
	{
		setInput(KEYBOARD, keyCode, value);
		if (value == 1)
		{
			pushKey(keyCode);
		}
	}

	public static void pushKey(int keyCode)
	{
		if (keyQueueTimer <= 0 && curKeyQueueLoc < MAX_KEYQUEUE_LEN - 1)
		{
			curKeyQueueLoc++;
			input[KEYQUEUE][curKeyQueueLoc] = keyCode;
			keyQueueTimer += DELTA_KEY_TIME;
		}
	}

	public static int popKey()
	{
		int ret = -1;
		if (curKeyQueueLoc >= 0)
		{
			ret = input[KEYQUEUE][curKeyQueueLoc];
			curKeyQueueLoc--;
		}
		return ret;
	}

	public static void setSize(int width, int height)
	{
		Screen.width = width;
		Screen.height = height;
		if (MainFrame.instance != null)
			MainFrame.instance.setSize(width, height);
	}
}
