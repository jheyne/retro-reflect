package image.processing.util;

import java.awt.Point;
import java.util.Iterator;

import image.processing.util.VirtualCoordinateAccess.Transformation;

/**
 * @see java.lang.Iterable<Point> which supplies a sequence of Point to traverse
 *      an array diagonally starting at the specified corner
 *
 * @see DiagonalArrayFinder and @see DiagonalArraySearchTest for an example of
 *      usage
 */
public class DiagonalArraySearch<T> implements Iterable<Point>, Iterator<Point> {

	class DiagonalLoop {

		class CellLoop {
			private int row;

			int row_start;

			int row_stop;

			public CellLoop() {
				reset();
			}

			boolean hasMore() {
				return row >= row_stop;
			}

			Point next() {
				final int col = diag - row;
				final int x = col;
				final int y = row;
				// matrix coordinates
				final Point point = virtualAccess.get(x, y);
				this.row--;
				return point;
			}

			private void reset() {
				row_stop = Math.max(0, diag - width + 1);
				row_start = Math.min(diag, height - 1);
				this.row = row_start;
			}
		}

		private int diag = 0;

		final CellLoop cellLoop;

		public DiagonalLoop() {
			cellLoop = new CellLoop();
		}

		boolean hasMore() {
			return diag == ndiags - 1 ? cellLoop.hasMore() : diag < ndiags;
		}

		Point next() {
			if (!cellLoop.hasMore()) {
				this.diag++;
				cellLoop.reset();
				if (!hasMore()) {
					return null;
				}
			}
			return cellLoop.next();
		}

		public void reset() {
			this.diag = 0;
			cellLoop.reset();
		}
	};

	public enum StartCorner {
		BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT
	}

	final VirtualCoordinateAccess virtualAccess;

	final DiagonalLoop diagonalLoop;
	final int height;
	final int ndiags;

	final int width;

	public DiagonalArraySearch(T[][] matrix, StartCorner startCorner) {
		width = matrix[0].length;
		height = matrix.length;
		ndiags = width + height - 1;
		virtualAccess = getVirtualCoordinateAccess(startCorner, matrix);
		diagonalLoop = new DiagonalLoop();
	}

	private VirtualCoordinateAccess getVirtualCoordinateAccess(StartCorner startCorner, T[][] matrix) {
		final int xMax = matrix[0].length - 1;
		final int yMax = matrix.length - 1;
		switch (startCorner) {
		case TOP_LEFT:
			return new VirtualCoordinateAccess(xMax, yMax, Transformation.NORMAL);
		case BOTTOM_LEFT:
			return new VirtualCoordinateAccess(xMax, yMax, Transformation.REVERSE_TOP_AND_BOTTOM);
		case TOP_RIGHT:
			return new VirtualCoordinateAccess(xMax, yMax, Transformation.MIRROR_LEFT_AND_RIGHT);
		case BOTTOM_RIGHT:
			return new VirtualCoordinateAccess(xMax, yMax, Transformation.REVERSE_AND_MIRROR);
		default:
			break;
		}
		return null;
	}

	@Override
	public boolean hasNext() {
		return diagonalLoop.hasMore();
	}

	@Override
	public Iterator<Point> iterator() {
		return this;
	}

	@Override
	public Point next() {
		return diagonalLoop.next();
	}

	/**
	 * reset state to begin search again
	 */
	public void reset() {
		diagonalLoop.reset();
	}

}