package image.processing.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import image.processing.profile.TargetProfile;

/**
 * utility methods to acquire HSV colors
 */
public abstract class Util {

	/**
	 * @return A TargetProfile by merging the specified HSV values
	 */
	public static TargetProfile createTargetProfile(float[]... hsvArray) {
		final TargetProfile profile = new TargetProfile();
		for (final float[] hsv : hsvArray) {
			// Experiment : ignore extreme values
			if (hsv[0] < 0.1f || hsv[0] > 0.9f) {
				continue;
			}
			if (hsv[1] < 0.1f || hsv[1] > 0.9f) {
				continue;
			}
			if (hsv[2] < 0.1f || hsv[2] > 0.9f) {
				continue;
			}
			profile.hueMin = Math.min(profile.hueMin, hsv[0]);
			profile.hueMax = Math.max(profile.hueMax, hsv[0]);
			profile.saturationMin = Math.min(profile.saturationMin, hsv[1]);
			profile.saturationMax = Math.max(profile.saturationMax, hsv[1]);
			profile.brightnessMin = Math.min(profile.brightnessMin, hsv[2]);
			profile.brightnessMax = Math.max(profile.brightnessMax, hsv[2]);
		}
		return profile;
	}

	/**
	 * evaluate the callback for each point on the expanded line between the two
	 * points
	 */
	public static void forEachPointOnExpandedLine(Point start, Point end, Function<Point, Object> callback,
			int expandBy) {
		final int absX = Math.abs(start.x - end.x);
		final int absY = Math.abs(start.y - end.y);
		if (absY >= absX) {
			for (int expand = expandBy * -1; expand < expandBy; expand++) {
				forEachPointOnLine(new Point(start.x + expand, start.y), new Point(end.x + expand, end.y), callback);
			}
		} else {
			for (int expand = expandBy * -1; expand < expandBy; expand++) {
				forEachPointOnLine(new Point(start.x, start.y + expand), new Point(end.x, end.y + expand), callback);
			}
		}
	}

	/**
	 * evaluate the callback for each point on the line between the two points
	 */
	public static void forEachPointOnLine(Point start, Point end, Function<Point, Object> callback) {
		int x = start.x;
		int y = start.y;
		final int x2 = end.x;
		final int y2 = end.y;

		final int w = x2 - x;
		final int h = y2 - y;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0) {
			dx1 = -1;
		} else if (w > 0) {
			dx1 = 1;
		}
		if (h < 0) {
			dy1 = -1;
		} else if (h > 0) {
			dy1 = 1;
		}
		if (w < 0) {
			dx2 = -1;
		} else if (w > 0) {
			dx2 = 1;
		}
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0) {
				dy2 = -1;
			} else if (h > 0) {
				dy2 = 1;
			}
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			callback.apply(new Point(x, y));
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
	}

	public static TargetProfile getCompositeTargetProfile(List<TargetProfile> targetProfiles) {
		TargetProfile composite = new TargetProfile();
		for (final TargetProfile targetProfile : targetProfiles) {
			composite = composite.merge(targetProfile);
		}
		return composite;
	}

	/**
	 * @return an HSB array for the color
	 */
	public static float[] getHSV(Color color) {
		return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
	}

	/**
	 * @return an HSB array for the color at the specified coordinates in the
	 *         specified image
	 */
	public static float[] getHSV(int x, int y, BufferedImage image) {
		final Color color = new Color(image.getRGB(x, y));
		final float[] hsv = Util.getHSV(color);
		return hsv;
	}

	/**
	 * @return an HSB array for the color at the specified point in the
	 *         specified image
	 */
	public static float[] getHSV(Point point, BufferedImage image) {
		return Util.getHSV(point.x, point.y, image);
	}

	/**
	 * @return an array of HSV colors from an image along a line expanded by the
	 *         specified pixels
	 */
	public static float[][] getSampleHsv(BufferedImage image, Line line, int expandBy) {
		final List<float[]> samples = new ArrayList<>();
		Util.forEachPointOnExpandedLine(line.leftTop, line.rightBottom, e -> samples.add(Util.getHSV(e.x, e.y, image)),
				expandBy);
		return samples.toArray(new float[samples.size()][]);
	}

	/**
	 * @param image
	 *            the image which provides the samples
	 * @param center
	 *            the center point from which the samples should be taken
	 * @param expandBy
	 *            include samples which are this distance (by coordinates) from
	 *            the given center point
	 * @return
	 */
	public static float[][] getSampleHsv(BufferedImage image, Point center, int expandBy) {
		final List<float[]> samples = new ArrayList<>();
		final int left = center.x - expandBy;
		final int right = center.x + expandBy;
		final int top = center.y - expandBy;
		final int bottom = center.y + expandBy;
		for (int x = left; x < right; x++) {
			for (int y = top; y < bottom; y++) {
				samples.add(Util.getHSV(x, y, image));
			}
		}
		return samples.toArray(new float[samples.size()][]);
	}
}
