package it.demo.twitterlike.android.utils;

import it.demo.twitterlike.rest.api.UserProfile;

import org.apache.commons.lang3.StringUtils;

public class UserUtils {

	public static String getUserFullName(UserProfile user) {
		return user == null ? null : (StringUtils.trimToEmpty(user
				.getFirstName()) + " " + StringUtils.trimToEmpty(user
				.getLastName()));
	}

}
