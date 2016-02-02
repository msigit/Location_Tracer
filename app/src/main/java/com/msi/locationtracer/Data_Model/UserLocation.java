package com.msi.locationtracer.Data_Model;

import java.io.Serializable;

/**
 * Created by sahid_000 on 1/17/2016.
 */
public class UserLocation implements Serializable {

    private double userLatitude;
    private double userLongitude;


    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }
}
