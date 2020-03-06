package e.utilizador.around;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Boolean anonimo;
    ImageView edit,fotoperfil;
    TextView nomeperfil;
    String nomeuser;
    Integer iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            anonimo = extras.getBoolean("anonimo");
            nomeuser = extras.getString("nome");
            iduser = extras.getInt("id");
        }


        Log.d("USER", "onNavigationItemSelected: " +iduser);

        Menu nav_Menu = navigationView.getMenu();

        View headerView = navigationView.getHeaderView(0);
        edit = headerView.findViewById(R.id.edit_profile);
        nomeperfil= headerView.findViewById(R.id.nome_perfil);
        fotoperfil = headerView.findViewById(R.id.imageperfil);

        if(anonimo == true){
            nav_Menu.findItem(R.id.nav_report).setVisible(false);
            nav_Menu.findItem(R.id.nav_reports).setVisible(false);
            edit.setVisibility(View.INVISIBLE);
            nomeperfil.setText(getString(R.string.anonimo));
            fotoperfil.setImageResource(R.drawable.anonymous);
        }
        else{
            nomeperfil.setText(nomeuser);
        }


        navigationView.setNavigationItemSelectedListener(this);


        if(CheckFragment.getInstance().fragmento != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CheckFragment.getInstance().fragmento).commit();
        }
        else{
            HomeFragment home = new HomeFragment();
            CheckFragment.getInstance().fragmento = home;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_report){
            ReportFragment fragment = new ReportFragment();
            CheckFragment.getInstance().fragmento = fragment;
            Bundle args = new Bundle();
            args.putInt("id", iduser);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
        }
        else if(id == R.id.nav_reports){
            ReportsFragment fragment = new ReportsFragment();
            CheckFragment.getInstance().fragmento = fragment;
            Bundle args = new Bundle();
            args.putInt("id", iduser);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
        }
        else if(id == R.id.nav_notes){
            Fragment fragment = new Notas();
            CheckFragment.getInstance().fragmento = fragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        }
        else if(id == R.id.nav_logout) {
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
