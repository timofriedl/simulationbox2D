package com.timofriedl.simulationbox.camera;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.timofriedl.simulationbox.Simulation;
import com.timofriedl.simulationbox.display.Window;
import com.timofriedl.simulationbox.gameloop.Tickable;
import com.timofriedl.simulationbox.vector.Vector2D;

/**
 * A camera that can move, zoom, and rotate to show different parts of the
 * simulation map.
 * 
 * @author Timo Friedl
 */
public class Camera implements Tickable {

	/**
	 * the speed of the camera animations
	 */
	public static double ANIMATION_SPEED = 0.15;

	/**
	 * the speed of the camera zoom
	 */
	public static double ZOOM_SPEED = 0.1;

	/**
	 * the minimum and maximum zoom factor
	 */
	public static double MIN_ZOOM = 0.001, MAX_ZOOM = 1.0;

	/**
	 * the numer of different rotation angles
	 */
	public static int ROTATION_STEPS = 60;

	/**
	 * the WASD-Key camera speed
	 */
	public static double MOVING_SPEED = 20.0;

	/**
	 * the rectangle that describes the dimensions of the window
	 */
	private static final Rectangle2D.Double SCREEN_BOUNDS = new Rectangle2D.Double(0.0, 0.0, Window.WIDTH,
			Window.HEIGHT);

	/**
	 * the reference to the main simulation instance
	 */
	private Simulation simulation;

	/**
	 * the current center position of this cam
	 */
	private Vector2D position = Vector2D.ZERO;

	/**
	 * the aimed camera center position
	 */
	private Vector2D positionAim = Vector2D.ZERO;

	/**
	 * the current zoom factor of this cam
	 * 
	 * zoom = 1.0 => 1.0 units = 1px on screen</br>
	 * zoom = 2.0 => 1.0 units = 2px on screen
	 */
	private double zoom = MIN_ZOOM;

	/**
	 * the aimed camera zoom
	 */
	private double zoomAim = 0.1;

	/**
	 * the current rotation of this cam in radians
	 * 
	 * rotation = 0.0 =></br>
	 * all elements are represented as they are in the map
	 * 
	 * rotation = Math.PI / 2.0 =></br>
	 * the camera is rotated 90° clockwise</br>
	 * all elements are rendered 90° anti-clockwise
	 * 
	 * rotation = Math.PI =></br>
	 * the camera is rotated 180°</br>
	 * all elements are rendered 180° rotated
	 */
	private double rotation = 0.0;

	/**
	 * the aimed camera rotation
	 */
	private double rotationAim = 0.0;

	/**
	 * Creates a new camera instance.#
	 * 
	 * @param simulation the reference to the main simulation instance
	 */
	public Camera(Simulation simulation) {
		this.simulation = simulation;

		simulation.getWindow().getCanvas().addMouseWheelListener(zoomListener);
		simulation.getWindow().getCanvas().addMouseWheelListener(rotationListener);
	}

	@Override
	public void tick() {
		tickMovePosition();
		move();
	}

	/**
	 * Calculates the camera zoom from mouse wheel actions
	 */
	private MouseWheelListener zoomListener = e -> {
		if (!simulation.getKeyInput().getPressed()[KeyEvent.VK_CONTROL]) {
			zoomAim += -ZOOM_SPEED * e.getPreciseWheelRotation() * zoom;
			zoomAim = Math.min(Math.max(MIN_ZOOM, zoomAim), MAX_ZOOM);
		}
	};

	/**
	 * Calculates the camera rotation from mouse wheel actions
	 */
	private MouseWheelListener rotationListener = e -> {
		if (simulation.getKeyInput().getPressed()[KeyEvent.VK_CONTROL])
			rotationAim += e.getPreciseWheelRotation() / ROTATION_STEPS * 2.0 * Math.PI;
	};

