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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class SolicitarServicioActivity extends AppCompatActivity {

    public Button avanzar;
    EditText userNombre, userTelefono;
    public boolean band = true;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                    Intent i = new Intent(SolicitarServicioActivity.this, SubirImagenActivity.class);
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
        String uid = user.getUid();
        String nombre = userNombre.getText().toString();
        String telefono = userTelefono.getText().toString();
        String correo = user.getEmail();

        HashMap hashMap = new HashMap();
        hashMap.put("nombre",nombre);
        hashMap.put("telefono",telefono);
        hashMap.put("correo",correo);

        databaseReference.child("Usuario").child(uid).setValue(hashMap);
    }
}