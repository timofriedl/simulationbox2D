package com.timofriedl.simulationbox.gameloop;

import java.awt.Graphics2D;

/**
 * Forces some classes like simulation objects to have a render method.
 * 
 * @author Timo Friedl
 */
public interface Renderable {

	public void render(Graphics2D g);

}