	/**
	 * Uses the states of the WASD keys for camera moving.
	 */
	private void tickMovePosition() {
		final boolean[] pressed = simulation.getKeyInput().getPressed();

		final boolean w = pressed[KeyEvent.VK_W];
		final boolean a = pressed[KeyEvent.VK_A];
		final boolean s = pressed[KeyEvent.VK_S];
		final boolean d = pressed[KeyEvent.VK_D];

		Vector2D moveDirection = new Vector2D((a ? -1.0 : 0.0) + (d ? 1.0 : 0.0), (w ? -1.0 : 0.0) + (s ? 1.0 : 0.0))
				.rotate(rotation);
		if (moveDirection.squareLength() != 0.0)
			moveDirection = moveDirection.scaleTo(MOVING_SPEED / zoom);

		positionAim = positionAim.add(moveDirection);
	}

	/**
	 * Calculates the camera moving, zooming, and rotating.
	 */
	private void move() {
		position = position.add(positionAim.subtract(position).scale(ANIMATION_SPEED));
		zoom += ANIMATION_SPEED * (zoomAim - zoom);
		rotation += ANIMATION_SPEED * (rotationAim - rotation);
	}

	/**
	 * Converts an ingame position to an onscreen position.
	 * 
	 * @param ingamePosition the position in the simulation
	 * @return the position on the screen
	 */
	private Vector2D toPositionOnScreen(Vector2D ingamePosition) {
		return ingamePosition.subtract(position).rotate(-rotation).scale(zoom)
				.add(new Vector2D(Window.WIDTH / 2.0, Window.HEIGHT / 2.0));
	}

	/**
	 * Creates a new {@link BasicStroke} instance, representing the line width on
	 * screen that matches the ingame line width.
	 * 
	 * @param ingameLineWidth the ingame line width
	 * @return the onscreen stroke instance
	 */
	private Stroke createLineStroke(double ingameLineWidth) {
		return new BasicStroke((float) (ingameLineWidth * zoom));
	}

	/**
	 * Calculates the onscreen bounds of an ingame circle.
	 * 
	 * @param ingamePosition the ingame center position of the circle
	 * @param ingameDiameter the ingame diameter of the circle
	 * @return the calculated onscreen bounds of the circle
	 */
	private Ellipse2D.Double createCircleBounds(Vector2D ingamePosition, double ingameDiameter) {
		final double screenDiameter = ingameDiameter * zoom;
		final Vector2D screenPosition = toPositionOnScreen(ingamePosition)
				.subtract(new Vector2D(screenDiameter, screenDiameter).scale(0.5));

		return new Ellipse2D.Double(screenPosition.getX(), screenPosition.getY(), screenDiameter, screenDiameter);
	}

	/**
	 * Renders the bounds of a circle with given ingame dimensions on screen.
	 * 
	 * @param g               the {@link Graphics2D} to draw on
	 * @param ingamePosition  the ingame position of the circle
	 * @param ingameDiameter  the ingame diameter of the circle
	 * @param ingameLineWidth the ingame width of the boundary line
	 */
	public void drawCircle(Graphics2D g, Vector2D ingamePosition, double ingameDiameter, double ingameLineWidth) {
		final Ellipse2D.Double circle = createCircleBounds(ingamePosition, ingameDiameter);

		if (circle.intersects(SCREEN_BOUNDS)) {
			g.setStroke(createLineStroke(ingameLineWidth));
			g.draw(circle);
		}
	}

	/**
	 * Renders a filled circle with given ingame dimensions on screen.
	 * 
	 * @param g              the {@link Graphics2D} to draw on
	 * @param ingamePosition the ingame position of the circle
	 * @param ingameDiameter the ingame diameter of the circle
	 */
	public void fillCircle(Graphics2D g, Vector2D ingamePosition, double ingameDiameter) {
		final Ellipse2D.Double circle = createCircleBounds(ingamePosition, ingameDiameter);

		if (circle.intersects(SCREEN_BOUNDS))
			g.fill(circle);
	}

