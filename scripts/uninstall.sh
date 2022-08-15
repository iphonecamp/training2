#!/bin/bash

# This script uninstalls training 2 apps from connected device

source ./scripts/common.sh

adb shell "su -c \"\
    mount -o rw,remount,rw /system &
    rm ${SERVER_REMOTE_APK_TMP_PATH} &
    rm -rf ${SERVER_REMOTE_APP_DIR} &
    pm uninstall --user 0 ${SERVER_PACKAGE_NAME}
    \""


adb uninstall "${CLIENT_PACKAGE_NAME}"