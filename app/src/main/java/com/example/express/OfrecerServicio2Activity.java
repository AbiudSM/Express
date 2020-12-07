package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class OfrecerServicio2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText userNombre, userProfesion, userServicios, userCotizacion, userTelefono;
    public ImageView avanzar;
    public boolean band = true;
    String zona;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofrecer_servicio2);

        userNombre = findViewById(R.id.txt_nombre);
        userProfesion = findViewById(R.id.txt_profesion);
        userServicios = findViewById(R.id.txt_servicios);
        userCotizacion = findViewById(R.id.txt_cotizacion);
        userTelefono = findViewById(R.id.txt_telefono);
        inicializarFirebase();

        avanzar = (ImageView) findViewById(R.id.btn_avanzar);
        avanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                band = true;
                band = Validaciones(band);
                if(band == true){
                    UpdateUser();

                    Intent i = new Intent(OfrecerServicio2Activity.this, SubirImagenActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(OfrecerServicio2Activity.this, R.string.Debe_llenar_los_espacios_obligatorios, Toast.LENGTH_SHORT).show();
                }

            }
        });

        Spinner spinner = findViewById(R.id.zona_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.zona, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void UpdateUser() {
        String uid = user.getUid();
        String nombre = userNombre.getText().toString();
        String profesion = userProfesion.getText().toString();
        String servicios = userServicios.getText().toString();
        String cotizacion = userCotizacion.getText().toString();
        String telefono = userTelefono.getText().toString();
        String descripcion = getIntent().getStringExtra("descripcionSend");
        String correo = user.getEmail();

        Usuario u = new Usuario();
        u.setUid(uid);
        u.setNombre(nombre);
        u.setCotizacion(cotizacion);
        u.setProfesion(profesion);
        u.setServicios(servicios);
        u.setTelefono(telefono);
        u.setDescripcion(descripcion);
        u.setCorreo(correo);
        u.setZona(zona);

        float suma = (float) 0.0;
        u.setSuma(suma);
        int votantes = 0;
        u.setVotantes(votantes);
        float ratio = (float) 0.0;
        u.setRatio(ratio);

        databaseReference.child("Usuario").child(uid).setValue(u);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public boolean Validaciones(boolean band){
        String nombre = userNombre.getText().toString();
        String profesion = userProfesion.getText().toString();

        if (nombre.equals("")){
            userNombre.setError("Required");
            band = false;
        }

        if (profesion.equals("")){
            userProfesion.setError("Required");
            band = false;
        }

        return band;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        zona = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}