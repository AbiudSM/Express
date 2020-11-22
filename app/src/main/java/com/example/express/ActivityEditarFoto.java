package com.example.express;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.express.models.Usuario;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ActivityEditarFoto extends AppCompatActivity {

    public Button mUploadBtn;
    private ImageView foto;
    private ProgressDialog progressDialog;
    public ImageView avanzar;
    public TextView omitir;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    StorageReference storageReference;

    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_foto);

        inicializarFirebase();
        foto = (ImageView) findViewById(R.id.img_foto);
        progressDialog = new ProgressDialog(this);
        avanzar = (ImageView) findViewById(R.id.subir);
        avanzar.setVisibility(View.INVISIBLE);
        omitir = (TextView) findViewById(R.id.txt_omitir);
        mUploadBtn = findViewById(R.id.btn_seleccionar_imagen);

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference.child("Usuario").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Usuario u = snapshot.getValue(Usuario.class);
                    Picasso.get().load(u.getImagen()).into(foto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        omitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = user.getUid();
                HashMap hashMap = new HashMap();
                hashMap.put("imagen","https://firebasestorage.googleapis.com/v0/b/express-31164.appspot.com/o/fotos%2Fic_usuario.png?alt=media&token=f32f03a1-777d-4ddd-af0e-f1ac0df7c85a");

                databaseReference.child("Usuario").child(uid).updateChildren(hashMap);
                Intent i = new Intent(ActivityEditarFoto.this, MiPerfilActivity.class);
                startActivity(i);
            }
        });
    }// Fin del onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            final Uri uri = data.getData();

            foto.setImageURI(uri);
            avanzar.setVisibility(View.VISIBLE);

            avanzar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.setTitle("Subiendo imagen");
                    progressDialog.setMessage("Subiento imagen a la nube");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    StorageReference filePath = storageReference.child("fotos").child(uri.getLastPathSegment());

                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            final StorageReference ref = storageReference.child("fotos").child(uri.getLastPathSegment());
                            UploadTask uploadTask = ref.putFile(uri);

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        String downloadURL = downloadUri.toString();

                                        Glide.with(ActivityEditarFoto.this)
                                                .load(downloadURL)
                                                .fitCenter()
                                                .centerCrop()
                                                .into(foto);

                                        String uid = user.getUid();

                                        HashMap hashMap = new HashMap();
                                        hashMap.put("imagen",downloadURL);
                                        databaseReference.child("Usuario").child(uid).updateChildren(hashMap);

                                        progressDialog.dismiss();
                                        Toast.makeText(ActivityEditarFoto.this, "Imagen cargada exitosamente", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ActivityEditarFoto.this, MiPerfilActivity.class);
                                        startActivity(i);
                                    } else {
                                        // Handle failures
                                        // ...
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}