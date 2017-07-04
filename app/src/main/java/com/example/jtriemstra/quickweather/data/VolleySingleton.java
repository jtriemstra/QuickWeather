package com.example.jtriemstra.quickweather.data;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by JTriemstra on 7/3/2017.
 */
public class VolleySingleton {
    private static VolleySingleton m_objInstance = null;
    private RequestQueue m_objQueue = null;

    private VolleySingleton()
    {
    }

    private VolleySingleton(Context objAppContext)
    {
        m_objQueue = Volley.newRequestQueue(objAppContext);
    }

    public static VolleySingleton getInstance(Context objAppContext)
    {
        if (m_objInstance == null)
        {
            m_objInstance = new VolleySingleton(objAppContext);
        }

        return m_objInstance;
    }

    public RequestQueue getQueue()
    {
        return m_objQueue;
    }
}
