package e.utilizador.around;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportsFragment extends Fragment implements AdapterPontos.OnPontoListener {

    RecyclerView recyclerView;
    AdapterPontos adapterPontos;
    ArrayList<Ponto> pontos;
    int iduser;
    Ponto ponto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.reports);
        return inflater.inflate(R.layout.fragment_reports, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_pontos);
        pontos = new ArrayList<>();

        getPontosUser();


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPontos = new AdapterPontos(getContext(),pontos,this);
        recyclerView.setAdapter(adapterPontos);


    }

    @Override
    public void onPontoClick(int position) {
        Log.d("CLICK", "onPontoClick: " + "CLICADO");
    }

    public void getPontosUser() {


        Bundle args = getArguments();
        iduser = args.getInt("id");
        String url = MySingleton.server + "pontos/getpontosuser/" +iduser;


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);
                        double latitude = obj.getDouble("Latitude");
                        double longitude = obj.getDouble("Longitude");
                        String titulo = obj.getString("Titulo");
                        String descricao = obj.getString("Descricao");
                        int iduser = obj.getInt("IdUtilizador");
                        int idponto = obj.getInt("IdPonto");
                        String imagem = obj.getString("Imagem");

                        ponto = new Ponto(idponto,imagem,titulo,descricao,latitude,longitude,iduser);
                        pontos.add(ponto);
                        adapterPontos.notifyDataSetChanged();
                    }

                    Log.d("PONTOS", "onResponse: " + pontos);





                } catch (Exception ex) {
                    notificarErro(getString(R.string.erro), getString(R.string.webservice));
                    Log.d("EXER", "onResponse: " + ex.getMessage());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notificarErro(getString(R.string.erro), getString(R.string.conexao));
            }
        });

        MySingleton.getInstance(this.getContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void notificarErro(String titulo, String mensagem) {
        new AlertDialog.Builder(getContext())
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(R.drawable.error)
                .show();
    }


}
