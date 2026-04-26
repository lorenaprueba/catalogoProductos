# Validaciones Básicas de Negocio

En el contexto comercial de la aplicación "Catálogo de Productos", se han implementado diversas validaciones enfocadas netamente en la **lógica de negocio**. El objetivo de estas precauciones es evitar que un usuario altere las transacciones, genere recibos de compras inexistentes o modifique las matemáticas de su carrito llevándolo a un estado fraudulento.

A continuación, se explican las validaciones de negocio presentes en el flujo de la canasta comercial (`CarritoFragment` y `CartAdapter`):

---

## 1. Validación de Cantidad Mínima Restrictiva

Cuando un usuario interactúa con los controles para decrementar (`-`) la cantidad de un producto que ya ha sumado al carrito, el sistema impone una restricción lógica: **Un producto no puede tener cantidad igual a 0 o negativa si permanece explícitamente en la lista.**

* **Explicación Lógica:** Si el usuario presiona repetidamente el botón de restar, la variable de cantidad del producto se descuenta de $n$ hasta $1$. Si la cantidad es exactamente $1$, el código detiene matemáticamente la resta. 
* **Fragmento de su implementación:**
  ```kotlin
  // Dentro del CartAdapter.kt (Botón Disminuir)
  binding.buttonDecrease.setOnClickListener {
      if (product.quantity > 1) {             // Validación de Negocio Crítica
          product.quantity--                  // Solamente decrece si es estrictamente mayor a 1
          binding.quantityText.text = product.quantity.toString()
          binding.totalPrice.text = "Total: $ ${String.format("%,.0f", product.getTotalPrice())}"
          onQuantityChanged(product)
      }
  }
  ```

---

## 2. Escudo de Confirmación (Prevención de Pérdida de Conversiones)

Desde una perspectiva comercial, es imperativo evitar que los usuarios borren accidentalmente productos que ya tienen la intención de comprar. Una eliminación accidental puede provocar frustración y llevar al abandono del carrito de compras.

* **Explicación Lógica:** Al presionar el botón de **Eliminar** (Cesto de basura), el sistema entra en un estado de espera (Suspense). Invoca un `MaterialAlertDialogBuilder` forzando al comprador a re-validar su intención ("¿Estás seguro de que deseas eliminar este producto?"). Sólo si el usuario aprueba conscientemente el diálogo, el objeto se destruye de la memoria interna.

---

## 3. Prevención de Chequeos y Recibos Vacíos (Falsas Transacciones)

El botón principal destinado a emitir la orden final de compra (`checkoutButton`) representa el final del embudo transaccional (Funnel). Debe estar estrechamente vigilado para que no emita pagos nulos.

* **Explicación Lógica:** En un contexto contable, solicitar y confirmar un cobro por la suma de "Cero pesos en cero elementos" genera basuras en la base de datos (Dirty Data). Al invocar este botón, la matriz de productos es auditada (`cartProducts.isEmpty()`). 
* Si se encuentra totalmente libre de productos, el sistema bloquea interactivamente la compra y lanza una alerta visual indicando de forma explícita que su carrito requiere elementos. Solo al llenarla, se habilita el diálogo monetario confirmatorio de la orden total.
* **Fragmento de su implementación:**
  ```kotlin
  // Dentro de CarritoFragment.kt
  binding.checkoutButton.setOnClickListener {
      if (cartProducts.isEmpty()) {      // Validación contra transacciones vacías
          showEmptyCartDialog()          // Desvío preventivo
      } else {
          showConfirmPurchaseDialog()    // Éxito
      }
  }
  ```
