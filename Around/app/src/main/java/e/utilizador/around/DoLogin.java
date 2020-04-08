package e.utilizador.around;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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

        String url = MySingleton.server + "utilizador/login";

        StringRequest postResquest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        String emaillogin = email.getText().toString();
                        String passlogin = pass.getText().toString();

                        Log.d("RESPONSE", "RESPOSE" +response);

                        try {
                            Log.d("RESPONSE", "RESPOSE" +response);

                            JWT jwt = new JWT(response);
                            Claim subscriptionMetaData =jwt.getClaim("Nome");
                            String parsedValue = subscriptionMetaData.asString();

                            Claim subscriptionMetaData2 =jwt.getClaim("IdUtilizador");
                            Integer parsedValue2 = subscriptionMetaData2.asInt();

                            Log.d("USER", "onResponse: " +parsedValue2);



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
                                b.putString("nome",parsedValue);
                                b.putInt("id",parsedValue2);
                                intent.putExtras(b);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                        } catch (Exception e) {
                            notificarErro(getString(R.string.erro),getString(R.string.webservice));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        notificarErro(getString(R.string.erro),getString(R.string.credenciais));
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


                    try {
                       String emaillogado =  ChCrypto.encrypt(email.getText().toString().trim(), "MY SECRET KEY IS I AM A SECRET K");
                        String passlogada = ChCrypto.encrypt(pass.getText().toString().trim(),"MY SECRET KEY IS I AM A SECRET K");



                        jsonParams.put("Email", emaillogado);
                        jsonParams.put("Password", passlogada);



                    }
                    catch(Exception e){
                        Log.d("ERRO", "login: " + e);
                    }





                return jsonParams;
            }
        };

        saveLogin = sharedPreferences.getBoolean("saveLogin", true);
        MySingleton.getInstance(this).addToRequestQueue(postResquest);
        postResquest.setRetryPolicy(new DefaultRetryPolicy(
                300,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
