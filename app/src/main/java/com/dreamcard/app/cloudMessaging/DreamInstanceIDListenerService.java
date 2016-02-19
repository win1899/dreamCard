package com.dreamcard.app.cloudMessaging;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by WIN on 2/16/2016.
 */
public class DreamInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = DreamInstanceIDListenerService.class.getName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, DreamRegistrationIntentService.class);
        startService(intent);
    }
}
