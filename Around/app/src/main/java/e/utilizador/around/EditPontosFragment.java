package e.utilizador.around;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ReportFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EditPontosFragment extends Fragment {

    EditText edittitulo, editdescricao;
    String titulo,descricao,imagem;
    ImageView editimagem;
    int idponto;
    Button editarponto, removerponto, escolherfoto;

    //Permissões
    private static final int PERMISSSION_REQUEST = 0;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int CAMERA_REQUEST_CODE = 228;


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
        editarponto = view.findViewById(R.id.edit_ponto);
        removerponto = view.findViewById(R.id.delete_ponto);
        escolherfoto = view.findViewById(R.id.escolher_foto);


        titulo = getArguments().getString("titulo");
        descricao = getArguments().getString("descricao");
        imagem = getArguments().getString("imagem");
        idponto = getArguments().getInt("id");

        edittitulo.setText(titulo);
        editdescricao.setText(descricao);
        Picasso.get().load(MySingleton.server + imagem).into(editimagem);

        editarponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePonto();
            }
        });

        removerponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletePonto();
            }
        });

        escolherfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageGalleryClicked(getView());
            }
        });

    }


    public void UpdatePonto(){
        String url = MySingleton.server + "pontos/updatepontos/" + idponto;

        StringRequest postResquest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {



                           notificarSucesso(getString(R.string.sucesso),getString(R.string.ponto_updated));

                            getActivity().onBackPressed();


                        } catch (Exception e) {
                            notificarErro(getString(R.string.erro),getString(R.string.webservice));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                                Log.d("ERRO1", "onErrorResponse: " +e1.getMessage());
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                                Log.d("ERRO2", "onErrorResponse: " +e2.getMessage());
                            }
                        }
                    }
                }) {


            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> jsonParams = new HashMap<String, String>();
                String tituloe = edittitulo.getText().toString().trim();
                String descricaoe = editdescricao.getText().toString();

                BitmapDrawable drawable = (BitmapDrawable) editimagem.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                jsonParams.put("Titulo", tituloe);
                jsonParams.put("Descricao", descricaoe);
                jsonParams.put("Imagem",convertImage(bitmap));



                return jsonParams;
            }
        };
        MySingleton.getInstance(this.getContext()).addToRequestQueue(postResquest);

    }



    public void DeletePonto(){
        String url = MySingleton.server + "pontos/deletepontos/" + idponto;

        StringRequest postResquest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            notificarSucesso(getString(R.string.sucesso),getString(R.string.ponto_deleted));
                            getActivity().onBackPressed();
                        } catch (Exception ex) {
                            notificarErro(getString(R.string.erro), getString(R.string.webservice));
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notificarErro(getString(R.string.erro), getString(R.string.conexao));
            }
        });

        MySingleton.getInstance(this.getContext()).addToRequestQueue(postResquest);

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


    public String convertImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
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
            if (requestCode == CAMERA_REQUEST_CODE) {
                notificarSucesso(getResources().getString(R.string.sucesso),getString(R.string.image_saved));
            }

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
                    editimagem.setImageBitmap(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    notificarErro(getResources().getString(R.string.erro),getString(R.string.image_not_saved));
                }

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
}
