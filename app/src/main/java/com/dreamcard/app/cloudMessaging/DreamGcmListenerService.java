package com.dreamcard.app.cloudMessaging;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dreamcard.app.R;
import com.dreamcard.app.common.DatabaseController;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.NotificationDB;
import com.dreamcard.app.utils.PreferencesGCM;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.activity.ReviewStore;
import com.dreamcard.app.view.activity.SplashActivity;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by WIN on 2/16/2016.
 */
public class DreamGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    private static final int NOTIFICATION_ID = 18887721;
    private static final int REQUEST_ID = 54329;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            String topic = from.replaceFirst("/topics/", "");

            if (topic.equalsIgnoreCase(PreferencesGCM.GLOBAL_TOPIC)) {
                Utils.updateNotificationBadge(this, 1);
                sendGeneralNotification(message);
                return;
            }
            else if (topic.equalsIgnoreCase(PreferencesGCM.OFFER_TOPIC)) {
                Utils.updateOffersBadge(this, 1);
                NotificationDB notification = new NotificationDB();
                notification.type = NotificationDB.OFFER_TYPE;
                notification.id = message;
                DatabaseController.getInstance(this).saveNotificaiton(notification);

                sendGeneralNotification("New offers are available on Dream Card.");
                return;
            }
            else if (topic.equalsIgnoreCase(PreferencesGCM.STORE_TOPIC)) {
                Utils.updateStoreBadge(this, 1);
                NotificationDB notification = new NotificationDB();
                notification.type = NotificationDB.STORE_TYPE;
                notification.id = message;
                DatabaseController.getInstance(this).saveNotificaiton(notification);

                sendGeneralNotification("New store just joined Dream Card.");
                return;
            }
        }

        NotificationDB notification = new NotificationDB();
        notification.type = NotificationDB.REVIEW_TYPE;
        notification.id = message;
        DatabaseController.getInstance(this).saveNotificaiton(notification);

        sendReviewNotification(message);
    }

    /**
     * Create and show a simple notification containing the review intent.
     *
     * @param storeId store id that purchase happened at.
     */
    private void sendReviewNotification(String storeId) {
        Intent intent = new Intent(this, ReviewStore.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ReviewStore.BUSINESS_ID_EXTRA, storeId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_ID, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.dream_card_app_small)
                .setContentTitle("Dream Card")
                .setContentText("Thank you for using Dream Card. click here to review your purchase.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        Utils.updateNotificationBadge(this, 1);
    }

    /**
     * Create and show a simple notification containing the received GCM message, and splash intent.
     *
     * @param message GCM message received.
     */
    private void sendGeneralNotification(String message) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_ID, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.dream_card_app_small)
                .setContentTitle("Dream Card")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
