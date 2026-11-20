package com.ejemplo.comedor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

/**
 * Shared logout routine for Student and Secretary activities.
 */
public final class SessionLogoutHelper {

    private SessionLogoutHelper() {
    }

    /**
     * Invalidates the server API session, clears local session state and cookies,
     * and returns to MainActivity.
     */
    public static void logout(Activity activity) {
        Context appContext = activity.getApplicationContext();
        Url ip = new Url();
        String logoutUrl = ip.getIp() + "/comedor-api/cerrar_sesion.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                logoutUrl,
                response -> VolleySingleton.getInstance(appContext).clearCookies(),
                error -> VolleySingleton.getInstance(appContext).clearCookies()
        );
        VolleySingleton.getInstance(appContext).addToRequestQueue(request);

        Variables_globales.clearSession();
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Clears local session state and redirects to MainActivity when the server rejects
     * a request with HTTP 401 (expired or missing session).
     */
    public static void handleSessionExpired(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Context appContext = activity.getApplicationContext();
        Variables_globales.clearSession();
        VolleySingleton.getInstance(appContext).clearCookies();
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        Toast.makeText(appContext, "Sesión expirada. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
