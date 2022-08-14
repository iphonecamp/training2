# Exit on error
set -e

# Server app variables
SERVER_REMOTE_APP_NAME="training2"
SERVER_REMOTE_APP_DIR="/system/priv_app/${SERVER_REMOTE_APP_NAME}"
SERVER_REMOTE_APK_FILENAME="${SERVER_REMOTE_APP_NAME}.apk"
SERVER_REMOTE_APK_PATH="${SERVER_REMOTE_APP_DIR}/${SERVER_REMOTE_APK_FILENAME}"
SERVER_REMOTE_APK_TMP_PATH="/data/local/tmp/${SERVER_REMOTE_APK_FILENAME}"
SERVER_LOCAL_APK_PATH="./server/build/outputs/apk/debug/server-debug.apk"


# Client app variables
CLIENT_LOCAL_APK_PATH="./client/build/outputs/apk/debug/client-debug.apk"
CLIENT_PACKAGE_NAME="com.iphonecamp.training2.client"