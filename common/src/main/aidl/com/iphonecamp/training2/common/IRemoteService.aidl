package com.iphonecamp.training2.common;


// Remote service for controlling airplane mode and tethering mode
interface IRemoteService {
    // Check whether airplane mode is enabled
    boolean isAirplaneModeEnabled();

    // Enable/disable airplane mode
    void setAirplaneModeEnabled(boolean enabled);

    // Check whether wifi tether is enabled
    boolean isWifiTetherEnabled();

    // Enable/disable wifi tether
    void setWifiTetherEnabled(boolean enabled);
}