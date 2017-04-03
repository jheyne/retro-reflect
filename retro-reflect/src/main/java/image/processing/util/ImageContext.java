package image.processing.util;

import java.awt.image.BufferedImage;

import image.processing.finder.Finder;
import image.processing.profile.TargetProfile;

public class ImageContext<T extends Finder> {

	private final double cameraAngle;

	private int bottomClip = Integer.MAX_VALUE;
	private int topClip = 0;
	private final BufferedImage image;
	private final T finder;
	private final TargetProfile targetProfile;

	/**
	 * The number of Y pixels to skip between searches. This should be as big as
	 * possible without missing figures.
	 */
	private int searchIntervalY = 8;

	/**
	 * The number of X pixels to skip between searches. This should be as big as
	 * possible without missing figures.
	 */
	private int searchIntervalX = 7;

	/**
	 * This number should be less than half the width of the minimum cross
	 * section of the target to avoid ignoring a potential target
	 */
	private int peekAheadPixels = 2;

	public ImageContext(BufferedImage image, TargetProfile targetProfile, T finder, double cameraAngle) {
		super();
		this.cameraAngle = cameraAngle;
		this.image = image;
		this.finder = finder;
		this.targetProfile = targetProfile;
		bottomClip = image.getHeight();
	}

	public int getBottomClip() {
		return bottomClip;
	}

	public double getCameraAngle() {
		return cameraAngle;
	}

	public T getFinder() {
		return finder;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getPeekAheadPixels() {
		return peekAheadPixels;
	}

	public int getSearchIntervalX() {
		return searchIntervalX;
	}

	public int getSearchIntervalY() {
		return searchIntervalY;
	}

	public TargetProfile getTargetProfile() {
		return targetProfile;
	}

	public int getTopClip() {
		return topClip;
	}

	public void setBottomClip(int bottomClip) {
		this.bottomClip = bottomClip;
	}

	public void setPeekAheadPixels(int peekAheadPixels) {
		this.peekAheadPixels = peekAheadPixels;
	}

	public void setSearchIntervalX(int searchIntervalX) {
		this.searchIntervalX = searchIntervalX;
	}

	public void setSearchIntervalY(int searchIntervalY) {
		this.searchIntervalY = searchIntervalY;
	}

	public void setTopClip(int topClip) {
		this.topClip = topClip;
	}
}
