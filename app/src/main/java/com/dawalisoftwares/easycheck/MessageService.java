package com.dawalisoftwares.easycheck;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MessageService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        Log.w("token",s);
    }
}
