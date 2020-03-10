package e.utilizador.around;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SupportFragment extends Fragment {


    private TextView email,tele,labeltele;
    private Button enviar,ligar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.credits);
        if(CheckFragment.getInstance().fragmento != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CheckFragment.getInstance().fragmento).commit();
        }
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = getView().findViewById(R.id.email);
        tele = getView().findViewById(R.id.telemovel);
        labeltele = getView().findViewById(R.id.label_telemovel);
        enviar = getView().findViewById(R.id.button_email);
        ligar = getView().findViewById(R.id.button_ligar);


        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDial();
            }
        });
    }

    private void sendMail(){
        String emailList = email.getText().toString();
        String[] email = emailList.split(",");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,email);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, getString(R.string.email_client)));
    }

    private void openDial(){
        String call = tele.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+call));
        startActivity(intent);
    }
}
