package e.utilizador.around;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Nota> notas;

    Adapter(Context context, List<Nota> notas){
        this.inflater = LayoutInflater.from(context);
        this.notas = notas;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = inflater.inflate(R.layout.note_list_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder viewHolder, int i){
        String descricao = notas.get(i).getTitulo();
        String data = notas.get(i).getData();

        viewHolder.descricao.setText(descricao);
        viewHolder.data.setText(data);
    }

    @Override
    public int getItemCount(){
        return notas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView descricao, data;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.desc_nota);
            data = itemView.findViewById(R.id.data_nota);


        }
    }

}
