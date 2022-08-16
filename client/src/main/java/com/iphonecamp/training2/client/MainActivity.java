package com.iphonecamp.training2.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
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
    private @Nullable IRemoteService mService;
    private @Nullable Switch mAirplaneSwitch;
    private @Nullable Switch mTetheringSwitch;
    private @Nullable String mMiscFatalErrorMessage;
    private @Nullable String mErrorAlertTitle;
    private @Nullable String mOkText;
    private final @NonNull String REMOTE_SERVICE_PACKAGE_NAME = "com.iphonecamp.training2.server";
    private final @NonNull String REMOTE_SERVICE_CLASS_NAME = "com.iphonecamp.training2.server.RemoteService";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Util.LOG_TAG, "Main activity onCreate");
        setContentView(R.layout.activity_main);

        // Initialize switch variables
        mAirplaneSwitch = (Switch) findViewById(R.id.swAirplane);
        mTetheringSwitch = (Switch) findViewById(R.id.swTethering);

        try {
            initStrings();

            // Watch for airplane mode switch changes.
            nonNull(mAirplaneSwitch).setOnCheckedChangeListener((view, isChecked) -> {
                Log.d(Util.LOG_TAG, String.format("Airplane switch changed to %b", isChecked));
                try {
                    nonNull(mService).setAirplaneModeEnabled(isChecked);
                } catch (RemoteException | UnexpectedNullException e) {
                    handleException(e);
                }
            });

            // Watch for tethering mode switch changes.
            nonNull(mTetheringSwitch).setOnCheckedChangeListener((view, isChecked) -> {
                Log.d(Util.LOG_TAG, String.format("Tethering switch changed to %b", isChecked));
                try {
                    nonNull(mService).setWifiTetherEnabled(isChecked);
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
        intent.setComponent(new ComponentName(REMOTE_SERVICE_PACKAGE_NAME, REMOTE_SERVICE_CLASS_NAME));
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Note: this callback is always called from the main thread

            Log.d(Util.LOG_TAG, "Main activity service connected");
            try {
                mService = nonNull(IRemoteService.Stub.asInterface(service));

                // Initialize switches state
                nonNull(mAirplaneSwitch).setChecked(mService.isAirplaneModeEnabled());
                nonNull(mTetheringSwitch).setChecked(mService.isWifiTetherEnabled());

                // Enable switches
                nonNull(mAirplaneSwitch).setEnabled(true);
                nonNull(mTetheringSwitch).setEnabled(true);
            } catch (RemoteException | UnexpectedNullException e) {
                handleException(e);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // Note: this callback is always called from the main thread

            Log.d(Util.LOG_TAG, "Main activity service disconnected");
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;

            // Disable switches
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
    private void showErrorAlert(@Nullable String message) throws UnexpectedNullException {
        @NonNull AlertDialog alertDialog = nonNull(new AlertDialog.Builder(this).create());
        alertDialog.setTitle(mErrorAlertTitle);
        alertDialog.setMessage(message);
        alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL,
                mOkText,
                (dialog, which) -> { if (null != dialog) dialog.dismiss(); }
        );
        alertDialog.show();
    }


    /**
     * Show a default error alert dialog
     */
    private void showErrorAlert() throws UnexpectedNullException {
        showErrorAlert(mMiscFatalErrorMessage);
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
        // Log exception
        if (null == exc) {
            Log.e(Util.LOG_TAG, "Caught a null exception");
        } else {
            Log.e(Util.LOG_TAG, "Caught an exception", exc);
        }

        // Show an error alert
        try {
            if (null == message) {
                this.showErrorAlert();
            } else {
                this.showErrorAlert(message);
            }
        } catch (UnexpectedNullException e) {
            Log.e(Util.LOG_TAG, "Failed to show an error alert", e);
            // Nothing else to do
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


    /**
     * Initialize strings from app resources
     *
     * @throws UnexpectedNullException One or more of the needed strings are null
     */
    private void initStrings() throws UnexpectedNullException {
        Resources resources = getResources();
        if (null == resources) {
            Log.e(Util.LOG_TAG, "Failed to get app resources");
            return;
        }

        mMiscFatalErrorMessage = nonNull(resources.getString(R.string.misc_fatal_error_message));
        mErrorAlertTitle = nonNull(resources.getString(R.string.error_alert_title));
        mOkText = nonNull(resources.getString(R.string.ok_text));
    }
}