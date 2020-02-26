package e.utilizador.around;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private Button tipomapa,foto;
    ImageView imageView;
    String path;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, null, false);
        getActivity().setTitle(R.string.report);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        if(Build.VERSION.SDK_INT>=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }

        return view;




    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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


        imageView = view.findViewById(R.id.imageView);
        foto = view.findViewById(R.id.button_foto);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EscolherFoto();
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
        showDialog();
        Toast.makeText(getContext(), "Lat: " + String.valueOf(latLng.latitude) + "Long: " + String.valueOf(latLng.longitude), Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(latLng));
    }


    public void showDialog(/*String descricao*/){
        AddMarcador dialog = new AddMarcador();

        //Bundle args = new Bundle();
        //args.putString("desc", descricao);
        //dialog.setArguments(args);
        dialog.show(getFragmentManager(),"AddMarcador");

    }

    private void EscolherFoto(){
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getActivity().getPackageManager()) != null){
            File foto = null;
            foto = createFoto();

            if(foto != null) {
                path = foto.getAbsolutePath();
                Uri foturi = FileProvider.getUriForFile(getContext(),"com.thecodecity.cameraandroid.fileprovider",foto);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT,foturi);
                startActivityForResult(takePic,1);
            }


        }
    }

    private File createFoto(){
        String nome = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
        File storage =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imagem = null;
        try {
            imagem = File.createTempFile(nome, ".jpg", storage);
        }
        catch (Exception e){
            Log.d("Exception", "createFoto: " + e);
        }

        return imagem;

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 1){
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
        }
    }


}
