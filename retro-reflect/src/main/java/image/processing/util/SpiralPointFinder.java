package image.processing.util;

import java.awt.Point;
import java.util.Iterator;

@Deprecated // incomplete
public class SpiralPointFinder implements Iterator<Point> {

	Point point = new Point();
	int increment, left, right, top, bottom;

	@Override
	public boolean hasNext() {
		return point != null;
	}

	@Override
	public Point next() {
		// TODO Auto-generated method stub
		return point;
	}

}
