/************************************************************************************************
 *              TRABAJO PRACTICO 8: BASES DE DATOS PARA ANDROID                                 *
 *                              John David Molina                                               *
 ************************************************************************************************/
package com.johnmolina.ca_tp8_bd1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBSimple extends SQLiteOpenHelper {
    SQLiteDatabase db; // base de datos a usar
    Cursor cursor;



    public DBSimple(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        db = this.getWritableDatabase(); // que sea en modo escritura, no solo lectura
        String query = "select * from productos";
        cursor = db.rawQuery(query, null);
    } //constructor


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table productos(nombre text, precio single, descripcion text)"); // crea la tabla
    } //onCreate


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // no hacemos nada
    }



    /****************************************************************************************
     *         Metodos agregar, borrar y modificar registros de la tabla                    *
     ****************************************************************************************/

    public boolean insert(String nombre, float precio, String descripcion) {
        ContentValues contenedor = getContentValues(nombre, precio, descripcion);
        if ( db.insert("productos", null, contenedor)  != -1 )
            return true;
        else
            return false;
    } // insert



    public int delete(String nombre) {
        int cuantosBorro = db.delete("productos", "nombre='" + nombre + "'", null);
        return cuantosBorro;
    } //delete



    public int update(String nombre, float precio, String descripcion) {
        ContentValues contenedor = getContentValues(nombre, precio, descripcion);
        int cuantosActualizo = db.update("productos", contenedor, "nombre='" + nombre + "'", null);
        return cuantosActualizo;
    } // update



    public ArrayList<String> generarLista() {
        ArrayList<String> lista = new ArrayList<>();
        String query = "select * from productos";
        cursor = db.rawQuery(query, null); //permite leer una fila completa
        if ( cursor.moveToFirst() ) { // se ubica al inicio de registros y lee. Si pudo da true
            do {
                // el de registros.getString(0) es el nombre y el 1 es precio
                lista.add(cursor.getString(0) + "\n$ " + cursor.getString(1)
                        + "\n" + cursor.getString(2));
            } while ( cursor.moveToNext() );  // lee el proximo, false si no puede
        }
        return lista;
    } // generarLista



    private ContentValues getContentValues(String nombre, float precio, String descripcion) {
        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre", nombre);
        contenedor.put("precio", precio);
        contenedor.put("descripcion", descripcion);
        return contenedor;
    } //getContentValues



    public void close(){ // cierra la base de datos
        db.close(); // cierra la base de datos
        super.close();
    } //close



    public boolean existe(String sNombre) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "select nombre from productos where nombre like '" + sNombre + "'";
        Cursor registros = database.rawQuery(query, null); //permite leer una fila completa
        if (registros != null && registros.getCount() > 0)
            return true;
        else
            return false;
    } // existe



    //devuelve el contenido del nombreDelCampo del numeroDeRegistro especificado
    public String getCampo(int numeroDeRegistro, String nombreDelCampo) {
        if ( cursor.moveToPosition(numeroDeRegistro) ) {
            int nroColumna = cursor.getColumnIndex((nombreDelCampo));
            if ( nroColumna != -1 )
                return cursor.getString(nroColumna);
            else
                return "ERROR!!! (no existe el campo)";
        }
        else
            return "ERROR!!!!!(no existe el registro)";
    } // getCampo
} //class DBSimple2
