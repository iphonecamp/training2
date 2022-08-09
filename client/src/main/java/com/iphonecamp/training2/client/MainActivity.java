package com.iphonecamp.training2.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Switch;

import com.iphonecamp.training2.common.IRemoteService;
import com.iphonecamp.training2.common.Util;


public class MainActivity extends AppCompatActivity {
    private IRemoteService mService = null;
    private boolean isBound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Util.LOG_TAG, "Main activity onCreate");
        setContentView(R.layout.activity_main);

        // Watch for button clicks.
        Switch airplaneSwitch = (Switch) findViewById(R.id.swAirplane);
        airplaneSwitch.setOnCheckedChangeListener((view, isChecked) -> {
            Log.d(Util.LOG_TAG, "Airplane switch changed");
            try {
                Log.i(Util.LOG_TAG, String.format("Got pid=%d", mService.getPid()));
                mService.setAirplaneMode(isChecked);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

//        button.setOnClickListener(mBindListener);
//        button = (Button)findViewById(R.id.unbind);
//        button.setOnClickListener(unbindListener);
//        killButton = (Button)findViewById(R.id.kill);
//        killButton.setOnClickListener(killListener);
//        killButton.setEnabled(false);

    }

    @Override
    protected void onStart () {
        super.onStart();
        Log.d(Util.LOG_TAG, "Main activity onStart");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.iphonecamp.training2.server", "com.iphonecamp.training2.server.RemoteService"));
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(Util.LOG_TAG, "Main activity service connected");
            mService = IRemoteService.Stub.asInterface(service);

            try {
                Log.i(Util.LOG_TAG, String.format("Got pid=%d", mService.getPid()));

                Switch airplaneSwitch = (Switch) findViewById(R.id.swAirplane);
                airplaneSwitch.setChecked(mService.getAirplaneMode());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(Util.LOG_TAG, "Main activity service disconnected");
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
        }
    };

}