package it.demo.twitterlike.android.ui.utils;

import java.lang.reflect.Field;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings.Secure;

public class DeviceUtils {

	private DeviceUtils() {

	}

	public static final String WLAN_PARAMETER = "wlan0";

	public static final String EHTERNET_PARAMETER = "eth0";

	public static String getDeviceId(Context context) {
		return Secure
				.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	/**
	 * Checks whether there's a network connection
	 * 
	 * @param context
	 *            Context to use
	 * @return true if there's an active network connection, false otherwise
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isConnectedOrConnecting();
	}

	public static String getAndroidVersion() {
		StringBuilder builder = new StringBuilder();
		builder.append("android : ").append(Build.VERSION.RELEASE);

		Field[] fields = Build.VERSION_CODES.class.getFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			int fieldValue = -1;

			try {
				fieldValue = field.getInt(new Object());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			if (fieldValue == Build.VERSION.SDK_INT) {
				builder.append(" : ").append(fieldName).append(" : ");
				builder.append("sdk=").append(fieldValue);
			}
		}
		return builder.toString();
	}

	public static String getApplicationVersion(Context context) {
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pInfo.versionName;
		} catch (Exception e) {

		}
		return "";
	}

	public static String getEthernetMACAddress() {
		return getMACAddress(EHTERNET_PARAMETER);
	}

	public static String getWIFIAddress() {
		return getMACAddress(WLAN_PARAMETER);
	}

	/**
	 * Returns MAC address of the given interface name.
	 * 
	 * @param interfaceName
	 *            eth0, wlan0 or NULL=use first interface
	 * @return mac address or empty string
	 */
	public static String getMACAddress(String interfaceName) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName)) {
						continue;
					}
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac == null) {
					return "";
				}
				StringBuilder buf = new StringBuilder();
				for (int idx = 0; idx < mac.length; idx++) {
					buf.append(String.format("%02X:", mac[idx]));
				}
				if (buf.length() > 0) {
					buf.deleteCharAt(buf.length() - 1);
				}
				return buf.toString();
			}
		} catch (Exception ex) {
		} // ignore exceptions

		return "";
	}



}
