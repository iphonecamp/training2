package com.iphonecamp.training2.server;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iphonecamp.training2.common.IRemoteService;
import com.iphonecamp.training2.common.UnexpectedNullException;
import com.iphonecamp.training2.common.Util;

import static com.iphonecamp.training2.common.UnexpectedNullException.nonNull;
import static com.iphonecamp.training2.common.Util.withCause;


/**
 * Remote service for controlling airplane mode and wifi tether
 */
public class RemoteService extends Service {
    private @Nullable AirplaneMode mAirplaneMode;
    private @Nullable WifiTether mWifiTether;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Util.LOG_TAG, "Remote service created");

        try {
            mAirplaneMode = new AirplaneMode(getApplicationContext());
            mWifiTether = new WifiTether(getApplicationContext());
        } catch (UnexpectedNullException e) {
            Log.e(Util.LOG_TAG, "Failed to create remote service", e);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Util.LOG_TAG, "Service onBind");
        // Return the interface
        return mBinder;
    }

    private final @NonNull IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        @Override
        public boolean isAirplaneModeEnabled() throws RemoteException {
            Log.d(Util.LOG_TAG, "Getting airplane mode enabled");
            try {
                return nonNull(mAirplaneMode).isEnabled();
            } catch (AirplaneMode.AirplaneModeException | UnexpectedNullException e) {
                throw withCause(new RemoteException("Failed to get airplane mode"), e);
            }
        }

        @Override
        public void setAirplaneModeEnabled(boolean enabled) throws RemoteException {
            Log.d(Util.LOG_TAG, String.format("Setting airplane mode enabled to %b", enabled));
            try {
                nonNull(mAirplaneMode).setEnabled(enabled);
            } catch (UnexpectedNullException e) {
                throw withCause(new RemoteException(String.format("Failed to set airplane mode enabled to %b", enabled)), e);
            }
        }

        @Override
        public boolean isWifiTetherEnabled() throws RemoteException {
            Log.d(Util.LOG_TAG, "Getting wifi tether enabled");
            try {
                return nonNull(mWifiTether).isEnabled();
            } catch (UnexpectedNullException | WifiTether.WifiTetherException e) {
                throw withCause(new RemoteException("Failed to get wifi tether state"), e);
            }
        }

        @Override
        public void setWifiTetherEnabled(boolean enabled) throws RemoteException {
            Log.d(Util.LOG_TAG, String.format("Setting wifi tether enabled to %b", enabled));
            try {
                mWifiTether.setEnabled(enabled);
            } catch (WifiTether.WifiTetherException e) {
                throw withCause(new RemoteException(String.format("Failed to set wifi tether state to %b", enabled)), e);
            }
        }
    };
}

