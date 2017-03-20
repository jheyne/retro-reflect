package image.processing.strategy.marching_squares;

/**
 * A direction in the plane. As a convenience, directions provide unit vector
 * components (manhattan metric) for both the conventional plane and screen
 * coordinates (y axis reversed).
 *
 * @author Tom Gibara
 *
 */

public enum Direction {

	E(1, 0),

	N(0, 1),

	W(-1, 0),

	S(0, -1);

	/**
	 * The horizontal distance moved in this direction in screen coordinates.
	 */

	public final int xOffset;

	/**
	 * The vertical distance moved in this direction in screen coordinates.
	 */

	public final int yOffset;

	private Direction(int x, int y) {
		this.xOffset = x;
		this.yOffset = -y;
	}

}