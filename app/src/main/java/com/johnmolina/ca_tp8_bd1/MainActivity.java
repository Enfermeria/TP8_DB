/************************************************************************************************
 *              TRABAJO PRACTICO 8: BASES DE DATOS PARA ANDROID                                 *
 *                              John David Molina                                               *
 ************************************************************************************************/
package com.johnmolina.ca_tp8_bd1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etNombre, etPrecio, etDescripcion;
    private ListView lvProductos;
    private DBSimple dbms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = (EditText) findViewById(R.id.et_producto);
        etPrecio = (EditText) findViewById(R.id.et_precio);
        etDescripcion = (EditText) findViewById(R.id.et_descripcion);
        lvProductos = (ListView) findViewById(R.id.lv_productos);

        dbms = new DBSimple(this, "stocks", null, 1); //version 1 de la db stocks
        lvActualizar(); // para mostrar los datos inicialmente

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etNombre.setText(dbms.getCampo(position, "nombre"));
                etPrecio.setText(dbms.getCampo(position, "precio"));
                etDescripcion.setText(dbms.getCampo(position, "descripcion"));
            } //onItemClick
        }); //lvProductos.setOnItemClickListener
    }

    @Override
    protected void onDestroy() {
        dbms.close();
        super.onDestroy();
    }

    /****************************************************************************************
     *                Metodos para agregar, borrar y modificar registros                    *
     ****************************************************************************************/

    public void agregar(View view) {
        String sNombre = etNombre.getText().toString();
        String sPrecio = etPrecio.getText().toString();
        String sDescripcion = etDescripcion.getText().toString();
        if (!sNombre.isEmpty() && !sPrecio.isEmpty()) { // si los campos no estan vacios
            if ( dbms.existe(sNombre) ) { // si ya existe un registro con ese nombre
                Toast.makeText(this, "Ese producto ya existe. Pruebe pulsar [MODIFICAR]", Toast.LENGTH_LONG).show();
            }
            else if ( dbms.insert(sNombre, Float.parseFloat(sPrecio), sDescripcion) ) { // si lo pudo insertar
                Toast.makeText(this, "Producto agregado correctamente", Toast.LENGTH_LONG).show();
                lvActualizar();
                limpiarCampos();
            }
            else {
                Toast.makeText(this, "El producto no se pudo agregar", Toast.LENGTH_LONG).show();
                limpiarCampos();
            }
        }
        else {
            Toast.makeText(this, "No deje campos en blanco",
                    Toast.LENGTH_LONG).show();
        }
    } //agregar

    public void borrar(View view) {
        String sNombre = etNombre.getText().toString();
        if (!sNombre.isEmpty()) {
            int cantEliminados = dbms.delete(sNombre);
            if (cantEliminados !=0) {
                Toast.makeText(this, "Se eliminaron " + cantEliminados + " productos " + sNombre, Toast.LENGTH_LONG).show();
                limpiarCampos();
            }
            else {
                Toast.makeText(this, "No pudo borrar el producto", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Debe completar el campo nombre", Toast.LENGTH_LONG).show();
        }
        lvActualizar();
    } //borrar

    public void modificar(View view) {
        String sNombre = etNombre.getText().toString();
        String sPrecio = etPrecio.getText().toString();
        String sDescripcion = etDescripcion.getText().toString();
        if (!sNombre.isEmpty() && !sPrecio.isEmpty()) { // si los campos no estan vacios
            int cant = dbms.update( sNombre, Float.parseFloat(sPrecio), sDescripcion );
            if (cant !=0) {
                Toast.makeText(this, "Se actualizaron " + cant + " productos " + sNombre, Toast.LENGTH_LONG).show();
                limpiarCampos();
            }
            else {
                Toast.makeText(this, "No pudo actualizar el producto", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Debe completar ambos campos",
                    Toast.LENGTH_LONG).show();
        }
        lvActualizar();
    } //modificar

    /****************************************************************************************
     *                  Muestra la lista de prodcutos en lvProductos                        *
     ****************************************************************************************/
    public void lvActualizar() {
        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dbms.generarLista() );
        lvProductos.setAdapter(adaptador);
    } //lvActualizar

    private void limpiarCampos(){
        etPrecio.setText("");
        etNombre.setText("");
        etDescripcion.setText("");
    } //limpiarCampos

} //class MainActivity