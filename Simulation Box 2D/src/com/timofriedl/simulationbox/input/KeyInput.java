package com.timofriedl.simulationbox.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.timofriedl.simulationbox.Simulation;

/**
 * Allows key input actions.
 * 
 * @author Timo Friedl
 */

public class KeyInput implements KeyListener {

	/**
	 * Collection of all key states dependent of the key code
	 */
	private boolean[] pressed;

	/**
	 * Creates a new key input instance.
	 * 
	 * @param simulation the reference to the main simulation instance
	 */
	public KeyInput(Simulation simulation) {
		pressed = new boolean[KeyEvent.RESERVED_ID_MAX];

		simulation.getWindow().getCanvas().addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		pressed[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressed[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// unused
	}

	/**
	 * @return the array of keys states dependent of the key code
	 */
	public boolean[] getPressed() {
		return pressed;
	}

}
