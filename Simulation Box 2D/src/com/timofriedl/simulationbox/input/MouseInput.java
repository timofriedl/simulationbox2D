package com.timofriedl.simulationbox.input;

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.timofriedl.simulationbox.Simulation;
import com.timofriedl.simulationbox.vector.Vector2D;

/**
 * Allows mouse input.
 * 
 * @author Timo Friedl
 */

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {

	/**
	 * the mouse position
	 */
	private Vector2D mousePosition;

	/**
	 * the mouse button status
	 */
	private boolean leftPressed, middlePressed, rightPressed;

	/*
	 * the mouse-entered / mouse-exited status
	 */
	private boolean onScreen;

	/**
	 * the mouse wheel rotation
	 */
	private int wheelRotation;

	/**
	 * the precise mouse wheel rotation
	 */
	private double preciseWheelRotation;

	/**
	 * Creates a new mouse input instance.
	 * 
	 * @param simulation the reference to the main simulation instance
	 */
	public MouseInput(Simulation simulation) {
		mousePosition = Vector2D.ZERO;

		final Canvas c = simulation.getWindow().getCanvas();
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
		c.addMouseWheelListener(this);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		wheelRotation += e.getWheelRotation();
		preciseWheelRotation += e.getPreciseWheelRotation();

		updatePos(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		updatePos(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		updatePos(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		updatePos(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		onScreen = true;
		updatePos(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		onScreen = false;
		updatePos(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			leftPressed = true;
			break;
		case MouseEvent.BUTTON2:
			middlePressed = true;
			break;
		case MouseEvent.BUTTON3:
			rightPressed = true;
			break;
		}

		updatePos(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			leftPressed = false;
			break;
		case MouseEvent.BUTTON2:
			middlePressed = false;
			break;
		case MouseEvent.BUTTON3:
			rightPressed = false;
			break;
		}

		updatePos(e);
	}

	/**
	 * Updates the mouse position.
	 * 
	 * @param e the {@link MouseEvent} to extract the coordinates
	 */
	private void updatePos(MouseEvent e) {
		mousePosition = new Vector2D(e.getX(), e.getY());
	}

	/**
	 * @return the mouse position as {@link Vector2D}
	 */
	public Vector2D getMousePosition() {
		return mousePosition;
	}

	/**
	 * @return true if the left mouse button is pressed, false else
	 */
	public boolean isLeftPressed() {
		return leftPressed;
	}

	/**
	 * @return true if the mouse wheel or middle mouse button is pressed, false else
	 */
	public boolean isMiddlePressed() {
		return middlePressed;
	}

	/**
	 * @return true if the right mouse button is pressed, false else
	 */
	public boolean isRightPressed() {
		return rightPressed;
	}

	/**
	 * @return true if the mouse is hovering over the window
	 * @return false if the mouse was moved away from the window
	 */
	public boolean isOnScreen() {
		return onScreen;
	}

	/**
	 * @return the precise wheel rotation
	 */
	public double getPreciseWheelRotation() {
		return preciseWheelRotation;
	}

	/**
	 * @return the wheel rotation
	 */
	public int getWheelRotation() {
		return wheelRotation;
	}

}
