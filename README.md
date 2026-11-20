# Comedor — Tickets Comedor

Sistema para la venta de tickets de **almuerzo** y **cena** de un comedor universitario.

- **`comedor-api/`** — Backend en PHP + MySQL con respuestas JSON y panel web de reportes (Highcharts v13.1.1).
- **`comedor-app/`** — App Android nativa en Java (responsiva para celular y tablet), con perfiles de **Alumno** y **Secretaría**.

> **Proyecto base (plantilla):** Incluye datos ficticios de prueba. Para producción debes configurar tu propio dominio HTTPS, credenciales de base de datos y firma del APK.

## Estructura

```text
comedor/
├── comedor-api/
│   ├── Alumno/          # compras, comprobaciones y tickets del alumno
│   ├── Secretaria/      # acciones, registros y reportes
│   ├── reportes/        # panel web con gráficos Highcharts
│   ├── conexion.php     # conexión MySQL y zona horaria
│   └── db_helper.php    # sesiones, validación y SQL preparado
├── comedor-app/         # proyecto Android (Gradle)
└── comedor.sql          # esquema + procedimientos + datos de prueba
```

## Requisitos

- **PHP 8.3+** con extensión `mysqli` y servidor web (Apache/Nginx).
- **MySQL 8.4 LTS** (o MariaDB 11.4 LTS).
- **JDK 17** y **Android SDK** (Gradle 8.9 incluido en el wrapper).

## Puesta en marcha

### 1. Base de datos

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS comedor CHARACTER SET utf8mb4;"
mysql -u root -p comedor < comedor.sql
```

### 2. API PHP

Publicar `comedor-api/` en el servidor web para que responda en `https://<host>/comedor-api/`.
Configurar mediante variables de entorno:

| Variable | Default | Descripción |
|---|---|---|
| `DB_HOST` | `localhost` | Host de MySQL |
| `DB_USER` | `root` | Usuario de MySQL |
| `DB_PASS` | *(vacío)* | Contraseña de MySQL |
| `DB_NAME` | `comedor` | Nombre de la base de datos |
| `APP_TIMEZONE` | `America/Lima` | Zona horaria (sincroniza PHP y MySQL) |

Panel web de reportes: `https://<host>/comedor-api/reportes/index.php`.

### 3. App Android

Configurar la URL del servidor en `comedor-app/gradle.properties`:

- **`API_BASE_URL_DEBUG`** (desarrollo, permite HTTP): `http://10.0.2.2` para emulador o `http://<IP-LAN>` para dispositivo físico.
- **`API_BASE_URL`** (producción, exige HTTPS): `https://tu-dominio.com`.

Compilar APK de prueba:

```bash
cd comedor-app
./gradlew assembleDebug
```

### 4. Compilar Release firmado (opcional)

Crear un keystore (fuera del repositorio) y definir las variables antes de compilar:

```powershell
keytool -genkeypair -v -keystore comedor.jks -alias comedor -keyalg RSA -keysize 2048 -validity 10000

$env:COMEDOR_KEYSTORE = "C:\ruta\comedor.jks"
$env:COMEDOR_STORE_PASSWORD = "tu-contraseña"
$env:COMEDOR_KEY_PASSWORD = "tu-contraseña"
$env:COMEDOR_KEY_ALIAS = "comedor"
.\gradlew.bat assembleRelease
```

*(Sin estas variables, `assembleRelease` genera `app-release-unsigned.apk`).*

## Usuarios de prueba

| Rol | Código / DNI | Clave |
|---|---|---|
| Alumno | `0000000001` | `0000000001` |
| Secretaría | `12345678` | `12345678` |

## Detalles importantes

- **Detección de rol en el Login:** El código de **alumno debe empezar con `000`**; cualquier otro código o DNI inicia sesión como **Secretaría**.
- **Contraseñas:** Las claves de ejemplo vienen en texto plano, pero la API y el panel web aceptan hashes `password_hash()` / `password_verify()` directamente en `alum_pass` y `usu_contrasena` (`VARCHAR(255)`).
- **Sesiones y HTTPS:** Todos los endpoints requieren sesión activa (`COMEDORAPI` / `COMEDORREPORTES`). El build `release` de Android bloquea tráfico HTTP en texto claro y exige HTTPS.
- **Video de bienvenida (opcional):** Si colocas un archivo en `comedor-app/app/src/main/res/raw/introduccion.mp4`, se reproducirá en el inicio del alumno; si no existe, el recuadro se oculta automáticamente.
