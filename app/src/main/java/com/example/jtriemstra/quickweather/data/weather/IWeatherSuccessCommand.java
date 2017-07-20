package com.example.jtriemstra.quickweather.data.weather;

import com.example.jtriemstra.quickweather.data.weather.IWeather;

/**
 * Created by JTriemstra on 7/3/2017.
 */
public interface IWeatherSuccessCommand {
    void onSuccess(IWeather objResult);
}
