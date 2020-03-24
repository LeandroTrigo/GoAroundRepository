package e.utilizador.around;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onNewToken(String token) {
        Log.d("TOKEN", "onNewToken: " +token);
        sharedPreferences = getSharedPreferences("TOKENF",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("token",token);
        editor.commit();
    }

    
}
