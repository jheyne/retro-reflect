package image.processing.strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import image.processing.Searcher;
import image.processing.finder.Figure;

/**
 * Finds
 */
public class FilledBlobFinder extends CandidateFigureFinder {

	final List<Point> pending = new ArrayList<>();

	final Set<Point> processed = new HashSet<>();

	public FilledBlobFinder(Searcher searcher, Figure figure) {
		super(searcher, figure);
	}

	private Point dequeue() {
		return pending.remove(0);
	}

	private void enqueue(int x, int y) {
		final Point pt = new Point(x, y);
		if (processed.contains(pt)) {
			return;
		}
		pending.add(pt);
		processed.add(pt);
	}

	public void fill(Point startPoint) {
		enqueue(startPoint.x, startPoint.y);
		while (!pending.isEmpty()) {
			final Point pt = dequeue();
			if (isSet(pt.x, pt.y)) {
				enqueue(pt.x, pt.y + 1);
				enqueue(pt.x, pt.y - 1);
				enqueue(pt.x + 1, pt.y);
				enqueue(pt.x - 1, pt.y);
			}
		}
	}

	@Override
	public void find(Point startPoint) {
		fill(startPoint);
	}

}