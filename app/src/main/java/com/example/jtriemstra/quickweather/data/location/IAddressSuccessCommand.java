package com.example.jtriemstra.quickweather.data.location;

/**
 * Created by JTriemstra on 7/3/2017.
 */
public interface IAddressSuccessCommand {
    void onSuccess(String strAddress, String strZip);
}
