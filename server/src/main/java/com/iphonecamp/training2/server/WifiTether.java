package com.iphonecamp.training2.server;


import static com.iphonecamp.training2.common.UnexpectedNullException.nonNull;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iphonecamp.training2.common.UnexpectedNullException;
import com.iphonecamp.training2.common.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Wifi tether controller
 */
public class WifiTether {
    private final @NonNull WifiManager mWifiManager;
    private final static String SET_WIFI_AP_ENABLED_METHOD_NAME = "setWifiApEnabled";
    private final static String IS_WIFI_AP_ENABLED_METHOD_NAME = "isWifiApEnabled";

    /**
     * Wifi tether exception
     *
     * This exception is thrown when failed to get or set a valid wifi tether state
     */
    public static class WifiTetherException extends Exception {
        /**
         * Construct from message
         *
         * @param message Detailed error message
         */
        public WifiTetherException(@Nullable String message) {
            super(message);
        }

        /**
         * Construct from message and cause
         *
         * @param message Detailed error message
         * @param cause Suppressed exception
         */
        public WifiTetherException(@Nullable String message, @Nullable Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Constructor
     *
     * @param context Context to use for obtaining a wifi manager system service
     */
    WifiTether(Context context) throws UnexpectedNullException {
        mWifiManager = nonNull(
                (WifiManager) nonNull(context).getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE)
        );
    }


    /**
     * Enable/disable wifi tether
     *
     * @param enabled Whether to enable or disable
     *
     * @throws WifiTetherException If failed to set wifi tether state
     */
    public void setEnabled(boolean enabled) throws WifiTetherException {
        Log.d(Util.LOG_TAG, String.format("Setting wifi tether enabled to %b", enabled));
        try {
            Method setWifiApEnabledMethod = mWifiManager.getClass()
                    .getMethod(SET_WIFI_AP_ENABLED_METHOD_NAME, WifiConfiguration.class, boolean.class);
            setWifiApEnabledMethod.invoke(mWifiManager, null, enabled);
        } catch (NoSuchMethodException |IllegalAccessException | InvocationTargetException e) {
            throw new WifiTetherException("Failed get wifi tether state using reflection", e);
        }
    }


    /**
     *
     * @return Check whether wifi tether is enabled
     * @throws WifiTetherException If failed to obtain a valid wifi tether state
     */
    public boolean isEnabled() throws WifiTetherException {
        Log.d(Util.LOG_TAG, "Getting wifi tether is enabled");
        try {
            Method setWifiApEnabledMethod = mWifiManager.getClass()
                    .getMethod(IS_WIFI_AP_ENABLED_METHOD_NAME);
            boolean enabled = (boolean) nonNull(setWifiApEnabledMethod.invoke(mWifiManager));
            Log.d(Util.LOG_TAG, String.format("Got wifi ap enabled is %b", enabled));
            return enabled;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | UnexpectedNullException e) {
            throw new WifiTetherException("Failed to get wifi tether state", e);
        }
    }
}
