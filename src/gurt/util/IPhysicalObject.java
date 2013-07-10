package gurt.util;

/**
 * A physical object in the game universe is modeled on a server. The game
 * client is merely an interface to that data on the server. Graphical elements
 * are then decals that are painted in the location of that object. The
 * <b>IPhysicalObject</b> interface defines an interface for retrieving the
 * physical location of objects as well as the interface for manipulating those
 * objects on the server.
 * 
 * The only means for manipulating an object is to apply some force to it.
 * Objects that can be manipulated will have "engines" attached to them. To
 * manipulate the object it is necessary to engage one of those engines. In the
 * case of rotational thrusters, this may or may not result in a physically
 * accurate calculation for rotations based on thruster engines.
 * 
 * This engine state is sent as a message to the physics server. The physics
 * server will perform the necessary calculations and update the status of the
 * object. This prevents the user from cheating by only enabling them access to
 * a "reallistic" control board for a "ship".
 * 
 * This interface is the main means of communication between the client and the
 * server.
 * 
 * @author smcbeth
 * 
 */
public interface IPhysicalObject {
	/**
	 * @return the X component of an object's location
	 */
	public double getX();

	/**
	 * @return the Y component of an object's location
	 */
	public double getY();

	/**
	 * @return the X component of the object's velocity
	 */
	public double getVX();

	/**
	 * @return the Y component of the object's velocity
	 */
	public double getVY();

	/**
	 * @return the direction that the object is pointing -- an angle in radians
	 *         for rotation.
	 */
	public double getHeading();

	/**
	 * The object ID is the fingerprint of the object. No two objects will share
	 * an object ID, though new objects may be given object IDs that were once
	 * owned by older objects.
	 * 
	 * @return the unique object identifier number for this particular object
	 */
	public int getObjectID();

	/**
	 * The object type ID references an object profile in an object dictionary.
	 * The profile covers physical specifications of the object. These profiles
	 * are static, only being updated with updates to the game itself. This
	 * allows indicating a large amount of information about an object with a
	 * single integer identifier.
	 * 
	 * @return the index of the type of item that this object represents
	 */
	public int getObjectTypeID();

	/**
	 * Turn an engine "on" or "off", causing it to apply force to the object
	 * 
	 * @param index
	 *            the integer index of the engine to engage
	 * @param state
	 *            the boolean state of the engine, <b>on</b> or <b>off</b>
	 */
	public void setEngineState(int index, boolean state);

	/**
	 * Send a special encoded message to the physical object. The meaning will
	 * usually be something context-specific, like "fire weapon 1".
	 * 
	 * @param msg
	 *            the numbered message to send
	 */
	public void sendCode(int msg);

	/**
	 * Send a text message to the physical object. Usually some kind of
	 * communique that will be sent to other users.
	 * 
	 * @param msg
	 *            the text to send
	 */
	public void sendMsg(String msg);
}
