#!/bin/bash

# This script uninstalls training 2 apps from connected device

source ./scripts/common.sh

adb shell "su -c \"\
    mount -o rw,remount,rw /system &&
    [ -e ${SERVER_REMOTE_APK_TMP_PATH} ] && rm ${SERVER_REMOTE_APK_TMP_PATH}
    [ -d ${SERVER_REMOTE_APP_DIR} ] || rm -rf ${SERVER_REMOTE_APP_DIR}
    \""


adb uninstall "${CLIENT_PACKAGE_NAME}"