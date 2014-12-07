package it.demo.twitterlike.android.utils;

import java.io.File;

import android.content.Context;

public class ConfigurationUtil {

	public static File getFileStorage(Context context) {
		return context.getFilesDir();
	}

	public static File getOrderFile(Context context, long userId) {
		return new File(getFileStorage(context), "unsyncronizedElements"
				+ userId + ".raw");
	}

	public static File getCachedRepository(Context context) {
		return new File(getFileStorage(context), "cachedRepository.raw");
	}

}
