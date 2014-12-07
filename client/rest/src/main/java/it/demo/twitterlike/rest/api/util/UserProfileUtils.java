package it.demo.twitterlike.rest.api.util;

import it.demo.twitterlike.rest.api.UserProfile;

import java.util.LinkedHashSet;
import java.util.Set;

public class UserProfileUtils {

	public static String[] getUserIds(Iterable<? extends UserProfile> profiles) {
		Set<String> result = new LinkedHashSet<String>();
		if (profiles != null) {
			for (UserProfile current : profiles) {
				if (current != null) {
					result.add(current.getId());
				}
			}
		}
		return result.toArray(new String[] {});

	}

}
