package com.example.inventario_app.vistas;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.inventario_app.R;
import com.example.inventario_app.modelo.Producto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AgregarProductoDialog extends Dialog {

    public interface OnAgregarProductoListener {
        void onRegistrarNuevo(String codigo, String nombre, String tipo, int cantidad, double precio);
        void onIncrementarStock(String codigo, int cantidadAdicional);
    }

    private final List<Producto> productosExistentes;
    private final OnAgregarProductoListener listener;

    private RadioGroup rgModo;
    private RadioButton rbNuevo;
    private RadioButton rbIncrementar;
    private LinearLayout layoutNuevo;
    private LinearLayout layoutIncrementar;

    private TextInputLayout tilCodigo, tilNombre, tilCantidad, tilPrecioUnitario, tilCantidadAdicional;
    private TextInputEditText etCodigo, etNombre, etCantidad, etPrecioUnitario, etCantidadAdicional;
    private Spinner spTipoHardware, spProductosExistentes;

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

    public AgregarProductoDialog(@NonNull Context context, List<Producto> productosExistentes, OnAgregarProductoListener listener) {
        super(context);
        this.productosExistentes = productosExistentes != null ? productosExistentes : new ArrayList<>();
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_agregar_producto);

        if (getWindow() != null) {
            getWindow().setLayout(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        initViews();
        setupSpinners();
        setupEvents();
    }

    private void initViews() {
        rgModo = findViewById(R.id.rgModoAgregar);
        rbNuevo = findViewById(R.id.rbNuevoProducto);
        rbIncrementar = findViewById(R.id.rbIncrementarStock);
        layoutNuevo = findViewById(R.id.layoutNuevoProducto);
        layoutIncrementar = findViewById(R.id.layoutIncrementarStock);

        tilCodigo = findViewById(R.id.tilCodigo);
        tilNombre = findViewById(R.id.tilNombre);
        tilCantidad = findViewById(R.id.tilCantidad);
        tilPrecioUnitario = findViewById(R.id.tilPrecioUnitario);
        tilCantidadAdicional = findViewById(R.id.tilCantidadAdicional);

        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etCantidad = findViewById(R.id.etCantidad);
        etPrecioUnitario = findViewById(R.id.etPrecioUnitario);
        etCantidadAdicional = findViewById(R.id.etCantidadAdicional);

        spTipoHardware = findViewById(R.id.spTipoHardware);
        spProductosExistentes = findViewById(R.id.spProductosExistentes);
    }

    private void setupSpinners() {
        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, TIPOS_HARDWARE
        );
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoHardware.setAdapter(adapterTipos);

        List<String> itemsProductos = new ArrayList<>();
        for (Producto p : productosExistentes) {
            itemsProductos.add(p.getCodigo() + " - " + p.getNombre() + " (Stock actual: " + p.getCantidad() + ")");
        }
        if (itemsProductos.isEmpty()) {
            itemsProductos.add("No hay productos registrados");
            rbIncrementar.setEnabled(false);
        }

        ArrayAdapter<String> adapterExistentes = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, itemsProductos
        );
        adapterExistentes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProductosExistentes.setAdapter(adapterExistentes);
    }

    private void setupEvents() {
        rgModo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbNuevoProducto) {
                layoutNuevo.setVisibility(View.VISIBLE);
                layoutIncrementar.setVisibility(View.GONE);
            } else {
                layoutNuevo.setVisibility(View.GONE);
                layoutIncrementar.setVisibility(View.VISIBLE);
            }
        });

        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        btnCancelar.setOnClickListener(v -> dismiss());

        btnGuardar.setOnClickListener(v -> procesarGuardar());
    }

    private void procesarGuardar() {
        if (rbNuevo.isChecked()) {
            guardarNuevoProducto();
        } else {
            incrementarStock();
        }
    }

    private void guardarNuevoProducto() {
        tilCodigo.setError(null);
        tilNombre.setError(null);
        tilCantidad.setError(null);
        tilPrecioUnitario.setError(null);

        String codigo = etCodigo.getText() != null ? etCodigo.getText().toString().trim() : "";
        String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String tipo = spTipoHardware.getSelectedItem() != null ? spTipoHardware.getSelectedItem().toString() : "";
        String strCantidad = etCantidad.getText() != null ? etCantidad.getText().toString().trim() : "";
        String strPrecio = etPrecioUnitario.getText() != null ? etPrecioUnitario.getText().toString().trim() : "";

        boolean valido = true;

        if (codigo.isEmpty()) {
            tilCodigo.setError("El código es obligatorio");
            valido = false;
        }

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
            listener.onRegistrarNuevo(codigo, nombre, tipo, cantidad, precio);
            dismiss();
        }
    }

    private void incrementarStock() {
        tilCantidadAdicional.setError(null);

        if (productosExistentes.isEmpty()) {
            Toast.makeText(getContext(), "No hay productos registrados para incrementar stock", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = spProductosExistentes.getSelectedItemPosition();
        if (selectedIndex < 0 || selectedIndex >= productosExistentes.size()) {
            Toast.makeText(getContext(), "Seleccione un producto válido", Toast.LENGTH_SHORT).show();
            return;
        }

        Producto seleccionado = productosExistentes.get(selectedIndex);
        String strAdicional = etCantidadAdicional.getText() != null ? etCantidadAdicional.getText().toString().trim() : "";

        if (strAdicional.isEmpty()) {
            tilCantidadAdicional.setError("Ingrese la cantidad a adicionar");
            return;
        }

        int cantidadAdicional;
        try {
            cantidadAdicional = Integer.parseInt(strAdicional);
            if (cantidadAdicional <= 0) {
                tilCantidadAdicional.setError("La cantidad debe ser mayor a 0");
                return;
            }
        } catch (NumberFormatException e) {
            tilCantidadAdicional.setError("Número inválido");
            return;
        }

        if (listener != null) {
            listener.onIncrementarStock(seleccionado.getCodigo(), cantidadAdicional);
            dismiss();
        }
    }
}
