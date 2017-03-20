package image.processing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Converts RGB pixels in an image to HSB. It can be configured to remember the
 * access path for debugging search strategies.
 */
public class HsbFilter {

	public final BufferedImage image;
	final WritableRaster raster;

	final float[] hsb = new float[3];
	final int[] rgb;

	boolean rememberPath = false;
	List<Point> path;
	Map<Color, List<Point>> coloredPath;

	public HsbFilter(BufferedImage image) {
		super();
		this.image = image;
		this.raster = image.getRaster();
		rgb = new int[raster.getNumDataElements()];
	}

	/**
	 * Set a color which should be used to display all subsequent points, until
	 * the color is changed again.
	 */
	public void color(Color color) {
		if (rememberPath) {
			if (coloredPath == null) {
				coloredPath = new HashMap<>();
			}
			List<Point> existing = coloredPath.get(color);
			if (existing == null) {
				existing = new ArrayList<>();
				coloredPath.put(color, existing);
			}
			existing.addAll(path);
			path.clear();
		}
	}

	/**
	 * open a window to display the image
	 */
	public void displayAccessPath() {
		drawAccessPath();
		final ImageIcon icon = new ImageIcon(image);
		final JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(image.getWidth(), image.getHeight());
		final JLabel lbl = new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * draw the access path with the appropriate color on the image
	 */
	public void drawAccessPath() {
		if (rememberPath) {
			if (coloredPath != null) {
				for (final Entry<Color, List<Point>> entry : coloredPath.entrySet()) {
					final int color = entry.getKey().getRGB();
					for (final Point point : entry.getValue()) {
						image.setRGB(point.x, point.y, color);
					}
				}
			}
			final int red = Color.RED.getRGB();
			for (final Point point : path) {
				try {
					image.setRGB(point.x, point.y, red);
				} catch (final ArrayIndexOutOfBoundsException e) {
				}
			}
		}
	}

	/**
	 * Convert the image RGB pixel at the specified coordinates to HSB,
	 * recording the access if rememberPath is true.
	 *
	 * Warning: do NOT cache the result! The array is recycled for efficiency,
	 * and the internal values will be reassigned on any subsequent call
	 */
	public float[] hsb(int x, int y) {
		if (rememberPath) {
			path.add(new Point(x, y));
		}
		raster.getPixel(x, y, rgb);
		Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
		return hsb;
	}

	/**
	 * If rememberPath is true, pixel accesses will be remembered so that they
	 * can later be drawn. This can be useful for debugging, but should not be
	 * used for production.
	 */
	public void setRememberPath(boolean rememberPath) {
		this.rememberPath = rememberPath;
		if (rememberPath) {
			path = new ArrayList<>();
		}
	}

}
