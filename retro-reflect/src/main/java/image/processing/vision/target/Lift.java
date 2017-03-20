package image.processing.vision.target;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import image.processing.exception.NotFound;
import image.processing.finder.Figure;
import image.processing.vision.drawable.MultipatchDrawable;
import image.processing.vision.drawable.ReflectivePatch;

public class Lift extends MultipatchDrawable {

	@Override
	public void draw(Graphics2D g) {
		g.setBackground(Color.GREEN);
		for (final ReflectivePatch reflectivePatch : patches) {
			reflectivePatch.draw(g);
		}
	}

	public void setFigures(List<Figure> figures) {
		for (Figure figure : figures) {		
			ReflectivePatch patch = new ReflectivePatch();
			patch.setFigure(figure);
			addPatch(patch);
		}
	}
	
	public boolean validate() throws NotFound {
		if (patches.size() < 2) {
			throw NotFound.INSTANCE;
		}
		if (patches.size() == 2) {
			Rectangle2D first = patches.get(0).getBounds();
			double firstHeight = first.getHeight();
			if (first.getWidth() > firstHeight) {
				System.out.println("first is " + first.getWidth() + "x" + firstHeight);
				throw NotFound.INSTANCE;
			}
			Rectangle2D second = patches.get(1).getBounds();
			double lastHeight = second.getHeight();
			if (second.getWidth() > lastHeight) {
				System.out.println("second is " + second.getWidth() + "x" + lastHeight);
				throw NotFound.INSTANCE;
			}
			double differenceInHeight = Math.abs(firstHeight - lastHeight);
			double tallest = Math.max(firstHeight, lastHeight);
			if (differenceInHeight / tallest > 0.15) {
				System.out.println("Did not expect so great a difference in height");
				throw NotFound.INSTANCE;
			}
		}
		return true;
	}
}
