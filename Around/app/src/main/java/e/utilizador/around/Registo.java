package e.utilizador.around;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registo extends AppCompatActivity {

    EditText nome, email, pass, passc;
    Button registar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registo_activity);

        nome = findViewById(R.id.nome_user);
        email = findViewById(R.id.email_user);
        pass = findViewById(R.id.pass_user);
        passc = findViewById(R.id.confirm_pass);
        registar = findViewById(R.id.registar_button);

        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nome.getText().toString().length() != 0 && email.getText().toString().length() != 0 && pass.getText().toString().length() != 0) {
                    if (pass.getText().toString().equals(passc.getText().toString())) {
                        registar();
                    } else {
                        notificarErro(getString(R.string.erro), "As Passwords não Coincidem!");
                    }
                }
                else{
                    notificarErro(getString(R.string.erro), "Preencha Todos os Campos!");
                }
            }
        });

    }


    private void registar() {
        String url = MySingleton.server + "utilizador/registeruser";

        StringRequest postResquest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("REGISTO", "onResponse: " +response.length());

                        try {

                            if(response.length() != 32 ) {
                                Intent intent = new Intent(Registo.this, DoLogin.class);
                                startActivity(intent);
                            } else {
                                notificarErro(getString(R.string.erro), getString(R.string.email_existe));
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

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();

                try {
                    String emailr = ChCrypto.encrypt(email.getText().toString().trim(), "MY SECRET KEY IS I AM A SECRET K");
                    String nomer = ChCrypto.encrypt(nome.getText().toString().trim(), "MY SECRET KEY IS I AM A SECRET K");
                    String passwordr = ChCrypto.encrypt(pass.getText().toString().trim(), "MY SECRET KEY IS I AM A SECRET K");


                    parametros.put("Email", emailr);
                    parametros.put("Nome", nomer);
                    parametros.put("Password", passwordr);


                } catch(Exception e){
                    Log.d("ERRO", "login: " + e);
                }

                return parametros;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(postResquest);
    }



    public void notificarErro(String titulo, String mensagem) {
        new AlertDialog.Builder(this)
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
