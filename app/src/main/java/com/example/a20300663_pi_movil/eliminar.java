package com.example.a20300663_pi_movil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import adaptadores.adaptador2;

public class eliminar extends AppCompatActivity {

    Button elim;
    Toolbar toolbar;
    RecyclerView rv_eliminar;
    SharedPreferences archivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv_eliminar = findViewById(R.id.rv_eliminar);
        elim = findViewById(R.id.b_eliminar);

        adaptador2 ave = new adaptador2(this);

        LinearLayoutManager llm_e = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_eliminar.setLayoutManager(llm_e);
        rv_eliminar.setAdapter(ave);

        elim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_eliminar();
            }
        });

        archivo=this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
    }

    private void onclick_eliminar() {
        if(info.Lista_presta.size()==0){
            Toast.makeText(this, "No hay préstamo a eliminar", Toast.LENGTH_SHORT).show();
            Intent agregar = new Intent(this, registro.class);
            startActivity(agregar);
        } else if(info.Lista_eliminar.size()==0){
            Toast.makeText(this, "No selecciono ningún registro", Toast.LENGTH_SHORT).show();
        } else{
            for(int i=0;i<info.Lista_eliminar.size();i++){
                String url="http://192.168.100.192/bibliotech/eliminar_PI.php?f_pres="+info.Lista_eliminar.get(i).getFprestamo()+
                        "&nombre="+info.Lista_eliminar.get(i).getNombre();
                JsonObjectRequest pet = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {//Respuesta para errores
                        Log.d("yo",error.getMessage());
                        //"yo" es un alias o etiqueta que registra e identifica donde se esta mandando el mensaje
                        //getMessage() obtiene el mensaje de error para ver el flujo de la informacion
                    }
                });
                RequestQueue lanzarPeticion = Volley.newRequestQueue(eliminar.this);//RequestQueue gestiona las olicitudes en forma de una cola, para enviarlas al servidor
                lanzarPeticion.add(pet);//añade la solicitud de iniciar sesion a la cola de solicitudes

                Lector persona = info.Lista_eliminar.get(i);
                info.Lista_presta.remove(persona);
            }
            info.Lista_eliminar.clear();

            rv_eliminar.getAdapter().notifyDataSetChanged();//notificar cambios al adaptador
            Toast.makeText(this, "Se han eliminado los registros de préstamo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
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
                Intent actualizar = new Intent(this, modificar.class);
                startActivity(actualizar);
                break;
            case R.id.bote:
                Toast.makeText(this, "Esta en la pantalla de eliminar", Toast.LENGTH_SHORT).show();
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