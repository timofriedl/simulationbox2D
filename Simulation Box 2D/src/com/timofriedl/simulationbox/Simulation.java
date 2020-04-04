package com.timofriedl.simulationbox;

import java.awt.Color;

import com.timofriedl.simulationbox.camera.Camera;
import com.timofriedl.simulationbox.display.Window;
import com.timofriedl.simulationbox.gameloop.GameLoop;
import com.timofriedl.simulationbox.gameloop.Renderable;
import com.timofriedl.simulationbox.gameloop.Tickable;
import com.timofriedl.simulationbox.input.KeyInput;
import com.timofriedl.simulationbox.input.MouseInput;

/**
 * Inherit from this class to create your own simulation.
 * 
 * This simulation "engine" is reduced to just the things you need to create a
 * simple 2D Java simulation.
 * 
 * Not very performant.</br>
 * Not for all purposes.</br>
 * Not with all functionality you can imagine.
 * 
 * Just basic stuff for fun projects.
 * 
 * @author Timo Friedl
 */
public abstract class Simulation implements Tickable, Renderable {

	/**
	 * the window to render elements on
	 */
	protected final Window window;

	/**
	 * the tick-render-loop of this simulation
	 */
	protected final GameLoop gameLoop;

	/**
	 * the simulation camera that can be moved to explore the simulation map
	 */
	protected final Camera camera;

	/**
	 * the mouse input functionality
	 */
	protected final MouseInput mouseInput;

	/**
	 * the key input functionality
	 */
	protected final KeyInput keyInput;

	/**
	 * Creates a new simulation instance.
	 * 
	 * @param title   the window title
	 * @param bgColor the background color of this simulation
	 * @param camera  the simulation camera
	 */
	public Simulation(String title, Color bgColor) {
		window = new Window(this, title, bgColor);
		mouseInput = new MouseInput(this);
		keyInput = new KeyInput(this);
		gameLoop = new GameLoop(this);
		camera = new Camera(this);

		window.setVisible(true);
		gameLoop.start();
	}

	/**
	 * Space to initialize simulation objects etc.
	 */
	public abstract void init();

	/**
	 * @return the window instance
	 */
	public Window getWindow() {
		return window;
	}

	/**
	 * @return the tick-render-loop of this simulation
	 */
	public GameLoop getGameLoop() {
		return gameLoop;
	}

	/**
	 * @return the simulation camera
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * @return the reference to the mouse input
	 */
	public MouseInput getMouseInput() {
		return mouseInput;
	}

	/**
	 * @return the reference to the key input
	 */
	public KeyInput getKeyInput() {
		return keyInput;
	}

}