	/**
	 * Renders a line with a given ingame start and end coordinate.
	 * 
	 * @param g                the {@link Graphics2D} to draw on
	 * @param ingameStartPoint the start point of the line
	 * @param ingameEndPoint   the end point of the line
	 * @param ingameLineWidth  the ingame width of the line
	 */
	public void drawLine(Graphics2D g, Vector2D ingameStartPoint, Vector2D ingameEndPoint, double ingameLineWidth) {
		final Vector2D a = toPositionOnScreen(ingameStartPoint);
		final Vector2D b = toPositionOnScreen(ingameEndPoint);

		final Line2D.Double l = new Line2D.Double(a.getX(), a.getY(), b.getX(), b.getY());

		if (!SCREEN_BOUNDS.intersectsLine(l))
			return;

		g.setStroke(createLineStroke(ingameLineWidth));
		g.draw(l);
	}

	/**
	 * Calculates the onscreen bounds of an ingame rectangle.
	 * 
	 * @param ingamePosition the ingame rectangle center position
	 * @param ingameSize     the ingame width and height of the rectangle
	 * @param ingameRotation the clockwise rectangle rotation in radians
	 * @return the calculated onscren bounds as a four-element array
	 */
	private Vector2D[] createRectangleBounds(Vector2D ingamePosition, Vector2D ingameSize, double ingameRotation) {
		final Vector2D screenPosition = toPositionOnScreen(ingamePosition);
		final Vector2D halfAxisH = new Vector2D(ingameSize.getX() * 0.5 * zoom, 0.0).rotate(ingameRotation - rotation);
		final Vector2D halfAxisV = new Vector2D(0.0, ingameSize.getY() * 0.5 * zoom).rotate(ingameRotation - rotation);

		final Vector2D a = screenPosition.subtract(halfAxisH).subtract(halfAxisV);
		final Vector2D b = screenPosition.add(halfAxisH).subtract(halfAxisV);
		final Vector2D c = screenPosition.add(halfAxisH).add(halfAxisV);
		final Vector2D d = screenPosition.subtract(halfAxisH).add(halfAxisV);

		return new Vector2D[] { a, b, c, d };
	}

	/**
	 * Renders the bounds of a rectangle with given ingame dimensions on screen.
	 * 
	 * @param g               the {@link Graphics2D} to draw on
	 * @param ingamePosition  the ingame center position of the rectangle
	 * @param ingameSize      the ingame width and height of the rectangle
	 * @param ingameRotation  the rotation of the rectangle in radians
	 * @param ingameLineWidth the ingame width of the boundary line
	 */
	public void drawRectangle(Graphics2D g, Vector2D ingamePosition, Vector2D ingameSize, double ingameRotation,
			double ingameLineWidth) {
		drawPolygon(g, createRectangleBounds(ingamePosition, ingameSize, ingameRotation), ingameLineWidth);
	}

	/**
	 * Renders a filled rectangle with given ingame dimensions on screen.
	 * 
	 * @param g              the {@link Graphics2D} to draw on
	 * @param ingamePosition the ingame center position of the rectangle
	 * @param ingameSize     the ingame size of the rectangle
	 * @param ingameRotation the rotation of the rectangle in radians
	 */
	public void fillRectangle(Graphics2D g, Vector2D ingamePosition, Vector2D ingameSize, double ingameRotation) {
		fillPolygon(g, createRectangleBounds(ingamePosition, ingameSize, ingameRotation));
	}

	/**
	 * Calculates the onscreen bounds of an ingame polygon.
	 * 
	 * @param ingamePoints the ingame positions of the points that build the polygon
	 * @return the onscreen positions of the polygon as a new {@link Path2D.Double}
	 */
	private Path2D.Double createPolygonBounds(Vector2D[] ingamePoints) {
		final int pointCount = ingamePoints.length;

		if (pointCount < 3)
			throw new IllegalArgumentException("Polygon must contain at least 3 points.");

		final Path2D.Double p = new Path2D.Double();
		p.moveTo(ingamePoints[pointCount - 1].getX(), ingamePoints[pointCount - 1].getY());
		for (int i = 0; i < pointCount; i++)
			p.lineTo(ingamePoints[i].getX(), ingamePoints[i].getY());
		p.closePath();

		return p;
	}

