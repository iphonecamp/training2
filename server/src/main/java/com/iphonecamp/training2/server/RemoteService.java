package com.iphonecamp.training2.server;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import com.iphonecamp.training2.common.IRemoteService;
import com.iphonecamp.training2.common.Util;


public class RemoteService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Util.LOG_TAG, "Remote service created");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Util.LOG_TAG, "Service onBind");
        // Return the interface
        return binder;
    }

    private final IRemoteService.Stub binder = new IRemoteService.Stub() {
        @Override
        public int getPid(){
            return Process.myPid();
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean,
                               float aFloat, double aDouble, String aString) {
            // Does nothing
        }

        @Override
        public boolean getAirplaneMode() throws RemoteException {
            try {
                return AirplaneMode.get(RemoteService.this);
            } catch (AirplaneMode.InvalidState e) {
                throw new RemoteException(String.format("Could not get airplane mode: %s", e.getMessage()));
            }
        }

        @Override
        public void setAirplaneMode(boolean isOn) {
            Log.i(Util.LOG_TAG, String.format("Setting airplane mode to %b", isOn));
            AirplaneMode.set(RemoteService.this, isOn);
        }


    };
}

