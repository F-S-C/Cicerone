package com.fsc.cicerone.notifications;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.fsc.cicerone.view.UserMainActivity;
import com.fsc.cicerone.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private static final class Keys {
        static final String DATA = "data";
        static final String MESSAGE = "message";
        static final String TITLE = "title";
        static final String IS_BACKGROUND = "is_background";
        static final String IMAGE = "image";
        static final String TIMESTAMP = "timestamp";
        static final String PAYLOAD = "payload";
        static final String PAYLOAD_MESSAGE_TYPE = "message-type";

        @NonNull
        static String PAYLOAD_PARAM(final int i) {
            return "param-" + i;
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra(Keys.MESSAGE, message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
        // If the app is in background, firebase itself handles the notification
    }

    private void handleDataMessage(JSONObject json) {
        try {
            JSONObject data = json.getJSONObject(Keys.DATA);

            String title = data.getString(Keys.TITLE);
            String message = data.getString(Keys.MESSAGE);
            String imageUrl = data.getString(Keys.IMAGE);
            String timestamp = data.getString(Keys.TIMESTAMP);
            // boolean isBackground = data.getBoolean(Keys.IS_BACKGROUND);

            JSONObject payload = data.getJSONObject(Keys.PAYLOAD);
            if (payload.has(Keys.PAYLOAD_MESSAGE_TYPE)) {
                try {
                    switch (payload.getString(Keys.PAYLOAD_MESSAGE_TYPE)) {
                        case "to_cicerone_new_reservation":
                            message = getString(R.string.notification_to_cicerone_new_reservation, payload.getString(Keys.PAYLOAD_PARAM(0)));
                            title = getString(R.string.notification_title_to_cicerone_new_reservation);
                            break;
                        case "to_cicerone_removed_reservation":
                            message = getString(R.string.notification_to_cicerone_removed_reservation, payload.getString(Keys.PAYLOAD_PARAM(0)), payload.getString(Keys.PAYLOAD_PARAM(1)));
                            title = getString(R.string.notification_title_to_cicerone_removed_reservation);
                            break;
                        case "to_globetrotter_confirmed_reservation":
                            message = getString(R.string.notification_to_globetrotter_confirmed_reservation, payload.getString(Keys.PAYLOAD_PARAM(0)), payload.getString(Keys.PAYLOAD_PARAM(1)));
                            title = getString(R.string.notification_title_to_globetrotter_confirmed_reservation);
                            break;
                        case "to_globetrotter_deleted_itinerary":
                            message = getString(R.string.notification_to_globetrotter_deleted_itinerary, payload.getString(Keys.PAYLOAD_PARAM(0)), payload.getString(Keys.PAYLOAD_PARAM(1)));
                            title = getString(R.string.notification_title_to_globetrotter_deleted_itinerary);
                            break;
                        case "to_globetrotter_updated_itinerary":
                            message = getString(R.string.notification_to_globetrotter_updated_itinerary, payload.getString(Keys.PAYLOAD_PARAM(0)), payload.getString(Keys.PAYLOAD_PARAM(1)));
                            title = getString(R.string.notification_title_to_globetrotter_updated_itinerary);
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException: " + e.getMessage());
                    // Fallback to default values
                    message = data.getString(Keys.MESSAGE);
                    title = data.getString(Keys.TITLE);
                }
            }

            if (!NotificationUtils.isAppInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra(Keys.MESSAGE, message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), UserMainActivity.class);
                resultIntent.putExtra(Keys.MESSAGE, message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}