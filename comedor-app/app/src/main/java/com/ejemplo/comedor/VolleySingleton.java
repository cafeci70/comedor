package com.ejemplo.comedor;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Shared singleton wrapper around Volley's RequestQueue to avoid spawning
 * a new network queue and thread pool per request, preserving session cookies.
 */
public class VolleySingleton {

    private static volatile VolleySingleton instance;
    private RequestQueue requestQueue;
    private final Context appContext;
    private final CookieManager cookieManager;

    private VolleySingleton(Context context) {
        appContext = context.getApplicationContext();
        CookieHandler current = CookieHandler.getDefault();
        if (current instanceof CookieManager) {
            cookieManager = (CookieManager) current;
        } else {
            cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            CookieHandler.setDefault(cookieManager);
        }
        requestQueue = getRequestQueue();
    }

    public static VolleySingleton getInstance(Context context) {
        if (instance == null) {
            synchronized (VolleySingleton.class) {
                if (instance == null) {
                    instance = new VolleySingleton(context);
                }
            }
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(appContext);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    /**
     * Clears all HTTP cookies stored in the shared Volley CookieManager.
     */
    public void clearCookies() {
        cookieManager.getCookieStore().removeAll();
    }
}

