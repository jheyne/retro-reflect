package image.processing.util;

import java.awt.image.BufferedImage;

import image.processing.Searcher;
import image.processing.Searcher.FigureType;
import image.processing.exception.NotFound;
import image.processing.finder.Figure;
import image.processing.finder.Finder;
import image.processing.profile.TargetProfile;
import image.processing.vision.drawable.ReflectivePatch;
import image.processing.vision.target.Lift;

public class VisionUtil {

	@Deprecated  // not ready yet
	public static Lift findGearLift(BufferedImage image) throws NotFound {
		Searcher searcher = new Searcher(image, TargetProfile.goodReflection(), FigureType.GearLift);
		Finder finder = searcher.finder();
		Lift lift = new Lift();
		for (Figure figure : finder.figures) {		
			ReflectivePatch patch = new ReflectivePatch();
			patch.setFigure(figure);
			lift.addPatch(patch);
			patch.getCenter();
		}
		return lift;
	}
	
	public static int findGearLiftCenter(BufferedImage image) throws NotFound {
		Searcher searcher = new Searcher(image, TargetProfile.goodReflection(), FigureType.GearLift);
		Finder finder = searcher.finder();
		int min = Integer.MAX_VALUE;
		int max = Integer.MAX_VALUE;
		for (Figure figure : finder.figures) {		
			min = Math.min(min, figure.leftBoundary);
			max = Math.max(min, figure.rightBoundary);
		}
		return (min + max) / 2;
	}
}
