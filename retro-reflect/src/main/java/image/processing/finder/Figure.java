package image.processing.finder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Predicate;

import image.processing.Searcher;
import image.processing.util.DiagonalArrayFinder;
import image.processing.util.DiagonalArraySearch;

public class Figure {

	final Searcher searcher;
	public int leftBoundary = Integer.MAX_VALUE;
	public int topBoundary = Integer.MAX_VALUE;
	public int rightBoundary = Integer.MIN_VALUE;
	public int bottomBoundary = Integer.MIN_VALUE;
	public Point topLeftCorner = new Point();
	public Point topRightCorner = new Point();
	public Point bottomLeftCorner = new Point();
	public Point bottomRightCorner = new Point();
	public Point pointOutOfBounds;

	public Figure(Searcher searcher) {
		super();
		this.searcher = searcher;
	}

	public boolean encompasesX(int x) {
		return leftBoundary <= x && rightBoundary >= x;
	}

	public void findTargetCorners(Integer[][] matchCache) {
		final Integer[][] bounds = getBoundaryTracing(matchCache);
		int count = 0;
		for (final Integer[] is : bounds) {
			for (final Integer i : is) {
				if (i != null && i == 1) {
					count += 1;
				}
			}
		}
		System.out.println("My Bounds Count: " + count);
		if (pointOutOfBounds != null) {
			System.out.println("A point is out of bounds: " + pointOutOfBounds.x + '@' + pointOutOfBounds.y);
		}
		final DiagonalArrayFinder<Integer> finderTopLeft = new DiagonalArrayFinder<>(bounds,
				DiagonalArraySearch.StartCorner.TOP_LEFT);
		final Predicate<Integer> test = (Integer value) -> value != null && value == 1;
		topLeftCorner = finderTopLeft.find(test);
		final DiagonalArrayFinder<Integer> finderTopRight = new DiagonalArrayFinder<>(bounds,
				DiagonalArraySearch.StartCorner.TOP_RIGHT);
		topRightCorner = finderTopRight.find(test);
		final DiagonalArrayFinder<Integer> finderBottomLeft = new DiagonalArrayFinder<>(bounds,
				DiagonalArraySearch.StartCorner.BOTTOM_LEFT);
		bottomLeftCorner = finderBottomLeft.find(test);
		final DiagonalArrayFinder<Integer> finderBottomRight = new DiagonalArrayFinder<>(bounds,
				DiagonalArraySearch.StartCorner.BOTTOM_RIGHT);
		bottomRightCorner = finderBottomRight.find(test);
	}

	private Integer[][] getBoundaryTracing(Integer[][] source) {
		// final int sourceHeight = source.length;
		// final int sourceWidth = source[0].length;
		System.out.println(source[0]);
		final int height = bottomBoundary - topBoundary + 1;
		final Integer[][] target = new Integer[height][];
		// Revisit: return something
		final int l = leftBoundary == rightBoundary ? leftBoundary - 1 : leftBoundary;
		for (int i = topBoundary, r = 0; i <= bottomBoundary; i++, r++) {
			target[r] = Arrays.copyOfRange(source[i], l, rightBoundary);
		}
		return target;
	}

	public int getHeight() {
		return bottomBoundary - topBoundary;
	}

	public int getWidth() {
		return rightBoundary - leftBoundary;
	}

	public void showCorners() {
		final BufferedImage image = searcher.hsbFilter.image;
		final int green = Color.GREEN.getRGB();
		image.setRGB(leftBoundary, topBoundary, green);
		image.setRGB(rightBoundary, topBoundary, green);
		image.setRGB(leftBoundary, bottomBoundary, green);
		image.setRGB(rightBoundary, bottomBoundary, green);
	}

	public void showTargetCorners(Color displayColor) {
		final BufferedImage image = searcher.hsbFilter.image;
		if (topLeftCorner == null) {
			System.out.println("Target corners not available");
			return;
		}
		final Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(2));
		g2.setPaint(displayColor);

		final Point tl = new Point(leftBoundary + topLeftCorner.x, topBoundary + topLeftCorner.y);
		final Point tr = new Point(leftBoundary + topRightCorner.x, topBoundary + topRightCorner.y);
		final Point bl = new Point(leftBoundary + bottomLeftCorner.x, topBoundary + bottomLeftCorner.y);
		final Point br = new Point(leftBoundary + bottomRightCorner.x, topBoundary + bottomRightCorner.y);

		g2.draw(new Line2D.Float(tl, tr));
		g2.draw(new Line2D.Float(tr, br));
		g2.draw(new Line2D.Float(br, bl));
		g2.draw(new Line2D.Float(bl, tl));
	}

	public void updateBounds(int x, int y) {
		leftBoundary = Math.min(leftBoundary, x);
		topBoundary = Math.min(topBoundary, y);
		rightBoundary = Math.max(rightBoundary, x);
		bottomBoundary = Math.max(bottomBoundary, y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bottomBoundary;
		result = prime * result + ((bottomLeftCorner == null) ? 0 : bottomLeftCorner.hashCode());
		result = prime * result + ((bottomRightCorner == null) ? 0 : bottomRightCorner.hashCode());
		result = prime * result + leftBoundary;
		result = prime * result + rightBoundary;
		result = prime * result + topBoundary;
		result = prime * result + ((topLeftCorner == null) ? 0 : topLeftCorner.hashCode());
		result = prime * result + ((topRightCorner == null) ? 0 : topRightCorner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Figure other = (Figure) obj;
		if (bottomBoundary != other.bottomBoundary)
			return false;
		if (bottomLeftCorner == null) {
			if (other.bottomLeftCorner != null)
				return false;
		} else if (!bottomLeftCorner.equals(other.bottomLeftCorner))
			return false;
		if (bottomRightCorner == null) {
			if (other.bottomRightCorner != null)
				return false;
		} else if (!bottomRightCorner.equals(other.bottomRightCorner))
			return false;
		if (leftBoundary != other.leftBoundary)
			return false;
		if (rightBoundary != other.rightBoundary)
			return false;
		if (topBoundary != other.topBoundary)
			return false;
		if (topLeftCorner == null) {
			if (other.topLeftCorner != null)
				return false;
		} else if (!topLeftCorner.equals(other.topLeftCorner))
			return false;
		if (topRightCorner == null) {
			if (other.topRightCorner != null)
				return false;
		} else if (!topRightCorner.equals(other.topRightCorner))
			return false;
		return true;
	}

}
