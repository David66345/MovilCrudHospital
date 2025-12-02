package adaptadores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a20300663_pi_movil.R;
import com.example.a20300663_pi_movil.carta;

import java.util.List;

import Global.info;
import Pojo.Lector;

public class adaptador extends RecyclerView.Adapter<adaptador.Miactivity>{

    public Context context;
    public SharedPreferences archivo;

    public adaptador(Context context) {//Constructor que se utiliza para inicializar la instancia de la clase adaptador con el objeto de Context proporcionado
        this.context = context;
        archivo = context.getSharedPreferences("listar", Context.MODE_PRIVATE);
        info.Lista_presta.clear();
    }

    @NonNull
    @Override
    public adaptador.Miactivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(context,R.layout.v_holder,null);
        //Se infla la vista v_holder utilizando el método View.inflate(), para poder desplegar los elementos de otra Activity
        Miactivity objeto = new Miactivity(v);//Se crea un objeto de la clase Miactivity, que se utiliza para mantener una referencia a la vista v_holder y
        //devolverla como resultado de esta función. La vista inflada se pasa como parámetro al constructor de la clase Miactivity
        return objeto;
    }

    @Override
    public void onBindViewHolder(@NonNull adaptador.Miactivity miniactivity, int position) {
        final int pos = position;
        String nombreg = archivo.getString("nombre", info.Lista_presta.get(position).getNombre());//nombre guardado en SharedPreferences
        String fprestag = archivo.getString("f_pres", info.Lista_presta.get(position).getFprestamo());//f_presta guardado en SharedPreferences

        miniactivity.fpresta.setText(fprestag);//Se toman los datos de la lista dinamica, que es la de los pacientes para mostrarlos en pantalla
        miniactivity.nombre.setText(nombreg);//Se toman los datos del nombre y el apellido para mostrarlos, de acuerdo a la posicion de los mismos
        miniactivity.nombre.setOnClickListener(new View.OnClickListener() {//Se agrega el metodo listener para poder hacer clic en el nombre
            @Override
            public void onClick(View v) {
                Intent card = new Intent(context, carta.class);//Por medio de un Intent se manda los datos de la posicion seleccionada para mostrarlos en otra Activity
                card.putExtra("pos",pos);//La Activity tendra un CardView
                context.startActivity(card);
            }
        });
    }

    @Override
    public int getItemCount() {
        return info.Lista_presta.size();
    }

    public class Miactivity extends RecyclerView.ViewHolder{
        public TextView nombre,fpresta;
        public Miactivity(View itemView) {
            super(itemView);
            fpresta = itemView.findViewById(R.id.txt_fpresta);
            nombre=itemView.findViewById(R.id.txt_nom);

        }
    }
}
