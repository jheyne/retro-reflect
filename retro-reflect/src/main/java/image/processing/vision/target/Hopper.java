package image.processing.vision.target;

import java.awt.Color;
import java.awt.Graphics2D;

import image.processing.vision.drawable.MultipatchDrawable;
import image.processing.vision.drawable.ReflectivePatch;

public class Hopper extends MultipatchDrawable {

	@Override
	public void draw(Graphics2D g) {
		g.setBackground(Color.YELLOW);
		for (final ReflectivePatch reflectivePatch : patches) {
			reflectivePatch.draw(g);
		}
	}

}
