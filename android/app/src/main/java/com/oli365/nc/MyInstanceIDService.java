package com.oli365.nc;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by alvinlin on 2016/8/11.
 */
public class MyInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG =MyInstanceIDService.class.getName();

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token:"+token);
    }
}
