package com.iphonecamp.training2.server;

import android.content.Context;
import android.provider.Settings;

import java.nio.charset.StandardCharsets;


public class AirplaneMode {
    private static final int INVALID_VALUE = -1;
    private static final int OFF_VALUE = 0;
    private static final int ON_VALUE = 1;

    public static class InvalidState extends Exception {
        public InvalidState(int value) {
            super(String.format(StandardCharsets.UTF_8.name(), "Invalid state value %d", value));
        }

        public InvalidState() {
            super("Cannot use invalid state");
        }
    }


    public enum State {
        INVALID(INVALID_VALUE), OFF(OFF_VALUE), ON(ON_VALUE);

        private final int mValue;

        private State(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }

        public static State fromValue(int value) throws InvalidState {
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

        public static State fromBool(boolean isOn) {
            if (isOn) {
                return ON;
            }

            return OFF;
        }

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

        private static final int invalidValue = -1;
    }


    public static boolean get(Context context) throws InvalidState {
        int value = Settings.Global.getInt(
                context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON,
                State.INVALID.getValue()
        );

        return State.fromValue(value).toBool();
    }

    public static void set(Context context, boolean isOn) {

        Settings.Global.putInt(
                context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON,
                State.fromBool(isOn).getValue()
        );
    }
}
