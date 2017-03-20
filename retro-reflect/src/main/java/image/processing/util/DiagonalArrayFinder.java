package image.processing.util;

import java.awt.Point;
import java.util.function.Predicate;

import image.processing.util.DiagonalArraySearch.StartCorner;

/**
 * Search a 2-dimensional array diagonally from a specified corner to find an
 * element (or cell location) that matches a boolean test.
 */
public class DiagonalArrayFinder<T> {

	final private DiagonalArraySearch<T> iterator;
	private final T[][] matrix;

	public DiagonalArrayFinder(T[][] matrix, StartCorner startCorner) {
		this.matrix = matrix;
		iterator = new DiagonalArraySearch<>(matrix, startCorner);
	}

	/**
	 * Always starts at the beginning. Return the point where the matching value
	 * can be found.
	 */
	public Point find(Predicate<T> test) {
		iterator.reset();
		return findNext(test);
	}

	/**
	 * Continues searching at the previous position (or the beginning if not
	 * previously searched). Return the point where the matching value can be
	 * found.
	 */
	public Point findNext(Predicate<T> test) {
		while (iterator.hasNext()) {
			final Point point = iterator.next();
			final T t = matrix[point.y][point.x];
			if (test.test(t)) {
				return point;
			}
		}
		return null;
	}

	/**
	 * Continues searching at the previous position (or the beginning if not
	 * previously searched). Return the next value in the array that matches the
	 * test.
	 */
	public T findNextValue(Predicate<T> test) {
		while (iterator.hasNext()) {
			final Point point = iterator.next();
			final T t = matrix[point.y][point.x];
			if (test.test(t)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Always starts at the beginning. Return the value in the array that
	 * matches the test.
	 */
	public T findValue(Predicate<T> test) {
		iterator.reset();
		return findNextValue(test);
	}
}
