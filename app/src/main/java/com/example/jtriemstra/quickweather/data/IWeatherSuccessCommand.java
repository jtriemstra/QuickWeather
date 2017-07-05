package com.example.jtriemstra.quickweather.data;

/**
 * Created by JTriemstra on 7/3/2017.
 */
public interface IWeatherSuccessCommand {
    void onSuccess(Wunderground objResult);
}
