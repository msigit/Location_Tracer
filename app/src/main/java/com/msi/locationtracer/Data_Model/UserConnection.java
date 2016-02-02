package com.msi.locationtracer.Data_Model;

import java.io.Serializable;

/**
 * Created by sahid_000 on 1/23/2016.
 */
public class UserConnection implements Serializable {

    private String HostDeviceId;
    private String GuestDeviceId;
    private String Status;

    public String getHostDeviceId() {
        return HostDeviceId;
    }

    public void setHostDeviceId(String hostDeviceId) {
        HostDeviceId = hostDeviceId;
    }

    public String getGuestDeviceId() {
        return GuestDeviceId;
    }

    public void setGuestDeviceId(String guestDeviceId) {
        GuestDeviceId = guestDeviceId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