	/**
	 * Renders the bounds of a polygon with an arbitrary number of points greater
	 * than two on screen.
	 * 
	 * @param g               the {@link Graphics2D} to draw on
	 * @param ingamePoints    the points in the simulation that build the polygon
	 * @param ingameLineWidth the ingame width of the boundary line
	 */
	public void drawPolygon(Graphics2D g, Vector2D[] ingamePoints, double ingameLineWidth) {
		final Path2D.Double p = createPolygonBounds(ingamePoints);

		if (p.intersects(SCREEN_BOUNDS)) {
			g.setStroke(createLineStroke(ingameLineWidth));
			g.draw(p);
		}
	}

	/**
	 * Renders a filled polygon with an arbitrary number of points greater than two
	 * on screen.
	 * 
	 * @param g            the {@link Graphics2D} to draw on
	 * @param ingamePoints the points in the simulation that build the polygon
	 */
	public void fillPolygon(Graphics2D g, Vector2D[] ingamePoints) {
		final Path2D.Double p = createPolygonBounds(ingamePoints);

		if (p.intersects(SCREEN_BOUNDS))
			g.fill(p);
	}

	/**
	 * Calculates the onscreen bounds of the line of an ingame arrow.
	 * 
	 * @param ingameStartPosition the ingame start position of the arrow
	 * @param ingameEndPosition   the ingame end position of the arrow
	 * @param ingameArrowWidth    the ingame width of the tip of the arrow
	 * @param ingameLineWidth     the ingame width of the line of the arrow
	 * @return the onscreen positions of the arrow as a new {@link Line2D.Double}
	 */
	private Line2D.Double createArrowLine(Vector2D ingameStartPosition, Vector2D ingameEndPosition,
			double ingameArrowWidth, double ingameLineWidth) {
		final double halfArrowWidth = ingameArrowWidth * zoom * 0.5;
		final double lineWidth = ingameLineWidth * zoom;

		final Vector2D a = toPositionOnScreen(ingameStartPosition);
		final Vector2D b = toPositionOnScreen(ingameEndPosition);
		final Vector2D ab = b.subtract(a);
		final Vector2D m = b.subtract(ab.scaleTo(halfArrowWidth)).subtract(ab.scaleTo(lineWidth * 0.5));

		return new Line2D.Double(a.getX(), a.getY(), m.getX(), m.getY());
	}

	/**
	 * Calculates the onscreen bounds of the tip of an ingame arrow.
	 * 
	 * @param ingameStartPosition the ingame start position of the arrow
	 * @param ingameEndPosition   the ingame end position of the arrow
	 * @param ingameArrowWidth    the ingame width of the tip of the arrow
	 * @return the onscreen positions of the arrow as a new {@link Path2D.Double}
	 */
	private Path2D.Double createArrowTip(Vector2D ingameStartPosition, Vector2D ingameEndPosition,
			double ingameArrowWidth) {
		final double halfArrowWidth = ingameArrowWidth * zoom * 0.5;

		final Vector2D a = toPositionOnScreen(ingameStartPosition);
		final Vector2D b = toPositionOnScreen(ingameEndPosition);
		final Vector2D ab = b.subtract(a);
		final Vector2D m = b.subtract(ab.scaleTo(halfArrowWidth));
		final Vector2D perp = ab.rotate(Math.PI / -2.0).scaleTo(halfArrowWidth);
		final Vector2D l = m.add(perp);
		final Vector2D r = m.subtract(perp);

		final Path2D.Double p = new Path2D.Double();
		p.moveTo(l.getX(), l.getY());
		p.lineTo(r.getX(), r.getY());
		p.lineTo(b.getX(), b.getY());
		p.lineTo(l.getX(), l.getY());
		p.closePath();

		return p;
	}

