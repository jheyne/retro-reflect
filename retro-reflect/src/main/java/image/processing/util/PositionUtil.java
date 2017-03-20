package image.processing.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import image.processing.vision.drawable.Drawable;

public class PositionUtil {

	/**
	 * @param drawable a target
	 * @param image source of the target
	 * @param offset distance the camera is mounted off center (in image pixels)
	 * @return a negative number if the robot needs to go left, and a positive
	 *         number if the robot needs to go right
	 */
	public static int getSteeringDirection(Drawable drawable, BufferedImage image, int offset) {
		return (int) (drawable.getCenter().getX() - (image.getWidth() / 2) + offset);
	}
	
	public static double getDistance(Drawable drawable, BufferedImage image, double cameraAngle) {
//		d = Tft*FOVpixel/(2*Tpixel*tan)
		double targetFeet = 10.25/12;
		System.out.println("Target feet: " + targetFeet);
		int fieldOfViewPixels = image.getWidth();
		System.out.println("Field of View Pixels: " + fieldOfViewPixels);
		Rectangle2D targetBounds = drawable.getBounds();
		System.out.println("Target bounds: " + targetBounds);
		double targetPixels = targetBounds.getWidth(); // this calculation can be improved
		System.out.println("Target pixels: " + targetPixels);
		double cameraRadians = Math.toRadians(cameraAngle);
		return targetFeet * fieldOfViewPixels / (2 * targetPixels * Math.tan(cameraRadians));
	}

	/**
	 * @param imagePoint
	 *            in pixels
	 * @param image
	 * @return a point converted from the pixel system to the aiming system
	 */
	public static Point2D getAimingPosition(Point imagePoint, BufferedImage image) {
		final int halfWidth = image.getWidth() / 2;
		final double x = (imagePoint.getX() - halfWidth) / image.getWidth() / 2;
		final int halfHeight = image.getHeight() / 2;
		final double y = (imagePoint.getY() - halfHeight) / image.getHeight() / 2;
		return new Point2D.Double(x, y);
	}
	
}
