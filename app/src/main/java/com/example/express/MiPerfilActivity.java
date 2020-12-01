package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.express.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MiPerfilActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;
    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText userNombre, userTelefono, userProfesion, userServicios, userCotizacion, userDescripcion;
    ImageView photo, editarFoto;
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
        mbtn_guardar_cambios.setEnabled(false);
        photo = (ImageView) findViewById(R.id.myPhoto);
        editarFoto = (ImageView) findViewById(R.id.editPhoto);

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

        editarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MiPerfilActivity.this,ActivityEditarFoto.class);
                startActivity(intent);
            }
        });

        // Ocultar boton guardar
        OcultarBoton();
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
                if (snapshot.exists()){
                    Usuario u = snapshot.getValue(Usuario.class);
                    userNombre.setText(u.getNombre());
                    userTelefono.setText(u.getTelefono());
                    userProfesion.setText(u.getProfesion());
                    userServicios.setText(u.getServicios());
                    userCotizacion.setText(u.getCotizacion());
                    userDescripcion.setText(u.getDescripcion());

                    String imagen = u.getImagen();
                    Picasso.get().load(imagen).into(photo);
                }
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

            case R.id.btnDeleteAccount:
                AlertDialog.Builder builder = new AlertDialog.Builder(MiPerfilActivity.this);
                builder.setTitle(R.string.Eliminar_cuenta_permanentemente);
                builder.setMessage(R.string.Una_vez_que_elimine)
                        .setPositiveButton(R.string.Sí, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user.delete();
                                databaseReference.child("Usuario").child(uid).removeValue();

                                Toast.makeText(MiPerfilActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MiPerfilActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
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

        HashMap hashMap = new HashMap();
        hashMap.put("nombre",nombre);
        if (!profesion.equals("")){
            hashMap.put("profesion",profesion);
        }else{
            databaseReference.child("Usuario").child(uid).child("profesion").removeValue();
        }
        hashMap.put("servicios",servicios);
        hashMap.put("cotizacion",cotizacion);
        hashMap.put("telefono",telefono);
        hashMap.put("descripcion",descripcion);
        hashMap.put("uid",uid);


        databaseReference.child("Usuario").child(uid).updateChildren(hashMap);

        Toast.makeText(this, "Usuario Actualizado", Toast.LENGTH_SHORT).show();

        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void OcultarBoton() {
        databaseReference.child("Usuario").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario u = snapshot.getValue(Usuario.class);
                final String newNombre = u.getNombre();
                userNombre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if((charSequence.equals(newNombre))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if((editable.equals(newNombre))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }
                });

                final String newProfesion = u.getProfesion();
                userProfesion.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if((charSequence.equals(newProfesion))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if((editable.equals(newProfesion))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }
                });

                final String newServicios = u.getServicios();
                userServicios.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if((charSequence.equals(newServicios))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if((editable.equals(newServicios))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }
                });

                final String newCotizacion = u.getCotizacion();
                userCotizacion.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if((charSequence.equals(newCotizacion))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if((editable.equals(newCotizacion))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }
                });

                final String newTelefono = u.getTelefono();
                userTelefono.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if((charSequence.equals(newTelefono))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if((editable.equals(newTelefono))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }
                });

                final String newDescripcion = u.getDescripcion();
                userDescripcion.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if((charSequence.equals(newDescripcion))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if((editable.equals(newDescripcion))){
                            mbtn_guardar_cambios.setEnabled(false);
                        }else{
                            mbtn_guardar_cambios.setEnabled(true);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}