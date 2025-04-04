# Aplicación de Cámara para Android

Aplicación desarrollada para la asignatura M07 - Desarrollo de Aplicaciones Móviles del ciclo DAM2.

## Características

Esta aplicación demuestra el uso de los permisos de hardware del dispositivo Android para:

- **Capturar fotos** con la cámara y guardarlas en la galería
- **Acceder a la galería** del dispositivo para seleccionar imágenes
- Guardar datos asociados con las imágenes

## Tecnologías utilizadas

- **Kotlin**
- **Jetpack Compose** para la interfaz de usuario
- **MVVM** (Model-View-ViewModel) como patrón de arquitectura
- **LiveData** para la gestión reactiva de datos
- **Lazy Composables** para carga eficiente de elementos UI
- **Firebase Firestore** para almacenamiento de datos

## Funcionalidades principales

### Captura de imágenes
La aplicación permite tomar fotos con la cámara del dispositivo y guardarlas automáticamente en la galería.

### Galería de imágenes
Se puede acceder a la galería del dispositivo para seleccionar imágenes existentes.

### Persistencia de datos
Las imágenes se pueden asociar con datos de usuario (nombre, edad) y guardar en Firebase.

## Estructura del proyecto

El proyecto sigue el patrón MVVM:

- **Model**: Clases que representan los datos (User)
- **View**: Composables que muestran la interfaz (CameraScreen, GalleryScreen, etc.)
- **ViewModel**: Lógica de negocio y gestión de estado (MyViewModel)

## Permisos necesarios

- `android.permission.CAMERA` - Para usar la cámara
- `android.permission.READ_EXTERNAL_STORAGE` - Para acceder a la galería
- `android.permission.WRITE_EXTERNAL_STORAGE` - Para guardar imágenes en la galería

## Capturas de pantalla

[Aquí irán las capturas de pantalla]

## Desarrollado por

[Nombres de los desarrolladores]

---

Proyecto desarrollado como parte del módulo M07 de DAM2. 