package image.processing.vision.drawable;

import java.awt.Graphics;
import java.awt.Polygon;

public class Quadrilateral extends Polygon {

	private static final long serialVersionUID = 1L;

	public void draw(Graphics g) {
		g.drawPolygon(this);
	}

	public void fill(Graphics g) {
		g.fillPolygon(this);
	}

}