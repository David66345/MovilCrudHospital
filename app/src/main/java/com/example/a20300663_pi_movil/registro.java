package com.example.a20300663_pi_movil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Global.info;
import Pojo.Lector;

public class registro extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText nom,ap,am,dom,tel,autor,editorial,prestamo_a,prestamo_m,prestamo_d,devolucion_a,devolucion_m,devolucion_d;
    TextView libro;
    String[] tlibro={
            "Don Quijote de la Mancha",
            "El Principito",
            "El Señor de los Anillos",
            "1984",
            "Cien años de soledad",
            "Harry Potter y la piedra filosofal",
            "El Alquimista",
            "Orgullo y prejuicio",
            "Matar a un ruiseñor",
            "El Gran Gatsby"
    };//arreglo de strings de los elementos
    Spinner spin;
    Button agregar, limpiar;
    Toolbar toolbar;
    SharedPreferences archivo, mostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spin=findViewById(R.id.spin);
        spin.setOnItemSelectedListener(this);

        nom=findViewById(R.id.ed_nombre);
        ap=findViewById(R.id.ed_apep);
        am=findViewById(R.id.ed_apem);
        dom=findViewById(R.id.ed_domicilio);
        libro=findViewById(R.id.txt_libro);
        tel=findViewById(R.id.ed_telefono);
        autor=findViewById(R.id.ed_autor);
        editorial=findViewById(R.id.ed_editorial);
        prestamo_a=findViewById(R.id.ed_prea);
        prestamo_m=findViewById(R.id.ed_prem);
        prestamo_d=findViewById(R.id.ed_pred);
        devolucion_a=findViewById(R.id.ed_deva);
        devolucion_m=findViewById(R.id.ed_devm);
        devolucion_d=findViewById(R.id.ed_devd);
        agregar=findViewById(R.id.b_agregar);
        limpiar=findViewById(R.id.b_limpiar);

        archivo=this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        mostrar =this.getSharedPreferences("listar", Context.MODE_PRIVATE);

        agregar.setOnClickListener(new View.OnClickListener() {//Metodo que utiliza el boton para atenderlo
            @Override
            public void onClick(View view) {//Metodo para que al hacer clic se realice la accion señalada
                onclick_agregar();
            }
        });

        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick_limpiar();
            }
        });

        ArrayAdapter adaptador_libro = new ArrayAdapter(this, android.R.layout.simple_spinner_item,tlibro);//Constructor que crea un objeto para vincular los elementos y mostrarlos en el spinner de tipo Adapter
        adaptador_libro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//metodo, permite el diseño para la creacion de las vistas desplegables
        spin.setAdapter(adaptador_libro);//metodo, permite el uso del adapatador permitiendo la visualizacion de los datos
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {//Funcion para cuando se selecciona un item
        Toast.makeText(registro.this, ""+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();//funcion que manda un mensaje rapido, en este caso dice el pais elegido
        libro.setText(""+parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {//Funcion para cuando no se selecciona un item

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);//Metodo para poder utilizar mas de un xml en un mismo java, y poder desplegarlo
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.i_registro:
                Toast.makeText(this, "Esta en la pantalla de Registro", Toast.LENGTH_LONG).show();
                break;
            case R.id.i_lista:
                String url="http://192.168.100.192/bibliotech/vacia_PI.php";
                JsonObjectRequest pet = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            boolean vacia = response.getBoolean("vacia");
                            if(vacia){
                                Toast.makeText(registro.this, "Ingrese un prestamo", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent list = new Intent(registro.this, lista.class);
                                startActivity(list);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("yo",error.getMessage());
                    }
                });
                RequestQueue lanzarPeticion = Volley.newRequestQueue(registro.this);//RequestQueue gestiona las olicitudes en forma de una cola, para enviarlas al servidor
                lanzarPeticion.add(pet);
                break;
            case R.id.i_modificar:
                Intent actualizar = new Intent(this,modificar.class);
                if(info.Lista_presta.isEmpty()){
                    Toast.makeText(this, "Verifique primero la lista de préstamos", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(actualizar);
                }
                break;
            case R.id.bote:
                Intent borrar = new Intent(this,eliminar.class);
                if(info.Lista_presta.isEmpty()){
                    Toast.makeText(this, "Verifique primero la lista de préstamos", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(borrar);
                }
                break;
            case R.id.i_dlogin:
                Toast.makeText(this, "Ha cerrado su sesión", Toast.LENGTH_SHORT).show();
                if(archivo.contains("id")){
                    SharedPreferences.Editor editor = archivo.edit();
                    editor.remove("id");
                    /*editor.remove("contra");
                    editor.remove("valido");*/
                    editor.commit();
                    Intent fin = new Intent(this, MainActivity.class);
                    startActivity(fin);
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onclick_limpiar() {
        Toast.makeText(this, "Se limpio la pantalla", Toast.LENGTH_LONG).show();
        nom.setText("");
        ap.setText("");
        am.setText("");
        dom.setText("");
        tel.setText("");
        autor.setText("");
        editorial.setText("");
        prestamo_a.setText("");
        prestamo_m.setText("");
        prestamo_d.setText("");
        devolucion_a.setText("");
        devolucion_m.setText("");
        devolucion_d.setText("");
    }

    private void onclick_agregar() {
        if(nom.getText().toString().equals("")||ap.getText().toString().equals("")||am.getText().toString().equals("")||dom.getText().toString().equals("") ||
                tel.getText().toString().equals("")||autor.getText().toString().equals("")||
                editorial.getText().toString().equals("")||prestamo_a.getText().toString().equals("")||prestamo_m.getText().toString().equals("")||
                prestamo_d.getText().toString().equals("")||devolucion_a.getText().toString().equals("")||
                devolucion_m.getText().toString().equals("")||devolucion_d.getText().toString().equals("")){
            Toast.makeText(this, "Rellene todos los espacios", Toast.LENGTH_LONG).show();
        }
        else {
            String f_prestamo=prestamo_a.getText().toString()+"-"+prestamo_m.getText().toString()+"-"+prestamo_d.getText().toString();
            String f_devolucion=devolucion_a.getText().toString()+"-"+devolucion_m.getText().toString()+"-"+devolucion_d.getText().toString();
            Toast.makeText(this, "Se agrego un préstamo", Toast.LENGTH_SHORT).show();
            Lector cliente=new Lector();
            cliente.setNombre(nom.getText().toString());
            cliente.setApaterno(ap.getText().toString());
            cliente.setAmaterno(am.getText().toString());
            cliente.setDomicilio(dom.getText().toString());
            cliente.setTelefono(tel.getText().toString());
            cliente.setLibro(libro.getText().toString());
            cliente.setAutor(autor.getText().toString());
            cliente.setEditorial(editorial.getText().toString());
            cliente.setFprestamo(f_prestamo);
            cliente.setFdevolucion(f_devolucion);

            info.Lista_presta.add(cliente);

            String url="http://192.168.100.192/bibliotech/agregar_PI.php?nombre="+nom.getText().toString()+"&apep="+ap.getText().toString()+
                "&apem="+am.getText().toString()+"&dom="+dom.getText().toString()+"&tel="+tel.getText().toString()+"&libro="+libro.getText().toString()+
                "&autor="+autor.getText().toString()+"&edit="+editorial.getText().toString()+"&f_pres="+f_prestamo+"&f_dev="+f_devolucion;
            JsonObjectRequest pet = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(registro.this, "Prestamo agregado con éxito", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {//Respuesta para errores
                    Log.d("yo",error.getMessage());
                    //"yo" es un alias o etiqueta que registra e identifica donde se esta mandando el mensaje
                    //getMessage() obtiene el mensaje de error para ver el flujo de la informacion
                }
            });

            RequestQueue lanzarPeticion = Volley.newRequestQueue(registro.this);//RequestQueue gestiona las olicitudes en forma de una cola, para enviarlas al servidor
            lanzarPeticion.add(pet);//añade la solicitud de iniciar sesion a la cola de solicitudes
        }
    }
}