package com.msi.locationtracer.ApplicationHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.msi.locationtracer.R;

/**
 * Created by sahid_000 on 1/17/2016.
 */
public class InternetChecker{

    private Context context;
    public InternetChecker(Context context) {
        this.context = context;
    }


    public boolean isInternetConnected() {
        boolean isConnectedWifi = false;
        boolean isonnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo net : netInfo) {
            if (net.getTypeName().equalsIgnoreCase("WIFI"))
                if (net.isConnected())
                    isConnectedWifi = true;
            if (net.getTypeName().equalsIgnoreCase("MOBILE"))
                if (net.isConnected())
                    isonnectedMobile = true;
        }
        return isConnectedWifi || isonnectedMobile;
    }


    public void showInternetAlert()
    {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);

        alertdialog.setTitle("Network Settings");
        alertdialog.setMessage("No network is available. Please check your internet settings");
        //alertdialog.setCancelable(false);

        alertdialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                // alertdialog.show();
            }
        });

        alertdialog.show();
    }
}
