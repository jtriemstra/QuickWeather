package com.example.jtriemstra.quickweather.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jtriemstra.quickweather.BuildConfig;

import org.json.JSONObject;

/**
 * Created by JTriemstra on 7/3/2017.
 */
public class Wunderground {

    private JSONObject m_objData;

    public Wunderground(JSONObject objData)
    {
        m_objData = objData;
    }

    public String getTemperature()
    {
        return "";
    }

    public String getDewpoint()
    {
        return "";
    }

    public String getRadarImageUrl()
    {
        return "";
    }

}
