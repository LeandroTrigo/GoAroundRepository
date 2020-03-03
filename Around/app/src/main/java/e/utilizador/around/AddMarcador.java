package e.utilizador.around;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddMarcador extends DialogFragment {

    DatabaseHelper db;
    Button foto;
    ImageView imageView;


    //Permissões
    private static final int PERMISSSION_REQUEST = 0;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int CAMERA_REQUEST_CODE = 228;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adicionar_marcador,container,false);
        //descricao = getArguments().getString("desc");


        db = new DatabaseHelper(getActivity());
        imageView = view.findViewById(R.id.imageView);
        foto = view.findViewById(R.id.button_foto);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageGalleryClicked(getView());
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
                    notificarSucesso(getResources().getString(R.string.sucesso),"Permissões Concedidas!");
                }else{
                    notificarErro(getResources().getString(R.string.erro),"Permissões Negadas!");
                }
        }
    }


    //Metodo para tranferir imagens da galeria para a aplicação

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                notificarSucesso(getResources().getString(R.string.sucesso),"Imagem Guardada!");
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
                    imageView.setImageBitmap(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    notificarErro(getResources().getString(R.string.erro),"Imagem não guardada!");
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


}
