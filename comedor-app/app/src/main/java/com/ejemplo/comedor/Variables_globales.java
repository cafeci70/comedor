package com.ejemplo.comedor;

import java.util.Locale;

public class Variables_globales {
    //Datos de secretaria
    public static String s_nombre;
    public static String s_apellido;
    public static String s_direccion;
    public static String s_celular;
    public static String s_correo;
    public static String s_codigo;
    public static String s_clave;
    public static String s_reportes_cookie;

    //Datos de alumno
    public static String a_id;
    public static String a_nombre;
    public static String a_apellido_p;
    public static String a_apellido_m;
    public static String a_correo;
    public static String a_celular;
    public static String a_saldo;
    public static String a_beca;
    public static String a_codigo;

    //Dato alumno codigo para editar saldo
    public static String alumn_codigo;
    public static String alumn_saldo;

    //Datos tabla stock

    public static String periodo;
    public static String becados;
    public static String id_tabla_stock;


    //Datos tabla menu
    public static String id_tabla_menu;

    public static String menu_fecha;
    public static String menu_desayuno;
    public static String menu_alm_entrada;
    public static String menu_alm_postre;
    public static String menu_almmuerzo;
    public static String menu_alm_bebida;
    public static String menu_cen_entrada;
    public static String menu_cen_postre;
    public static String menu_cena;
    public static String menu_cen_bebida;

    /**
     * Returns true when an authenticated student session is present in memory.
     */
    public static boolean hasAlumnoSession() {
        return a_id != null && !a_id.isEmpty() && a_codigo != null && !a_codigo.isEmpty();
    }

    /**
     * Returns true when an authenticated secretary session is present in memory.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasSecretariaSession() {
        return s_nombre != null && !s_nombre.isEmpty();
    }

    /**
     * Clears all in-memory user session fields on logout.
     */
    public static void clearSession() {
        s_nombre = null;
        s_apellido = null;
        s_direccion = null;
        s_celular = null;
        s_correo = null;
        s_codigo = null;
        s_clave = null;
        s_reportes_cookie = null;

        a_id = null;
        a_nombre = null;
        a_apellido_p = null;
        a_apellido_m = null;
        a_correo = null;
        a_celular = null;
        a_saldo = null;
        a_beca = null;
        a_codigo = null;
    }

    /**
     * Safely parses an integer string, returning defaultValue if null or malformed.
     */
    public static int parseIntOrDefault(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Formats a zero-based month calendar date as YYYY-MM-DD for MySQL DATE columns.
     */
    public static String formatIsoDate(int year, int monthZeroBased, int dayOfMonth) {
        return String.format(Locale.US, "%04d-%02d-%02d", year, monthZeroBased + 1, dayOfMonth);
    }

}
