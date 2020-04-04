package com.timofriedl.simulationbox.object;

import com.timofriedl.simulationbox.Simulation;
import com.timofriedl.simulationbox.vector.Vector2D;

/**
 * Represents {@link SimulationObject}s that can move and rotate.
 * 
 * @author Timo Friedl
 */
public abstract class MovingObject extends SimulationObject {

	/**
	 * the speed of this object
	 */
	protected Vector2D speed;

	/**
	 * the rotation speed of this object in radians per tick
	 */
	protected double rotationSpeed;

	/**
	 * Creates a new moving simulation object with given {@link SimulationObject}
	 * attributes and a given speed and rotation speed.
	 * 
	 * @param simulation    the reference to the main simulation instance
	 * @param position      the (x,y) vector of the object position in this
	 *                      simulation
	 * @param speed         the speed vector of this object
	 * @param size          the (x,y) vector of the object size in this simulation
	 * @param rotation      the rotation of this object in radians
	 * @param rotationSpeed the rotation speed of this object in radians per tick
	 */
	public MovingObject(Simulation simulation, Vector2D position, Vector2D speed, Vector2D size, double rotation,
			double rotationSpeed) {
		super(simulation, position, size, rotation);

		this.speed = speed;
		this.rotationSpeed = rotationSpeed;
	}

	@Override
	public void tick() {
		move();
	}

	/**
	 * Moves this object according to its current speed and rotation speed.
	 */
	private void move() {
		position = position.add(speed);
		rotation += rotationSpeed;
	}

	/**
	 * @return the object speed
	 */
	public Vector2D getSpeed() {
		return speed;
	}

	/**
	 * @param speed the new object speed
	 */
	public void setSpeed(Vector2D speed) {
		this.speed = speed;
	}

	/**
	 * @return the rotation speed
	 */
	public double getRotationSpeed() {
		return rotationSpeed;
	}

	/**
	 * @param rotationSpeed the new rotation speed
	 */
	public void setRotationSpeed(double rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

}
