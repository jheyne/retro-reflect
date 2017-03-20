package image.processing.strategy;

import java.awt.Point;

import image.processing.HsbFilter;
import image.processing.Searcher;
import image.processing.finder.Figure;

public abstract class CandidateFigureFinder {

	protected final Searcher searcher;

	final HsbFilter hsbFilter;
	protected final int width;
	protected final int height;
	final Figure figure;
	public final Integer[][] matchCache;

	public CandidateFigureFinder(Searcher searcher, Figure figure) {
		this.searcher = searcher;
		hsbFilter = searcher.hsbFilter;
		width = hsbFilter.image.getWidth() - 1;
		height = hsbFilter.image.getHeight() - 1;
		this.figure = figure;
		matchCache = new Integer[height + 1][width + 1];
	}

	/**
	 * @param use
	 *            the search strategy to begin searching at the specified
	 *            startPoint
	 */
	abstract public void find(Point startPoint);

	/**
	 * @param x
	 * @param y
	 * @return true if the point at the specified coordinates matches the
	 *         searcher specifications. As side effects, update the match cache,
	 *         and the bounds of the searched area
	 */
	protected boolean isSet(int x, int y) {
		if (x < 0 || x > width || y < 0 || y > height) {
			figure.pointOutOfBounds = new Point(x, y);
			return false;
		}
		final Integer match = matchCache[y][x];
		if (match == null || match == 0) {
			final boolean matches = searcher.matches(x, y);
			if (matches) {
				matchCache[y][x] = 1;
				figure.updateBounds(x, y);
			} else {
				matchCache[y][x] = -1;
			}
			return matches;
		} else {
			return match > 0;
		}
	}

}