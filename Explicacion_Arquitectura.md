# Explicación de la Arquitectura General de la Aplicación

El diseño y la estructura de la aplicación "Catálogo de Productos" están basados en los estándares recomendados por Google (Android Jetpack y Material Design). En lugar de usar múltiples Actividades, el sistema centraliza la navegación en un esquema de *Single-Activity Architecture* (Arquitectura de Actividad Única), apoyándose fuertemente en Fragmentos. A continuación, se detallan los elementos arquitectónicos principales:

---

## 1. Single-Activity Architecture y Fragments

La aplicación consta de una única Actividad (`MainActivity.kt`) que actúa como el lienzo o contenedor principal de la aplicación. Todo el contenido dinámico de las distintas pantallas (como el inicio, la tienda, el carrito, y el perfil) se construye mediante **Fragments** o Fragmentos.

Un **Fragmento** representa una porción de la interfaz de usuario que se puede reutilizar e intercambiar sin destruir la actividad subyacente.
* **Ventaja Principal:** Hace que la navegación entre secciones sea sumamente fluida. No hay pantallas de carga enteras (Intent routing) intentando cargar los menús de nuevo; solo cambia la zona derecha del aplicativo mientras el entorno permanece responsivo.
* **Uso en el proyecto:** Observamos fragmentos autónomos como `InicioFragment`, `CatalogoFragment`, y `ProfileFragment`, cada uno encargado de resolver un problema particular de la vista.

---

## 2. Navigation Component

El **Componente de Navegación (Navigation Component)** es la herramienta oficial de Android Jetpack que se utiliza para coordinar los intercambios entre estos Fragmentos. 

En esta aplicación, el flujo está gobernado por:
* **El NavGraph (`nav_graph.xml`):** Es un mapa visual de todas las pantallas disponibles dentro del sistema. Se definen individualmente todos los fragmentos con su nombre de clase e identificador único.
* **El NavHostFragment:** Es el espacio vacío insertado en la `MainActivity` (`<androidx.fragment.app.FragmentContainerView>`) preparado para recibir y mostrar los Fragmentos a los que viaja el componente. El componente de navegación infla el Fragmento solicitado directamente acá de forma segura y automatizada, manejando la pila de retrospectiva (las veces que el usuario presiona el botón "Atrás").

---

## 3. NavigationRail (Menú Lateral Material Design)

El entorno gráfico aprovecha el ecosistema Material Design de Android implementando una barra de navegación lateral, el **`NavigationRailView`**. 

* **Funcionalidad:** En lugar del clásico menú inferior (`BottomNavigationView`), el *Navigation Rail* se ancla ergonómicamente al lado izquierdo de la aplicación, proporcionando iconos para Inicio, Catálogo, Favoritos, Carrito, Perfil y Web. 
* **Vinculación dinámica:** Gracias a la directiva gráfica y lógica, el *NavigationRail* está directamente suscrito al controlador de navegación (`setupWithNavController`). Esto significa que con solo crear un ítem en el menú (como el `id="@+id/catalogoFragment"`) que comparta el **mismo ID** que en el NavGraph, Android interconecta automáticamente los clics; eliminando la necesidad de programar manualmente un `Listener` u ordenamiento manual para ir de un punto a otro.

---

## 4. RecyclerView y Adapters

La vista interna del catálogo incluye el uso fundamental de un **`RecyclerView`** con la conjunción de una cuadrícula (`GridLayoutManager`). 

El `RecyclerView` es un widget avanzado que sustituyó a las clásicas ListView. Su función principal es el patrón de **reciclaje de vistas**. 
* Mientras el usuario empuje (scroll) una lista inmensa hacia arriba o abajo, no se crearán mil plantillas XML para mostrar mil perfumes. Simplemente se retiran de la pantalla las plantillas superiores que van desapareciendo y se sitúan reactualmente por debajo para llenarse con información nueva.
* **Uso en el proyecto:** Esto es complementado en `CatalogoFragment` donde el `RecyclerView` tiene inyectado un adaptador (`ProductoAdapter`). El adaptador conecta los ítems sueltos (del modelo `Producto`) con un archivo XML en miniatura (`item_producto.xml`), pintando exitosamente los nombres, los precios y las descripciones de las marcas.
