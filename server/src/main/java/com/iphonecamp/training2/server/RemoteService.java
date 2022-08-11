package com.iphonecamp.training2.server;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.iphonecamp.training2.common.IRemoteService;
import com.iphonecamp.training2.common.UnexpectedNullException;
import com.iphonecamp.training2.common.Util;

import static com.iphonecamp.training2.common.UnexpectedNullException.nonNull;


/**
 * Remote service for controlling airplane mode and tethering state
 */
public class RemoteService extends Service {
    private @Nullable AirplaneMode mAirplaneMode;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Util.LOG_TAG, "Remote service created");

        try {
            mAirplaneMode = new AirplaneMode(this);
        } catch (UnexpectedNullException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Util.LOG_TAG, "Service onBind");
        // Return the interface
        return binder;
    }

    private final IRemoteService.Stub binder = new IRemoteService.Stub() {
        @Override
        public boolean getAirplaneModeEnabled() throws RemoteException {
            Log.d(Util.LOG_TAG, "Getting airplane mode to %b");
            try {
                return mAirplaneMode.get();
            } catch (AirplaneMode.InvalidState e) {
                RemoteException remoteException = new RemoteException("Failed to get airplane mode");
                remoteException.addSuppressed(e);
                throw remoteException;
            }
        }

        @Override
        public void setAirplaneModeEnabled(boolean isOn) throws RemoteException {
            Log.d(Util.LOG_TAG, String.format("Setting airplane mode to %b", isOn));
            try {
                nonNull(mAirplaneMode).set(isOn);
            } catch (UnexpectedNullException e) {
                RemoteException remoteException = new RemoteException("Failed to set airplane mode");
                remoteException.addSuppressed(e);
                throw remoteException;
            }
        }

        @Override
        public boolean getTetheringEnabled() throws RemoteException {
            Log.d(Util.LOG_TAG, "Getting tethering state to %b");
            // TODO
            return false;
        }

        @Override
        public void setTetheringEnabled(boolean isOn) throws RemoteException {
            Log.d(Util.LOG_TAG, String.format("Setting tethering state to %b", isOn));
            // TODO
        }
    };
}

