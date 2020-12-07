package com.example.express;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.express.models.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class SolicitarServicioActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView avanzar;
    EditText userNombre, userTelefono;
    public boolean band = true;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String zona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_servicio);

        //BackButton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userNombre = findViewById(R.id.txt_nombre_ss);
        userTelefono = findViewById(R.id.txt_telefono_ss);
        inicializarFirebase();

        avanzar = (ImageView) findViewById(R.id.btn_avanzar);
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
                    Toast.makeText(SolicitarServicioActivity.this, R.string.Debe_llenar_los_espacios_obligatorios, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Spinner spinner = findViewById(R.id.zona_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.zona, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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

        float suma = (float) 0.0;
        int votantes = 0;
        float ratio = (float) 0.0;

        HashMap hashMap = new HashMap();
        hashMap.put("nombre",nombre);
        hashMap.put("telefono",telefono);
        hashMap.put("correo",correo);
        hashMap.put("uid",uid);
        hashMap.put("zona",zona);
        hashMap.put("suma",suma);
        hashMap.put("votantes",votantes);
        hashMap.put("ratio",ratio);

        databaseReference.child("Usuario").child(uid).setValue(hashMap);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        zona = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}