package e.utilizador.around;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.util.Locale;



public class DefinitionsFragment extends Fragment {

    ImageView picklanguagept,picklanguageen;
    Boolean anonimo;
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

}
