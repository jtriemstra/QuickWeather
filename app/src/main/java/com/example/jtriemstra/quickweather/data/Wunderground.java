package com.example.jtriemstra.quickweather.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by JTriemstra on 7/3/2017.
 */
public class Wunderground {

    /*private JSONObject m_objData;

    public String getTemperature()
    {

    }

    public String getDewpoint()
    {

    }

    public String getRadarImageUrl()
    {

    }

    public static Wunderground getDataByZip(String strZip)
    {
        Wunderground objReturn = new Wunderground();
        objReturn.m_objData = doDataRequest(strZip);

        return objReturn;
    }

    private static JSONObject doDataRequest(String strZip)
    {
        Log.d("x", "about to make request");

        RequestQueue queue = Volley.newRequestQueue(this);

        String url ="http://api.wunderground.com/api/02ed4fc1de9b9832/conditions/q/MI/Kalamazoo.json";
        Log.d("x","about to define request");
// Request a string response from the provided URL.
        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        Log.d("y", response.toString().substring(0,50));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Log.e("y", error.getMessage());
            }
        });
        Log.d("x", "about to add request");
// Add the request to the RequestQueue.
        queue.add(objRequest);
    }
    */
}
