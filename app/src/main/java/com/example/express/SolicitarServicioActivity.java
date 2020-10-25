package com.example.express;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.express.models.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class SolicitarServicioActivity extends AppCompatActivity {

    public Button avanzar;
    EditText userNombre, userTelefono;
    public boolean band = true;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_servicio);

        //BackButton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userNombre = findViewById(R.id.txt_nombre_ss);
        userTelefono = findViewById(R.id.txt_telefono_ss);
        inicializarFirebase();

        avanzar = findViewById(R.id.btn_avanzar);
        avanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                band = true;
                band = Validaciones(band);
                if(band == true){
                    UpdateUser();
                    Intent i = new Intent(SolicitarServicioActivity.this, InicioActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(SolicitarServicioActivity.this, "Debe llenar los espacios obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean Validaciones(boolean band){
        String nombre = userNombre.getText().toString();

        if (nombre.equals("")){
            userNombre.setError("Required");
            band = false;
        }

        return band;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void UpdateUser() {
        String nombre = userNombre.getText().toString();
        String telefono = userTelefono.getText().toString();

        Usuario u = new Usuario();
        u.setUid(UUID.randomUUID().toString());
        u.setNombre(nombre);
        u.setTelefono(telefono);

        databaseReference.child("Usuario").child(u.getUid()).setValue(u);
    }
}