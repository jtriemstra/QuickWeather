package com.example.jtriemstra.quickweather.data;

/**
 * Created by JTriemstra on 7/4/2017.
 */
public interface IWeatherFactory {
    public void loadDataByZip(String strZip, final IWeatherSuccessCommand objCallback);
}
