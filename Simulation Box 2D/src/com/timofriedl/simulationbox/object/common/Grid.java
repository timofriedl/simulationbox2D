package com.timofriedl.simulationbox.object.common;

import java.awt.Color;
import java.awt.Graphics2D;

import com.timofriedl.simulationbox.Simulation;
import com.timofriedl.simulationbox.object.SimulationObject;
import com.timofriedl.simulationbox.vector.Vector2D;

/**
 * An ingame grid for better map orientation.
 * 
 * @author Timo Friedl
 */
public class Grid extends SimulationObject {

	/**
	 * the grid color
	 */
	protected Color color;

	/**
	 * the width of the grid lines
	 */
	protected double lineWidth = 1.0;

	/**
	 * the size of one grid square
	 */
	protected final double squareSize;

	/**
	 * the number of squares in one grid row
	 */
	protected final int gridSize;

	/**
	 * the lines to render
	 */
	protected Vector2D[][] lines;

	/**
	 * Creates a new square grid for better map orientation.
	 * 
	 * @param simulation the reference to the main simulation instance
	 * @param position   the ingame center position of the grid
	 * @param gridSize   the number of squares in one grid row
	 * @param squareSize the ingame width and height of one grid square
	 * @param color      the color of the grid
	 */
	public Grid(Simulation simulation, Vector2D position, int gridSize, double squareSize, Color color) {
		super(simulation, position, new Vector2D(gridSize * squareSize, gridSize * squareSize), 0.0);

		this.color = color;
		this.squareSize = squareSize;
		this.gridSize = gridSize;
	}

	@Override
	public void tick() {
		lines = new Vector2D[gridSize * 2 + 2][2];

		final Vector2D ul = position.subtract(size).scale(0.5);

		for (int x = 0; x < gridSize + 1; x++) {
			final double px = ul.getX() + x * squareSize;
			lines[x][0] = new Vector2D(px, position.getY() - size.getY() * 0.5);
			lines[x][1] = new Vector2D(px, position.getY() + size.getY() * 0.5);
		}

		for (int y = 0; y < gridSize + 1; y++) {
			final double py = ul.getY() + y * squareSize;
			lines[gridSize + 1 + y][0] = new Vector2D(position.getX() - size.getX() * 0.5, py);
			lines[gridSize + 1 + y][1] = new Vector2D(position.getX() + size.getX() * 0.5, py);
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		for (Vector2D[] line : lines)
			simulation.getCamera().drawLine(g, line[0], line[1], lineWidth);
	}

	/**
	 * @return the current grid color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the new grid color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the current grid line width
	 */
	public double getLineWidth() {
		return lineWidth;
	}

	/**
	 * @param lineWidth the new grid line width
	 */
	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

}
