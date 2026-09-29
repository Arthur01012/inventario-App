package com.example.inventario_app.presentador;

import com.example.inventario_app.modelo.Producto;

import java.util.ArrayList;

public interface InventarioContrato {

    interface VistaPrincipal {
        void mostrarProductos(ArrayList<Producto> productos);
        void actualizarResumen(double valorTotal, int totalProductos);
        void mostrarMensaje(String mensaje);
        void mostrarError(String error);
        void mostrarEstadoVacio(boolean vacio, String mensaje);
    }

    interface PresentadorPrincipal {
        void cargarInventario();
        void buscarProductos(String consulta);
        void registrarNuevoProducto(String codigo, String nombre, String tipo, int cantidad, double precio);
        void incrementarStockProducto(String codigo, int cantidadAdicional);
        void actualizarProducto(String codigo, String nombre, String tipo, int cantidad, double precio);
        void eliminarProducto(String codigo);
    }
}
