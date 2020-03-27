package e.utilizador.around;

import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class DefinitionsFragment extends Fragment {

    ImageView picklanguagept,picklanguageen;
    Boolean anonimo;
    Button finger;
    String nomeuser;
    int iduser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.definitions);
        if(CheckFragment.getInstance().fragmento != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CheckFragment.getInstance().fragmento).commit();
        }


        return inflater.inflate(R.layout.fragment_defenicoes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        anonimo = args.getBoolean("anonimo");
        nomeuser = args.getString("nome");
        iduser = args.getInt("id");



        picklanguagept = view.findViewById(R.id.pick_language_pt);
        picklanguageen = view.findViewById(R.id.pick_language_en);

        picklanguagept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLanguage("pt");

            }
        });

        picklanguageen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("en");
            }
        });



        finger = view.findViewById(R.id.button_finger);


        finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFinger();
            }
        });
    }

    private void setLanguage(String language){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language.toLowerCase()));
        res.updateConfiguration(conf,dm);
        Intent refresh = new Intent(this.getActivity(), MainActivity.class);
        CheckFragment.getInstance().fragmento = null;
        Bundle b = new Bundle();
        b.putBoolean("anomnimo", false);
        b.putString("nome",nomeuser);
        b.putInt("id",iduser);
        refresh.putExtras(b);
        startActivity(refresh);
        getActivity().finish();
    }


    public void showDialogFinger(){
        DialogFinger dialog = new DialogFinger();
        Bundle args = new Bundle();
        args.putInt("iduser", iduser);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(),"DialogFinger");

    }
}
