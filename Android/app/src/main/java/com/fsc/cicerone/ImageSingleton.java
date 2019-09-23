/*
 * Copyright 2019 FSC - Five Students of Computer Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
