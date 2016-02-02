package com.msi.locationtracer.ApplicationHelper;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.msi.locationtracer.R;

/**
 * Created by sahid_000 on 1/17/2016.
 */
public class GpsTracker extends Service implements LocationListener {

    private Location location;
    private LocationManager locationManager;
    private Context context;
    private double latitude;
    private double longitude;

    private boolean isGpsEnabled;
    private boolean isNetworkEnabled;
    private boolean canGetLocation;

    private static final long minDistanceForUpdate = 2;
    private static final long minTimeBitweenUpdate = 1000 * 30;

    private InternetChecker internetChecker;

    public GpsTracker(Context context) {
        this.context = context;
        getLocation();
    }


    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isNetworkEnabled || !isGpsEnabled) {
//                if (!isNetworkEnabled) {
//                    internetChecker = new InternetChecker();
//                    internetChecker.showInternetAlert();
//                } else if (!isGpsEnabled)
//                    showGPSAlert();
            }
            else {

                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTimeBitweenUpdate, minDistanceForUpdate, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }


                if (isGpsEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeBitweenUpdate, minDistanceForUpdate, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return location;
    }

    public double getLatitude()
    {
        if(location != null)
            latitude = location.getLatitude();
        return latitude;
    }

    public  double getLongitude()
    {
        if(location != null)
            longitude = location.getLongitude();
        return longitude;
    }

    public boolean canGetLocation(){return this.canGetLocation;}

    public void showGPSAlert()
    {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);

        alertdialog.setTitle("GPS Settings");
        alertdialog.setMessage("GPS is not enabled.Please enable GPS to continue");
        // alertdialog.setCancelable(false);

        alertdialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertdialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                dialog.cancel();
                // alertdialog.show();
            }
        });

        alertdialog.show();
    }




    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
