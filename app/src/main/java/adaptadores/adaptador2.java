package adaptadores;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a20300663_pi_movil.R;

import Global.info;

public class adaptador2 extends RecyclerView.Adapter<adaptador2.Miactividad> {

    public Context context;

    public adaptador2(Context context){//Constructor que se utiliza para inicializar la instancia de la clase adaptador con el objeto de Context proporcionado
        this.context=context;
    }


    @NonNull
    @Override
    public adaptador2.Miactividad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(context, R.layout.v_eliminar,null);
        Miactividad objeto = new Miactividad(v);
        return objeto;
    }

    @Override
    public void onBindViewHolder(@NonNull adaptador2.Miactividad miniactividad, int position) {
        final int pos = position;

        miniactividad.nombre.setText(info.Lista_presta.get(position).getNombre());
        miniactividad.f_presa.setText(info.Lista_presta.get(position).getFprestamo());
        miniactividad.caja.setChecked(false);
        miniactividad.caja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    info.Lista_eliminar.add(info.Lista_presta.get(pos));
                }else if(!((CheckBox)v).isChecked()){
                    info.Lista_eliminar.remove(info.Lista_presta.get(pos));
                }else{
                    info.Lista_presta.remove(info.Lista_presta.get(pos));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return info.Lista_presta.size();
    }

    public class Miactividad extends RecyclerView.ViewHolder {

        public TextView nombre,f_presa;
        public CheckBox caja;

        public Miactividad(@NonNull View itemView) {
            super(itemView);

            f_presa=itemView.findViewById(R.id.txt_fpre);
            nombre = itemView.findViewById(R.id.txt_nom);
            caja = itemView.findViewById(R.id.checkBox);
        }
    }
}