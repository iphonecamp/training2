package com.iphonecamp.training2.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import com.iphonecamp.training2.common.UnexpectedNullException;
import com.iphonecamp.training2.common.Util;

import static com.iphonecamp.training2.common.UnexpectedNullException.nonNull;


/**
 * Activity for controlling airplane mode and tethering via a remote service
 */
public class MainActivity extends AppCompatActivity {
    private static final @NonNull String MISC_FATAL_ERROR_MESSAGE = "Something went wrong, please restart the app";
    private static final @NonNull String ERROR_ALERT_TITLE = "Oops...";
    private static final @NonNull String OK_TEXT = "OK";
    private @Nullable IRemoteService mService;
    private @Nullable Switch mAirplaneSwitch;
    private @Nullable Switch mTetheringSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Util.LOG_TAG, "Main activity onCreate");
        setContentView(R.layout.activity_main);

        // Initialize switch variables
        mAirplaneSwitch = (Switch) findViewById(R.id.swAirplane);
        mTetheringSwitch = (Switch) findViewById(R.id.swTethering);

        // Watch for airplane mode switch changes.
        try {
            nonNull(mAirplaneSwitch).setOnCheckedChangeListener((view, isChecked) -> {
                Log.d(Util.LOG_TAG, String.format("Airplane switch changed to %b", isChecked));
                try {
                    nonNull(mService).setAirplaneModeEnabled(isChecked);
                } catch (RemoteException | UnexpectedNullException e) {
                    handleException(e);
                }
            });
        } catch (UnexpectedNullException e) {
            handleException(e);
        }

        // Watch for tethering mode switch changes.
        try {
            nonNull(mTetheringSwitch).setOnCheckedChangeListener((view, isChecked) -> {
                Log.d(Util.LOG_TAG, String.format("Tethering switch changed to %b", isChecked));
                try {
                    nonNull(mService).setTetheringEnabled(isChecked);
                } catch (RemoteException | UnexpectedNullException e) {
                    handleException(e);
                }
            });
        } catch (UnexpectedNullException e) {
            handleException(e);
        }
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
                // Initialize switches state
                nonNull(mAirplaneSwitch).setChecked(mService.getAirplaneModeEnabled());
                nonNull(mTetheringSwitch).setChecked(mService.getTetheringEnabled());

                // Enable switches
                nonNull(mAirplaneSwitch).setEnabled(true);
                nonNull(mTetheringSwitch).setEnabled(true);
            } catch (RemoteException | UnexpectedNullException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(Util.LOG_TAG, "Main activity service disconnected");
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;

            // Disable switch
            try {
                nonNull(mAirplaneSwitch).setEnabled(false);
                nonNull(mTetheringSwitch).setEnabled(false);
            } catch (UnexpectedNullException e) {
                handleException(e);
            }
        }
    };

    /**
     * Show an error alert dialog with a custom message
     *
     * @param message A message to display to the user
     */
    private void showErrorAlert(@NonNull String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(ERROR_ALERT_TITLE);
        alertDialog.setMessage(message);
        alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL,
                OK_TEXT,
                (dialog, which) -> dialog.dismiss()
        );
        alertDialog.show();
    }


    /**
     * Show a default error alert dialog
     */
    private void showErrorAlert() {
        showErrorAlert(MISC_FATAL_ERROR_MESSAGE);
    }


    /**
     * Handle an exception
     *
     * Print stack trace and show an alert to the user
     *
     * @param exc Occurred exception to handle
     * @param message A descriptive message to display to the user
     */
    public void handleException(@Nullable Exception exc, @Nullable String message) {
        Log.e(Util.LOG_TAG, "Caught an exception");
        if (null != exc) {
            exc.printStackTrace();
        }

        if (null == message) {
            this.showErrorAlert();
        } else {
            this.showErrorAlert(message);
        }
    }

    /**
     * Handle an exception with default message
     *
     * Print stack trace and show an alert to the user
     *
     * @param exc Occurred exception to handle
     */
    public void handleException(@NonNull Exception exc) {
        handleException(exc, null);
    }
}