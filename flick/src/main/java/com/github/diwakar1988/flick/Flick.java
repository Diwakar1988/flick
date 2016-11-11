package com.github.diwakar1988.flick;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by diwakar.mishra on 10/11/16.
 */

public class Flick extends Application {

    private static Flick instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static Flick getInstance() {
        if (instance==null){
            throw new IllegalStateException("Flick instance not initialized!");
        }
        return instance;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
