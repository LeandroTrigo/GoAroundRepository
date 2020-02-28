package e.utilizador.around;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import e.utilizador.around.R;

public class DoLogin extends AppCompatActivity {

    TextView registar;
    private EditText email,pass;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Boolean saveLogin;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_2);

        registar = findViewById(R.id.nova_conta);
        email = findViewById(R.id.email_login);
        pass = findViewById(R.id.pass_login);

        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.checkboxlogin);
        editor = sharedPreferences.edit();


        saveLogin = sharedPreferences.getBoolean("saveLogin", true);
        if (saveLogin) {
            Log.d("EMAIL", "onCreate: " + email);
            email.setText(sharedPreferences.getString("email", null));
            pass.setText(sharedPreferences.getString("password", null));
        }

        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoLogin.this, Registo.class);
                startActivity(intent);
            }
        });
    }

    public void login(final View view) {
        String url = "http://192.168.1.66:5000/utilizador/login";

        Log.d("ENTROU1", "login: ");

        StringRequest postResquest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("ENTROU2", "login: " +response);
                        /*

                        String emaillogin = email.getText().toString();
                        String passlogin = pass.getText().toString();

                        try {
                            Log.d("RESPONSE", "RESPOSE" +response);

                            if (response != null){

                                Log.d("ENTROU4", "login: ");



                                //Se o utilizador escolher salvar a sua sessao a variavel saveLogin passa a true e guardamos o seu username e password.
                                if (saveLoginCheckBox.isChecked()) {
                                    editor.putBoolean("saveLogin", true);
                                    editor.putString("email", emaillogin);
                                    editor.putString("password", passlogin);
                                    editor.commit();
                                } else {
                                    editor.putBoolean("saveLogin", false);
                                    editor.putString("email", null);
                                    editor.putString("password", null);
                                    editor.commit();
                                }

                                Intent intent = new Intent(DoLogin.this, MainActivity.class);
                                Bundle b = new Bundle();
                                b.putBoolean("anomnimo", false);
                                intent.putExtras(b);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                notificarErro(getString(R.string.erro),getString(R.string.credenciais));
                            }
                        } catch (Exception e) {
                            notificarErro(getString(R.string.erro),getString(R.string.webservice));
                        }
                    */}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        Log.d("ERRO DE VOLLEY", "onErrorResponse: " +error);
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
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
                String emaillogado = email.getText().toString().trim();
                String passlogada = pass.getText().toString();


                jsonParams.put("Email", emaillogado);
                jsonParams.put("Password", passlogada);



                return jsonParams;
            }
        };

        saveLogin = sharedPreferences.getBoolean("saveLogin", true);
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
