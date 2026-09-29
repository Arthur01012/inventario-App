package com.example.inventario_app.modelo;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private final List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public ArrayList<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

    public void agregarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        for (Producto actual : productos) {
            if (actual.getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un producto registrado con ese código");
            }
        }

        productos.add(producto);
    }

    public boolean eliminarProducto(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es inválido");
        }

        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo().equalsIgnoreCase(codigo)) {
                productos.remove(i);
                return true;
            }
        }

        return false;
    }

    public Producto buscarProducto(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }

        for (Producto producto : productos) {
            if (producto.getCodigo().equalsIgnoreCase(codigo)) {
                return producto;
            }
        }

        return null;
    }

    public ArrayList<Producto> buscarPorFiltro(String consulta) {
        ArrayList<Producto> resultados = new ArrayList<>();
        if (consulta == null || consulta.trim().isEmpty()) {
            return getProductos();
        }
        String filtro = consulta.trim().toLowerCase();
        for (Producto p : productos) {
            if (p.getCodigo().toLowerCase().contains(filtro) ||
                p.getNombre().toLowerCase().contains(filtro) ||
                p.getTipo().toLowerCase().contains(filtro)) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    public double calcularValorTotal() {
        double total = 0;
        for (Producto producto : productos) {
            total += producto.getSubtotal();
        }
        return total;
    }

    public ArrayList<Producto> listarProductos() {
        return getProductos();
    }

    public void limpiar() {
        productos.clear();
    }
}
