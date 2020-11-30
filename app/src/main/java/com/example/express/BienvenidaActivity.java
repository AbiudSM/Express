package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.express.models.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BienvenidaActivity extends AppCompatActivity {

    public Button ofrecerServicio;
    public Button solicitarServicio;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        ofrecerServicio = findViewById(R.id.btn_OfrecerServicio);
        solicitarServicio = findViewById(R.id.btn_SolicitarServicio);

        ofrecerServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BienvenidaActivity.this);
                builder.setTitle(R.string.Ofrecer_un_servicio);
                builder.setMessage(R.string.Si_eres_un_profesionista_o_trabajador)
                        .setPositiveButton(R.string.Seleccionar_esta_opción, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(BienvenidaActivity.this, OfrecerServicioActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

        solicitarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BienvenidaActivity.this);
                builder.setTitle(R.string.Solicitar_un_servicio);
                builder.setMessage(R.string.Si_estás_en_busca_de_un_servicio)
                        .setPositiveButton(R.string.Seleccionar_esta_opción, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(BienvenidaActivity.this, SolicitarServicioActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }// Fin onCreate

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Usuario").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Usuario u = snapshot.getValue(Usuario.class);
                    String nombre = u.getNombre();
                    String imagen = u.getImagen();
                    if(nombre != null) {
                        if (imagen != null){
                            Intent intent = new Intent(BienvenidaActivity.this,InicioActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }else{
                            Intent intent = new Intent(BienvenidaActivity.this,SubirImagenActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}