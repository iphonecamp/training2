#!/bin/bash

# This script uninstalls training2 apps from connected device and reboots the device

source ./scripts/common.sh

# Remove server app
adb shell "su -c mount -o rw,remount,rw /system"
adb shell "su -c \"\
    ([ -e SERVER_REMOTE_APK_TMP_PATH ] && rm ${SERVER_REMOTE_APK_TMP_PATH}) &
    pm uninstall --user 0 ${SERVER_PACKAGE_NAME} &
    rm -rf ${SERVER_REMOTE_APP_DIR} &
    \""


# Remove client app
adb uninstall "${CLIENT_PACKAGE_NAME}"

adb reboot