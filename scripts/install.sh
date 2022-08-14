#!/bin/bash

# This script installs training2 apps on connected device.
#
# Server app is installed app as a privileged app
# Steps:
#   1. Remount '/system' as writable
#   2. Push the apk into '/system/priv_app/training2/training.apk
#   3. Set appropriate permissions both to the apk and to the directory it's placed in
#
# Client app is installed normally

source ./scripts/common.sh

# Upload apk into a tmp dir
adb push "${SERVER_LOCAL_APK_PATH}" "${SERVER_REMOTE_APK_TMP_PATH}"

# Place the apk in priv_app dir
adb shell "su -c \"\
    mount -o rw,remount,rw /system &&
    [ -d ${SERVER_REMOTE_APP_DIR} ] || mkdir ${SERVER_REMOTE_APP_DIR} &&
    chmod 755 ${SERVER_REMOTE_APP_DIR}
    mv ${SERVER_REMOTE_APK_TMP_PATH} ${SERVER_REMOTE_APK_PATH} &&
    chmod 644 ${SERVER_REMOTE_APK_PATH}
    \""

adb install -t "${CLIENT_LOCAL_APK_PATH}"