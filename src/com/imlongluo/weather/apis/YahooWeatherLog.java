package com.imlongluo.weather.apis;

import android.util.Log;

public class YahooWeatherLog {
	public static final String TAG = "YahooWeather";
	public static boolean isDebuggable = true;

	public static void setDebuggable(final boolean isDebuggable) {
		YahooWeatherLog.isDebuggable = isDebuggable;
	}

	public static void d(final String tag, final String message) {
		if (!isDebuggable) {
			return;
		}

		Log.d(tag, message);
	}

	public static void d(final String message) {
		if (!isDebuggable) {
			return;
		}

		Log.d(TAG, message);
	}

	public static void printStack(final Exception e) {
		if (!isDebuggable) {
			return;
		}
		e.printStackTrace();
	}

}
