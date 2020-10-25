package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MiPerfilActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setSelectedItemId((R.id.menu_account));

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.menu_home){
                    Intent i = new Intent(getApplicationContext(), InicioActivity.class);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }

                if(item.getItemId() == R.id.menu_contacts){
                    Intent i = new Intent(getApplicationContext(), ContactosActivity.class);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }

                return true;
            }
        });
    }

    // MOSTRAR TOP MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return true;
    }

    // SWITCH DEL TOP MENU
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnSignOut:
                //Detectar al usuario actual
                mAuth = FirebaseAuth.getInstance();

                //SIGN OUT
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}