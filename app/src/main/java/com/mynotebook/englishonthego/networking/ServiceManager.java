package com.mynotebook.englishonthego.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class ServiceManager {
    Context context;

    public ServiceManager(Context base) {
        context = base;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        assert connectivityManager != null;
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
