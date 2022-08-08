package com.iphonecamp.training2.server;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
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
        public int getPid(){
            return Process.myPid();
        }
        public void basicTypes(int anInt, long aLong, boolean aBoolean,
                               float aFloat, double aDouble, String aString) {
            // Does nothing
        }
    };
}

