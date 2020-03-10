package e.utilizador.around;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

public class DialogImagem extends DialogFragment {

    String imagempath;
    ImageView imagem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_imagem, container, false);

        imagem = view.findViewById(R.id.imagem_popup);

        imagempath = getArguments().getString("imagem");

        Picasso.get().load(MySingleton.server + imagempath).into(imagem);

        return view;
    }
}
