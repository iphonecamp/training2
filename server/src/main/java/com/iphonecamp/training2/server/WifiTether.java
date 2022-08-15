package com.iphonecamp.training2.server;


import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.iphonecamp.training2.common.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class WifiTether {
    private final @NonNull Context mContext;
    private final @NonNull WifiManager mWifiManager;

    /**
     * Invalid state integer value exception
     *
     * This exception is thrown when trying to covert the State enum from or to an invalid value.
     */
    public static class InvalidState extends Exception {
        /**
         * Constructor from an invalid integer
         *
         * Thrown when cannot convert an int to enum.
         *
         * @param value A value which cannot be converted to a State enum
         */
        public InvalidState(int value) {
            super(String.format(StandardCharsets.UTF_8.name(), "Invalid state value %d", value));
        }

        public InvalidState() {
            super("Could not get state");
        }
    }

    WifiTether(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) mContext.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
    }

    public void setEnabled(boolean enabled) {
        Log.d(Util.LOG_TAG, String.format("Setting WiFi tether enabled to %b", enabled));
        try {
            Method setWifiApEnabledMethod = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            Log.d(Util.LOG_TAG, "Got method");
            setWifiApEnabledMethod.invoke(mWifiManager, null, enabled);
            Log.d(Util.LOG_TAG, "Invoked method");
        } catch (NoSuchMethodException e) {
            Log.e(Util.LOG_TAG, "Failed to get method", e);
            e.printStackTrace();
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.e(Util.LOG_TAG, "Failed to invoke method", e);
            e.printStackTrace();
        }
    }

    public boolean getEnabled() throws InvalidState {
        Log.d(Util.LOG_TAG, "Getting WiFi tether enabled");
        try {
            Method setWifiApEnabledMethod = mWifiManager.getClass().getMethod("isWifiApEnabled");
            Log.d(Util.LOG_TAG, "Got method");
            boolean enabled = (boolean) setWifiApEnabledMethod.invoke(mWifiManager);
            Log.d(Util.LOG_TAG, String.format("Invoked method; wifi ap enables is %b", enabled));
            return enabled;
        } catch (NoSuchMethodException e) {
            Log.e(Util.LOG_TAG, "Failed to get method", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.e(Util.LOG_TAG, "Failed to invoke method", e);
        }

        throw new InvalidState();
    }
}
