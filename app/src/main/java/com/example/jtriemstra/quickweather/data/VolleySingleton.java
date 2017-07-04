package com.example.jtriemstra.quickweather.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by JTriemstra on 7/3/2017.
 */
public class VolleySingleton {
    private static VolleySingleton m_objInstance = null;
    private RequestQueue m_objQueue = null;
    private ImageLoader m_objImageLoader = null;
    private static final String TAG = "VolleySingleton";

    private VolleySingleton()
    {

    }

    private VolleySingleton(Context objAppContext)
    {
        m_objQueue = Volley.newRequestQueue(objAppContext);
        m_objImageLoader = new ImageLoader(this.m_objQueue, new ImageLoader.ImageCache()
            {
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
                public void putBitmap(String url, Bitmap bitmap) {
                    Log.d(TAG, "in putBitmap");
                    mCache.put(url, bitmap);
                }
                public Bitmap getBitmap(String url) {
                    Log.d(TAG, "in getBitmap");
                    return mCache.get(url);
                }
            }
        );
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

    public ImageLoader getImageLoader()
    {
        return m_objImageLoader;
    }
}
