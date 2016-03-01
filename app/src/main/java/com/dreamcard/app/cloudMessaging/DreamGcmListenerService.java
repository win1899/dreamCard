package com.dreamcard.app.cloudMessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dreamcard.app.R;
import com.dreamcard.app.utils.PreferencesGCM;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.activity.ReviewStore;
import com.dreamcard.app.view.activity.SplashActivity;
import com.google.android.gms.gcm.GcmListenerService;

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
            }
            else if (topic.equalsIgnoreCase(PreferencesGCM.OFFER_TOPIC)) {
                Utils.updateOffersBadge(this, 1);
                sendGeneralNotification("New offers available.");
            }
            else if (topic.equalsIgnoreCase(PreferencesGCM.STORE_TOPIC)) {
                Utils.updateStoreBadge(this, 1);
                sendGeneralNotification("New stores are added");
            }
        }

        sendReviewNotification(message);
    }

    /**
     * Create and show a simple notification containing the review intent.
     *
     * @param message store id that purchase happened at.
     */
    private void sendReviewNotification(String message) {
        Intent intent = new Intent(this, ReviewStore.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ReviewStore.BUSINESS_ID_EXTRA, message);
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
