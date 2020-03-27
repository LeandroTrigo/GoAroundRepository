package e.utilizador.around;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

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

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private CancellationSignal cancellationSignal;
    private Context context;
    private int id;

    public FingerprintHandler(Context mContext, int id) {
        context = mContext;
        this.id = id;
    }

    //Implement the startAuth method, which is responsible for starting the fingerprint authentication process//

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        notificarErro(context.getString(R.string.erro),context.getString(R.string.finger6) + errString);
    }

    @Override

    public void onAuthenticationFailed() {
        notificarErro(context.getString(R.string.erro),context.getString(R.string.finger5));
    }

    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {

        deleteUser(id);

    }



    public void deleteUser(int id){


        String url = MySingleton.server + "utilizador/" +id;

        StringRequest postResquest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("LOGS", "onResponse: " + response);

                        try {

                            if(response.length() == 2) {
                                ((Activity) context).finish();
                            } else {
                                notificarErro(context.getString(R.string.erro), context.getString(R.string.erro_eliminar_conta));
                            }

                        } catch (Exception e) {
                            notificarErro(context.getString(R.string.erro),context.getString(R.string.webservice));
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
                });

        MySingleton.getInstance(context).addToRequestQueue(postResquest);

    }


    public void notificarErro(String titulo, String mensagem) {
        new AlertDialog.Builder(this.context)
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
