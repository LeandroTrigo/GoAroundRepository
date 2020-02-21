package e.utilizador.around;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

public class AddNota extends Fragment {

    Calendar calendario;
    String data;
    EditText titulo;
    Button addNota;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.add_nota);
        return inflater.inflate(R.layout.fragment_add_nota, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titulo = view.findViewById(R.id.titulo_nota);
        calendario = Calendar.getInstance();
        data = calendario.get(Calendar.YEAR)+ "/" + (calendario.get(Calendar.MONTH)+1) + "/" + calendario.get(Calendar.DAY_OF_MONTH);
        addNota = view.findViewById(R.id.add_nota_bd);
        db = new DatabaseHelper(getActivity());

        addNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titulo.getText().length() <= 150) {
                    db.addNota(titulo.getText().toString(), data);
                    getFragmentManager().popBackStackImmediate();
                }
                else{
                    Toast.makeText(getContext(), "Tamanho Excedido", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
