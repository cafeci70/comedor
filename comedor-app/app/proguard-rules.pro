# Reglas de R8/ProGuard para el build de release (minifyEnabled true).

# Conservar nombres y líneas para poder leer los stack traces de los reportes de errores.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Clases de la app (referenciadas desde XML de navegación/layouts).
-keep class com.ejemplo.comedor.** { *; }

# Librerías que usan reflexión o se crean dinámicamente.
-keep class com.android.volley.** { *; }
-keep class net.glxn.qrgen.** { *; }
-keep class com.google.zxing.** { *; }

# Avisos conocidos de dependencias que no afectan en tiempo de ejecución.
-dontwarn com.google.zxing.**
