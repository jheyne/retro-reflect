package image.processing.validator;

import image.processing.finder.Figure;
import image.processing.profile.TargetProfile;

/**
 * Rudimentary Figure validator which checks for minimum width
 */
public class UTargetValidator implements Validator {

	@Override
	public boolean isValid(Figure figure, TargetProfile profile) {
		if (figure.getWidth() < 60) {
			return false;
		}
		return true;
	}

}
