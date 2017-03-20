package image.processing.strategy.marching_squares;

import static image.processing.strategy.marching_squares.Direction.E;
import static image.processing.strategy.marching_squares.Direction.N;
import static image.processing.strategy.marching_squares.Direction.S;
import static image.processing.strategy.marching_squares.Direction.W;

import java.awt.Point;

import image.processing.Searcher;
import image.processing.finder.Figure;
import image.processing.strategy.CandidateFigureFinder;

/**
 * A simple implementation of the marching squares algorithm that can identify
 * perimeters in an supplied byte array. The array of data over which this
 * instances of this class operate is not cloned by this class's constructor
 * (for obvious efficiency reasons) and should therefore not be modified while
 * the object is in use. It is expected that the data elements supplied to the
 * algorithm have already been thresholded. The algorithm only distinguishes
 * between zero and non-zero values.
 *
 * Courtesy http://www.tomgibara.com/computer-vision/marching-squares
 *
 * @author Tom Gibara
 *
 */

public class BlobPerimeterFinder extends CandidateFigureFinder {

	public BlobPerimeterFinder(Searcher searcher, Figure figure) {
		super(searcher, figure);
	}

	private Direction direction(int x, int y, Direction previous) {
		int sum = 0;
		final boolean topLeft = isSet(x, y);
		if (topLeft) {
			sum |= 1;
		}
		final boolean topRight = isSet(x + 1, y);
		if (topRight) {
			sum |= 2;
		}
		final boolean bottomLeft = isSet(x, y + 1);
		if (bottomLeft) {
			sum |= 4;
		}
		final boolean bottomRight = isSet(x + 1, y + 1);
		if (bottomRight) {
			sum |= 8;
		}
		final Direction direction;
		switch (sum) {
		case 1:
			direction = N;
			break;
		case 2:
			direction = E;
			break;
		case 3:
			direction = E;
			break;
		case 4:
			direction = W;
			break;
		case 5:
			direction = N;
			break;
		case 6:
			direction = previous == N ? W : E;
			break;
		case 7:
			direction = E;
			break;
		case 8:
			direction = S;
			break;
		case 9:
			direction = previous == E ? N : S;
			break;
		case 10:
			direction = S;
			break;
		case 11:
			direction = S;
			break;
		case 12:
			direction = W;
			break;
		case 13:
			direction = N;
			break;
		case 14:
			direction = W;
			break;
		default:
			throw new IllegalStateException();
		}
		return direction;
	}

	@Override
	public void find(Point startPoint) {
		while (startPoint.y < height && searcher.matches(startPoint.x, startPoint.y + 1)) {
			startPoint.y++;
		}
		tracePerimeter(startPoint.x, startPoint.y);
	}

	/**
	 * Finds the perimeter between a set of zero and non-zero values which
	 * begins at the specified data element. If no initial point is known,
	 * consider using the convenience method supplied. The paths returned by
	 * this method are always closed.
	 *
	 * @param initialX
	 *            the column of the data matrix at which to start tracing the
	 *            perimeter
	 * @param initialY
	 *            the row of the data matrix at which to start tracing the
	 *            perimeter
	 *
	 * @return a closed, anti-clockwise path that is a perimeter of between a
	 *         set of zero and non-zero values in the data.
	 * @throws IllegalArgumentException
	 *             if there is no perimeter at the specified initial point.
	 */

	public void tracePerimeter(int initialX, int initialY) {
		if (initialX < 0) {
			initialX = 0;
		}
		if (initialX > width) {
			initialX = width;
		}
		if (initialY < 0) {
			initialY = 0;
		}
		if (initialY > height) {
			initialY = height;
		}

		final int initialValue = value(initialX, initialY);
		if (initialValue == 0 || initialValue == 15) {
			throw new IllegalArgumentException(String
					.format("Supplied initial coordinates (%d, %d) do not lie on a perimeter.", initialX, initialY));
		}
		int x = initialX;
		int y = initialY;
		Direction previous = null;
		do {
			final Direction direction = direction(x, y, previous);
			x += direction.xOffset;
			y += direction.yOffset;
			previous = direction;
		} while (x != initialX || y != initialY);

	}

	private int value(int x, int y) {
		int sum = 0;
		if (isSet(x, y)) {
			sum |= 1;
		}
		if (isSet(x + 1, y)) {
			sum |= 2;
		}
		if (isSet(x, y + 1)) {
			sum |= 4;
		}
		if (isSet(x + 1, y + 1)) {
			sum |= 8;
		}
		return sum;
	}

}