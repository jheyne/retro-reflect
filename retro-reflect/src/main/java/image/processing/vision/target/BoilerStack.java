package image.processing.vision.target;

import java.awt.Color;
import java.awt.Graphics2D;

import image.processing.vision.drawable.MultipatchDrawable;
import image.processing.vision.drawable.ReflectivePatch;

public class BoilerStack extends MultipatchDrawable {

	@Override
	public void draw(Graphics2D g) {
		g.setBackground(Color.GREEN);
		// Collections.sort(patches, ReflectivePatch.getVerticalComparator());
		for (final ReflectivePatch patch : patches) {
			// Quadrilateral quadrilateral = patch.getQuadrilateral();
			// for (int i = 0; i < quadrilateral.npoints; i++) {
			// int x = quadrilateral.xpoints[i];
			// int y = quadrilateral.ypoints[i];
			// }
			patch.draw(g);
		}
	}

}
