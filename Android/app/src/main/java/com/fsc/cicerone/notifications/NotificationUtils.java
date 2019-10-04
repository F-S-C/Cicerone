package com.fsc.cicerone.notifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.fsc.cicerone.R;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class NotificationUtils {

    private static final String SUBSCRIBED_TOPIC_LIST_KEY = "subscribed-topics";

    private Context mContext;

    public static void subscribeToTopic(@NonNull Context context, @NonNull String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);

        Set<String> topics = getSubscribedTopics(context);
        if (topics == null)
            topics = new HashSet<>(1);
        topics.add(topic);

        SharedPreferences sharedPreferences = context.getSharedPreferences(com.fsc.cicerone.Config.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SUBSCRIBED_TOPIC_LIST_KEY, new JSONArray(topics).toString()).apply();
    }

    @Nullable
    private static Set<String> getSubscribedTopics(@NonNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(com.fsc.cicerone.Config.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        Set<String> topics;
        try {
            JSONArray topicsJsonArray = new JSONArray(sharedPreferences.getString(SUBSCRIBED_TOPIC_LIST_KEY, ""));
            topics = new HashSet<>(topicsJsonArray.length());
            for (int i = 0; i < topicsJsonArray.length(); i++) {
                topics.add(topicsJsonArray.getString(i));
            }
        } catch (JSONException e) {
            Log.e("GET_TOPICS_ERROR", e.getMessage());
            topics = null;
        }
        return topics;
    }

    public static void unsubscribeFromAllTopics(@NonNull Context context) {
        Set<String> topics = getSubscribedTopics(context);
        if (topics == null)
            return;

        for (String topic : topics) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(com.fsc.cicerone.Config.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SUBSCRIBED_TOPIC_LIST_KEY, new JSONArray().toString()).apply();
    }

    public static void unsubscribeFromTopic(@NonNull Context context, @NonNull String topic) {
        Set<String> topics = getSubscribedTopics(context);
        if (topics == null)
            return;
        topics.remove(topic);

        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);

        SharedPreferences sharedPreferences = context.getSharedPreferences(com.fsc.cicerone.Config.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SUBSCRIBED_TOPIC_LIST_KEY, new JSONArray(topics).toString()).apply();
    }

    NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        showNotificationMessage(title, message, timeStamp, intent, null);
    }

    void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, @Nullable String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(
                mContext,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Cicerone_Notifications",
                    "Cicerone's notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("All the notifications of the app Cicerone");
            mNotificationManager.createNotificationChannel(channel);
        }

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "Cicerone_Notifications");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, title, message, timeStamp, resultPendingIntent);
                } else {
                    showSmallNotification(mBuilder, title, message, timeStamp, resultPendingIntent);
                }
            }
        } else {
            showSmallNotification(mBuilder, title, message, timeStamp, resultPendingIntent);
        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, String title, String message, String timeStamp, PendingIntent resultPendingIntent) {
        final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final int icon = R.mipmap.ic_launcher_round;

        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID, notification);
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, String title, String message, String timeStamp, PendingIntent resultPendingIntent) {
        final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final int icon = R.mipmap.ic_launcher_round;

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .setBigContentTitle(title)
                .setSummaryText(Html.fromHtml(message).toString())
                .bigPicture(bitmap);

        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray.
     *
     * @param strURL The URL of the image.
     * @return A Bitmap containing the downloaded image.
     */
    @Nullable
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.e(NotificationUtils.class.getSimpleName(), e.getMessage());
            return null;
        }
    }

    // Playing notification sound
    void playNotificationSound() {
        try {
            Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mContext, notificationUri);
            r.play();
        } catch (Exception e) {
            Log.e(NotificationUtils.class.getSimpleName(), e.getMessage());
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    static boolean isAppInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        isInBackground = !activeProcess.equals(context.getPackageName());
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            isInBackground = !componentInfo.getPackageName().equals(context.getPackageName());
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            Log.e(NotificationUtils.class.getSimpleName(), e.getMessage());
        }
        return 0;
    }
}