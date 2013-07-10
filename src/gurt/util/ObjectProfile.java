package gurt.util;

import java.util.ArrayList;

public class ObjectProfile {
	private int decalID, startSoundID, endSoundID;

	private double[] engineThrust, engineDirection;

	public static ArrayList<ObjectProfile> profiles = new ArrayList<ObjectProfile>();

	private ObjectProfile(int decalID, int startSoundID, int endSoundID,
			double[] engineThrust, double[] engineDirection) {
		this.decalID = decalID;
		this.startSoundID = startSoundID;
		this.endSoundID = endSoundID;
		this.engineThrust = engineThrust;
		this.engineDirection = engineDirection;
	}

	public static ObjectProfile createObjectProfile(int decalID,
			int startSoundID, int endSoundID, double[] engineThrust,
			double[] engineDirection) {
		ObjectProfile obj = new ObjectProfile(decalID, startSoundID,
				endSoundID, engineThrust, engineDirection);
		profiles.add(obj);
		return obj;
	}

	public static ObjectProfile getObjectProfile(int objectID) {
		return profiles.get(objectID);
	}

	public int getDecalID() {
		return decalID;
	}

	public int getEndSoundID() {
		return endSoundID;
	}

	public int getStartSoundID() {
		return startSoundID;
	}

	public double[] getEngineDirection() {
		return engineDirection;
	}

	public double[] getEngineThrust() {
		return engineThrust;
	}

}
