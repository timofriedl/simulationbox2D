package com.timofriedl.simulationbox.object;

import com.timofriedl.simulationbox.Simulation;
import com.timofriedl.simulationbox.gameloop.Renderable;
import com.timofriedl.simulationbox.gameloop.Tickable;
import com.timofriedl.simulationbox.vector.Vector2D;

/**
 * Represents any object in the simulation that can be rendered and might have
 * some calculations to do.
 * 
 * @author Timo Friedl
 */
public abstract class SimulationObject implements Tickable, Renderable {

	/**
	 * the reference to the main simulation instance
	 */
	protected Simulation simulation;

	/**
	 * the (x,y) vector of the object position in this simulation
	 */
	protected Vector2D position;

	/**
	 * the (x,y) vector of the object size in this simulation
	 */
	protected Vector2D size;

	/**
	 * the rotation of this object in radians
	 */
	protected double rotation;

	/**
	 * Creates a new simulation object with a given position and size in the
	 * simulation.
	 * 
	 * @param simulation the reference to the main simulation instance
	 * @param position   the (x,y) vector of the object position in this simulation
	 * @param size       the (x,y) vector of the object size in this simulation
	 * @param rotation   the rotation of this object in radians
	 */
	public SimulationObject(Simulation simulation, Vector2D position, Vector2D size, double rotation) {
		this.simulation = simulation;
		this.position = position;
		this.size = size;
		this.rotation = rotation;
	}

	/**
	 * @return the ingame object position
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * @param position the new ingame object position
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	/**
	 * @return the ingame object width and height
	 */
	public Vector2D getSize() {
		return size;
	}

	/**
	 * @param size the new ingame object width and height
	 */
	public void setSize(Vector2D size) {
		this.size = size;
	}

	/**
	 * @return the object rotation in radians
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the new object rotation in radians
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

}
