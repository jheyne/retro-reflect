package image.processing.finder;

import java.awt.Color;
import java.awt.Point;

import image.processing.HsbFilter;
import image.processing.Searcher;
import image.processing.Searcher.Direction;
import image.processing.exception.NotFound;
import image.processing.profile.TargetProfile;
import image.processing.strategy.CandidateFigureFinder;

public class GearLiftFinder extends Finder {

	private static final int INTERVAL_BETWEEN_Y = 8;

	/**
	 * The number of X pixels to skip between searches. This should be as big as
	 * possible without missing figures.
	 */
	public static int INTERVAL_BETWEEN_X = 7;

	public GearLiftFinder(Searcher searcher) {
		super(searcher);
	}

	private void checkForThirdFigure() {
		if (figures.size() == 2) {
			final Figure figure0 = figures.get(0);
			final int height0 = figure0.getHeight();
			final Figure figure1 = figures.get(1);
			final int height1 = figure1.getHeight();
			final double relativeHeight = 0.9;
			if (height0 < height1 * relativeHeight) {
				searchForThirdFigure(figure0, figure1);
			} else if (height1 < height0 * relativeHeight) {
				searchForThirdFigure(figure1, figure0);
			}
		}

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
						final CandidateFigureFinder strategy = getSearchStrategy(figure);
						strategy.find(currentPoint);
						figure.findTargetCorners(strategy.matchCache);
						if (isValid(figure, searcher.targetProfile)) {
							figures.add(figure);
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
		if (figures.size() > 2) {
			checkForMismatch();
		}
		checkForThirdFigure();
	}

	private void checkForMismatch() {
		Figure first = figures.get(0);
		Figure second = figures.get(1);
		if (second.leftBoundary - first.rightBoundary > second.getHeight() * 4) {
			System.out.println("removing first figure");
			figures.remove(first);
		}
	}

	@Override
	public Color getColor() {
		return Color.WHITE;
	}

	@Override
	protected int getMinimumWidth() {
		return 9;
	}

	@Override
	public boolean isValid(Figure figure, TargetProfile profile) {
		return super.isValid(figure, profile);
		// return super.isValid(figure, profile) && figure.getHeight() * 2 <
		// figure.getWidth();
	}

	private void searchForThirdFigure(Direction direction, Figure shortFigure, Figure tallFigure) {
		System.out.println("Searching " + direction);
		final boolean isNorth = direction == Direction.NORTH;
		final int x = isNorth ? shortFigure.leftBoundary + shortFigure.topLeftCorner.x + shortFigure.getWidth() / 2
				: shortFigure.leftBoundary + shortFigure.bottomLeftCorner.x + shortFigure.getWidth() / 2;
		final int y = isNorth ? shortFigure.topBoundary : tallFigure.bottomBoundary;
		int oldX = searcher.currentPoint.x;
		int oldY = searcher.currentPoint.y;
		searcher.currentPoint.x = x;
		searcher.currentPoint.y = y;
		final int limit = isNorth ? tallFigure.topBoundary : tallFigure.topBoundary + tallFigure.getHeight() / 2;
		while (searcher.currentPoint.y > limit) {
			if (searcher.matches(searcher.currentPoint)) {
				final Point destination = searcher.getDestination(direction);
				if (searcher.matches(destination)) {
					System.out.println("Found figure: " + searcher.current());
					final Figure figure = new Figure(searcher);
					final CandidateFigureFinder strategy = getSearchStrategy(figure);
					strategy.find(searcher.currentPoint);
					figure.findTargetCorners(strategy.matchCache);
					if (isValid(figure, searcher.targetProfile)) {
						if (figures.contains(figure)) {
							System.out.println("existing figure found again");
						} else {
							figures.add(figure);
						}
					}
					break;
				}
			}
			searcher.currentPoint.y -= INTERVAL_BETWEEN_Y; // searcher.targetProfile.defaultLookAhead;
		}
		searcher.currentPoint.x = oldX;
		searcher.currentPoint.y = oldY;
	}

	private void searchForThirdFigure(Figure shortFigure, Figure tallFigure) {
		System.out.println("Searching for 3rd figure");
		final int shortCenter = shortFigure.topBoundary + shortFigure.getHeight() / 2;
		final int tallCenter = tallFigure.topBoundary + tallFigure.getHeight() / 2;
		final Direction direction = shortCenter <= tallCenter ? Direction.SOUTH : Direction.NORTH;
		searchForThirdFigure(direction, shortFigure, tallFigure);
	}
}