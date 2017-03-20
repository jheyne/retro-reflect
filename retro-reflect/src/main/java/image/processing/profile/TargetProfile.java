package image.processing.profile;

import java.awt.Color;

import image.processing.util.Util;

/**
 * Defines ranges of HSB values, and includes tests to determine whether a
 * particular pixel matches.
 */
public class TargetProfile {

	/**
	 * @return a sample which typically matches images from FIRST with good
	 *         reflection
	 */
	public static TargetProfile goodReflection() {
		// return new TargetProfile("Good reflection", 0.472f, 0.533f, 0.588f,
		// 1.0f, 0.235f, 0.635f);
		return new TargetProfile("Good reflection", 0.46f, 0.533f, 0.23f, 1.0f, 0.235f, 0.66f, Color.BLUE);
	}

	public static TargetProfile hopper() {
		return new TargetProfile(0.104f, 0.208f, 0.267f, 1.000f, 0.408f, 1.000f);
	}

	/**
	 * @return a sample which typically matches images from FIRST with side
	 *         reflection
	 */
	public static TargetProfile lateralReflection() {
		return new TargetProfile("Lateral reflection", 0.470f, 0.650f, 0.130f, 0.840f, 0.550f, 0.780f, Color.GREEN);
		// new TargetProfile(0.493f, 0.608f, 0.258f, 0.863f, 0.565f, 0.785f);
		// return new TargetProfile(0.480f, 0.620f, 0.140f, 0.930f, 0.320f,
		// 0.700f);
	}

	public String label = "No Label";
	public boolean active = true;
	public float hueMin;
	public float hueMax;
	public float saturationMin;
	public float saturationMax;
	public float brightnessMin;
	public int displayColor = Color.BLACK.getRGB();

	public float brightnessMax;

//	this value should be less than minimum target width - x skip ahead
	public int defaultLookAhead = 1;

	public TargetProfile() {
		this(1f, 0f, 1f, 0f, 1f, 0f);
	}

	public TargetProfile(Color color) {
		final float[] hsv = Util.getHSV(color);
		this.hueMin = hsv[0];
		this.hueMax = hsv[0];
		this.saturationMin = hsv[1];
		this.saturationMax = hsv[1];
		this.brightnessMin = hsv[2];
		this.brightnessMax = hsv[2];
	}

	public TargetProfile(float hueMin, float hueMax, float saturationMin, float saturationMax, float brightnessMin,
			float brightnessMax) {
		this("No Label", hueMin, hueMax, saturationMin, saturationMax, brightnessMin, brightnessMax, Color.BLACK);
	}

	public TargetProfile(String label, float hueMin, float hueMax, float saturationMin, float saturationMax,
			float brightnessMin, float brightnessMax, Color displayColor) {
		super();
		this.label = label;
		this.hueMin = hueMin;
		this.hueMax = hueMax;
		this.saturationMin = saturationMin;
		this.saturationMax = saturationMax;
		this.brightnessMin = brightnessMin;
		this.brightnessMax = brightnessMax;
		this.displayColor = displayColor.getRGB();
	}

	/**
	 * Adjust the HSB ranges to include the specified HSB values
	 */
	public void appendHsb(float[]... hsv) {
		for (final float[] fs : hsv) {
			// Experiment : ignore extreme values
			// if (fs[0] < 0.1f || fs[0] > 0.9f) {
			// continue;
			// }
			// if (fs[1] < 0.1f || fs[1] > 0.9f) {
			// continue;
			// }
			// if (fs[2] < 0.1f || fs[2] > 0.9f) {
			// continue;
			// }
			hueMin = Math.min(hueMin, fs[0]);
			hueMax = Math.max(hueMax, fs[0]);
			saturationMin = Math.min(saturationMin, fs[1]);
			saturationMax = Math.max(saturationMax, fs[1]);
			brightnessMin = Math.min(brightnessMin, fs[2]);
			brightnessMax = Math.max(brightnessMax, fs[2]);
		}
	}

	public void clearValues() {
		hueMin = saturationMin = brightnessMin = 1f;
		hueMax = saturationMax = brightnessMax = 0f;
	}

	public TargetProfile copy() {
		return new TargetProfile(hueMin, hueMax, saturationMin, saturationMax, brightnessMin, brightnessMax);
	}

	public Color getDisplayColor() {
		return new Color(displayColor);
	}

	/**
	 * @return true if the specified color satisfies the receiver's ranges
	 */
	public boolean matches(float[] hsb) {
		final float hue = hsb[0];
		final float saturation = hsb[1];
		final float brightness = hsb[2];
		return matchesHue(hue) && matchesSaturation(saturation) && matchesBrightness(brightness);
	}

	public boolean matchesBrightness(final float brightness) {
		return brightness >= brightnessMin && brightness <= brightnessMax;
	}

	public boolean matchesHue(final float hue) {
		return hue >= hueMin && hue <= hueMax;
	}

	public boolean matchesHueAndBrightness(float[] hsb) {
		final float hue = hsb[0];
		final float brightness = hsb[2];
		return matchesHue(hue) && matchesBrightness(brightness);
	}

	public boolean matchesSaturation(final float saturation) {
		return saturation >= saturationMin && saturation <= saturationMax;
	}

	/**
	 * @return a new TargetProfile
	 */
	public TargetProfile merge(TargetProfile p) {
		return new TargetProfile(Math.min(hueMin, p.hueMin), Math.max(hueMax, p.hueMax),
				Math.min(saturationMin, p.saturationMin), Math.max(saturationMax, p.saturationMax),
				Math.min(brightnessMin, p.brightnessMin), Math.max(brightnessMax, p.brightnessMax));
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		final TargetProfile p = this;
		b.append("new TargetProfile(");
		b.append(String.format("%.3f", p.hueMin));
		b.append("f, ");
		b.append(String.format("%.3f", p.hueMax));
		b.append("f, ");
		b.append(String.format("%.3f", p.saturationMin));
		b.append("f, ");
		b.append(String.format("%.3f", p.saturationMax));
		b.append("f, ");
		b.append(String.format("%.3f", p.brightnessMin));
		b.append("f, ");
		b.append(String.format("%.3f", p.brightnessMax));
		b.append("f");
		b.append(");");
		return b.toString();
	}
}
