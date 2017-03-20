package image.processing;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import image.processing.exception.NotFound;
import image.processing.finder.FigureFinder;
import image.processing.finder.Finder;
import image.processing.finder.GearLiftFinder;
import image.processing.finder.HopperFinder;
import image.processing.profile.TargetProfile;

/**
 * Utilities to navigate through an image and to test whether a coordinate
 * matches a target profile
 */
public class Searcher {

	public enum Direction {
		NORTH, EAST, SOUTH, WEST, NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST
	};

	public enum FigureType {
		GearLift, Hopper
	}

	public final Point currentPoint;
	final Point peekPoint;
	public final Point destinationPoint;
	public final TargetProfile targetProfile;

	public final HsbFilter hsbFilter;

	private Finder currentFinder = null;
	private final FigureType figureType;

	public Searcher(BufferedImage image, TargetProfile targetProfile, FigureType figureType) {
		hsbFilter = new HsbFilter(image);
		this.targetProfile = targetProfile;
		currentPoint = new Point(image.getWidth() / 2, image.getHeight() / 2);
		peekPoint = new Point(currentPoint);
		destinationPoint = new Point(currentPoint);
		this.figureType = figureType;
	}

	public Searcher(HsbFilter hsbFilter, TargetProfile targetProfile, Point startingPoint, FigureType figureType) {
		super();
		this.hsbFilter = hsbFilter;
		this.targetProfile = targetProfile;
		this.currentPoint = new Point(startingPoint);
		this.peekPoint = new Point(startingPoint);
		this.destinationPoint = new Point(startingPoint);
		this.figureType = figureType;
	}

	/**
	 * @return the current point as a string
	 */
	public String current() {
		return print(currentPoint);
	}

	/**
	 * @return the destination point as a string
	 */
	public String dest() {
		return print(destinationPoint);
	}

	public Point find(Direction direction, int maxDistance) throws NotFound {
		for (int i = 0; i < maxDistance; i++) {
			if (matches(getDestination(direction))) {
				return getDestination(direction);
			}
		}
		throw notFound();
	}

	public Finder finder() throws NotFound {
		Finder figureFinder = null;
		switch (figureType) {
		case GearLift:
			figureFinder = new GearLiftFinder(this);
			break;
		case Hopper:
			figureFinder = new HopperFinder(this);
			break;
		default:
			figureFinder = new FigureFinder(this);
			break;
		}
		figureFinder.find();
		return figureFinder;
	}

	/**
	 * @return the point in the specified direction from the current point using
	 *         the default target distance
	 */
	public Point getDestination(Direction direction) {
		return getDestination(direction, targetProfile.defaultLookAhead);
	}

	/**
	 * @return the point in the specified direction the specified distance from
	 *         the current point
	 */
	public Point getDestination(Direction direction, int distance) {
		int x = currentPoint.x, y = currentPoint.y;
		switch (direction) {
		case EAST:
			x += distance;
			break;
		case NORTH:
			y -= distance;
			break;
		case NORTHEAST:
			x += distance;
			y -= distance;
			break;
		case NORTHWEST:
			x -= distance;
			y -= distance;
			break;
		case SOUTH:
			y += distance;
			break;
		case SOUTHEAST:
			x += distance;
			y += distance;
			break;
		case SOUTHWEST:
			x -= distance;
			y += distance;
			break;
		case WEST:
			x -= distance;
			break;
		default:
			break;
		}
		destinationPoint.x = x;
		destinationPoint.y = y;
		// System.out.println("Dest " + direction + " from " + currentPoint + "
		// to " + destinationPoint + " by " + distance);
		return destinationPoint;
	}

	/**
	 * set the current point to the specified position
	 */
	public void goTo(int x, int y) {
		currentPoint.x = x;
		currentPoint.y = y;
	}

	/**
	 * @return true if the specified coordinates matche the target profile
	 */
	public boolean matches(int x, int y) {
		return targetProfile.matches(hsbFilter.hsb(x, y));
	}

	/**
	 * @return true if the specified point matches the target profile
	 */
	public boolean matches(Point point) {
		return matches(point.x, point.y);
	}

