package com.kircherelectronics.accelerationexplorer.prefs;

/*
 * AccelerationExplorer
 * Copyright 2018 Kircher Electronics, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;

import com.kircherelectronics.accelerationexplorer.activity.config.FilterConfigActivity;

public class PrefUtils
{
	public static boolean getPrefAndroidLinearAccelerationEnabled(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(FilterConfigActivity.ANDROID_LINEAR_ACCEL_ENABLED_KEY, false);
	}

	public static boolean getPrefFSensorLpfLinearAccelerationEnabled(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(FilterConfigActivity.FSENSOR_LPF_LINEAR_ACCEL_ENABLED_KEY, false);
	}

    public static float getPrefFSensorLpfLinearAccelerationTimeConstant(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Float.parseFloat(prefs.getString(FilterConfigActivity.FSENSOR_LPF_LINEAR_ACCEL_TIME_CONSTANT_KEY, String.valueOf(0.5f)));
    }

    public static boolean getPrefFSensorComplimentaryLinearAccelerationEnabled(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(FilterConfigActivity.FSENSOR_COMPLIMENTARY_LINEAR_ACCEL_ENABLED_KEY, false);
    }

    public static float getPrefFSensorComplimentaryLinearAccelerationTimeConstant(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Float.parseFloat(prefs.getString(FilterConfigActivity.FSENSOR_COMPLIMENTARY_LINEAR_ACCEL_TIME_CONSTANT_KEY, String.valueOf(0.5f)));
    }

    public static boolean getPrefFSensorKalmanLinearAccelerationEnabled(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(FilterConfigActivity.FSENSOR_KALMAN_LINEAR_ACCEL_ENABLED_KEY, false);
    }

    public static boolean getPrefLpfSmoothingEnabled(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(FilterConfigActivity.LPF_SMOOTHING_ENABLED_KEY, false);
	}

    public static float getPrefLpfSmoothingTimeConstant(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return Float.parseFloat(prefs.getString(FilterConfigActivity.LPF_SMOOTHING_TIME_CONSTANT_KEY, String.valueOf(0.5f)));
	}

    public static boolean getPrefMeanFilterSmoothingEnabled(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(FilterConfigActivity.MEAN_FILTER_SMOOTHING_ENABLED_KEY, false);
	}

    public static float getPrefMeanFilterSmoothingTimeConstant(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return Float.parseFloat(prefs.getString(FilterConfigActivity.MEAN_FILTER_SMOOTHING_TIME_CONSTANT_KEY, String.valueOf(0.5f)));
	}

    public static boolean getPrefMedianFilterSmoothingEnabled(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(FilterConfigActivity.MEDIAN_FILTER_SMOOTHING_ENABLED_KEY, false);
	}

    public static float getPrefMedianFilterSmoothingTimeConstant(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return Float.parseFloat(prefs.getString(FilterConfigActivity.MEDIAN_FILTER_SMOOTHING_TIME_CONSTANT_KEY, String.valueOf(0.5f)));
	}

    public static int getSensorFrequencyPrefs(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return Integer.parseInt(prefs.getString(FilterConfigActivity.SENSOR_FREQUENCY_KEY, String.valueOf(SensorManager.SENSOR_DELAY_FASTEST)));
	}
}
