package fr.ld36.utils;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import fr.mimus.jbasicgl.maths.Vec2;

public class Audio
{
	public static HashMap<String, Audio> list = new HashMap<String, Audio>();
	public static void loadSound()
	{
		new Audio("rsc/sounds/jump.wav");
		new Audio("rsc/sounds/opendoor.wav");
		new Audio("rsc/sounds/opentrap.wav");
		new Audio("rsc/sounds/lock.wav");
		new Audio("rsc/sounds/actionDefault.wav");
		new Audio("rsc/sounds/actionFail.wav");
		new Audio("rsc/sounds/dead.wav");
		new Thread() {
			public void run() 
			{
				Audio octa = new Audio("rsc/sounds/Octa.wav");
				octa.setVolume(0.7f).loop();
			}
		}.start();
	}
	
	Clip clip;
	FloatControl volumeControl;
	String path;
	boolean start;
	
	public Audio(String p)
	{
		path=p;
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(p));
			clip = AudioSystem.getClip();
			clip.open(ais);
			volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			setVolume(0.5f);
			start = false;
			list.put(path, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Audio setVolume(float p)
	{
		if (p < 0f)
			p = 0f;
		if (p > 1f)
			p = 1f;
		float m = volumeControl.getMaximum() - volumeControl.getMinimum();
		float v = volumeControl.getMinimum() + (m * p);
		if(v < volumeControl.getMinimum()) v = volumeControl.getMinimum();
		if(v > volumeControl.getMaximum()) v = volumeControl.getMaximum();
		volumeControl.setValue(v);
		return this;
	}
	
	public Audio play()
	{
		start = true;
		clip.setFramePosition(0);
		clip.start();
		return this;
	}
	public Audio play(Vec2 A, Vec2 B)
	{
		float len = (float) A.copy().sub(B).lengthSqrd();
		float volume = 0.25f + 0.5f - (float) Math.min(0.5, len / (320f * 320f));
		return (setVolume(volume).play());
	}
	
	public Audio loop()
	{
		start = true;
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		return this;
	}
	
	public Audio stop()
	{
		start = false;
		clip.close();
		return this;
	}
	
	public void print()
	{
		System.out.println(path);
		System.out.println("- Frame: " + clip.getFramePosition() + " / " + clip.getFrameLength());
		System.out.println("- Is Open: " + clip.isOpen());
		System.out.println("- Is Active: " + clip.isActive());
		System.out.println("- Is Running: " + clip.isRunning());
	}
	
	public boolean isStarted()
	{
		if (start && clip.getFramePosition() == clip.getFrameLength())
			start = false;
		return (start);
	}
	
	
}
