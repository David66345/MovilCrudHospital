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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Global.info;
import Pojo.Lector;
import adaptadores.adaptador;

public class lista extends AppCompatActivity {

    RecyclerView rv;
    Toolbar toolbar;
    SharedPreferences archivo;
    adaptador av;
    String nombre,apep,apem,domi,tele,lib,aut,edit,f_presta,f_devolu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.rv_lista);

        av = new adaptador(this);//Crea a partir de un constructor una nueva instancia del adaptador llamado adaptador y lo asigna a una variable de instancia av.

        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        //Se utiliza para administrar el diseño de la lista de elementos en una orientación vertical y no invertida, y lo asigna a una variable de instancia llm a partir de un constructor.
        rv.setLayoutManager(llm);// Establece el administrador de diseño llm en el RecyclerView rv.
        rv.setAdapter(av);//Establece el adaptador av en el RecyclerView rv, lo que permite que los elementos de la lista sean creados y mostrados en la pantalla.

        archivo=this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        archivo=this.getSharedPreferences("listar", Context.MODE_PRIVATE);

        obtenerDatos();
    }

    private void obtenerDatos() {
        String url="http://192.168.100.192/bibliotech/mostrar_PI.php";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta JSON obtenida
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                nombre = jsonObject.getString("nombre");
                                f_presta = jsonObject.getString("f_pres");
                                apep= jsonObject.getString("apep");
                                apem= jsonObject.getString("apem");
                                domi= jsonObject.getString("dom");
                                tele= jsonObject.getString("tel");
                                lib= jsonObject.getString("libro");
                                aut= jsonObject.getString("autor");
                                edit= jsonObject.getString("edit");
                                f_devolu= jsonObject.getString("f_dev");

                                Lector prestamo = new Lector();
                                prestamo.setNombre(nombre);
                                prestamo.setFprestamo(f_presta);
                                prestamo.setApaterno(apep);
                                prestamo.setAmaterno(apem);
                                prestamo.setDomicilio(domi);
                                prestamo.setTelefono(tele);
                                prestamo.setLibro(lib);
                                prestamo.setAutor(aut);
                                prestamo.setEditorial(edit);
                                prestamo.setFdevolucion(f_devolu);
                                info.Lista_presta.add(prestamo);
                            }

                            rv.getAdapter().notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(lista.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.i_registro:
                Intent agregar = new Intent(this, registro.class);
                startActivity(agregar);
                break;
            case R.id.i_lista:
                Toast.makeText(this, "Esta en la pantalla de Lista", Toast.LENGTH_LONG).show();
                break;
            case R.id.i_modificar:
                Intent actualizar = new Intent(this,modificar.class);
                startActivity(actualizar);
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