/**
 * FILE: AudioOutput.java
 * DATE: November 13th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.client.sound;

import java.net.URL;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;

import gurt.client.MainFrame;
import gurt.util.ErrorHandler;

public class AudioOutput
{
	private ArrayList<ArrayList<Clip>>	outputLines;
	private ArrayList<String>			names;
	private static Mixer.Info[]			mixers;
	private Mixer						mixer;
	static
	{
		mixers = AudioSystem.getMixerInfo();
	}

	public AudioOutput(int mixerIndex)
	{
		mixer = AudioSystem.getMixer(mixers[mixerIndex]);
		outputLines = new ArrayList<ArrayList<Clip>>();
		names = new ArrayList<String>();
	}

	public int addSoundFile(String path)
	{
		try
		{
			ClassLoader cl = this.getClass().getClassLoader();
			URL url = cl.getResource(path);
                        
                        
			names.add(path);
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
			Clip line = (Clip) mixer.getLine(info);
			line.open(ais);
			ArrayList<Clip> temp = new ArrayList<Clip>();
			temp.add(line);
			outputLines.add(temp);
			return outputLines.size() - 1;
		} catch (Exception e)
		{
			Throwable a = e;
			while (a != null)
			{
				System.err.println(a.getClass().toString() + ": "
						+ a.getMessage());
				a = a.getCause();
			}
			throw new RuntimeException(e);
		}
	}

	public void playSound(int i)
	{
		if (MainFrame.SOUND_ON)
		{
			ArrayList<Clip> lines = outputLines.get(i);
			boolean found = false;
			Clip toRun = null;
			for (int n = 0; n < lines.size() && !found; ++n)
			{
				Clip line = lines.get(n);
				if (!line.isRunning())
				{
					found = true;
					toRun = line;
				}
			}
			if (!found)
			{
				String path = names.get(i);
				ClassLoader cl = this.getClass().getClassLoader();
				URL url = cl.getResource(path);
				try
				{
					AudioInputStream ais = AudioSystem.getAudioInputStream(url);
					DataLine.Info info = new DataLine.Info(Clip.class, ais
							.getFormat());
					Clip line = (Clip) mixer.getLine(info);
					line.open(ais);
					lines.add(line);
					toRun = line;
				} catch (Exception e)
				{
					ErrorHandler.printError(e, "Couldn't load audio stream: "
							+ url.toString());
				}
			}
			if(toRun != null)
			{
				toRun.setFramePosition(0);
				toRun.start();
			}
		}
	}

	public static Mixer.Info[] getAvailableMixers()
	{
		return mixers;
	}

	public static int[] findOutputMixers()
	{
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < mixers.length; ++i)
		{
			if (mixers[i].getDescription().indexOf("Playback") > 0)
			{
				temp.add(i);
			} else if (mixers[i].getName().equals("Java Sound Audio Engine"))
			{
				temp.add(i);
			}
		}
		int[] output = new int[temp.size()];
		for (int i = 0; i < temp.size(); ++i)
		{
			output[i] = temp.get(i);
		}
		return output;
	}
}
