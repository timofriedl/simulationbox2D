package com.timofriedl.simulationbox.gameloop;

import com.timofriedl.simulationbox.Simulation;

/**
 * The tick-render-loop
 * 
 * @author Timo Friedl
 */
public class GameLoop implements Runnable {

	/**
	 * the number of ticks in one second
	 */
	public static final int TPS = 60;

	/**
	 * the number of nanoseconds in one simulation tick
	 */
	private final long NANOS_PER_TICK = 1_000_000_000 / TPS;

	/**
	 * the current number of rendered frames per second
	 */
	private float fps;

	/**
	 * the reference to the main simulation instance
	 */
	private Simulation simulation;

	/**
	 * Creates and starts the tick-render game loop.
	 * 
	 * @param simulation the reference to main simulation
	 */
	public GameLoop(Simulation simulation) {
		this.simulation = simulation;
	}

	/**
	 * Starts the game loop in a new {@link Thread}.
	 */
	public void start() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		long lastTickTime = System.nanoTime();
		long lastRenderTime = lastTickTime;

		for (simulation.init();;) {
			if (System.nanoTime() - lastTickTime < NANOS_PER_TICK)
				continue;

			while (System.nanoTime() - lastTickTime >= NANOS_PER_TICK) {
				simulation.getCamera().tick();
				simulation.tick();
				lastTickTime += NANOS_PER_TICK;
			}

			simulation.getWindow().render();

			final long now = System.nanoTime();
			fps = 1_000_000_000f / (now - lastRenderTime);
			lastRenderTime = now;
		}
	}

	/**
	 * @return the current number of rendered frames per second
	 */
	public float getFps() {
		return fps;
	}
}
