package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.express.fragments.AccountFragment;
import com.example.express.fragments.ContactsFragment;
import com.example.express.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class InicioActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        showSelectedFragment(new HomeFragment());

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.menu_home){
                    showSelectedFragment(new HomeFragment());
                }

                if(item.getItemId() == R.id.menu_contacts){
                    showSelectedFragment(new ContactsFragment());
                }

                if(item.getItemId() == R.id.menu_account){
                    showSelectedFragment(new AccountFragment());
                }

                return true;
            }
        });
    }// Fin on Create

    private void showSelectedFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container_frame, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnSignOut:
                //Detectar al usuario actual
                mAuth = FirebaseAuth.getInstance();

                //SIGN OUT
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(InicioActivity.this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

/*
SIGN OUT
    private Button btn_cerrar;
    FirebaseAuth mAuth;
    btn_cerrar = (Button) findViewById(R.id.btn_cerrar_sesion);

    //Detectar al usuario actual
    mAuth = FirebaseAuth.getInstance();

        btn_cerrar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(InicioActivity.this,MainActivity.class));
        }
    });

*/