package com.example.inventario_app.modelo;

public class Producto {
    private final String codigo;
    private String nombre;
    private String tipo;
    private int cantidad;
    private double precioUnitario;

    //Recibe los los datos de producto, la verificacion que no esten nulos
    public Producto(String codigo, String nombre, String tipo, int cantidad, double precioUnitario) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del producto es obligatorio");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo del producto es obligatorio");
        }
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo");
        }
        //Elinmina los espacios en blanco
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.tipo = tipo.trim();
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    //get para retornar el codigo
    public String getCodigo() {
        return codigo;
    }
    //get para retornar el nombre de un prodructo
    public String getNombre() {
        return nombre;
    }
    //set actualizar el nombre
    public void setNombre(String nombre) {
        //verifica que el nombre no sea nulo o este vacio
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        //actializa el nombre eliminando espacios
        this.nombre = nombre.trim();
    }
    //get para obtener el tipo
    public String getTipo() {
        return tipo;
    }
    //set para actualizar el tipo
    public void setTipo(String tipo) {
        //verifica que el tipo no sea nulo o este vacio
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo del producto es obligatorio");
        }
        //actualiza el tipo eliminando espacios
        this.tipo = tipo.trim();
    }
    //get para obtener la cantidad
    public int getCantidad() {
        return cantidad;
    }
    //set para actualzar
    public void setCantidad(int cantidad) {
        //verifica que la cantidad no sea menor a 0
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        //asigna el valor al atributo
        this.cantidad = cantidad;
    }
    //get para retornar el precio unitario
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    //set para actualzar el precio unitario
    public void setPrecioUnitario(double precioUnitario) {
        //verifica que el precio no sea menor a 0
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo");
        }
        //asigbna el valor al atributo
        this.precioUnitario = precioUnitario;
    }
    //
    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
