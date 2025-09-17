# Pokédex Nativa en Kotlin (1ª Gen)

Esta aplicación es un proyecto de portafolio diseñado para demostrar un dominio de las tecnologías y arquitecturas fundamentales del desarrollo moderno de Android nativo con Kotlin. No es solo una lista de Pokémon; es una aplicación completa, robusta y eficiente que consume una API REST pública, gestiona estados de carga y error, y presenta los datos en una interfaz de usuario pulida con Material Design 3.

## 📸 Vitrina de Funcionalidades

| Splash & Lista Principal | Búsqueda en Tiempo Real | Detalles del Pokémon |
| :---: | :---: | :---: |
| ![Splash y Lista](https://github.com/FelixMGZ/API_Pokedex_Android_Studio/blob/main/img/pokedex_list.JPG) | ![Búsqueda](https://github.com/FelixMGZ/API_Pokedex_Android_Studio/blob/main/img/pokedex_busqueda.JPG) | ![Detalles](https://github.com/FelixMGZ/API_Pokedex_Android_Studio/blob/main/img/card_pokedex.JPG) |
| *Inicio rápido con la API de Splash Screen y lista eficiente con imágenes.* | *Filtrado instantáneo con animaciones suaves gracias a `DiffUtil`.* | *Datos completos, tipos con colores y transiciones de elementos compartidos.* |

*(Instrucciones: Sube tus capturas de pantalla a la raíz de tu repositorio de GitHub y reemplaza las URLs de ejemplo con la ruta relativa, ej: `screenshots/lista.png`)*

---

## 🚀 Características

* **Catálogo Completo (1ª Gen):** Carga y muestra los 151 Pokémon originales desde la PokeAPI.
* **Búsqueda Instantánea:** Un `SearchView` en la `Toolbar` filtra la lista en tiempo real por nombre.
* **Vista de Detalles Enriquecida:**
    * **Imagen de Alta Calidad:** Carga la mejor imagen disponible (`official-artwork`) con un **Plan B** que carga el sprite clásico si la primera falla.
    * **Datos Esenciales:** Muestra el número de Pokédex, altura y peso convertidos a unidades métricas.
    * **Tipos con Estilo:** Muestra los tipos del Pokémon usando `Chip`s de Material 3 con colores personalizados para cada tipo.
    * **Descripción Oficial:** Extrae y muestra la descripción de la Pokédex en español.
    * **Cadena de Evolución:** Realiza una cadena de llamadas a la API para mostrar la línea evolutiva completa, ocultándose inteligentemente si el Pokémon no tiene evoluciones.
* **Experiencia de Usuario Pulida:**
    * **Splash Screen Adaptativa:** Utiliza la API moderna de Android 12+ para una transición de inicio suave y profesional.
    * **Transiciones de Elemento Compartido:** Al hacer clic, la imagen del Pokémon viaja fluidamente desde la lista hasta la pantalla de detalles, creando una conexión visual elegante.
    * **Estados de Carga y Error:** Muestra `ProgressBar`s durante las llamadas a la red y maneja los errores (como la falta de conexión) de forma segura.

---

## 🛠️ Metodología y Arquitectura: El "Porqué" del Código

Este proyecto se construyó con un enfoque en la eficiencia, la seguridad y las mejores prácticas modernas.

### Capa de Interfaz (UI)
* **XML y ViewBinding:** Los layouts se definieron con XML, utilizando `ConstraintLayout` para diseños responsivos. Se usó **`ViewBinding`** para garantizar una comunicación 100% segura y libre de `NullPointerExceptions` entre el código Kotlin y las vistas, eliminando la necesidad del anticuado `findViewById`.
* **RecyclerView con `ListAdapter` y `DiffUtil`:** La lista principal se implementó con un **`ListAdapter`**, que trabaja junto a **`DiffUtil`**. Esta combinación es la forma más eficiente de manejar listas. En lugar de redibujar todo con `notifyDataSetChanged`, `DiffUtil` calcula las diferencias exactas entre la lista vieja y la nueva en un hilo secundario y el `RecyclerView` ejecuta animaciones precisas y suaves para las actualizaciones.
* **Material Design 3:** Toda la estética se basa en los componentes de `com.google.android.material`. Esto asegura una apariencia moderna y coherente, y permite funcionalidades como las transiciones de elementos compartidos.

### Capa de Datos y Red
* **Fuente de Datos:** La aplicación es 100% dinámica, obteniendo todos sus datos de la **[PokeAPI](https://pokeapi.co/)**.
* **Cliente de Red con Retrofit:** Se utilizó **Retrofit**, el estándar de la industria, para definir las llamadas a la API de forma declarativa a través de una `interface`. Se implementó un `object` singleton (`RetrofitClient`) para asegurar que solo exista una instancia del motor de red, optimizando los recursos.
* **Asincronía con Coroutines de Kotlin:** Todas las operaciones de red se ejecutan en un hilo secundario (`Dispatchers.IO`) usando **Coroutines**. Esto es crucial para que la interfaz nunca se congele. Las actualizaciones a la UI se devuelven de forma segura al hilo principal con `withContext(Dispatchers.Main)`.
* **Parseo de JSON con Gson:** Se usó **Gson** como el conversor de Retrofit para "traducir" automáticamente las complejas respuestas JSON de la API a nuestras `data class` de Kotlin. Se utilizó la anotación `@SerializedName` para manejar claves JSON que no son compatibles con los nombres de variables de Kotlin.
* **Carga de Imágenes con Coil:** Para cargar las imágenes desde una URL, se implementó **Coil**, una biblioteca moderna recomendada por Google. Es ligera, rápida y maneja la caché y optimizaciones automáticamente con una simple función `.load()`.

---

## 💡 Retos Superados 
* **Manejo de Nulos en la API:** Se descubrió que la API no siempre devuelve la imagen de alta calidad. Se implementó una lógica de "Plan B" usando el operador Elvis (`?:`) de Kotlin para cargar un sprite de respaldo.
* **Depuración de la `Toolbar`:** Se aprendió la importancia de conectar explícitamente la `Activity` con su `Toolbar` usando `setSupportActionBar()` para que los menús y los botones de navegación aparezcan y funcionen.
* **Depuración de Crashes con Logcat:** Múltiples errores "fantasma" (como `ClassNotFoundException` o `NullPointerException` al inflar vistas) se diagnosticaron y resolvieron usando `Logcat` para leer el `stacktrace` e identificar la causa raíz.

---

## 📜 Licencia y Derechos de Autor

Este proyecto fue creado con fines educativos y de portafolio. El código fuente está disponible bajo la **Licencia MIT**.

Los datos son obtenidos de la increíble [PokeAPI](https://pokeapi.co/), un proyecto de código abierto.
