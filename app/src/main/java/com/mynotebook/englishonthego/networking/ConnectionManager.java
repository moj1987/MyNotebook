package com.mynotebook.englishonthego.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import com.mynotebook.englishonthego.utilities.Variables;
import org.jetbrains.annotations.NotNull;

public class ConnectionManager {

    public ConnectionManager() {
    }

    public static boolean isNetworkAvailable(Context context) {
        android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            assert connectivityManager != null;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null &&
                    networkInfo.isConnectedOrConnecting();
        } else {
            new NetworkRequest.Builder();
            assert connectivityManager != null;
            connectivityManager.registerDefaultNetworkCallback(new android.net.ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NotNull Network network) {
                    Variables.isNetworkConnected = true;
                }

                @Override
                public void onLost(Network network) {
                    Variables.isNetworkConnected = false;
                }
            });

            return Variables.isNetworkConnected;
        }
    }

}
