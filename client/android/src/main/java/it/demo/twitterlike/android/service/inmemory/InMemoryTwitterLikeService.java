package it.demo.twitterlike.android.service.inmemory;

import it.demo.twitterlike.android.service.TwitterLikeService;
import it.demo.twitterlike.android.utils.ConfigurationUtil;
import it.demo.twitterlike.android.utils.SerializeUtil;
import it.demo.twitterlike.rest.api.TwitterLike;
import it.demo.twitterlike.rest.api.UserProfile;

import java.io.Serializable;
import java.util.Date;

import android.content.Context;

public class InMemoryTwitterLikeService implements TwitterLikeService,
		Serializable {

	public static InMemoryTwitterLikeService getInstance(Context context,
			TwitterLike api) {
		InMemoryTwitterLikeService result = null;
		try {
			result = SerializeUtil.readObject(ConfigurationUtil
					.getCachedRepository(context));

		} catch (Exception e) {

		}
		if (result == null) {
			result = new InMemoryTwitterLikeService();
		}
		result.api = api;
		result.context = context;
		return result;
	}

	private static final long serialVersionUID = 1L;

	private transient TwitterLike api;

	private transient Context context;

	private Date lastUpdate;

	public Date getLastUpdate() {
		return lastUpdate;
	}

	private UserProfile userProfile;

	public synchronized void syncronize() {
		syncronizeUserProfile();
		this.lastUpdate = new Date();
		updateCache();

	}

	protected void updateCache() {
		try {
			SerializeUtil.writeObject(this,
					ConfigurationUtil.getCachedRepository(context));

		} catch (Exception e) {
			throw new RuntimeException("Error Storing cache", e);
		}
	}

	@Override
	public UserProfile getAuthenticatedUser() {
		return userProfile;
	}

	protected UserProfile syncronizeUserProfile() {
		this.userProfile = api.userOperations().getUserProfile().getContent();
		return this.userProfile;
	}

}
