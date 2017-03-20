package image.processing.util;

import java.util.ArrayList;
import java.util.List;

import image.processing.profile.TargetProfile;

/**
 * Create a single TargetProfile encompassing the matches of multiple
 * TargetProfiles
 */
@Deprecated
public class TargetProfileManager {

	TargetProfile currentlyEditing;
	final List<TargetProfile> targetProfiles = new ArrayList<>();

	TargetProfile getCompositeTargetProfile() {
		TargetProfile composite = new TargetProfile();
		for (final TargetProfile targetProfile : targetProfiles) {
			composite = composite.merge(targetProfile);
		}
		return composite;
	}
}
