/**
 * FILE: Menu.java
 * DATE: November 8th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.client.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import gurt.client.GameState;
import gurt.client.sound.AudioOutput;

public class Menu extends Screen
{
	private String[]	options;
	private Font		f1, f2;
	private int			sel;

	public Menu()
	{
		options = new String[] { "Start", "Options", "Exit" };
		sel = 0;
		f1 = new Font("Arial", Font.PLAIN, 20);
		f2 = new Font("Arial", Font.BOLD, 20);
	}

	@Override
	public void sound(AudioOutput audio)
	{
	}

	@Override
	public void render(Graphics2D g)
	{
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, Screen.width, Screen.height);
		g.setColor(Color.WHITE);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		for (int i = 0; i < options.length; ++i)
		{
			if (i == sel)
			{
				g.setFont(f2);
				g.drawString("-" + options[i] + "-", 20, 40 * (i + 2));
			} else
			{
				g.setFont(f1);
				g.drawString(options[i], 20, 40 * (i + 2));
			}
		}
	}

	@Override
	public void doInput()
	{
		int key = popKey();
		switch (key)
		{
			case KeyEvent.VK_UP:
				sel--;
				break;
			case KeyEvent.VK_DOWN:
				sel++;
				break;
			case KeyEvent.VK_ENTER:
				select();
				break;
			case KeyEvent.VK_ESCAPE:
				this.nextState = GameState.Exit;
				this.stateChanged = true;
				break;
			default:
				break;
		}
		if (sel < 0)
			sel = options.length - 1;
		if (sel >= options.length)
			sel = 0;
	}

	private void select()
	{
		switch (sel)
		{
			case 0:
				this.nextState = GameState.Game;
				options[0] = "Continue";
				this.stateChanged = true;
				break;
			case 1:
				this.nextState = GameState.Options;
				this.stateChanged = true;
				break;
			case 2:
				this.nextState = GameState.Exit;
				this.stateChanged = true;
				break;
			default:
		}
	}
}
