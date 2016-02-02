package com.msi.locationtracer.ApplicationHelper;

import android.content.Context;

import com.msi.locationtracer.Data_Model.UserLocation;

/**
 * Created by sahid_000 on 1/17/2016.
 */
public class LocationPublisher {
    private Context context;

    private InternetChecker internetChecker;
    private GpsTracker gpsTracker;
    private UserLocation userLocation;


    public LocationPublisher(Context context) {
        this.context = context;
    }

    public UserLocation LocationProvider()
    {
        internetChecker = new InternetChecker(context);
        gpsTracker = new GpsTracker(context);
        userLocation = new UserLocation();


        if(!internetChecker.isInternetConnected())
        {
            internetChecker.showInternetAlert();
        }
        else
        {
            if(!gpsTracker.canGetLocation())
            {
                gpsTracker.showGPSAlert();
            }
            else
            {
                userLocation.setUserLatitude(gpsTracker.getLatitude());
                userLocation.setUserLongitude(gpsTracker.getLongitude());
            }
        }


//        if(internetChecker.isInternetConnected())
//        {
//            if(gpsTracker.canGetLocation())
//            {
//                userInfoDataModel.setUserLatitude(gpsTracker.getLatitude());
//                userInfoDataModel.setUserLongitude(gpsTracker.getLongitude());
//            }
//            else if(!gpsTracker.canGetLocation())
//            {
//                gpsTracker.showGPSAlert();
//            }
//        }
//        else if(!internetChecker.isInternetConnected())
//        {
//            internetChecker.showInternetAlert();
//        }
        return userLocation;
    }
}
