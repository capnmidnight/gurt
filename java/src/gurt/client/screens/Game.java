/**
 * FILE: Game.java
 * DATE: November 7th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.client.screens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import gurt.client.GameState;
import gurt.client.sound.AudioOutput;
import gurt.physics.Universe;
import gurt.physics.objects.Bullet;
import gurt.physics.objects.Ship;

/**
 * The screen state for playing the game.
 * 
 * @author stm
 */
public class Game extends Screen
{
	// the user controlled ship
	private Ship		ship;
	// the entire universe
	private Universe	uni;
	private Map			map	= null;
	private boolean bulletShot;

	/**
	 * Creates a new game state, with a universe and one user controlled ship.
	 * The ship is used as the focal point for the viewport.
	 */
	public Game()
	{
		ship = new Ship(1000, 1000);
		uni = new Universe();
		uni.setFocus(ship);
		bulletShot = false;
	}

	boolean	startMusic	= true;

	@Override
	public void update(float dt)
	{
		super.update(dt);
		uni.update(dt);
	}

	public void doInput()
	{
		if (getInputState(KEYBOARD, KeyEvent.VK_LEFT) == 1)
		{
			ship.turn(-1);
		} else if (getInputState(KEYBOARD, KeyEvent.VK_RIGHT) == 1)
		{
			ship.turn(1);
		} else
		{
			ship.turn(0);
		}
		if (getInputState(KEYBOARD, KeyEvent.VK_UP) == 1)
		{
			ship.thrust(1 + (Screen.shift ? 1 : 0));
		} else if (getInputState(KEYBOARD, KeyEvent.VK_DOWN) == 1)
		{
			ship.thrust(-1);
		} else
		{
			ship.thrust(0);
		}
		int key = Screen.popKey();
		switch (key)
		{
			case KeyEvent.VK_M:
				this.nextState = GameState.Map;
				this.stateChanged = true;
				break;
			case KeyEvent.VK_ESCAPE:
				this.nextState = GameState.Menu;
				this.stateChanged = true;
				break;
			case KeyEvent.VK_SPACE:
				if (ship.canFire())
				{
					Bullet b = ship.fireBullet();
					bulletShot = true;
					uni.addObject(b);
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void sound(AudioOutput audio)
	{
		if (ship.getThrust() != 0)
		{
			audio.playSound(8);
		}
		if(bulletShot)
		{
			bulletShot = false;
			audio.playSound(0);
		}
		uni.sound(audio);
	}

	@Override
	public void render(Graphics2D g)
	{
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, Screen.width, Screen.height);
		uni.render(g);
	}

	/**
	 * Creates a new Map screen with the objects in the game.
	 * 
	 * @return a single instance of a Map screen
	 */
	public Map makeMap()
	{
		// TODO improve mapping
		if (map == null)
		{
			map = new Map(this.uni);
		}
		return map;
	}
}
