package image.processing.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import image.processing.profile.TargetProfile;

/**
 * Experimental utility to search for TargetProfile parameters which improve
 * match results
 */
@Deprecated
public class TargetProfileBuilder {

	static class Option implements Comparable<Option> {
		Parameter parameter;
		float offset = 0f;
		Integer count = 0;

		public Option(Parameter parameter) {
			super();
			this.parameter = parameter;
		}

		public void applyTo(TargetProfile p) {
			switch (parameter) {
			case BRI_MAX:
				p.brightnessMax += offset;
				p.brightnessMax = Math.max(0f, p.brightnessMax);
				break;
			case BRI_MIN:
				p.brightnessMin += offset;
				p.brightnessMin = Math.min(1.0f, p.brightnessMin);
				break;
			case HUE_MAX:
				p.hueMax += offset;
				p.hueMax = Math.max(0f, p.hueMax);
				break;
			case HUE_MIN:
				p.hueMin += offset;
				p.hueMin = Math.min(1.0f, p.hueMin);
				break;
			case SAT_MAX:
				p.saturationMax += offset;
				p.saturationMax = Math.max(0f, p.saturationMax);
				break;
			case SAT_MIN:
				p.saturationMin += offset;
				p.saturationMin = Math.min(1.0f, p.saturationMin);
				break;
			default:
				break;
			}
		}

		@Override
		public int compareTo(Option o) {
			return o.count.compareTo(count);
		}

	};

	enum Parameter {
		HUE_MIN, HUE_MAX, SAT_MIN, SAT_MAX, BRI_MIN, BRI_MAX
	}

	class ValueCache {
		final float[] hsb = new float[3];
		final int[][] cache;
		final BufferedImage image;
		int count = 0;

		public ValueCache(BufferedImage img) {
			this.image = img;
			cache = new int[img.getWidth()][img.getHeight()];
		}

		int countMatches(TargetProfile profile) {
			final int width = image.getWidth();
			final int height = image.getHeight();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					matches(x, y, profile);
				}
			}
			return count;
		}

		boolean matches(int x, int y, TargetProfile profile) {
			final int cached = cache[x][y];
			if (cached == 0) {
				final Color c = new Color(image.getRGB(x, y));
				Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
				final boolean matches = profile.matches(hsb);
				cache[x][y] = matches ? 1 : -1;
				if (matches) {
					count++;
				}
				return matches;
			} else {
				return cached == 1;
			}
		}
	}

	final List<Option> options = new ArrayList<>();
	final TargetProfile originalProfile;
	final TargetProfile profile;
	final BufferedImage image;

	ValueCache valueCache;

	float increment = 0.01f;

	int lastCount = 0;

	public TargetProfileBuilder(TargetProfile targetProfile, BufferedImage subimage) {
		originalProfile = targetProfile;
		profile = originalProfile.copy();
		this.image = subimage;
		for (final Parameter parameter : Parameter.values()) {
			options.add(new Option(parameter));
		}
		valueCache = new ValueCache(image);
	}

	boolean applyLargestOffset() {
		Collections.sort(options);
		final Option largest = options.get(0);
		if (largest.count <= lastCount) {
			return false;
		}
		largest.applyTo(profile);
		lastCount = largest.count;
		return true;
	}

	void clearOptionOffsets() {
		for (final Option option : options) {
			option.offset = 0f;
		}
	}

	void incrementOptionOffsets() {
		for (final Option option : options) {
			option.offset += increment;
		}
	}

	public TargetProfile optimizeProfile() {
		// set a baseline to see if we can improve matches
		lastCount = new ValueCache(image).countMatches(profile);
		// keep track of whether we are cycling without improving
		int unchangedCount = 0;
		while (true) {
			incrementOptionOffsets();
			// try all the options to find which improves matching
			for (final Option option : options) {
				final TargetProfile copy = profile.copy();
				option.applyTo(copy);
				final int count = new ValueCache(image).countMatches(copy);
				option.count = count;
			}
			// apply the most effective option
			final boolean applied = applyLargestOffset();
			if (applied) {
				// start fresh
				clearOptionOffsets();
			} else {
				// continue incrementing options hoping to improve matches
				if (unchangedCount++ > 20) {
					System.out.println("Abort search - no recent improvements to profile");
					System.out.println("Original profile: ");
					System.out.println(originalProfile);
					System.out.println("Adjusted profile: ");
					System.out.println(profile);
					return profile;
				}
			}
		}
	}

}
