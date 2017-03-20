package image.processing.finder;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import image.processing.Searcher;
import image.processing.exception.NotFound;
import image.processing.profile.TargetProfile;
import image.processing.strategy.CandidateFigureFinder;
import image.processing.strategy.FilledBlobFinder;
import image.processing.validator.Validator;

public abstract class Finder implements Validator {

	final Searcher searcher;

	/**
	 * The figures that are found
	 */
	public final List<Figure> figures = new ArrayList<>();

	public Finder(Searcher searcher) {
		super();
		this.searcher = searcher;
	}

	public abstract void find() throws NotFound;

	public abstract Color getColor();

	protected int getMinimumWidth() {
		return 60;
	}

	/**
	 * @return the appropriate search strategy
	 */
	protected CandidateFigureFinder getSearchStrategy(final Figure figure) {
		// return new MarchingSquares(searcher, figure);
		return new FilledBlobFinder(searcher, figure);
	}

	/**
	 * @return true of one of the found figures includes the specified X
	 *         coordinate
	 */
	public boolean hasProcessedImageColumn(int x) {
		for (final Figure figure : figures) {
			if (figure.encompasesX(x)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isValid(Figure figure, TargetProfile profile) {
		final boolean b = figure.getWidth() >= getMinimumWidth();
		System.out.println("isValid: " + b + " width: " + figure.getWidth() + " min width: " + getMinimumWidth());
		return b;
	}

	protected void visualize() {
		searcher.visualize(this);
	}

}
