package unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;

import image.processing.util.DiagonalArrayFinder;
import image.processing.util.DiagonalArraySearch;
import image.processing.util.DiagonalArraySearch.StartCorner;

public class DiagonalArraySearchTest {

	@Test
	public void testBottomLeft() {
		/* @formatter:off */
		final Integer[][] array = { { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0 }, { 0, 1, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0 }, { 23, 0, 0, 0, 1, 0, 0 } };
		/* @formatter:on */

		final StartCorner corner = DiagonalArraySearch.StartCorner.BOTTOM_LEFT;
		final DiagonalArraySearch<Integer> search = new DiagonalArraySearch<>(array, corner);
		testElementCount(array, search);
		testFindValue(array, corner);
	}

	@Test
	public void testBottomRight() {
		/* @formatter:off */
		final Integer[][] array = { { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0 }, { 0, 1, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 23, 0, 0 } };
		/* @formatter:on */

		final StartCorner corner = DiagonalArraySearch.StartCorner.BOTTOM_RIGHT;
		final DiagonalArraySearch<Integer> search = new DiagonalArraySearch<>(array, corner);
		testElementCount(array, search);
		testFindValue(array, corner);
	}

	private void testElementCount(Integer[][] array, final DiagonalArraySearch<Integer> search) {
		assertTrue(search.hasNext());
		int count = 0;
		for (@SuppressWarnings("unused")
		final Point point : search) {
			// System.out.println("Found " + point.x + '@' + point.y);
			count++;
		}
		assertEquals(array.length * array[0].length, count);
	}

	private void testFindValue(Integer[][] array, final StartCorner corner) {
		final DiagonalArrayFinder<Integer> finder = new DiagonalArrayFinder<>(array, corner);
		final Integer found = finder.findValue((Integer i) -> i > 0);
		assertEquals((Integer) 23, found);
	}

	@Test
	public void testTopLeft() {
		/* @formatter:off */
		final Integer[][] array = { { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0 }, { 0, 23, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 1, 0 }, { 1, 0, 0, 0, 0, 23, 0 } };
		/* @formatter:on */

		final StartCorner corner = DiagonalArraySearch.StartCorner.TOP_LEFT;
		final DiagonalArraySearch<Integer> search = new DiagonalArraySearch<>(array, corner);
		testElementCount(array, search);
		testFindValue(array, corner);
	}

	@Test
	public void testTopRight() {
		/* @formatter:off */
		final Integer[][] array = { { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 23, 0 }, { 0, 1, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 1, 0 }, { 1, 0, 0, 0, 0, 0, 0 } };
		/* @formatter:on */

		final DiagonalArrayFinder<Integer> finder = new DiagonalArrayFinder<>(array,
				DiagonalArraySearch.StartCorner.TOP_RIGHT);
		final Integer found = finder.findValue((Integer i) -> i > 0);
		assertEquals((Integer) 23, found);
	}

}
