package com.iphonecamp.training2.server;

import android.content.Context;
import android.provider.Settings;

import java.nio.charset.StandardCharsets;

import static com.iphonecamp.training2.common.UnexpectedNullException.nonNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iphonecamp.training2.common.UnexpectedNullException;


/**
 * Airplane mode controller
 */
public class AirplaneMode {
    private static final int STATE_INVALID_VALUE = -1;
    private static final int STATE_OFF_VALUE = 0;
    private static final int STATE_ON_VALUE = 1;
    private final @NonNull Context mContext;

    AirplaneMode(@NonNull Context context) throws UnexpectedNullException {
        mContext = nonNull(context);
    }

    /**
     * Invalid state integer value exception
     *
     * This exception is thrown when failed to make a conversion between state enum and a different type.
     */
    public static class AirplaneModeException extends Exception {
        /**
         * Construct from message
         *
         * @param message Detailed error message
         */
        public AirplaneModeException(@Nullable String message) {
            super(message);
        }

        /**
         * Construct from message and cause
         *
         * @param message Detailed error message
         * @param cause Suppressed exception
         */
        public AirplaneModeException(@Nullable String message, @Nullable Throwable cause) {
            super(message, cause);
        }
    }


    /**
     * Airplane mode state
     */
    private enum State {
        INVALID(STATE_INVALID_VALUE), OFF(STATE_OFF_VALUE), ON(STATE_ON_VALUE);

        private final int mValue;

        /**
         * Construct from an integer value
         *
         * @param value An integer value corresponding to an airplane mode state
         */
        State(int value) {
            mValue = value;
        }

        /**
         * Get integer value corresponding to airplane mode state enum
         *
         * @return A number that corresponds to state enum
         */
        private int getValue() {
            return mValue;
        }

        /**
         * Get an airplane mode state from an integer value
         *
         * @param value A number to convert to enum
         * @return An enum corresponding to given number
         * @throws AirplaneModeException if given number doesn't correspond to any enum value
         */
        public static @NonNull State fromValue(int value) throws AirplaneModeException {
            switch(value) {
                case STATE_INVALID_VALUE:
                    return INVALID;
                case STATE_OFF_VALUE:
                    return OFF;
                case STATE_ON_VALUE:
                    return ON;
                default:
                    throw new AirplaneModeException(String.format("%d does not correspond to an airplane mode state", value));
            }
        }

        /**
         * Get an airplane mode state from a boolean
         *
         * @param isOn Whether airplane mode is ON
         * @return ON_VALUE if true is given, OFF_VALUE otherwise
         */
        public static @NonNull State fromBool(boolean isOn) {
            if (isOn) {
                return ON;
            }

            return OFF;
        }

        /**
         * Convert state enum to boolean
         *
         * @return true if airplane mode is ON, false if airplane mode is OFF
         * @throws AirplaneModeException if enum is not ON or OFF
         */
        public boolean toBool() throws AirplaneModeException {
            switch (this) {
                case OFF:
                    return false;
                case ON:
                    return true;
                default:
                    throw new AirplaneModeException(String.format("Cannot convert airplane mode state %s to a boolean", this));
            }
        }
    }

    /**
     * Get airplane mode state
     *
     * @return true if airplane mode is ON, false if airplane mode is OFF
     * @throws AirplaneModeException if cannot get airplane mode state
     */
    public boolean isEnabled() throws AirplaneModeException {
        int value = Settings.Global.getInt(
                mContext.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON,
                State.INVALID.getValue()
        );

        return State.fromValue(value).toBool();
    }

    /**
     * Set airplane mode state
     *
     * @param enabled Whether to set airplane mode to ON (or OFF)
     */
    public void setEnabled(boolean enabled) {

        Settings.Global.putInt(
                mContext.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON,
                State.fromBool(enabled).getValue()
        );
    }
}
