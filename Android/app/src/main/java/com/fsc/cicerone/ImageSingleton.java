package com.fsc.cicerone;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * ImageSingleton class created for com.android.volley usage.
 */
public class ImageSingleton {
    private static ImageSingleton mInstance;
    private RequestQueue requestQueue;
    private Context mContext;

    /**
     * Constructor.
     * @param context Application context.
     */
    private ImageSingleton(Context context){
        mContext = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Return the request queue.
     * @return RequestQueue
     */
    private RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Return the current instance.
     * @param context Application context.
     * @return The new instance if not present, otherwise the current instance.
     */
    public static synchronized ImageSingleton getInstance(Context context) {
        if(mInstance == null){
            mInstance = new ImageSingleton(context);
        }
        return mInstance;
    }

    /**
     * Add a request to request queue.
     * @param request Request to add.
     * @param <T> The type of response that the request expects.
     */
    public<T> void addToRequestQue(Request<T> request){
        getRequestQueue().add(request);
    }
}
