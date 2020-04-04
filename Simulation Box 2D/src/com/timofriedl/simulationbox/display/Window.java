package com.timofriedl.simulationbox.display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.timofriedl.simulationbox.Simulation;

/**
 * The {@link JFrame} with a {@link Canvas} to render elements on. Screen mode
 * is always borderless window.
 * 
 * @author Timo Friedl
 */
public class Window extends JFrame {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = -4053271768132484720L;

	/**
	 * the {@link DisplayMode} instance of this (default) screen device
	 */
	public static final DisplayMode MODE = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDisplayMode();

	/**
	 * the display width
	 */
	public static final int WIDTH = MODE.getWidth();

	/**
	 * the display height
	 */
	public static final int HEIGHT = MODE.getHeight();

	/**
	 * the reference to the main simulation instance
	 */
	private Simulation simulation;

	/**
	 * the background color of this simulation
	 */
	private Color bgColor;

	/**
	 * the canvas to render elements on
	 */
	private Canvas canvas;

	/**
	 * the buffer strategy for rendering
	 */
	private BufferStrategy bs;

	/**
	 * the graphics instance to draw on the canvas
	 */
	private Graphics graphics;

	/**
	 * Creates a new window instance.
	 * 
	 * @param simulation    the reference to the main simulation instance
	 * @param title   the window title
	 * @param bgColor the background color of this simulation
	 */
	public Window(Simulation simulation, String title, Color bgColor) {
		super(title);

		this.simulation = simulation;
		this.bgColor = bgColor;

		setSize(WIDTH, HEIGHT);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setFocusable(false);

		init();
	}

	/**
	 * Initializes the canvas.
	 */
	private void init() {
		canvas = new Canvas();
		canvas.setSize(getSize());

		add(canvas);
	}

	/**
	 * Sets render preferences and forces all elements of this simulation to draw their
	 * content on the canvas.
	 */
	public void render() {
		if ((bs = canvas.getBufferStrategy()) == null) {
			canvas.createBufferStrategy(3);
			return;
		}

		graphics = bs.getDrawGraphics();

		Graphics2D g = null;
		try {
			g = (Graphics2D) graphics.create();
			setupRenderingHints(g);

			renderBackground(g);
			simulation.render(g);

			bs.show();
			graphics.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (g != null)
				g.dispose();
		}
	}

	/**
	 * Sets some fance rendering options for better quality
	 * 
	 * @param g the {@link Graphics2D} instance to set the rendereing hints
	 */
	public static void setupRenderingHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	}

	/**
	 * Draws a rectangle on the screen to create a background.
	 * 
	 * @param g the graphics instance to draw on the canvas
	 */
	private void renderBackground(Graphics2D g) {
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * @return the canvas for object rendering
	 */
	public Canvas getCanvas() {
		return canvas;
	}

	/**
	 * @return the current background color of this simulation
	 */
	public Color getBgColor() {
		return bgColor;
	}

	/**
	 * Sets the background color of this simulation
	 */
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
}
