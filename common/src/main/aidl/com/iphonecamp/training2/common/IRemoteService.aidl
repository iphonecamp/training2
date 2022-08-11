package com.iphonecamp.training2.common;


// Remote service for controlling airplane mode and tethering mode
interface IRemoteService {
    // Get airplane mode
    boolean getAirplaneModeEnabled();

    // Set airplane mode
    void setAirplaneModeEnabled(boolean enabled);

    // Get tethering state
    boolean getTetheringEnabled();

    // Set tethering state
    void setTetheringEnabled(boolean enabled);
}