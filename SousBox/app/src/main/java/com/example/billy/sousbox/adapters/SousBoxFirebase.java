package com.example.billy.sousbox.adapters;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Billy on 5/6/16.
 */
public class SousBoxFirebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
