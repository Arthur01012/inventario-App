package com.example.inventario_app.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventarioDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventario_hardware.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTOS = "productos";
    public static final String COLUMN_CODIGO = "codigo";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_TIPO = "tipo";
    public static final String COLUMN_CANTIDAD = "cantidad";
    public static final String COLUMN_PRECIO = "precio_unitario";

    public InventarioDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_PRODUCTOS + " (" +
                COLUMN_CODIGO + " TEXT PRIMARY KEY, " +
                COLUMN_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_TIPO + " TEXT NOT NULL, " +
                COLUMN_CANTIDAD + " INTEGER NOT NULL, " +
                COLUMN_PRECIO + " REAL NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
    }

    public void cargarEnInventario(Inventario inventario) {
        inventario.limpiar();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTOS, null, null, null, null, null, null);

        if (cursor != null) {
            try {
                int idxCodigo = cursor.getColumnIndexOrThrow(COLUMN_CODIGO);
                int idxNombre = cursor.getColumnIndexOrThrow(COLUMN_NOMBRE);
                int idxTipo = cursor.getColumnIndexOrThrow(COLUMN_TIPO);
                int idxCantidad = cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD);
                int idxPrecio = cursor.getColumnIndexOrThrow(COLUMN_PRECIO);

                while (cursor.moveToNext()) {
                    String codigo = cursor.getString(idxCodigo);
                    String nombre = cursor.getString(idxNombre);
                    String tipo = cursor.getString(idxTipo);
                    int cantidad = cursor.getInt(idxCantidad);
                    double precio = cursor.getDouble(idxPrecio);

                    Producto producto = new Producto(codigo, nombre, tipo, cantidad, precio);
                    inventario.agregarProducto(producto);
                }
            } finally {
                cursor.close();
            }
        }
    }

    public boolean insertarProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO, producto.getCodigo());
        values.put(COLUMN_NOMBRE, producto.getNombre());
        values.put(COLUMN_TIPO, producto.getTipo());
        values.put(COLUMN_CANTIDAD, producto.getCantidad());
        values.put(COLUMN_PRECIO, producto.getPrecioUnitario());

        long result = db.insert(TABLE_PRODUCTOS, null, values);
        return result != -1;
    }

    public boolean actualizarProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, producto.getNombre());
        values.put(COLUMN_TIPO, producto.getTipo());
        values.put(COLUMN_CANTIDAD, producto.getCantidad());
        values.put(COLUMN_PRECIO, producto.getPrecioUnitario());

        int rows = db.update(TABLE_PRODUCTOS, values, COLUMN_CODIGO + " = ?", new String[]{producto.getCodigo()});
        return rows > 0;
    }

    public boolean actualizarCantidadProducto(String codigo, int nuevaCantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANTIDAD, nuevaCantidad);

        int rows = db.update(TABLE_PRODUCTOS, values, COLUMN_CODIGO + " = ?", new String[]{codigo});
        return rows > 0;
    }

    public boolean eliminarProducto(String codigo) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_PRODUCTOS, COLUMN_CODIGO + " = ?", new String[]{codigo});
        return rows > 0;
    }
}
