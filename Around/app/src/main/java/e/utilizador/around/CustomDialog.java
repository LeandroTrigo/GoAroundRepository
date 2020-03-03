package e.utilizador.around;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomDialog  extends DialogFragment {

    String descricao;
    EditText desc;
    Button apagar;
    Button editar;
    DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_notas,container,false);
         descricao = getArguments().getString("desc");


        desc = view.findViewById(R.id.descricao_nota);
        apagar = view.findViewById(R.id.button_delete_nota);
        editar = view.findViewById(R.id.button_edit_nota);
        db = new DatabaseHelper(getActivity());
        desc.setText(descricao);


         apagar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 db.deleteNota(descricao);
                 getDialog().dismiss();
                 final FragmentTransaction ft = getFragmentManager().beginTransaction();
                 ft.replace(R.id.fragment_container, new Notas(), "Adicionar Nota");
                 ft.commit();
                 ft.addToBackStack(null);
                 notificarSucesso(getString(R.string.sucesso),getString(R.string.nota_apagada));
             }
         });

         editar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 db.updateNota(descricao,desc.getText().toString());
                 getDialog().dismiss();
                 final FragmentTransaction ft = getFragmentManager().beginTransaction();
                 ft.replace(R.id.fragment_container, new Notas(), "Adicionar Nota");
                 ft.commit();
                 ft.addToBackStack(null);
                 notificarSucesso(getString(R.string.sucesso),getString(R.string.nota_atualizada));
             }
         });



        return view;
    }

    public void notificarSucesso(String titulo, String mensagem) {
        new AlertDialog.Builder(getContext())
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(R.drawable.sucess)
                .show();
    }

}
