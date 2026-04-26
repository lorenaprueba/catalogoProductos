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
