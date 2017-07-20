package com.example.jtriemstra.quickweather.data.weather;

import com.example.jtriemstra.quickweather.BuildConfig;
import com.example.jtriemstra.quickweather.data.weather.IWeather;

import org.json.JSONObject;

/**
 * Created by JTriemstra on 7/3/2017.
 */
public class Wunderground implements IWeather
{

    private JSONObject m_objData;
    private String m_strZip;

    public Wunderground(JSONObject objData, String strZip)
    {
        m_objData = objData;
        m_strZip = strZip;
    }

    public String getTemperature() throws Exception
    {
        JSONObject objCurrent = m_objData.getJSONObject("current_observation");
        return objCurrent.getString("temperature_string");
    }

    public String getDewpoint() throws Exception
    {
        JSONObject objCurrent = m_objData.getJSONObject("current_observation");
        return objCurrent.getString("dewpoint_string");
    }

    public String getRadarImageUrl() throws Exception
    {
        /*JSONObject objRadar = m_objData.getJSONObject("radar");
        return objRadar.getString("image_url");*/

        return "http://api.wunderground.com/api/" + BuildConfig.WUNDERGROUND_API_KEY + "/radar/q/" + m_strZip + ".gif?width=656&height=656&newmaps=1&noclutter=1";
    }

}
