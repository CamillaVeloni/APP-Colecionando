package com.app.teste.colecionando.Ajuda;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AjudaNetwork {

    public static boolean conectadoNet(Context context) {
        ConnectivityManager connectManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( connectManager != null ) {
            NetworkInfo netInfo = connectManager.getActiveNetworkInfo();

            return netInfo != null && netInfo.isConnected(); // está conectado
        }

        return false; // não está conectado
    }
}
