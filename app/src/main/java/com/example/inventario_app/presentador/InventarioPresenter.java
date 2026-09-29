package com.example.inventario_app.presentador;

import com.example.inventario_app.modelo.Inventario;
import com.example.inventario_app.modelo.InventarioDatabaseHelper;
import com.example.inventario_app.modelo.Producto;

import java.util.ArrayList;

public class InventarioPresenter implements InventarioContrato.PresentadorPrincipal {

    private final InventarioContrato.VistaPrincipal vista;
    private final Inventario inventario;
    private final InventarioDatabaseHelper dbHelper;
    private String consultaActual = "";

    public InventarioPresenter(InventarioContrato.VistaPrincipal vista, InventarioDatabaseHelper dbHelper) {
        this.vista = vista;
        this.dbHelper = dbHelper;
        this.inventario = new Inventario();
    }

    @Override
    public void cargarInventario() {
        try {
            dbHelper.cargarEnInventario(inventario);
            actualizarVista();
        } catch (Exception e) {
            vista.mostrarError("Error al cargar el inventario: " + e.getMessage());
        }
    }

    @Override
    public void buscarProductos(String consulta) {
        this.consultaActual = consulta != null ? consulta : "";
        actualizarVista();
    }

    @Override
    public void registrarNuevoProducto(String codigo, String nombre, String tipo, int cantidad, double precio) {
        try {
            Producto nuevo = new Producto(codigo, nombre, tipo, cantidad, precio);
            inventario.agregarProducto(nuevo);

            boolean insertado = dbHelper.insertarProducto(nuevo);
            if (insertado) {
                vista.mostrarMensaje("Producto registrado con éxito");
                actualizarVista();
            } else {
                inventario.eliminarProducto(codigo);
                vista.mostrarError("No se pudo guardar en la base de datos");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        } catch (Exception e) {
            vista.mostrarError("Error inesperado: " + e.getMessage());
        }
    }

    @Override
    public void incrementarStockProducto(String codigo, int cantidadAdicional) {
        try {
            if (codigo == null || codigo.trim().isEmpty()) {
                vista.mostrarError("Seleccione un producto válido");
                return;
            }
            if (cantidadAdicional <= 0) {
                vista.mostrarError("La cantidad a adicionar debe ser mayor a 0");
                return;
            }

            Producto existente = inventario.buscarProducto(codigo);
            if (existente == null) {
                vista.mostrarError("El producto especificado no existe");
                return;
            }

            int nuevaCantidad = existente.getCantidad() + cantidadAdicional;
            existente.setCantidad(nuevaCantidad);

            boolean actualizado = dbHelper.actualizarCantidadProducto(codigo, nuevaCantidad);
            if (actualizado) {
                vista.mostrarMensaje("Stock incrementado (+ " + cantidadAdicional + ")");
                actualizarVista();
            } else {
                vista.mostrarError("No se pudo actualizar el stock en la base de datos");
            }
        } catch (Exception e) {
            vista.mostrarError("Error al actualizar stock: " + e.getMessage());
        }
    }

    @Override
    public void actualizarProducto(String codigo, String nombre, String tipo, int cantidad, double precio) {
        try {
            Producto producto = inventario.buscarProducto(codigo);
            if (producto == null) {
                vista.mostrarError("Producto no encontrado");
                return;
            }

            producto.   setNombre(nombre);
            producto.setTipo(tipo);
            producto.setCantidad(cantidad);
            producto.setPrecioUnitario(precio);

            boolean actualizado = dbHelper.actualizarProducto(producto);
            if (actualizado) {
                vista.mostrarMensaje("Producto actualizado correctamente");
                actualizarVista();
            } else {
                vista.mostrarError("Error al actualizar la base de datos");
            }
        } catch (IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        } catch (Exception e) {
            vista.mostrarError("Error inesperado: " + e.getMessage());
        }
    }

    @Override
    public void eliminarProducto(String codigo) {
        try {
            boolean eliminadoTda = inventario.eliminarProducto(codigo);
            boolean eliminadoDb = dbHelper.eliminarProducto(codigo);

            if (eliminadoTda && eliminadoDb) {
                vista.mostrarMensaje("Producto eliminado del inventario");
                actualizarVista();
            } else {
                vista.mostrarError("No se pudo eliminar el producto completamente");
            }
        } catch (Exception e) {
            vista.mostrarError("Error al eliminar producto: " + e.getMessage());
        }
    }

    private void actualizarVista() {
        ArrayList<Producto> listaFiltrada = inventario.buscarPorFiltro(consultaActual);
        vista.mostrarProductos(listaFiltrada);

        double valorTotal = inventario.calcularValorTotal();
        int totalItems = inventario.listarProductos().size();

        vista.actualizarResumen(valorTotal, totalItems);

        if (listaFiltrada.isEmpty()) {
            if (consultaActual.trim().isEmpty()) {
                vista.mostrarEstadoVacio(true, "No hay productos en inventario");
            } else {
                vista.mostrarEstadoVacio(true, "Sin resultados para: \"" + consultaActual + "\"");
            }
        } else {
            vista.mostrarEstadoVacio(false, "");
        }
    }

    public ArrayList<Producto> getTodosLosProductos() {
        return inventario.listarProductos();
    }
}
