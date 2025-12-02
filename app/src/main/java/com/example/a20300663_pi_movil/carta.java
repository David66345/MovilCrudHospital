package com.example.a20300663_pi_movil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import Global.info;
import Pojo.Lector;

public class carta extends AppCompatActivity {

    TextView nombre,apaterno,amaterno,dom,tel,libro,autor,editorial,fprestamo,fdevolucion;

    Button llamar;

    Toolbar toolbar;
    SharedPreferences archivo,mostrar;
    String nom,apep,apem,domi,tele,lib,aut,edit,f_presta,f_devolu;
    int posi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombre = findViewById(R.id.tx_nom);
        apaterno = findViewById(R.id.tx_ap);
        amaterno = findViewById(R.id.tx_am);
        dom = findViewById(R.id.tx_d);
        tel=findViewById(R.id.tx_tel);
        libro= findViewById(R.id.tx_libro);
        autor= findViewById(R.id.tx_autor);
        editorial= findViewById(R.id.tx_edit);
        fprestamo=findViewById(R.id.tx_fpres);
        fdevolucion=findViewById(R.id.tx_fdev);
        llamar= findViewById(R.id.b_llamar);

        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_llamar();
            }
        });
        archivo=this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        mostrar=this.getSharedPreferences("listar", Context.MODE_PRIVATE);

        posi=getIntent().getIntExtra("pos",-1);//Metodo para obtener la posicion, a partir de un valor entero, del Intent

        nombre.setText("Nombre: "+info.Lista_presta.get(posi).getNombre());
        apaterno.setText("Apellido Paterno: "+info.Lista_presta.get(posi).getApaterno());
        amaterno.setText("Apellido Materno: "+info.Lista_presta.get(posi).getAmaterno());
        dom.setText("Domicilio: "+info.Lista_presta.get(posi).getDomicilio());
        tel.setText(info.Lista_presta.get(posi).getTelefono());
        libro.setText("Libro: "+info.Lista_presta.get(posi).getLibro());
        autor.setText("Autor: "+info.Lista_presta.get(posi).getAutor());
        editorial.setText("Editorial: "+info.Lista_presta.get(posi).getEditorial());
        fprestamo.setText("Fecha de préstamo: "+info.Lista_presta.get(posi).getFprestamo());
        fdevolucion.setText("Fecha de devolución: "+info.Lista_presta.get(posi).getFdevolucion());
    }

    private void onclick_llamar() {//Es el método que se llama cuando se hace clic en el boton de llamada
        Intent llama =  new Intent(Intent.ACTION_CALL);//Crea un nuevo objeto Intent con la acción Intent.ACTION_CALL, que se utiliza para realizar una llamada telefónica
        llama.setData(Uri.parse("tel:"+tel.getText().toString()));//Establece los datos del Intent como la URI del número de teléfono que se va a llamar,
        //donde tel.getText().toString() es el número de teléfono introducido en un campo de texto
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){//Verifica si la aplicación tiene permiso para realizar llamadas telefónicas
            ActivityCompat.requestPermissions(this, new String[]{//Si la aplicación no tiene permiso, se solicita el permiso al usuario mediante un cuadro de diálogo de solicitud de permisos
                    android.Manifest.permission.CALL_PHONE},10);
            return;//Si el usuario no concede el permiso, el método devuelve el control sin realizar la llamada telefónica.
        }
        startActivity(llama);//Si el usuario concede el permiso, se inicia la actividad correspondiente para realizar la llamada telefónica utilizando el Intent creado en la línea de llama
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
                Intent registro = new Intent(this, MainActivity.class);
                startActivity(registro);
                break;
            case R.id.i_lista:
                finish();
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