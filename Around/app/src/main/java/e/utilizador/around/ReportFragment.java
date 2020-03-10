package e.utilizador.around;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private Button tipomapa;
    private Integer iduser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, null, false);
        getActivity().setTitle(R.string.report);

        if (savedInstanceState == null) {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);


    }

        getPontos();

        return view;




    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        iduser = args.getInt("id");
        Log.d("USER", "onCreateView: " + iduser);


        tipomapa = view.findViewById(R.id.button_tipo_mapa);

        tipomapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });





    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng statue = new LatLng(40.689247, -74.044502);
        googleMap.addMarker(new MarkerOptions().position(statue).title("Portugal").snippet("SOME DESCRIPTION"));


        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);



    }


    @Override
    public void onMapClick(LatLng latLng) {

    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        String lat = String.valueOf(latLng.latitude);
        String lon = String.valueOf(latLng.longitude);
        showDialog(iduser,lat,lon);
        Toast.makeText(getContext(), "Lat: " + String.valueOf(latLng.latitude) + "Long: " + String.valueOf(latLng.longitude), Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(latLng));
    }




    public void showDialog(Integer id,String lat, String lon){
        AddMarcador dialog = new AddMarcador();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("lat",lat);
        args.putString("lon",lon);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(),"AddMarcador");

    }


    public void getPontos() {
        String url = MySingleton.server + "pontos/getpontos";

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                try {

                    Log.d("RESPONSE", "onResponse: " +response.get(0));


                        for (int i = 0; i < response.length(); i++) {

                            JSONObject obj = response.getJSONObject(i);
                            double latitude = obj.getDouble("Latitude");
                            double longitude = obj.getDouble("Longitude");
                            String titulo = obj.getString("Titulo");
                            String descricao = obj.getString("Descricao");
                            LatLng statue = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(statue).title(titulo).snippet(descricao));


                        }



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
