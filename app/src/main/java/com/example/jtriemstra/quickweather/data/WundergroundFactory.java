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
public class WundergroundFactory {
    private Context m_objContext;

    public WundergroundFactory(Context objAppContext)
    {
        m_objContext = objAppContext;
    }

    public void loadDataByZip(String strZip, final WeatherSuccessCallback objCallback)
    {
        RequestQueue queue = VolleySingleton.getInstance(m_objContext).getQueue();

        String url ="http://api.wunderground.com/api/" + BuildConfig.WUNDERGROUND_API_KEY + "/conditions/q/MI/Kalamazoo.json";

        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("y", response.toString().substring(0, 50));
                        objCallback.onSuccess(new Wunderground(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("y", error.getMessage());
            }
        });

        queue.add(objRequest);
    }
}
