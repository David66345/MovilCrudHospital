package com.example.a20300663_pi_movil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText usuario, password;
    Button ingresar;
    SharedPreferences archivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario=findViewById(R.id.ed_usuario);
        password=findViewById(R.id.ed_password);
        ingresar=findViewById(R.id.b_ingresar);

        archivo=this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        if(archivo.contains("id")){
            Intent inicio = new Intent(this, registro.class);
            startActivity(inicio);
            finish();
        }

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_login();
            }
        });
    }

    private void onclick_login() {
        String url="http://192.168.100.192/bibliotech/ingreso_PI.php?user=";
        url=url+usuario.getText().toString();
        url=url+"&pass=";
        url=url+password.getText().toString();
        JsonObjectRequest pet = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            //Un Json es un formato para el intercambio de informacion entre un servidor y un cliente, se compone de una coleccion de pares clave-valor
            //El JsonObjectRequest envia una solicitud http y recibe los datos como json, y es especialmente para GET o POST
            //Necesita el metodo GET o POST, el url, y el como manejara la respuesta: de forma exitosa y otro para manejar errores
            @Override
            public void onResponse(JSONObject response) {//Respuesta exitosa
                try {//Se utiliza para manejar excepciones y casos de respuestas
                    if (response.getInt("user") != -1) {
                        Toast.makeText(MainActivity.this, "Ha iniciado sesión", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, registro.class);
                        SharedPreferences.Editor editor = archivo.edit();
                        editor.putInt("id", response.getInt("user"));
                        editor.commit();
                        startActivity(i);
                        finish();
                    } else {
                        usuario.setText("");
                        password.setText("");
                        Toast.makeText(MainActivity.this, "El usuario y/o contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {//indica que se ha producido un error con el Json, es la excepcion
                    e.printStackTrace();//imprime la excepcion, es decir donde se produjo el error
                }
                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();//mensaje rapido de la respuesta, usuario que inicio sesion
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {//Respuesta para errores
                Log.d("yo",error.getMessage());
                //"yo" es un alias o etiqueta que registra e identifica donde se esta mandando el mensaje
                //getMessage() obtiene el mensaje de error para ver el flujo de la informacion
            }
        });
        RequestQueue lanzarPeticion = Volley.newRequestQueue(this);//RequestQueue gestiona las olicitudes en forma de una cola, para enviarlas al servidor
        lanzarPeticion.add(pet);//añade la solicitud de iniciar sesion a la cola de solicitudes
    }
}