package image.processing.deprecated;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import image.processing.Searcher.Direction;

public class SlopeTest {

	@Test
	public void testReverse() {
		final Slope east = new Slope(Direction.EAST, 3, 2);
		final Slope west = east.reverse();
		assertEquals(east, west.reverse());
	}

	@Test
	public void testTurnLeft() {
		final Slope east = new Slope(Direction.EAST, 3, 2);
		final Slope north = east.turnLeft();
		final Slope west = north.turnLeft();
		final Slope south = west.turnLeft();
		assertEquals(east, south.turnLeft());

		assertEquals(east, east.turnLeft().turnRight());
		assertEquals(north, north.turnLeft().turnRight());
		assertEquals(west, west.turnLeft().turnRight());
		assertEquals(south, south.turnLeft().turnRight());
	}

	@Test
	public void testTurnRight() {
		final Slope east = new Slope(Direction.EAST, 3, 2);
		final Slope north = east.turnRight();
		final Slope west = north.turnRight();
		final Slope south = west.turnRight();
		assertEquals(east, south.turnRight());

		assertEquals(east, east.turnRight().turnLeft());
		assertEquals(north, north.turnRight().turnLeft());
		assertEquals(west, west.turnRight().turnLeft());
		assertEquals(south, south.turnRight().turnLeft());
	}

	@Test
	public void testUnsupportedDirection() {
		try {
			new Slope(Direction.NORTHEAST, 3, 2);
			fail("expected unsupported direction");
		} catch (final AssertionError e) {
		}
	}

}