	/**
	 * Renders the bounds of an arrow with a start and end position.
	 * 
	 * @param g                   the {@link Graphics2D} to draw on
	 * @param ingameStartPosition the ingame start position of the arrow
	 * @param ingameEndPosition   the ingame end position of the arrow
	 * @param ingameArrowWidth    the ingame width of the tip of the arrow
	 * @param ingameLineWidth     the ingame arrow line width
	 */
	public void drawArrow(Graphics2D g, Vector2D ingameStartPosition, Vector2D ingameEndPosition,
			double ingameArrowWidth, double ingameLineWidth) {
		final Path2D.Double at = createArrowTip(ingameStartPosition, ingameEndPosition, ingameArrowWidth);
		final Line2D.Double al = createArrowLine(ingameStartPosition, ingameEndPosition, ingameArrowWidth,
				ingameLineWidth);

		if (al.intersects(SCREEN_BOUNDS) || at.intersects(SCREEN_BOUNDS)) {
			g.setStroke(createLineStroke(ingameLineWidth));
			g.draw(al);
			g.draw(at);
		}
	}

	/**
	 * Renders a filled arrow with a start and end position.
	 * 
	 * @param g                   the {@link Graphics2D} to draw on
	 * @param ingameStartPosition the ingame start position of the arrow
	 * @param ingameEndPosition   the ingame end position of the arrow
	 * @param ingameArrowWidth    the ingame width of the tip of the arrow
	 * @param ingameLineWidth     the ingame arrow line width
	 */
	public void fillArrow(Graphics2D g, Vector2D ingameStartPosition, Vector2D ingameEndPosition,
			double ingameArrowWidth, double ingameLineWidth) {
		final Path2D.Double at = createArrowTip(ingameStartPosition, ingameEndPosition, ingameArrowWidth);
		final Line2D.Double al = createArrowLine(ingameStartPosition, ingameEndPosition, ingameArrowWidth,
				ingameLineWidth);

		if (al.intersects(SCREEN_BOUNDS) || at.intersects(SCREEN_BOUNDS)) {
			g.setStroke(createLineStroke(ingameLineWidth));
			g.draw(al);
			g.fill(at);
		}
	}

	/**
	 * Renders a rotated image at a given position.
	 * 
	 * @param g                    the {@link Graphics2D} to draw on
	 * @param img                  the {@link BufferedImage} to render
	 * @param ingameCenterPosition the ingame center position of the image
	 * @param ingameSize           the ingame width and height of the image
	 * @param ingameRotation       the ingame rotation of the image
	 */
	public void drawImage(Graphics2D g, BufferedImage img, Vector2D ingameCenterPosition, Vector2D ingameSize,
			double ingameRotation) {
		final Vector2D ul = toPositionOnScreen(ingameCenterPosition);
		final AffineTransform matrix = g.getTransform();

		g.translate(ul.getX(), ul.getY());
		g.rotate(ingameRotation - rotation);
		g.translate(ingameSize.getX() * zoom * -0.5, ingameSize.getY() * zoom * -0.5);
		g.scale(zoom * ingameSize.getX() / img.getWidth(), zoom * ingameSize.getY() / img.getHeight());
		g.drawImage(img, matrix, null);
		g.setTransform(matrix);
	}

	/**
	 * @return the current aimed camera zoom value
	 */
	public double getZoomAim() {
		return zoomAim;
	}

	/**
	 * @param zoomAim the new aimed camera zoom value
	 */
	public void setZoomAim(double zoomAim) {
		this.zoomAim = zoomAim;
	}

	/**
	 * @return the current rotation aim in radias
	 */
	public double getRotationAim() {
		return rotationAim;
	}

	/**
	 * @param rotationAim the new rotation aim in radians
	 */
	public void setRotationAim(double rotationAim) {
		this.rotationAim = rotationAim;
	}

	/**
	 * @return the current center position
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * @param positionAim the new aimed camera center position
	 */
	public void setPositionAim(Vector2D positionAim) {
		this.positionAim = positionAim;
	}

	/**
	 * @return the current aimed camera center position
	 */
	public Vector2D getPositionAim() {
		return positionAim;
	}

	/**
	 * @return the current camera zoom value
	 */
	public double getZoom() {
		return zoom;
	}

	/**
	 * @return the current camera rotation in radians
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	/**
	 * @param zoom the zoom to set
	 */
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

}
