# Entrega de Proyecto: Interfaz Gráfica y Vinculación Lógica

**Nombre del Estudiante**  
[Tu Nombre Aquí]  

**Nombre de la Institución**  
[Tu Institución Aquí]  

**Asignatura / Curso**  
[Nombre de la Asignatura]  

**Fecha**  
[Día, Mes, Año]

---

## 1. Actividad Principal (MainActivity)

En esta sección se muestra la actividad principal de la aplicación, la cual contiene el contenedor de navegación y el menú lateral dinámico.

### 1.1 Pantallazo de la Interfaz (MainActivity)
*[INSERTAR PANTALLAZO DE LA APLICACIÓN MOSTRANDO LA PANTALLA PRINCIPAL Y EL MENÚ LATERAL AQUÍ]*

### 1.2 Código XML (`activity_main.xml`)
El siguiente código XML define la estructura del menú lateral (NavigationRailView) y el host de los fragmentos (FragmentContainerView).

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.google.android.material.navigationrail.NavigationRailView
        android:id="@+id/navigation_rail"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:menu="@menu/side_menu" />

    <View
        android:id="@+id/divider"
        android:layout_width="1dp"
        android:layout_height="match_parent"
        android:background="?android:attr/listDivider"
        app:layout_constraintStart_toEndOf="@id/navigation_rail"
        app:layout_constraintTop_toTopOf="parent" />

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toEndOf="@id/divider"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/nav_graph" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 1.3 Código Lógico (`MainActivity.kt`)
En el siguiente fragmento de código Kotlin se evidencia la declaración de variables y la vinculación de eventos usando **ViewBinding** y **Navigation Component**. Se aprecia cómo las variables lógicas se conectan con los identificadores XML.

```kotlin
package com.example.catalogoproductos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.catalogoproductos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Declaración del objeto para ViewBinding vinculado con activity_main.xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Vinculación de las variables con los identificadores XML
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Asignación de variables al FragmentContainer
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        // Método que enlaza la barra de navegación lateral con el NavController
        binding.navigationRail.setupWithNavController(navController)
    }
}
```

---

## 2. Fragmento de Catálogo (CatalogoFragment)

Esta sección aborda la pantalla del catálogo de productos, la cual contiene la barra de búsqueda interactiva y el listado de los productos.

### 2.1 Pantallazo de la Interfaz (Catálogo)
*[INSERTAR PANTALLAZO MOSTRANDO A LA APLICACIÓN CON LA LISTA DE PRODUCTOS Y LA BARRA DE BÚSQUEDA AQUÍ]*

### 2.2 Código XML (`fragment_catalogo.xml`)
Este Layout presenta un diseño vertical con la barra de texto que habilita la búsqueda, con sus correspondientes identificadores lógicos.

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:padding="8dp"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!--  Barra de búsqueda vinculada -->
    <EditText
        android:id="@+id/edtBuscar"
        android:hint="Buscar marca..."
        android:padding="10dp"
        android:background="@android:drawable/edit_text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

    <!-- Filtros de categoría -->
    <LinearLayout
        android:orientation="horizontal"
        android:layout_marginTop="8dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <Button
            android:id="@+id/btnTodos"
            android:text="Todos"
            android:layout_width="0dp"
            android:layout_weight="1"
            android:layout_height="wrap_content"/>

        <!-- ... -->
    </LinearLayout>

    <!-- Lista de productos mediante un RecyclerView -->
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvProductos"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_marginTop="8dp"
        android:layout_weight="1"/>
</LinearLayout>
```

### 2.3 Código Lógico (`CatalogoFragment.kt`)
A continuación se detalla el vínculo entre el XML y el código usando `findViewById`, además de la declaración del método (`Listeners`) para realizar los filtrados interactivos.

```kotlin
package com.example.catalogoproductos.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogoproductos.R

class CatalogoFragment : Fragment(R.layout.fragment_catalogo) {

    // Declaración de variable para el Recycler
    private lateinit var recycler: RecyclerView
    // ...

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vinculación de las variables gráficas con los IDs del XML correspondiente
        recycler = view.findViewById(R.id.rvProductos)
        val edtBuscar = view.findViewById<EditText>(R.id.edtBuscar)
        val btnTodos = view.findViewById<Button>(R.id.btnTodos)

        // Declaración de los métodos de evento de escucha (TextWatcher)
        edtBuscar.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                filtrar(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Implementación y vinculación del evento OnClickListener 
        btnTodos.setOnClickListener {
            // Lógica interna ...
        }
    }

