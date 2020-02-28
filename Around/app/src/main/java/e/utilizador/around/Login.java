package e.utilizador.around;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {

    private Button main,login;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        main = findViewById(R.id.anonymous);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, DoLogin.class);
                startActivity(intent);
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("anonimo", true);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }



}
