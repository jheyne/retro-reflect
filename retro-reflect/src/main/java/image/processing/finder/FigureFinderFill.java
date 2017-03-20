package image.processing.finder;

import java.awt.Color;
import java.awt.Point;

import image.processing.HsbFilter;
import image.processing.Searcher;
import image.processing.Searcher.Direction;
import image.processing.exception.NotFound;
import image.processing.strategy.FilledBlobFinder;

@Deprecated
public class FigureFinderFill extends Finder {

	public static int INTERVAL_BETWEEN_X = 7;

	public FigureFinderFill(Searcher searcher) {
		super(searcher);
	}

	@Override
	public void find() throws NotFound {
		visualize();
		final HsbFilter hsbFilter = searcher.hsbFilter;
		final Point currentPoint = searcher.currentPoint;
		final int width = hsbFilter.image.getWidth();
		final int height = hsbFilter.image.getHeight() - 1;
		for (int x = 0; x < width; x += INTERVAL_BETWEEN_X) {
			if (hasProcessedImageColumn(x)) {
				continue;
			}
			currentPoint.y = height;
			currentPoint.x = x;
			while (currentPoint.y > 0) {
				if (searcher.matches(currentPoint)) {
					final Point destination = searcher.getDestination(Direction.EAST);
					if (destination.x < width && searcher.matches(destination)) {
						System.out.println("Found figure: " + searcher.current());
						final Figure figure = new Figure(searcher);
						final FilledBlobFinder strategy = new FilledBlobFinder(searcher, figure);
						strategy.fill(currentPoint);
						figure.findTargetCorners(strategy.matchCache);
						figures.add(figure);
						break;
					}
				}
				currentPoint.y -= searcher.targetProfile.defaultLookAhead;
			}
			currentPoint.y = 0;
		}
		if (figures.isEmpty()) {
			throw new NotFound();
		}

	}

	@Override
	public Color getColor() {
		return Color.WHITE;
	}
}