    // Declaración de los métodos lógicos utilizados
    private fun filtrar(texto: String) {
        // Lógica de filtrado ...
    }
}
```

---

## 3. Fragmento de Perfil de Usuario (ProfileFragment)

Por último, esta sección abarca las vistas de Perfil, que despliega de forma dinámica el historial de usuario al ser pulsada e interactúa mediante eventos.

### 3.1 Pantallazo de la Interfaz (Perfil)
*[INSERTAR PANTALLAZO DE LA PESTAÑA PERFIL AQUÍ]*

### 3.2 Código Lógico (`ProfileFragment.kt`)
A continuación se ilustra la vinculación puntual y declaración del método que gobierna las condiciones descritas.

```kotlin
package com.example.catalogoproductos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.catalogoproductos.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Enlace del archivo fragment_profile.xml al fragmento actual
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vinculación de variables gráficas instanciándolas por su Id
        val btnHistorial = view.findViewById<Button>(R.id.btnHistorial)
        val btnFavoritos = view.findViewById<Button>(R.id.btnFavoritos)
        val layoutHistorial = view.findViewById<LinearLayout>(R.id.layoutHistorial)

        // Declaración de los métodos para accionar los eventos de click (Click Listeners)
        btnHistorial.setOnClickListener {
            // Declaraciones de lógica para establecer visibilidad
            if (layoutHistorial.visibility == View.GONE) {
                layoutHistorial.visibility = View.VISIBLE
            } else {
                layoutHistorial.visibility = View.GONE
            }
        }
    }
}
```
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
# Validaciones y Lógica Condicional de la Aplicación

La aplicación implementa múltiples validaciones para controlar el flujo de usuario, evitar errores en el ingreso de datos y gestionar la visibilidad de los elementos en pantalla dependiendo del contexto de interacción. A continuación, se detallan las validaciones técnicas más relevantes implementadas en el código (Kotlin):

---

## 1. Validaciones de Estado (Vistas Condicionales)

Estas validaciones verifican el estado actual de los contenedores gráficos para alternar su visibilidad sirviendo como interruptor (Toggle UI).

* **Despliegue del Historial (ProfileFragment.kt):**  
  Se valida si el historial está oculto (`View.GONE`). Si es cierto, lo hace visible (`View.VISIBLE`) y lanza un mensaje visual de éxito (Toast). De lo contrario, lo esconde. Esto evita inflar múltiples vistas simultáneamente y asegura un comportamiento hermético.  
  ```kotlin
  if (layoutHistorial.visibility == View.GONE) {
      layoutHistorial.visibility = View.VISIBLE
      Toast.makeText(requireContext(), "Mostrando historial", Toast.LENGTH_SHORT).show()
  } else {
      layoutHistorial.visibility = View.GONE
  }
  ```

* **Visibilidad del Carrito Vacío (CarritoFragment.kt):**  
  Al actualizar los totales matemáticos del carrito, existe una validación que enciende textos disuasivos (placeholder) si no hay elementos.  
  ```kotlin
  if (cartProducts.isEmpty()) {
      binding.emptyCartText.visibility = View.VISIBLE
      binding.cartRecyclerView.visibility = View.GONE
  } else {
      binding.emptyCartText.visibility = View.GONE
      binding.cartRecyclerView.visibility = View.VISIBLE
  }
  ```

---

## 2. Validaciones de Estructura de Datos (Búsqueda y Filtros)

En el fragmento del catálogo, se establecen validaciones cíclicas estrictas antes de reinyectar datos en el `RecyclerView`. Se impide que la lista muestre objetos que no cumplan con los parámetros solicitados por el usuario.

* **Filtros por Texto o Búsqueda Asíncrona (CatalogoFragment.kt):**  
  Un bloque `if` se encarga de ignorar las minúsculas/mayúsculas (true flag ignoreCase) y validar que el nombre base del perfume o de su marca contenga las letras exactas escritas en el bloque de texto.  
  ```kotlin
  if (producto.nombre.contains(texto, true) || producto.marca.contains(texto, true)) {
      listaFiltrada.add(producto)
  }
  ```

* **Filtros Parametrizados por Categorías (CatalogoFragment.kt):**  
  En los botones "Masc" y "Fem", la iteración valida la pre-existencia de la cadena (String) de la categoría dentro del arreglo nativo o lista de `notas` estipulada para cada producto.  
  ```kotlin
  if (producto.notas.contains(categoria)) {
      listaFiltrada.add(producto)
  }
  ```

---

## 3. Validaciones de Seguridad Preventiva (Navegación Intencional)

Este tipo de validaciones se utilizan para frenar que el aplicativo trate de resolver peticiones vacías a nivel del modelo de negocio, lo que comúnmente causaría excepciones gráficas a la hora de confirmar un recibo.

* **Prevención de Compras Inexistentes (CarritoFragment.kt):**  
  Al instante que el usuario pulsa el `checkoutButton` (Botón Pagar), se valida en memoria si la matriz interna está completamente vacía (sin elementos) `cartProducts.isEmpty()`. Así, evita lanzar la boleta virtual con un total negativo o falso e interrumpe la compra, optando por lanzar la alerta controlada `showEmptyCartDialog()`.  
  ```kotlin
  binding.checkoutButton.setOnClickListener {
      if (cartProducts.isEmpty()) {
          showEmptyCartDialog()
      } else {
          showConfirmPurchaseDialog()
      }
  }
  ```
