package com.example.inventario_app.vistas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventario_app.R;
import com.example.inventario_app.modelo.Producto;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    public interface OnProductoClickListener {
        void onEditarProducto(Producto producto);
        void onEliminarProducto(Producto producto);
    }

    private final List<Producto> listaProductos = new ArrayList<>();
    private final OnProductoClickListener listener;
    private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));

    public ProductoAdapter(OnProductoClickListener listener) {
        this.listener = listener;
    }

    public void setProductos(List<Producto> productos) {
        this.listaProductos.clear();
        if (productos != null) {
            this.listaProductos.addAll(productos);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        holder.bind(listaProductos.get(position), listener, formatoMoneda);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNombre;
        private final TextView tvCodigo;
        private final TextView tvTipo;
        private final TextView tvBadgeStock;
        private final TextView tvPrecioUnitario;
        private final TextView tvSubtotal;
        private final ImageButton btnEditar;
        private final ImageButton btnEliminar;
        private final ImageView imgTipoHardware;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvCodigo = itemView.findViewById(R.id.tvCodigoProducto);
            tvTipo = itemView.findViewById(R.id.tvTipoProducto);
            tvBadgeStock = itemView.findViewById(R.id.tvBadgeStock);
            tvPrecioUnitario = itemView.findViewById(R.id.tvPrecioUnitario);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            imgTipoHardware = itemView.findViewById(R.id.imgTipoHardware);
        }

        public void bind(Producto producto, OnProductoClickListener listener, NumberFormat formatoMoneda) {
            tvNombre.setText(producto.getNombre());
            tvCodigo.setText(producto.getCodigo());
            tvTipo.setText(producto.getTipo());

            tvPrecioUnitario.setText(formatoMoneda.format(producto.getPrecioUnitario()) + " c/u");
            tvSubtotal.setText("Subtotal: " + formatoMoneda.format(producto.getSubtotal()));

            int cantidad = producto.getCantidad();
            tvBadgeStock.setText("Stock: " + cantidad + " unid.");

            if (cantidad <= 3) {
                tvBadgeStock.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.badge_low_stock_bg));
                tvBadgeStock.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.badge_low_stock_text));
            } else {
                tvBadgeStock.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.badge_ok_stock_bg));
                tvBadgeStock.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.badge_ok_stock_text));
            }

            if (producto.getTipo() != null) {
                String tipo = producto.getTipo().toLowerCase();
                if (tipo.contains("procesador") || tipo.contains("cpu")) {
                    imgTipoHardware.setImageResource(R.drawable.ic_cpu);
                } else if (tipo.contains("video") || tipo.contains("gpu")) {
                    imgTipoHardware.setImageResource(R.drawable.ic_gpu);
                } else if (tipo.contains("ram") || tipo.contains("memoria")) {
                    imgTipoHardware.setImageResource(R.drawable.ic_ram);
                } else if (tipo.contains("almacenamiento") || tipo.contains("ssd") || tipo.contains("hdd")) {
                    imgTipoHardware.setImageResource(R.drawable.ic_storage);
                } else if (tipo.contains("madre") || tipo.contains("motherboard")) {
                    imgTipoHardware.setImageResource(R.drawable.ic_motherboard);
                } else if (tipo.contains("fuente") || tipo.contains("psu")) {
                    imgTipoHardware.setImageResource(R.drawable.ic_psu);
                } else if (tipo.contains("gabinete") || tipo.contains("case")) {
                    imgTipoHardware.setImageResource(R.drawable.ic_case);
                } else if (tipo.contains("enfriamiento") || tipo.contains("cooler")) {
                    imgTipoHardware.setImageResource(R.drawable.ic_cooler);
                } else if (tipo.contains("periférico") || tipo.contains("periferico")) {
                    imgTipoHardware.setImageResource(R.drawable.ic_peripheral);
                } else {
                    imgTipoHardware.setImageResource(R.drawable.ic_other);
                }
            } else {
                imgTipoHardware.setImageResource(R.drawable.ic_other);
            }

            btnEditar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditarProducto(producto);
                }
            });

            btnEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEliminarProducto(producto);
                }
            });
        }
    }
}
