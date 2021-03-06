package e.utilizador.around;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import javax.security.auth.Destroyable;

public class Notas extends Fragment implements Adapter.OnNoteListener {


    RecyclerView recyclerView;
    Button addNota;
    Adapter adapter;
    ArrayList<Nota> notas;
    Nota nota;
    DatabaseHelper db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.notas);
        return inflater.inflate(R.layout.fragment_notas, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        addNota = view.findViewById(R.id.add_nota);
        recyclerView = view.findViewById(R.id.recycler_notas);

        db = new DatabaseHelper(getActivity());
        notas = new ArrayList<>();

        final Cursor cursor = db.getNotas();
        int numRows = cursor.getCount();
        if (numRows == 0) {
            Log.d("ROWS", "SEM DADOS");
        } else {
            while (cursor.moveToNext()) {
                nota = new Nota(cursor.getString(1), cursor.getString(2));
                notas.add(nota);
            }
        }




        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), notas,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        addNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddNota();
                CheckFragment.getInstance().fragmento = fragment;
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddNota()).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        notas.get(position);
        showDialog(notas.get(position).getTitulo());
        Log.d("Position", "Nota: " +position);
        Log.d("Nome", "Nota: " +notas.get(position).getTitulo());
    }


    public void showDialog(String descricao){
        CustomDialog dialog = new CustomDialog();
        Bundle args = new Bundle();
        args.putString("desc", descricao);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(),"CustomDialog");

    }

}


