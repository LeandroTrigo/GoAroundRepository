package e.utilizador.around;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ReportFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private Button tipomapa,marcarponto,info;
    private boolean mLocationPermissionGranted;
    private Integer iduser;
    private FusedLocationProviderClient fusedLocationClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLastKnownLocation;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private Map<Marker, String> allMarkersMap = new HashMap<Marker, String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, null, false);
        getActivity().setTitle(R.string.report);

        if (savedInstanceState == null) {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

    }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        getPontos();

        return view;




    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        iduser = args.getInt("id");


        tipomapa = view.findViewById(R.id.button_tipo_mapa);
        marcarponto = view.findViewById(R.id.button_waypoint);
        info = view.findViewById(R.id.button_info);

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

        marcarponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

                if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    setWaypoint();
                }else{
                    buildAlertMessageNoGps();
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPontos();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);

        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }



    @Override
    public void onMapLongClick(LatLng latLng) {
        String lat = String.valueOf(latLng.latitude);
        String lon = String.valueOf(latLng.longitude);
        showDialog(iduser,lat,lon);
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



                        for (int i = 0; i < response.length(); i++) {

                            JSONObject obj = response.getJSONObject(i);
                            double latitude = obj.getDouble("Latitude");
                            double longitude = obj.getDouble("Longitude");
                            String titulo = obj.getString("Titulo");
                            String descricao = obj.getString("Descricao");
                            String imagem = obj.getString("Imagem");
                            LatLng coordenadas = new LatLng(latitude, longitude);

                            Marker marker = mMap.addMarker(new MarkerOptions().position(coordenadas).title(titulo).snippet(descricao));
                            allMarkersMap.put(marker, imagem);


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

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d("LOCATION", "Current location is null. Using defaults.");
                            Log.e("LOCATION", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void setWaypoint() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));


                                String lat = String.valueOf(mLastKnownLocation.getLatitude());
                                String lon = String.valueOf(mLastKnownLocation.getLongitude());
                                LatLng coordenadas = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                                showDialog(iduser,lat,lon);
                                mMap.addMarker(new MarkerOptions().position(coordenadas));
                            }
                        } else {
                            Log.d("LOCATION", "Current location is null. Using defaults.");
                            Log.e("LOCATION", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage(getString(R.string.ativar_gps))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.nao), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void showDialogPontos(){
        DialogPontos dialog = new DialogPontos();
        dialog.show(getFragmentManager(),"DialogPontos");

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        DialogImagem dialog = new DialogImagem();
        Bundle args = new Bundle();
        args.putString("imagem", allMarkersMap.get(marker));
        dialog.setArguments(args);
        dialog.show(getFragmentManager(),"DialogImagem");
    }
}
