package e.utilizador.around;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import e.utilizador.around.R;

public class DoLogin extends AppCompatActivity {

    Button loggedin;
    TextView registar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_2);

        loggedin = findViewById(R.id.button_login);
        registar = findViewById(R.id.nova_conta);


        loggedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoLogin.this, MainActivity.class);
                startActivity(intent);
            }
        });

        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoLogin.this, Registo.class);
                startActivity(intent);
            }
        });
    }




}
