package com.timofriedl.simulationbox.vector;

/**
 * An immutable 2D <code>double</code> vector class.
 * 
 * @author Timo Friedl
 */
public class Vector2D {

	/**
	 * the constant zero vector
	 */
	public static final Vector2D ZERO = new Vector2D(0.0, 0.0);

	/**
	 * the two elements of this vector
	 */
	private final double x, y;

	/**
	 * Creates a new 2D <code>double</code> vector from two <code>double</code>
	 * values.
	 * 
	 * @param x the first element of this vector
	 * @param y the second element of this vector
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a {@link Vector2D} from a given angle value and a length.
	 * 
	 * @param angle  the angle of this vector in radians
	 * @param length the length of this vector
	 * @return a new vector instance that satifies the given requirements
	 */
	public static Vector2D fromAngle(double angle, double length) {
		return new Vector2D(Math.cos(angle), Math.sin(angle)).scaleTo(length);
	}

	/**
	 * Adds another vector to this one.
	 * 
	 * @param addend the vector to add
	 * @return the sum of both vectors as a new {@link Vector2D} instance
	 */
	public Vector2D add(Vector2D addend) {
		return new Vector2D(x + addend.x, y + addend.y);
	}

	/**
	 * Subtracts another vector from this one.
	 * 
	 * @param subtrahend the vector to subtract
	 * @return the difference of both vectors as a new {@link Vector2D} instance
	 */
	public Vector2D subtract(Vector2D subtrahend) {
		return new Vector2D(x - subtrahend.x, y - subtrahend.y);
	}

	/**
	 * Scales this vector with a given scalar.
	 * 
	 * @param scalar the factor to scale this vector with
	 * @return the scaled vector as a new {@link Vector2D} instance
	 */
	public Vector2D scale(double scalar) {
		return new Vector2D(scalar * x, scalar * y);
	}

	/**
	 * Scales this vector to a given length.
	 * 
	 * @param length the length of result vector
	 * @return a new {@link Vector2D} instance with length <code>length</code> and
	 *         the direction of this vector
	 */
	public Vector2D scaleTo(double length) {
		if (x == 0.0 && y == 0.0)
			if (length == 0.0)
				return this;
			else
				throw new ArithmeticException("Tried to scale zero vector to a non-zero length.");

		return scale(length / length());
	}

	/**
	 * Calculates the scalar product with another {@link Vector2D}.
	 * 
	 * @param factor the second factor for the scalar product
	 * @return the result of the scalar product as a <code>double</code> value
	 */
	public double scalarProduct(Vector2D factor) {
		return x * factor.x + y * factor.y;
	}

	/**
	 * Calculates the sum of both elements of this {@link Vector2D}.
	 * 
	 * @return the value of x + y
	 */
	public double sum() {
		return x + y;
	}

	/**
	 * Calculates the length of this {@link Vector2D}.
	 * 
	 * @see #squareLength()
	 * @return the length of this vector
	 */
	public double length() {
		return Math.sqrt(squareLength());
	}

	/**
	 * Calculates the square of the length of this {@link Vector2D}. Use this method
	 * if you don't need the rooted value.
	 * 
	 * @return the square of the length of this vector
	 */
	public double squareLength() {
		return x * x + y * y;
	}

	/**
	 * Rotates this vector by a given angle in radians.
	 * 
	 * @param angle the rotation angle in radians
	 * @return the rotated vector as a new {@link Vector2D} instance
	 */
	public Vector2D rotate(double angle) {
		return new Vector2D(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle));
	}

	/**
	 * Calculates the anti-clockwise angle to the vector (1,0).
	 * 
	 * @return the phase of this {@link Vector2D} in radians
	 */
	public double rotation() {
		return Math.atan2(y, x);
	}

	/**
	 * Calculates the angle between this {@link Vector2D} and another.
	 * 
	 * @param v the vector to compare directions
	 * @return the angle between the two vectors in radians
	 */
	public double angleTo(Vector2D v) {
		return Math.acos(scalarProduct(v) / (length() * v.length()));
	}

	@Override
	public Vector2D clone() {
		return new Vector2D(x, y);
	}

	@Override
	public String toString() {
		return "(" + x + ",\t" + y + ")";
	}

	/**
	 * @return the first element of this vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the second element of this vector
	 */
	public double getY() {
		return y;
	}

}
