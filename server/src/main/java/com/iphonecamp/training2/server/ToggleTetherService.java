package com.iphonecamp.training2.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
//import android.telephony.Phone

import android.provider.Settings;

public class ToggleTetherService extends Service {
    public ToggleTetherService() {
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        Log.i("TRN", "On start command");

        Settings.Global.putInt(this.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 1);
//        try {
//            // Get the ALL user handle
//            Class userHandleCls = Class.forName("android.os.UserHandle");
//
//            Field allField = userHandleCls.getDeclaredField("ALL");
//            UserHandle allUserHandle = (UserHandle) allField.get(null);
//
//            Field currentField = userHandleCls.getDeclaredField("CURRENT");
//            UserHandle currentUserHandle = (UserHandle) currentField.get(null);
//
//            Field ownerField = userHandleCls.getDeclaredField("OWNER");
//            UserHandle ownerUserHandle = (UserHandle) ownerField.get(null);
//
//            // Post the intent
//            Intent intentToSend = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//            intent.putExtra("state", false);
//            getApplicationContext().sendBroadcastAsUser(intent, ownerUserHandle);
//
//        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//            return START_NOT_STICKY;
//
//        }

        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}