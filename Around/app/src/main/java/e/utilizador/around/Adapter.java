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

    private LayoutInflater inflater;
    private List<Nota> notas;
    private OnNoteListener mOnNoteListener;

    Adapter(Context context, List<Nota> notas, OnNoteListener onNoteListener){
        this.inflater = LayoutInflater.from(context);
        this.mOnNoteListener = onNoteListener;
        this.notas = notas;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = inflater.inflate(R.layout.note_list_view,viewGroup,false);
        return new ViewHolder(view,mOnNoteListener);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView descricao, data;
        OnNoteListener onNoteListener;


        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);



            descricao = itemView.findViewById(R.id.desc_nota);
            data = itemView.findViewById(R.id.data_nota);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }


    public interface OnNoteListener{
        void onNoteClick(int position);
    }

}
