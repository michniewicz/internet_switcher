package com.rubchick.internetswitcher.networkdata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.rubchick.internetswitcher.Main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

// class for mobile network operations

public class MobileData {

	public static boolean isMobileDataEnabled(Activity mainActivity) {
		try {

			ConnectivityManager nInfo = (ConnectivityManager) mainActivity
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			nInfo.getActiveNetworkInfo().isConnectedOrConnecting();

			Log.d("mobile", "Net avail:"
					+ nInfo.getActiveNetworkInfo().isConnectedOrConnecting());

			ConnectivityManager cm = (ConnectivityManager) mainActivity
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				if (netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
					return true;
				}
			} else {
				Log.d("mobile", "Network available:false");
				return false;
			}

		} catch (Exception e) {
			return false;
		}

		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setMobileDataEnabled(Context context, boolean enabled)
			throws Exception {
		final ConnectivityManager conman = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final Class conmanClass = Class.forName(conman.getClass().getName());
		final Field iConnectivityManagerField = conmanClass
				.getDeclaredField("mService");
		iConnectivityManagerField.setAccessible(true);
		final Object iConnectivityManager = iConnectivityManagerField
				.get(conman);
		final Class iConnectivityManagerClass = Class
				.forName(iConnectivityManager.getClass().getName());
		final Method setMobileDataEnabledMethod = iConnectivityManagerClass
				.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);

		setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}
}
