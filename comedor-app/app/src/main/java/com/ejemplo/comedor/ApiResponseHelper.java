package com.ejemplo.comedor;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * Helper utilities for parsing standardized JSON responses and error payloads
 * returned by comedor-api endpoints.
 */
public final class ApiResponseHelper {

    private ApiResponseHelper() {
    }

    /**
     * Returns true when a JSON response object indicates success (or when the payload
     * does not specify an "ok" flag).
     */
    public static boolean isSuccessResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return false;
        }
        try {
            JSONObject obj = new JSONObject(response);
            return obj.optBoolean("ok", true);
        } catch (JSONException e) {
            return true;
        }
    }

    /**
     * Extracts the "mensaje" field from a JSON response object, or returns fallback.
     */
    public static String extractMessage(String response, String fallback) {
        if (response == null || response.trim().isEmpty()) {
            return fallback;
        }
        try {
            JSONObject obj = new JSONObject(response);
            String msg = obj.optString("mensaje", "").trim();
            return msg.isEmpty() ? fallback : msg;
        } catch (JSONException e) {
            return fallback;
        }
    }

    /**
     * Extracts a human-readable error message from a VolleyError JSON body,
     * falling back to fallback if no JSON message is available.
     */
    public static String extractErrorMessage(VolleyError error, String fallback) {
        if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
            String body = new String(error.networkResponse.data, StandardCharsets.UTF_8).trim();
            if (!body.isEmpty()) {
                try {
                    JSONObject obj = new JSONObject(body);
                    String msg = obj.optString("mensaje", "").trim();
                    if (!msg.isEmpty()) {
                        return msg;
                    }
                } catch (JSONException ignored) {
                }
            }
        }
        return fallback;
    }

    /**
     * Returns true when the server responded with HTTP 401 Unauthorized.
     */
    public static boolean isUnauthorized(VolleyError error) {
        return error != null && error.networkResponse != null && error.networkResponse.statusCode == 401;
    }

    /**
     * Redirects to MainActivity when a fragment request receives HTTP 401.
     */
    public static boolean handleUnauthorized(Fragment fragment, VolleyError error) {
        if (isUnauthorized(error) && fragment != null && fragment.getActivity() != null) {
            SessionLogoutHelper.handleSessionExpired(fragment.getActivity());
            return true;
        }
        return false;
    }

    /**
     * Redirects to MainActivity when an activity request receives HTTP 401.
     */
    public static boolean handleUnauthorized(Activity activity, VolleyError error) {
        if (isUnauthorized(error) && activity != null) {
            SessionLogoutHelper.handleSessionExpired(activity);
            return true;
        }
        return false;
    }
}
