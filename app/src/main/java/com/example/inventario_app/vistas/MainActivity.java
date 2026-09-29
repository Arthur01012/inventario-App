package com.example.inventario_app.vistas;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventario_app.R;
import com.example.inventario_app.modelo.InventarioDatabaseHelper;
import com.example.inventario_app.modelo.Producto;
import com.example.inventario_app.presentador.InventarioContrato;
import com.example.inventario_app.presentador.InventarioPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements InventarioContrato.VistaPrincipal, ProductoAdapter.OnProductoClickListener {

    private InventarioPresenter presenter;
    private ProductoAdapter adapter;

    private TextView tvValorTotal;
    private TextView tvTotalProductos;
    private TextView tvMensajeVacio;
    private LinearLayout layoutVacio;
    private RecyclerView rvProductos;
    private EditText etBuscar;
    private FloatingActionButton fabAgregar;

    private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupRecyclerView();
        setupPresenter();
        setupSearchAndFab();

        presenter.cargarInventario();
    }

    private void initViews() {
        tvValorTotal = findViewById(R.id.tvValorTotal);
        tvTotalProductos = findViewById(R.id.tvTotalProductos);
        tvMensajeVacio = findViewById(R.id.tvMensajeVacio);
        layoutVacio = findViewById(R.id.layoutVacio);
        rvProductos = findViewById(R.id.rvProductos);
        etBuscar = findViewById(R.id.etBuscar);
        fabAgregar = findViewById(R.id.fabAgregar);
    }

    private void setupRecyclerView() {
        adapter = new ProductoAdapter(this);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        rvProductos.setAdapter(adapter);
    }

    private void setupPresenter() {
        InventarioDatabaseHelper dbHelper = new InventarioDatabaseHelper(this);
        presenter = new InventarioPresenter(this, dbHelper);
    }

    private void setupSearchAndFab() {
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.buscarProductos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAgregar.setOnClickListener(v -> abrirDialogoAgregar());
    }

    private void abrirDialogoAgregar() {
        AgregarProductoDialog dialog = new AgregarProductoDialog(
                this,
                presenter.getTodosLosProductos(),
                new AgregarProductoDialog.OnAgregarProductoListener() {
                    @Override
                    public void onRegistrarNuevo(String codigo, String nombre, String tipo, int cantidad, double precio) {
                        presenter.registrarNuevoProducto(codigo, nombre, tipo, cantidad, precio);
                    }

                    @Override
                    public void onIncrementarStock(String codigo, int cantidadAdicional) {
                        presenter.incrementarStockProducto(codigo, cantidadAdicional);
                    }
                }
        );
        dialog.show();
    }

    @Override
    public void mostrarProductos(ArrayList<Producto> productos) {
        adapter.setProductos(productos);
    }

    @Override
    public void actualizarResumen(double valorTotal, int totalProductos) {
        tvValorTotal.setText(formatoMoneda.format(valorTotal));
        String textoTotal = totalProductos + (totalProductos == 1 ? " producto" : " productos");
        tvTotalProductos.setText(textoTotal);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarError(String error) {
        new AlertDialog.Builder(this)
                .setTitle("Atención")
                .setMessage(error)
                .setPositiveButton("Aceptar", null)
                .show();
    }

    @Override
    public void mostrarEstadoVacio(boolean vacio, String mensaje) {
        if (vacio) {
            layoutVacio.setVisibility(View.VISIBLE);
            rvProductos.setVisibility(View.GONE);
            tvMensajeVacio.setText(mensaje);
        } else {
            layoutVacio.setVisibility(View.GONE);
            rvProductos.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEditarProducto(Producto producto) {
        EditarProductoDialog dialog = new EditarProductoDialog(
                this,
                producto,
                (codigo, nombre, tipo, cantidad, precio) ->
                        presenter.actualizarProducto(codigo, nombre, tipo, cantidad, precio)
        );
        dialog.show();
    }

    @Override
    public void onEliminarProducto(Producto producto) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Producto")
                .setMessage("¿Desea eliminar el producto " + producto.getNombre() + " (" + producto.getCodigo() + ")?")
                .setPositiveButton("Eliminar", (d, which) -> presenter.eliminarProducto(producto.getCodigo()))
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
