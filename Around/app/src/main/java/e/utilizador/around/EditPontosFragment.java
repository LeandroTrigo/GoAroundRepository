package e.utilizador.around;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class EditPontosFragment extends Fragment {

    EditText edittitulo, editdescricao;
    String titulo,descricao,imagem;
    ImageView editimagem;
    int idponto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Editar Report");
        if(CheckFragment.getInstance().fragmento != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CheckFragment.getInstance().fragmento).commit();
        }
        return inflater.inflate(R.layout.fragment_edit_pontos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edittitulo = view.findViewById(R.id.edit_titulo);
        editdescricao = view.findViewById(R.id.edit_desc);
        editimagem = view.findViewById(R.id.edit_img_ponto);


        titulo = getArguments().getString("titulo");
        descricao = getArguments().getString("descricao");
        imagem = getArguments().getString("imagem");
        idponto = getArguments().getInt("id");

        edittitulo.setText(titulo);
        editdescricao.setText(descricao);
        Picasso.get().load(MySingleton.server + imagem).into(editimagem);

        Log.d("WORKS", "onViewCreated: " +idponto);



    }


}
