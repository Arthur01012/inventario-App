package com.example.inventario_app.vistas;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.inventario_app.R;
import com.example.inventario_app.modelo.Producto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditarProductoDialog extends Dialog {

    public interface OnEditarProductoListener {
        void onGuardarEdicion(String codigo, String nombre, String tipo, int cantidad, double precio);
    }

    private final Producto producto;
    private final OnEditarProductoListener listener;

    private TextInputLayout tilNombre, tilCantidad, tilPrecioUnitario;
    private TextInputEditText etCodigo, etNombre, etCantidad, etPrecioUnitario;
    private Spinner spTipoHardware;

    private static final String[] TIPOS_HARDWARE = {
            "Procesador (CPU)",
            "Tarjeta de Video (GPU)",
            "Memoria RAM",
            "Almacenamiento (SSD / HDD)",
            "Tarjeta Madre (Motherboard)",
            "Fuente de Poder (PSU)",
            "Gabinete",
            "Enfriamiento / Cooler",
            "Periférico",
            "Otro Componente"
    };

    public EditarProductoDialog(@NonNull Context context, Producto producto, OnEditarProductoListener listener) {
        super(context);
        this.producto = producto;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_editar_producto);

        if (getWindow() != null) {
            getWindow().setLayout(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        initViews();
        setupSpinner();
        cargarDatosProducto();
        setupEvents();
    }

    private void initViews() {
        etCodigo = findViewById(R.id.etEditCodigo);
        tilNombre = findViewById(R.id.tilEditNombre);
        etNombre = findViewById(R.id.etEditNombre);
        tilCantidad = findViewById(R.id.tilEditCantidad);
        etCantidad = findViewById(R.id.etEditCantidad);
        tilPrecioUnitario = findViewById(R.id.tilEditPrecioUnitario);
        etPrecioUnitario = findViewById(R.id.etEditPrecioUnitario);
        spTipoHardware = findViewById(R.id.spEditTipoHardware);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, TIPOS_HARDWARE
        );
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoHardware.setAdapter(adapterTipos);
    }

    private void cargarDatosProducto() {
        if (producto != null) {
            etCodigo.setText(producto.getCodigo());
            etNombre.setText(producto.getNombre());
            etCantidad.setText(String.valueOf(producto.getCantidad()));
            etPrecioUnitario.setText(String.valueOf(producto.getPrecioUnitario()));

            for (int i = 0; i < TIPOS_HARDWARE.length; i++) {
                if (TIPOS_HARDWARE[i].equalsIgnoreCase(producto.getTipo())) {
                    spTipoHardware.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setupEvents() {
        Button btnCancelar = findViewById(R.id.btnEditCancelar);
        Button btnGuardar = findViewById(R.id.btnEditGuardar);

        btnCancelar.setOnClickListener(v -> dismiss());
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void guardarCambios() {
        tilNombre.setError(null);
        tilCantidad.setError(null);
        tilPrecioUnitario.setError(null);

        String codigo = producto.getCodigo();
        String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String tipo = spTipoHardware.getSelectedItem() != null ? spTipoHardware.getSelectedItem().toString() : "";
        String strCantidad = etCantidad.getText() != null ? etCantidad.getText().toString().trim() : "";
        String strPrecio = etPrecioUnitario.getText() != null ? etPrecioUnitario.getText().toString().trim() : "";

        boolean valido = true;

        if (nombre.isEmpty()) {
            tilNombre.setError("El nombre es obligatorio");
            valido = false;
        }

        int cantidad = 0;
        if (strCantidad.isEmpty()) {
            tilCantidad.setError("Ingrese la cantidad");
            valido = false;
        } else {
            try {
                cantidad = Integer.parseInt(strCantidad);
                if (cantidad < 0) {
                    tilCantidad.setError("La cantidad no puede ser negativa");
                    valido = false;
                }
            } catch (NumberFormatException e) {
                tilCantidad.setError("Número inválido");
                valido = false;
            }
        }

        double precio = 0;
        if (strPrecio.isEmpty()) {
            tilPrecioUnitario.setError("Ingrese el precio unitario");
            valido = false;
        } else {
            try {
                precio = Double.parseDouble(strPrecio);
                if (precio < 0) {
                    tilPrecioUnitario.setError("El precio no puede ser negativo");
                    valido = false;
                }
            } catch (NumberFormatException e) {
                tilPrecioUnitario.setError("Precio inválido");
                valido = false;
            }
        }

        if (valido && listener != null) {
            listener.onGuardarEdicion(codigo, nombre, tipo, cantidad, precio);
            dismiss();
        }
    }
}
