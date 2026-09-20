# App-DB

Aplicación móvil para Android desarrollada en Java que permite buscar y visualizar datos, imágenes y transformaciones de personajes de Dragon Ball a través de una API.

## Características Principales

* **Búsqueda por ID:** Consulta personajes ingresando su identificador numérico.
* **Datos del personaje:** Muestra el nombre, nivel de ki, raza y género.
* **Imagen del personaje:** Carga y muestra la ilustración del personaje consultado.
* **Transformaciones:** Botón para ver las transformaciones disponibles en una ventana emergente.
* **Botón de reinicio:** Limpia los campos e imagen para iniciar una nueva búsqueda rápidamente.
* **Avisos en pantalla:** Notificaciones (Toast) cuando no se encuentra un personaje o hay fallos de conexión.

## Tecnologías y Librerías Utilizadas

* **Lenguaje:** Java
* **Sistema Operativo:** Android
* **Peticiones de Red:** Volley
* **Procesamiento JSON:** org.json
* **Carga de Imágenes:** Glide

## Requisitos

* Android 12.0 (API nivel 31) o superior.
* Conexión a Internet activa.

## Estructura del Proyecto

```text
app/src/main/
├── java/com/example/applistas/
│   └── BuscadorPersonaje.java        # Lógica de conexión, consumo de API y eventos
└── res/layout/
    └── activity_buscador_personaje.xml    # Diseño de la interfaz de usuario
```

## Instalación y Ejecución

1. Clona este repositorio en tu equipo local:
   ```bash
   git clone https://github.com/Isaac-9026/App-DBZ.git
   ```
2. Abre el proyecto en Android Studio.
3. Espera a que Gradle sincronice las dependencias del proyecto.
4. Ejecuta la aplicación en un emulador o dispositivo físico.
