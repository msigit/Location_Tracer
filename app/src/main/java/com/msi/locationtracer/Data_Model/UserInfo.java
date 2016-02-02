package com.msi.locationtracer.Data_Model;

import java.io.Serializable;

/**
 * Created by sahid_000 on 1/8/2016.
 */
public class UserInfo implements Serializable {

    private String DeviceID;
    private String ConnectID;
    private String UserName;
    private String ApiStatus;
    private String PhoneNumber;

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getConnectID() {
        return ConnectID;
    }

    public void setConnectID(String connectID) {
        ConnectID = connectID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getApiStatus() {
        return ApiStatus;
    }

    public void setApiStatus(String apiStatus) {
        ApiStatus = apiStatus;
    }
}
