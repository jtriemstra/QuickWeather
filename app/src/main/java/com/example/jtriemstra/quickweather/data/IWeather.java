package com.example.jtriemstra.quickweather.data;

/**
 * Created by JTriemstra on 7/4/2017.
 */
public interface IWeather {
    public String getTemperature() throws Exception;
    public String getDewpoint() throws Exception;
    public String getRadarImageUrl() throws Exception;
}
