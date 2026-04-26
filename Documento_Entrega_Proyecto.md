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
