package com.timofriedl.simulationbox.object.common;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.Box;

import com.timofriedl.simulationbox.Simulation;
import com.timofriedl.simulationbox.object.MassObject;
import com.timofriedl.simulationbox.object.SimulationObject;
import com.timofriedl.simulationbox.vector.Vector2D;

/**
 * Represents a rectangle-shaped {@link SimulationObject}
 */
public class PlainBox extends MassObject {

	/**
	 * the color of this box
	 */
	private Color color;

	/**
	 * Creates a new {@link Box} instance.
	 * 
	 * @param simulation    the reference to the main simulation instance
	 * @param position      the center position of the box
	 * @param speed         the speed of the box in units per tick
	 * @param size          the width and height of the box
	 * @param rotation      the rotation of the box in radians
	 * @param rotationSpeed the rotation speed of the box in radians per tick
	 * @param mass          the mass of the box
	 * @param color         the color of the box
	 */
	public PlainBox(Simulation simulation, Vector2D position, Vector2D speed, Vector2D size, double rotation,
			double rotationSpeed, double mass, Color color) {
		super(simulation, position, speed, size, rotation, rotationSpeed, mass);

		this.color = color;
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		simulation.getCamera().fillRectangle(g, position, size, rotation);
	}

	/**
	 * @return the current color of this box
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the new color of this box
	 */
	public void setColor(Color color) {
		this.color = color;
	}

}
