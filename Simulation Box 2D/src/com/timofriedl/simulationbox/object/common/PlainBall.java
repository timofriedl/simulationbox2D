package com.timofriedl.simulationbox.object.common;

import java.awt.Color;
import java.awt.Graphics2D;

import com.timofriedl.simulationbox.Simulation;
import com.timofriedl.simulationbox.object.MassObject;
import com.timofriedl.simulationbox.vector.Vector2D;

/**
 * Represents a round {@link MassObject} with a given color.
 * 
 * @author Timo Friedl
 */
public class PlainBall extends MassObject {

	/**
	 * the color of this ball
	 */
	protected Color color;

	/**
	 * Creates a new {@link Ball} instance.
	 * 
	 * @param simulation the reference to the main game instance
	 * @param position   the ingame position of this ball
	 * @param speed      the speed of this ball
	 * @param diameter   the diameter of this ball
	 * @param mass       the mass of this ball
	 */
	public PlainBall(Simulation simulation, Vector2D position, Vector2D speed, double diameter, double mass,
			Color color) {
		super(simulation, position, speed, new Vector2D(diameter, diameter), 0.0, 0.0, mass);

		this.color = color;
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		simulation.getCamera().fillCircle(g, position, size.getX());
	}

}
