package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.express.models.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InicioActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ListView listV_usuarios;
    private List<Usuario> listUsuario = new ArrayList<Usuario>();
    ArrayAdapter<Usuario> arrayAdapterUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setSelectedItemId((R.id.menu_home));
        listV_usuarios = findViewById(R.id.lv_datosUsuarios);

        inicializarFirebase();
        ListarDatos();

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.menu_contacts){
                    Intent i = new Intent(getApplicationContext(), ContactosActivity.class);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }

                if(item.getItemId() == R.id.menu_account){
                    Intent i = new Intent(getApplicationContext(), MiPerfilActivity.class);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }

                return true;
            }
        });
    }// Fin on Create

    private void ListarDatos() {
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUsuario.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()){
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    listUsuario.add(u);

                    arrayAdapterUsuario = new ArrayAdapter<Usuario>(getApplicationContext(), R.layout.my_list,listUsuario);
                    listV_usuarios.setAdapter(arrayAdapterUsuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
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