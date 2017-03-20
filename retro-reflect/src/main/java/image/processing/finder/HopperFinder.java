package image.processing.finder;

import java.awt.Color;
import java.awt.Point;

import image.processing.HsbFilter;
import image.processing.Searcher;
import image.processing.Searcher.Direction;
import image.processing.exception.NotFound;
import image.processing.strategy.CandidateFigureFinder;

public class HopperFinder extends Finder {

	private static final int INTERVAL_BETWEEN_Y = 20;

	/**
	 * The number of X pixels to skip between searches. This should be as big as
	 * possible without missing figures.
	 */
	public static int INTERVAL_BETWEEN_X = 20;

	public HopperFinder(Searcher searcher) {
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
						// while (currentPoint.y < height &&
						// searcher.matches(currentPoint.x, currentPoint.y + 1))
						// {
						// currentPoint.y++;
						// }
						final Figure figure = new Figure(searcher);
						final CandidateFigureFinder strategy = getSearchStrategy(figure);
						strategy.find(currentPoint);
						figure.findTargetCorners(strategy.matchCache);
						if (isValid(figure, searcher.targetProfile)) {
							figures.add(figure);
						} else {
							System.out.println("ignoring");
						}
						break;
					}
				}
				currentPoint.y -= INTERVAL_BETWEEN_Y; // searcher.targetProfile.defaultLookAhead;
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

	@Override
	protected int getMinimumWidth() {
		return 70;
	}
}