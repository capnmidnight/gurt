/**
 * FILE: Options.java
 * DATE: November 11th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.client.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import gurt.client.GameState;
import gurt.client.MainFrame;
import gurt.client.sound.AudioOutput;

public class Options extends Screen
{
	private String[]			options;
	private static final int[]	widths	= { 640, 800, 1024, 1152 };
	private static final int[]	heights	= { 480, 600, 768, 864 };
	private static int			curRes;
	private Font				f1, f2;
	private int					sel;
	private boolean				hit;

	public Options()
	{
		options = new String[] { "Anti-aliasing: ", "Screen Size: ", "Sound: ",
				"Exit" };
		sel = 0;
		hit = false;
		f1 = new Font("Arial", Font.PLAIN, 20);
		f2 = new Font("Arial", Font.BOLD, 20);
		int curWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		for (int i = 0; i < widths.length; ++i)
		{
			if (curWidth == widths[i])
			{
				curRes = i;
			}
		}
	}

	@Override
	public void sound(AudioOutput audio)
	{
		if (hit)
		{
			audio.playSound(1);
			hit = false;
		}
	}

	@Override
	public void render(Graphics2D g)
	{
		options[0] = "Anti-aliasing: "
				+ ((MainFrame.AA_OPTION
						.equals(RenderingHints.VALUE_ANTIALIAS_ON)) ? "ON"
						: "OFF");
		options[1] = "Screen Size: " + Screen.width + "x" + Screen.height;
		options[2] = "Sound: " + (MainFrame.SOUND_ON ? "ON" : "OFF");
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
				hit = true;
				select();
				break;
			case KeyEvent.VK_ESCAPE:
				this.nextState = GameState.Menu;
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
				if (MainFrame.AA_OPTION
						.equals(RenderingHints.VALUE_ANTIALIAS_ON))
				{
					MainFrame.AA_OPTION = RenderingHints.VALUE_ANTIALIAS_OFF;
					MainFrame.AA_TEXT_OPTION = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
				} else
				{
					MainFrame.AA_OPTION = RenderingHints.VALUE_ANTIALIAS_ON;
					MainFrame.AA_TEXT_OPTION = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
				}
				break;
			case 1:
				curRes++;
				if (curRes >= widths.length)
				{
					curRes = 0;
				}
				Screen.setSize(widths[curRes], heights[curRes]);
				break;
			case 2:
				MainFrame.SOUND_ON = !MainFrame.SOUND_ON;
				break;
			case 3:
				this.stateChanged = true;
				this.nextState = GameState.Menu;
				break;
			default:
		}
	}
}
