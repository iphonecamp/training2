package com.iphonecamp.training2.server;

import android.content.Context;
import android.provider.Settings;

import java.nio.charset.StandardCharsets;

import static com.iphonecamp.training2.common.UnexpectedNullException.nonNull;

import androidx.annotation.NonNull;

import com.iphonecamp.training2.common.UnexpectedNullException;


/**
 * Airplane mode getter and setter functions
 */
public class AirplaneMode {
    private static final int INVALID_VALUE = -1;
    private static final int OFF_VALUE = 0;
    private static final int ON_VALUE = 1;
    private final @NonNull Context mContext;

    AirplaneMode(@NonNull Context context) throws UnexpectedNullException {
        mContext = nonNull(context);
    }

    /**
     * Invalid state integer value exception
     *
     * This exception is thrown when trying to covert the State enum from or to an invalid value.
     */
    public static class InvalidState extends Exception {
        /**
         * Constructor from an invalid integer
         *
         * Thrown when cannot convert an int to enum.
         *
         * @param value A value which cannot be converted to a State enum
         */
        public InvalidState(int value) {
            super(String.format(StandardCharsets.UTF_8.name(), "Invalid state value %d", value));
        }

        /**
         * Default constructor
         *
         * Thrown when cannot convert enum to boolean (INVALID_VALUE).
         */
        public InvalidState() {
            super("Cannot use invalid state");
        }
    }


    /**
     * Possible airplane mode states
     */
    public enum State {
        INVALID(INVALID_VALUE), OFF(OFF_VALUE), ON(ON_VALUE);

        private final int mValue;

        /**
         * Construct from an integer value
         * @param value An integer value corresponding to enum
         */
        private State(int value) {
            mValue = value;
        }

        /**
         * Get integer value corresponding to enum
         *
         * @return A number that corresponds to state enum
         */
        public int getValue() {
            return mValue;
        }

        /**
         * Convert a number to an enum
         *
         * @param value A number to convert to enum
         * @return An enum corresponding to given number
         * @throws InvalidState if given number doesn't correspond to any enum value
         */
        public static @NonNull State fromValue(int value) throws InvalidState {
            switch(value) {
                case INVALID_VALUE:
                    return INVALID;
                case OFF_VALUE:
                    return OFF;
                case ON_VALUE:
                    return ON;
                default:
                    throw new InvalidState(value);
            }
        }

        /**
         * Convert boolean to enum
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
         * Convert enum to boolean
         *
         * @return true if enum is ON_VALUE, false if enum is OFF_VALUE
         * @throws InvalidState if enum is not VALUE_ON or VALUE_OFF
         */
        public boolean toBool() throws InvalidState {
            switch (this) {
                case OFF:
                    return false;
                case ON:
                    return true;
                default:
                    throw new InvalidState();
            }
        }
    }

    /**
     * Get airplane state
     *
     * @return true if airplane mode is ON, false if airplane mode is OFF
     * @throws InvalidState if cannot get airplane mode state
     */
    public boolean get() throws InvalidState {
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
     * @param isOn Whether to set airplane mode to ON (or OFF)
     */
    public void set(boolean isOn) {

        Settings.Global.putInt(
                mContext.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON,
                State.fromBool(isOn).getValue()
        );
    }
}