	/**
	 * @throw NotFound
	 */
	private NotFound notFound() {
		// return NotFound.INSTANCE;
		return new NotFound();
	}

	/**
	 * @return the specified points as a string
	 */
	public String print(List<Point> points) {
		final StringBuilder b = new StringBuilder();
		for (final Point pt : points) {
			b.append(print(pt));
			b.append("  ");
		}
		return b.toString();
	}

	/**
	 * represent the point as a string
	 */
	public String print(Point point) {
		return "" + point.x + '@' + point.y;
	}

	/**
	 * The maximum distance straight ahead is searched first, then lateral
	 * points are increasingly searched. If not found, the process is repeated,
	 * decrementing the maxDistance.
	 *
	 * @param direction
	 *            the direction to search
	 * @param minDistance
	 *            search at least this distance from the current point
	 * @param maxDistance
	 *            the maximum distance to search (straight ahead)
	 * @param breadth
	 *            the maximum distance to search to each side
	 * @return the matching point
	 * @throws NotFound
	 *             if a match is not found
	 */
	public Point radialSearch(Direction direction, int minDistance, int maxDistance, int breadth) throws NotFound {
		final Point cached = new Point();
		for (int dist = maxDistance; dist > minDistance; dist--) {
			final Point destination = getDestination(direction, dist);
			updatePoint(cached, destination);
			for (int radial = 0; radial < breadth; radial++) {
				if (matches(destination)) {
					return updateCurrentPoint(destination);
				}
				updateOrthogonalPosition(destination, direction, radial, false);
				if (matches(destination)) {
					return updateCurrentPoint(destination);
				}
				updatePoint(destination, cached);
				updateOrthogonalPosition(destination, direction, radial, true);
				if (matches(destination)) {
					return updateCurrentPoint(destination);
				}
				updatePoint(destination, cached);
			}
		}
		throw notFound();
	}

	private Point updateCurrentPoint(Point destination) {
		return updatePoint(currentPoint, destination.x, destination.y);
	}

	/**
	 * @param destination
	 *            the point to adjust
	 * @param direction
	 *            the direction impacts which point coordinates are altered
	 * @param radial
	 *            the lateral offset
	 * @param clockwise
	 *            specifies whether the left or right orthogonal direction
	 *            should be changed
	 */
	private void updateOrthogonalPosition(Point destination, Direction direction, int radial, boolean clockwise) {
		int x = destination.x;
		int y = destination.y;
		int delta = 0;
		switch (direction) {
		case EAST:
			delta = clockwise ? radial : radial * -1;
			y += delta;
			break;
		case NORTH:
			delta = clockwise ? radial : radial * -1;
			x += delta;
			break;
		case NORTHEAST:
			if (clockwise) {
				x += radial;
			} else {
				y -= radial;
			}
			break;
		case NORTHWEST:
			if (clockwise) {
				y += radial;
			} else {
				x += radial;
			}
			break;
		case SOUTH:
			delta = clockwise ? radial * -1 : radial;
			x += delta;
			break;
		case SOUTHEAST:
			if (clockwise) {
				y -= radial;
			} else {
				x -= radial;
			}
			break;
		case SOUTHWEST:
			if (clockwise) {
				y -= radial;
			} else {
				x += radial;
			}
			break;
		case WEST:
			delta = clockwise ? radial * -1 : radial;
			y += delta;
			break;
		default:
			break;
		}
		destination.x = x;
		destination.y = y;
	}

	/**
	 * @return the given point after updating coordinates
	 */
	private Point updatePoint(Point point, int x, int y) {
		point.x = x;
		point.y = y;
		return point;
	}

	/**
	 * @return the given point after updating coordinates from the specified
	 *         point
	 */
	private Point updatePoint(Point to, Point from) {
		return updatePoint(to, from.x, from.y);
	}

	/**
	 * @param finder
	 *            call this method before accessing image pixels. It provides an
	 *            opportunity the changed the color of the accessed points for
	 *            debugging.
	 */
	public void visualize(Finder finder) {
		if (hsbFilter.rememberPath) {
			if (currentFinder != null) {
				hsbFilter.color(currentFinder.getColor());
			}
			currentFinder = finder;
		}

	}

}
