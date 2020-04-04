package com.timofriedl.simulationbox.object;

import com.timofriedl.simulationbox.Simulation;
import com.timofriedl.simulationbox.vector.Vector2D;

/**
 * Represents a {@link MovingObject} that has a mass.
 */
public abstract class MassObject extends MovingObject {

	/**
	 * the (non-final) gravity constant in cube-units per (massUnit * square tick)
	 */
	public static double G = 6.67408E-11 * 0.001;

	/**
	 * the mass of this object
	 */
	protected double mass;

	/**
	 * Creates a new object with the attributes of a {@link MovingObject} and a
	 * given mass.
	 * 
	 * @param simulation    the reference to the main simulation instance
	 * @param position      the ingame center position of the object
	 * @param speed         the ingame speed of the object in units per tick
	 * @param size          the ingame width and height of the object
	 * @param rotation      the ingame rotation of the object in radians
	 * @param rotationSpeed the ingame rotation speed of the object in radians per
	 *                      tick
	 * @param mass          the mass of the object
	 */
	public MassObject(Simulation simulation, Vector2D position, Vector2D speed, Vector2D size, double rotation,
			double rotationSpeed, double mass) {
		super(simulation, position, speed, size, rotation, rotationSpeed);

		this.mass = mass;
	}

	/**
	 * Calculates the gravity acceleration two another {@link MassObject}s.
	 * 
	 * Does nothing if this is <code>o</code>.
	 * 
	 * @param o the object that this {@link MassObject} will be attracted to
	 */
	protected void tickGravityTo(MassObject o) {
		if (o == this)
			return;

		final Vector2D dis = o.position.subtract(position);
		final Vector2D acc = dis.scale(G * o.mass / Math.pow(dis.length(), 3));

		speed = speed.add(acc);
	}

	/**
	 * @return the current mass of this {@link MassObject}
	 */
	public double getMass() {
		return mass;
	}

	/**
	 * @param mass the new mass of this {@link MassObject}
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}

}
