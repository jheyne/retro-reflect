package image.processing.util;

//import java.awt.image.BufferedImage;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.ArrayList;
//import java.util.Collections;

//import org.apache.commons.collections4.Bag;
//import org.apache.commons.collections4.bag.HashBag;

//import image.processing.HsbFilter;
//import image.processing.profile.TargetProfile;

/**
 * Experimental analyze colors in a section of an image
 */
@Deprecated
public class Analyzer {

//	public static void analyze(final BufferedImage img, int x, int y, final int width, final int height,
//			TargetProfile targetProfile) {
//		final HsbFilter filter = new HsbFilter(img);
//		final Bag<Double> hues = new HashBag<>();
//		final Bag<Double> rawHues = new HashBag<>();
//		final Bag<Double> rawSaturations = new HashBag<>();
//		final Bag<Double> rawBrightnesses = new HashBag<>();
//		final Bag<Double> saturations = new HashBag<>();
//		final Bag<Double> brightnesses = new HashBag<>();
//		final Bag<String> matches = new HashBag<>();
//		final int maxX = Math.min(x + width, img.getWidth());
//		final int maxY = Math.min(y + height, img.getHeight());
//		final int decimals = 2;
//		for (int i = x; i < maxX; i++) {
//			for (int j = y; j < maxY; j++) {
//				// System.out.println(i + " @ " + j);
//				final float[] hsb = filter.hsb(i, j);
//				final float hue = hsb[0];
//				final float saturation = hsb[1];
//				final float brightness = hsb[2];
//				final double rawHue = new BigDecimal(hue).setScale(decimals, RoundingMode.HALF_UP).doubleValue();
//				hues.add(rawHue);
//				rawHues.add(rawHue);
//				final double rawSaturation = new BigDecimal(saturation).setScale(decimals, RoundingMode.HALF_UP)
//						.doubleValue();
//				saturations.add(rawSaturation);
//				rawSaturations.add(rawSaturation);
//				final double rawBrightness = new BigDecimal(brightness).setScale(decimals, RoundingMode.HALF_UP)
//						.doubleValue();
//				brightnesses.add(rawBrightness);
//				rawBrightnesses.add(rawBrightness);
//				if (targetProfile.matches(hsb)) {
//					matches.add("" + hue + ":" + saturation + ":" + brightness);
//				}
//			}
//		}
//		System.out.println("Raw Hues: " + rawHues.size());
//		System.out.println("Raw Saturations: " + rawSaturations.size());
//		System.out.println("Raw Brightness: " + rawBrightnesses.size());
//		System.out.println("Matches: " + matches.size() + " of " + width * height + " or "
//				+ 100 * matches.size() / (width * height) + "%");
//		final ArrayList<Double> sorted = new ArrayList<>(hues.uniqueSet());
//		Collections.sort(sorted);
//		for (final double hue : sorted) {
//			System.out.println("H: " + Math.round(180 * hue) + " (" + hues.getCount(hue) + "), \tS:"
//					+ Math.round(255 * hue) + " (" + saturations.getCount(hue) + "), \tB: " + Math.round(255 * hue)
//					+ " (" + brightnesses.getCount(hue) + ")");
//		}
//	}
//
//	public static void analyze(final BufferedImage img, TargetProfile targetProfile) {
//		analyze(img, 0, 0, img.getWidth(), img.getHeight(), targetProfile);
//	}
}
