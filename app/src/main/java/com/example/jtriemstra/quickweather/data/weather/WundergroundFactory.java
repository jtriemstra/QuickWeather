package com.example.jtriemstra.quickweather.data.weather;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jtriemstra.quickweather.BuildConfig;
import com.example.jtriemstra.quickweather.data.VolleySingleton;
import com.example.jtriemstra.quickweather.data.weather.IWeatherFactory;
import com.example.jtriemstra.quickweather.data.weather.IWeatherSuccessCommand;
import com.example.jtriemstra.quickweather.data.weather.Wunderground;

import org.json.JSONObject;

/**
 * Created by JTriemstra on 7/3/2017.
 */
public class WundergroundFactory implements IWeatherFactory {
    private Context m_objContext;
    private static final String TAG = "WundergroundFactory";

    public WundergroundFactory(Context objAppContext)
    {
        m_objContext = objAppContext;
    }

    public void loadDataByZip(final String strZip, final IWeatherSuccessCommand objCallback)
    {
        RequestQueue queue = VolleySingleton.getInstance(m_objContext).getQueue();

        String url ="http://api.wunderground.com/api/" + BuildConfig.WUNDERGROUND_API_KEY + "/conditions/q/" + strZip + ".json";

        //TODO: move the anonymous classes out to named ones?
        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString().substring(0, 50));
                    objCallback.onSuccess(new Wunderground(response, strZip));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e(TAG, error.getMessage());
                }
            });

        queue.add(objRequest);
    }

}
