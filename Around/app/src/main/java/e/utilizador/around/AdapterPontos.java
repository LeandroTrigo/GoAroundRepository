package e.utilizador.around;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPontos extends RecyclerView.Adapter<AdapterPontos.ViewHolder> {

    private LayoutInflater inflater;
    private List<Ponto> pontos;
    private AdapterPontos.OnPontoListener mOnPontoListener;

    AdapterPontos(Context context, List<Ponto> pontos, OnPontoListener onPontoListener){
        this.inflater = LayoutInflater.from(context);
        this.mOnPontoListener = onPontoListener;
        this.pontos = pontos;
    }

    @NonNull
    @Override
    public AdapterPontos.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = inflater.inflate(R.layout.ponto_list_view,viewGroup,false);
        return new AdapterPontos.ViewHolder(view,mOnPontoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPontos.ViewHolder viewHolder, int i){

        String descricao = pontos.get(i).getDescricao();
        String titulo = pontos.get(i).getTitulo();
        String designacaoimagem = pontos.get(i).getImagem();

        Log.d("TESTE", "onBindViewHolder: " + designacaoimagem);

        viewHolder.descricao.setText(descricao);
        viewHolder.titulo.setText(titulo);
        Picasso.get().load(MySingleton.server + designacaoimagem).into(viewHolder.imagem);

    }



    @Override
    public int getItemCount(){
        return pontos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView titulo, descricao;
        ImageView imagem;
        AdapterPontos.OnPontoListener onPontoListener;


        public ViewHolder(@NonNull View itemView, AdapterPontos.OnPontoListener onPontoListener) {
            super(itemView);



            titulo = itemView.findViewById(R.id.titulo_problema);
            descricao = itemView.findViewById(R.id.descricao_problema);
            imagem = itemView.findViewById(R.id.img_problema);
            this.onPontoListener = onPontoListener;
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            onPontoListener.onPontoClick(getAdapterPosition());
        }
    }


    public interface OnPontoListener{
        void onPontoClick(int position);
    }

}
