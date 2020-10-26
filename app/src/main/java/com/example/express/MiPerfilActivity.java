package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.express.models.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MiPerfilActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;
    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText userNombre, userTelefono, userProfesion, userServicios, userCotizacion, userDescripcion;
    Button mbtn_guardar_cambios;
    public String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setSelectedItemId((R.id.menu_account));

        userNombre = findViewById(R.id.txt_nombre_perfil);
        userTelefono = findViewById(R.id.txt_telefono_perfil);
        userProfesion = findViewById(R.id.txt_profesion_perfil);
        userServicios = findViewById(R.id.txt_servicios_perfil);
        userCotizacion = findViewById(R.id.txt_cotizacion_perfil);
        userDescripcion = findViewById(R.id.txt_descripcion_perfil);
        mbtn_guardar_cambios = findViewById(R.id.btn_guardar_cambios);

        inicializarFirebase();
        MostrarDatos();

        mbtn_guardar_cambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUser();
            }
        });

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

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void MostrarDatos() {
        databaseReference.child("Usuario").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario u = snapshot.getValue(Usuario.class);
                userNombre.setText(u.getNombre());
                userTelefono.setText(u.getTelefono());
                userProfesion.setText(u.getProfesion());
                userServicios.setText(u.getServicios());
                userCotizacion.setText(u.getCotizacion());
                userDescripcion.setText(u.getDescripcion());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void UpdateUser() {
        String uid = user.getUid();
        String nombre = userNombre.getText().toString();
        String profesion = userProfesion.getText().toString();
        String servicios = userServicios.getText().toString();
        String cotizacion = userCotizacion.getText().toString();
        String telefono = userTelefono.getText().toString();
        String descripcion = userDescripcion.getText().toString();

        Usuario newUserData = new Usuario();
        newUserData.setUid(uid);
        newUserData.setNombre(nombre);
        newUserData.setCotizacion(cotizacion);
        newUserData.setProfesion(profesion);
        newUserData.setServicios(servicios);
        newUserData.setTelefono(telefono);
        newUserData.setDescripcion(descripcion);

        databaseReference.child("Usuario").child(newUserData.getUid()).setValue(newUserData);

        Toast.makeText(this, "Usuario Actualizado", Toast.LENGTH_SHORT).show();
    }
}