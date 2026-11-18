# Comedor — Tickets Comedor

Sistema para la venta de tickets de **almuerzo** y **cena** de un comedor universitario.
Consta de dos proyectos independientes que se comunican mediante una API REST sobre HTTPS, con respuestas JSON:

- **`comedor-api/`** — backend en PHP + MySQL: endpoints JSON para la app y panel web de reportes con Highcharts.
- **`comedor-app/`** — aplicación Android nativa en Java, con perfiles de **Alumno** y **Secretaría**.

> **Este repositorio es un proyecto base (plantilla).** No está desplegado ni incluye un dominio real:
> quien desee usarlo debe adaptarlo a su entorno (URL de la API, credenciales de base de datos,
> certificado HTTPS y firma del APK). Las credenciales y los datos incluidos son ficticios y
> sirven únicamente como ejemplo para probar.

## Estructura

```
comedor/
├── comedor-api/
│   ├── Alumno/          # compras, comprobaciones y tickets del alumno
│   ├── Secretaria/      # acciones, registros y reportes
│   ├── reportes/        # panel web (requiere login) + Highcharts
│   ├── conexion.php     # conexión MySQL (variables de entorno)
│   └── db_helper.php    # respuestas JSON, validación y SQL preparado
├── comedor-app/         # proyecto Android (Gradle)
├── comedor.sql          # estructura + datos ficticios de prueba
└── .gitignore
```

## Requisitos

- **PHP 8.3 o superior** con extensión `mysqli`.
- **MySQL 8.4 LTS** (o MariaDB 11.4 LTS). El dump está adaptado a MySQL 8.4: usa `utf8mb4` y un `sql_mode` sin `NO_AUTO_CREATE_USER`.
- Servidor web con soporte PHP (Apache, Nginx, etc.).
- Android SDK, JDK 17 y Android Studio (o Gradle 8.9 incluido en el wrapper).

## Puesta en marcha

### 1. Base de datos

Crear una base de datos llamada `comedor` e importar el dump:

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS comedor CHARACTER SET utf8mb4;"
mysql -u root -p comedor < comedor.sql
```

El dump incluye **solo datos ficticios** y usa `CURDATE()` en el menú y las compras,
así que las pruebas funcionan el mismo día en que se importe.

### 2. API PHP

Publicar la carpeta `comedor-api/` en el servidor web de modo que sea accesible como:

```
https://<host>/comedor-api/
```

La conexión se configura con variables de entorno (valores por defecto entre paréntesis):

| Variable | Default |
|---|---|
| `DB_HOST` | `localhost` |
| `DB_USER` | `root` |
| `DB_PASS` | *(vacío)* |
| `DB_NAME` | `comedor` |

Ejemplo:

```bash
export DB_HOST=localhost DB_USER=comedor DB_PASS=clave DB_NAME=comedor
```

El panel de reportes está en `https://<host>/comedor-api/reportes/index.php`.

### 3. App Android

Abrir `comedor-app/` en Android Studio o compilar por consola:

```bash
cd comedor-app
./gradlew assembleDebug
```

La IP del servidor se configura en
`app/src/main/java/com/ejemplo/comedor/Url.java`:

- Emulador Android: `http://10.0.2.2`
- Dispositivo físico: IP del PC en la red (por ejemplo `http://192.168.1.10`)

> **Video de la pantalla de inicio:** el proyecto **no incluye ningún video**.
> Si quieres mostrar uno, coloca tu propio archivo en
> `comedor-app/app/src/main/res/raw/introduccion.mp4`. La app lo detectará
> automáticamente; si el archivo no existe, la pantalla funciona igual, sin video.
>

> La URL base ya está preparada para producción: el build **debug** usa
> `http://10.0.2.2` y el build **release** usa HTTPS. No hay que editar código
> ni el Manifest: solo configurar `API_BASE_URL` en `comedor-app/gradle.properties`
> (o la variable de entorno `COMEDOR_API_URL`) con el dominio real.
>
> La configuración de red prohíbe tráfico HTTP en release y lo permite únicamente
> en debug, para seguir probando con el emulador o con una IP de la red local.

## Usuarios de prueba

| Rol | Código / DNI | Clave |
|---|---|---|
| Alumno | `0000000001` | `0000000001` |
| Secretaría | `12345678` | `12345678` |

- El código de **alumno debe empezar con `000`**: la app lo usa para elegir entre login de alumno y de secretaría.
- El usuario de **secretaría** también sirve para entrar a los reportes web.
- El alumno de ejemplo no es becado y tiene saldo 10; se pueden comprar almuerzo y cena.

## Notas

- Las contraseñas en la base de datos de ejemplo están en texto plano (esquema heredado).
  El código de autenticación ya acepta hashes `password_verify()` si se migran.
- Los reportes web requieren sesión de secretaría; la app abre esa sesión automáticamente
  para el WebView de la pantalla de inicio.
- El build de release tiene R8/minificado activado; `versionCode = 2`, `versionName = 1.1`.
- `release` usa `API_BASE_URL` (HTTPS) y `debug` usa `API_BASE_URL_DEBUG`. En release,
  el API debe servirse por HTTPS; el backend ya respeta `HTTPS`/`X-Forwarded-Proto`
  para marcar la cookie de sesión como `Secure`.
- `API_BASE_URL` trae un valor de ejemplo (`https://comedor.example.com`): cada persona que adopte
  el proyecto debe reemplazarlo por su propio host antes de compilar el release.
- El dump original con datos personales reales fue reemplazado por datos ficticios.
- PHP 7.4/8.0/8.1 y MySQL 5.7/8.0 ya están fuera de soporte; no se recomiendan para nuevos despliegues.
