package com.ejemplo.comedor;

/**
 * URL base del API. El valor se inyecta en tiempo de compilación desde
 * gradle.properties (API_BASE_URL / API_BASE_URL_DEBUG), de modo que el
 * build de release apunta a HTTPS sin tocar código fuente.
 */
public class Url {

    public Url() {
    }

    public String getIp() {
        return BuildConfig.API_BASE_URL;
    }
}
