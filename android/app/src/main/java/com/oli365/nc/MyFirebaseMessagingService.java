package com.oli365.nc;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by alvinlin on 2016/8/11.
 */



  public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG=MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived:"+remoteMessage.getFrom());
    }
}
