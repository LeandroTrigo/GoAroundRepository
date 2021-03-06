package e.utilizador.around;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class MySingleton {

    private static MySingleton instance;

    private  RequestQueue requestQueue;
    private static Context mCtx;

    public static String server = "http://192.168.1.66:5000/";

    private MySingleton(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySingleton getInstance(Context context) {
        if(instance == null) {
            instance = new MySingleton(context);
        }

        return instance;
    }


    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {getRequestQueue().add(req);}

}
