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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import Global.info;
import Pojo.Lector;

public class modificar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText nombre,apep,apem,domi,tel,aut,edit,f_presta,f_devolu;
    TextView lib;
    Button anterior,actualizar,siguiente;
    Toolbar toolbar;
    SharedPreferences archivo;
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
    Spinner spinn;
    int pos=0;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinn=findViewById(R.id.spin);
        spinn.setOnItemSelectedListener(this);

        nombre=findViewById(R.id.ed_nom);
        apep=findViewById(R.id.ed_ap);
        apem=findViewById(R.id.ed_am);
        domi=findViewById(R.id.ed_dom);
        tel=findViewById(R.id.ed_tel);
        lib=findViewById(R.id.txt_lib);
        aut=findViewById(R.id.ed_aut);
        edit=findViewById(R.id.ed_edit);
        f_presta=findViewById(R.id.ed_pres);
        f_devolu=findViewById(R.id.ed_devo);
        anterior=findViewById(R.id.b_ant);
        actualizar=findViewById(R.id.b_act);
        siguiente=findViewById(R.id.b_sig);

        archivo=this.getSharedPreferences("sesion", Context.MODE_PRIVATE);

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_anterior();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_actualizar();
            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_siguiente();
            }
        });

        mostrar();
        ArrayAdapter adaptador_libro = new ArrayAdapter(this, android.R.layout.simple_spinner_item,tlibro);//Constructor que crea un objeto para vincular los elementos y mostrarlos en el spinner de tipo Adapter
        adaptador_libro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//metodo, permite el diseño para la creacion de las vistas desplegables
        spinn.setAdapter(adaptador_libro);//metodo, permite el uso del adapatador permitiendo la visualizacion de los datos
    }

    private void onclick_anterior() {
        if(info.Lista_presta.size()==1){
            Toast.makeText(this, "Solo hay un préstamo ingresado", Toast.LENGTH_SHORT).show();
        } else if(i<info.Lista_presta.size()-1 && i==0){
            pos=info.Lista_presta.size()-1;
            i=info.Lista_presta.size()-1;
            mostrar();
            Toast.makeText(this, "Información del anterior préstamo", Toast.LENGTH_SHORT).show();
        }else if(pos<=info.Lista_presta.size() && pos!=0){
            pos--;
            if(pos!=0) {
                i--;
                mostrar();
                Toast.makeText(this, "Información del anterior préstamo", Toast.LENGTH_SHORT).show();
            }else {
                i = 0;
                mostrar();
                Toast.makeText(this, "Información del primer préstamo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onclick_actualizar() {
        if(nombre.getText().toString().equals("")||apep.getText().toString().equals("")||apem.getText().toString().equals("")||domi.getText().toString().equals("")||
                aut.getText().toString().equals("")||tel.getText().toString().equals("")||edit.getText().toString().equals("")||
                f_presta.getText().toString().equals("")||f_devolu.getText().toString().equals("")){
            Toast.makeText(this, "Rellene todos los espacios", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Se actualizo la información del préstamo", Toast.LENGTH_LONG).show();
            Lector cliente = new Lector();
            cliente.setNombre(nombre.getText().toString());//Metodo que obtiene la informacion de cada EditText
            cliente.setApaterno(apep.getText().toString());
            cliente.setAmaterno(apem.getText().toString());
            cliente.setDomicilio(domi.getText().toString());
            cliente.setTelefono(tel.getText().toString());
            cliente.setLibro(lib.getText().toString());
            cliente.setAutor(aut.getText().toString());
            cliente.setEditorial(edit.getText().toString());
            cliente.setFprestamo(f_presta.getText().toString());
            cliente.setFdevolucion(f_devolu.getText().toString());

            info.Lista_presta.set(pos,cliente);

            String url="http://192.168.100.192/bibliotech/modificar_PI.php?nombre="+nombre.getText().toString()+"&apep="+apep.getText().toString()+
                    "&apem="+apem.getText().toString()+"&dom="+domi.getText().toString()+"&tel="+tel.getText().toString()+"&libro="+lib.getText().toString()+
                    "&autor="+aut.getText().toString()+"&edit="+edit.getText().toString()+"&f_pres="+f_presta.getText().toString()+"&f_dev="+f_devolu.getText().toString();
            JsonObjectRequest pet = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        if (response.getInt("nombre") != -1) {
                            Toast.makeText(modificar.this, "Ha iniciado sesión", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(modificar.this, registro.class);
                            SharedPreferences.Editor editor = archivo.edit();
                            editor.putInt("pedido", response.getInt("nombre"));
                            editor.commit();
                            startActivity(i);
                            finish();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {//Respuesta para errores
                    Log.d("yo",error.getMessage());
                    //"yo" es un alias o etiqueta que registra e identifica donde se esta mandando el mensaje
                    //getMessage() obtiene el mensaje de error para ver el flujo de la informacion
                }
            });

            RequestQueue lanzarPeticion = Volley.newRequestQueue(modificar.this);//RequestQueue gestiona las olicitudes en forma de una cola, para enviarlas al servidor
            lanzarPeticion.add(pet);//añade la solicitud de iniciar sesion a la cola de solicitudes
        }
    }

    private void onclick_siguiente() {
        if(info.Lista_presta.size()==1){
            Toast.makeText(this, "Solo hay un préstamo ingresado", Toast.LENGTH_SHORT).show();
        } else if(i<info.Lista_presta.size()-1){
            pos++;
            i++;
            mostrar();
            Toast.makeText(this, "Información del siguiente préstamo", Toast.LENGTH_SHORT).show();
        }else if(i==info.Lista_presta.size()-1){
            pos=0;
            i=0;
            mostrar();
            Toast.makeText(this, "Información del primer préstamo", Toast.LENGTH_SHORT).show();
        }

    }

    private void mostrar() {
        nombre.setText(info.Lista_presta.get(pos).getNombre());
        apep.setText(info.Lista_presta.get(pos).getApaterno());
        apem.setText(info.Lista_presta.get(pos).getAmaterno());
        domi.setText(info.Lista_presta.get(pos).getDomicilio());
        tel.setText(info.Lista_presta.get(pos).getTelefono());
        lib.setText(info.Lista_presta.get(pos).getLibro());
        aut.setText(info.Lista_presta.get(pos).getAutor());
        edit.setText(info.Lista_presta.get(pos).getEditorial());
        f_presta.setText(info.Lista_presta.get(pos).getFprestamo());
        f_devolu.setText(info.Lista_presta.get(pos).getFdevolucion());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {//Funcion para cuando se selecciona un item
        Toast.makeText(modificar.this, ""+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();//funcion que manda un mensaje rapido, en este caso dice el pais elegido
        lib.setText(""+parent.getItemAtPosition(position));
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
                Intent registro = new Intent(this, registro.class);
                startActivity(registro);
                break;
            case R.id.i_lista:
                Intent lista = new Intent(this, lista.class);
                startActivity(lista);
                break;
            case R.id.i_modificar:
                Toast.makeText(this, "Esta en la pantalla de modificar", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bote:
                Intent borrar = new Intent(this,eliminar.class);
                startActivity(borrar);
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
}