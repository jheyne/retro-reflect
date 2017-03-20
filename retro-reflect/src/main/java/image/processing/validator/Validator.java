package image.processing.validator;

import image.processing.finder.Figure;
import image.processing.profile.TargetProfile;

/**
 * Interface to determine whether a Figure is a valid candidate
 */
public interface Validator {

	boolean isValid(Figure figure, TargetProfile profile);

}
