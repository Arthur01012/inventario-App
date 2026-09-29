Aplicación de Gestión de Inventario de Hardware en Android (Java)

===Ficha Técnica del Proyecto===
-Patrón de Arquitectura: Model-View-Presenter (MVP)
Estructura de Datos (TDA): Contenedor TDA Inventario y Elemento TDA Producto
Persistencia Local: SQLite (mediante SQLiteOpenHelper)
Lenguaje & SDK: Java 11 / Android SDK (Min API 26, Target SDK 37)
Sistema de Diseño: Material Design 3 (Paleta Ocean Hardware)
=========================================================================================================================================

TDA Elemento: Producto.java
Representa la entidad individual de un componente de hardware. Posee atributos inmutables para la clave
primaria y métodos accesores/mutadores con validación de dominio.

===Producto(...) --- Constructor --- Inicializa el producto. Valida que código, nombre y tipo no sean
nulos/vacíos, y que cantidad y precio unitario sean ‡ 0.
===getCodigo() --- String --- Retorna la clave única inmutable del producto.
===getNombre() / setNombre() --- String / void --- Obtiene o establece el nombre. Lanza IllegalArgumentException si es nulo
o vacío
===getTipo() / setTipo() --- String / void ---  Obtiene o establece la categoría de hardware (CPU, GPU, RAM, etc.).
===getCantidad() / setCantidad() --- int / void --- Obtiene o establece la cantidad en stock. Lanza excepción si la cantidad
es menor a cero.
===getPrecioUnitario() / setPrecioUnitario() --- double / void --- Obtiene o establece el precio unitario en moneda local.
===getSubtotal() --- double --- Calcula dinámicamente el valor total de la línea (cantidad * precioUnitario).

=========================================================================================================================================

TDA Contenedor: Inventario.java

===agregarProducto(Producto p) --- void --- Verifica que no exista otro producto con el mismo código e inserta el ítem en la colección.
===eliminarProducto(String codigo --- boolean --- Busca por clave única y remueve el producto de la colección si se encuentra.
===buscarProducto(String codigo) --- Producto --- Retorna la referencia del producto coincidente o null si no existe
===buscarPorFiltro(String consulta) ---  ArrayList<Producto> --- Filtra en tiempo real comparando la cadena de consulta contra código, nombre o categoría.
===calcularValorTotal() --- double --- Acumula el subtotal de todos los productos almacenados en el inventario.
===getProductos() / listarProductos() --- ArrayList<Producto> --- Devuelve una copia defensiva de la lista de productos para proteger la encapsulación.
===limpiar() --- void --- Vacía por completo la lista en memoria (utilizado antes de recargar de SQLite).
