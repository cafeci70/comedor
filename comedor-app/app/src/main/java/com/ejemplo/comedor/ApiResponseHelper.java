package com.ejemplo.comedor;

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
}
