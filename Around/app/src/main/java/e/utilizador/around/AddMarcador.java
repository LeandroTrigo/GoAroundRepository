package e.utilizador.around;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.RemoteMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddMarcador extends DialogFragment {

    DatabaseHelper db;
    Button foto,adicionarponto,camera;
    ImageView imageView;
    EditText titulo,descricao;
    Integer id;
    String latitude,longitude;




    //Permissões
    private static final int PERMISSSION_REQUEST = 0;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int CAMERA_REQUEST_CODE = 228;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adicionar_marcador,container,false);

        id = getArguments().getInt("id");
        latitude = getArguments().getString("lat");
        longitude = getArguments().getString("lon");


        db = new DatabaseHelper(getActivity());
        imageView = view.findViewById(R.id.imageView);
        foto = view.findViewById(R.id.button_foto);
        camera = view.findViewById(R.id.button_camera);
        titulo = view.findViewById(R.id.titulo_ponto);
        descricao = view.findViewById(R.id.descricao_ponto);
        adicionarponto = view.findViewById(R.id.button_adicionar_ponto);


        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageGalleryClicked(getView());
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        adicionarponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            addPonto();
            }
        });



        return view;
    }

    //Exibir mensagem de permissão para aceder à galeria e verificar resposta do utilizador
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode){
            case PERMISSSION_REQUEST:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    notificarSucesso(getResources().getString(R.string.sucesso),getString(R.string.permissions_ok));
                }else{
                    notificarErro(getResources().getString(R.string.erro),getString(R.string.permissions_denied));
                }
        }
    }


    //Metodo para tranferir imagens da galeria para a aplicação

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {

            if (requestCode == IMAGE_GALLERY_REQUEST) {


                // Endereço da imagem no telemóvel
                Uri imageUri = data.getData();

                // Declaramos um stream para ler a imagem do telemóvel
                InputStream inputStream;

                // Esse input é passado no url da imagem
                try {
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                    // Obtemos um bitmap para a stream
                    Bitmap image = BitmapFactory.decodeStream(inputStream);


                    // Mostramos a imagem ao utilizador colocando no imageview disponibilizado.
                    imageView.setImageBitmap(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    notificarErro(getResources().getString(R.string.erro),getString(R.string.image_not_saved));
                }

            }

            else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }

    //Metodo para ao carregar na imagem e exibila na aplicação
    public void onImageGalleryClicked(View v) {
        // Abrir a Galeria
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // Directoria onde vamos buscar as fotos.
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        // Url da fotografia
        Uri data = Uri.parse(pictureDirectoryPath);

        // Vamos buscar todos os tipos de imagem
        photoPickerIntent.setDataAndType(data, "image/*");

        //Invocamos a atividade e recebemos uma fotografia
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }



    private void addPonto() {
        String url = MySingleton.server + "pontos/criarponto";

        StringRequest postResquest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            sendNotificationFirebase();
                            notificarSucesso(getString(R.string.sucesso),getString(R.string.report_done));
                            getDialog().dismiss();

                        } catch (Exception e) {
                            notificarErro(getString(R.string.erro),getString(R.string.report_error));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notificarErro(getString(R.string.erro),getString(R.string.escolher_imagem));
                    }
                }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();


                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();


                try {
                    String titulop = ChCrypto.encrypt(titulo.getText().toString().trim(), "MY SECRET KEY IS I AM A SECRET K");
                    String descp = ChCrypto.encrypt(descricao.getText().toString().trim(), "MY SECRET KEY IS I AM A SECRET K");
                    String userp = ChCrypto.encrypt(id.toString().trim(), "MY SECRET KEY IS I AM A SECRET K");
                    String latp = ChCrypto.encrypt(latitude, "MY SECRET KEY IS I AM A SECRET K");
                    String longp = ChCrypto.encrypt(longitude, "MY SECRET KEY IS I AM A SECRET K");
                    String imgp = ChCrypto.encrypt(convertImage(bitmap), "MY SECRET KEY IS I AM A SECRET K");


                    parametros.put("Titulo", titulop);
                parametros.put("Descricao", descp);
                parametros.put("IdUtilizador",userp);
                parametros.put("Latitude",latp);
                parametros.put("Longitude",longp);
                parametros.put("Imagem",imgp);

                }
                catch(Exception e){
                    Log.d("ERRO", "login: " + e);
                }


                return parametros;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(postResquest);



    }


    public String convertImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
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


    public void sendNotificationFirebase(){
        String url = MySingleton.server + "pontos/firebase";


        StringRequest postResquest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            if(response.length() != 0 ) {
                                Log.d("RESPOSTA", "onResponse: " +response);
                            }

                        } catch (Exception e) {
                            notificarErro(getString(R.string.erro),getString(R.string.webservice));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notificarErro(getString(R.string.erro), getString(R.string.conexao));
                    }
                }) {

        };

        MySingleton.getInstance(this.getActivity()).addToRequestQueue(postResquest);

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }




}
