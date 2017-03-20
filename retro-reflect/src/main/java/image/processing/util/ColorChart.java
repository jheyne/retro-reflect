package image.processing.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import image.processing.profile.TargetProfile;

/**
 * Create a swatch of color with pixel colors satisfying a target profile. This
 * can be used to present a visual sample for a target profile.
 */
public class ColorChart {

	final static Random random = new Random();
	final TargetProfile profile;

	public ColorChart(TargetProfile profile) {
		super();
		this.profile = profile;
	}

	public BufferedImage getImage(int width, int height) {
		final float hueRange = profile.hueMax - profile.hueMin;
		final float satRange = profile.saturationMax - profile.saturationMin;
		final float briRange = profile.brightnessMax - profile.brightnessMin;
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final float h = random.nextFloat() * hueRange + profile.hueMin;
				final float s = random.nextFloat() * satRange + profile.saturationMin;
				final float b = random.nextFloat() * briRange + profile.brightnessMin;
				image.setRGB(x, y, Color.HSBtoRGB(h, s, b));
			}
		}
		return image;
	}

}
