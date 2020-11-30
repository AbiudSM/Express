package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MostrarUsuarioActivity extends AppCompatActivity {

    ImageView imagenUsuario, eliminarContacto, guardarContacto;
    TextView nombreUsuario, profesionUsuario, telefonoUsuario, correoUsuario, cotizacionUsuario, serviciosUsuario, descripcionUsuario;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_usuario);
        inicializarFirebase();

        guardarContacto = (ImageView) findViewById(R.id.btn_guardar_contacto);

        nombreUsuario = (TextView) findViewById(R.id.usuarioNombre);
        profesionUsuario = (TextView) findViewById(R.id.usuarioProfesion);
        telefonoUsuario = (TextView) findViewById(R.id.usuarioTelefono);
        correoUsuario = (TextView) findViewById(R.id.usuarioCorreo);
        cotizacionUsuario = (TextView) findViewById(R.id.usuarioCotizacion);
        serviciosUsuario = (TextView) findViewById(R.id.usuarioServicios);
        descripcionUsuario = (TextView) findViewById(R.id.usuarioDescripcion);
        imagenUsuario = (ImageView) findViewById(R.id.userImage);

        eliminarContacto = (ImageView) findViewById(R.id.btnEliminarContacto);
        eliminarContacto.setVisibility(View.INVISIBLE);

        final String idUsuario = getIntent().getStringExtra("UsuarioID");
        final String isContacto = getIntent().getStringExtra("isContacto");


        databaseReference.child("Usuario").child(idUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Usuario u = snapshot.getValue(Usuario.class);
                    String nombre = u.getNombre();
                    String profesion = u.getProfesion();
                    final String telefono = u.getTelefono();
                    String cotizacion = u.getCotizacion();
                    String servicios = u.getServicios();
                    String descripcion = u.getDescripcion();
                    String imagen = u.getImagen();
                    final String correo = u.getCorreo();

                    Picasso.get().load(imagen).into(imagenUsuario);
                    nombreUsuario.setText(nombre);
                    profesionUsuario.setText(profesion);

                    if((telefono != null) && (!telefono.equals(""))){
                        telefonoUsuario.setText(telefono);
                    }

                    if((correo != null) && (!correo.equals(""))){
                        correoUsuario.setText(correo);
                        correoUsuario.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String [] addresses = new String[1];
                                addresses[0] = correo;
                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    if((cotizacion != null) && (!cotizacion.equals(""))){
                        cotizacionUsuario.setText(cotizacion);
                    }

                    if((servicios != null) && (!servicios.equals(""))){
                        serviciosUsuario.setText(servicios);
                    }

                    if((descripcion != null) && (!descripcion.equals(""))){
                        descripcionUsuario.setText(descripcion);
                    }


                    // Onclick Telefono
                    if((telefono != null) && (!telefono.equals(""))){
                        telefonoUsuario.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel: " + telefono.trim()));
                                startActivity(intent);
                            }
                        });
                    }

                    // Boton guardar contacto
                    guardarContacto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HashMap hashMap = new HashMap();
                            hashMap.put(uid,true);

                            databaseReference.child("Usuario").child(idUsuario).child("Contactos").updateChildren(hashMap);
                            Toast.makeText(MostrarUsuarioActivity.this, R.string.Usuario_guardado_en_contactos, Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Eliminar contacto
                    if(isContacto.equals("esContacto")){
                        guardarContacto.setVisibility(View.INVISIBLE);
                        eliminarContacto.setVisibility(View.VISIBLE);
                        eliminarContacto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MostrarUsuarioActivity.this);
                                builder.setTitle(R.string.Eliminar_contacto);
                                builder.setMessage(R.string.Desea_eliminar_este_contacto)
                                        .setPositiveButton(R.string.Sí, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                databaseReference.child("Usuario").child(idUsuario).child("Contactos").child(uid).removeValue();
                                                Intent intent = new Intent(MostrarUsuarioActivity.this,ContactosActivity.class);
                                                startActivity(intent);
                                                Toast.makeText(MostrarUsuarioActivity.this, R.string.Usuario_eliminado_de_contactos, Toast.LENGTH_SHORT).show();
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
                    }// Fin del if

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


}