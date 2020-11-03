package com.example.express;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class SubirImagenActivity extends AppCompatActivity {

    public Button avanzar, mUploadBtn;
    private ImageView foto;
    private ProgressDialog progressDialog;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    StorageReference storageReference;

    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_imagen);

        inicializarFirebase();
        foto = (ImageView) findViewById(R.id.img_foto);
        progressDialog = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();

        mUploadBtn = findViewById(R.id.btn_seleccionar_imagen);

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
    }// Fin del onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            final Uri uri = data.getData();

            progressDialog.setTitle("Subiendo imagen");
            progressDialog.setMessage("Subiento imagen a la nube");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference filePath = storageReference.child("fotos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

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

                                Glide.with(SubirImagenActivity.this)
                                        .load(downloadURL)
                                        .fitCenter()
                                        .centerCrop()
                                        .into(foto);

                                String uid = user.getUid();

                                HashMap hashMap = new HashMap();
                                hashMap.put("imagen",downloadURL);
                                databaseReference.child("Usuario").child(uid).updateChildren(hashMap);

                                Toast.makeText(SubirImagenActivity.this, "Imagen cargada exitosamente", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SubirImagenActivity.this, InicioActivity.class);
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
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this,data);

            // Recortar imagen
            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(640,480)
                    .setAspectRatio(1,2).start(SubirImagenActivity.this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File url = new File(resultUri.getPath());

                Picasso.get().load(url).into(foto);

                // Comprirmir imagen
                try{
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(640)
                            .setMaxHeight(1280)
                            .setQuality(90)
                            .compressToBitmap(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
                final byte [] thumb_byte = byteArrayOutputStream.toByteArray();

                // Boton avanzar
                subir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargando.setTitle("Subiendo imagen");
                        cargando.setMessage("Espere por favor...");
                        cargando.show();

                        final StorageReference ref = storageReference.child("fotoDePerfil.jpg");
                        UploadTask uploadTask = ref.putBytes(thumb_byte);

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw Objects.requireNonNull(task.getException());
                                }
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri downloaduri = task.getResult();
                                String uid = user.getUid();

                                HashMap hashMap = new HashMap();
                                hashMap.put("imagen",downloaduri.toString());
                                databaseReference.child("Usuario").child(uid).updateChildren(hashMap);

                                cargando.dismiss();

                                Toast.makeText(SubirImagenActivity.this, "Imagen cargada exitosamente", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(SubirImagenActivity.this, InicioActivity.class);
                                startActivity(i);
                            }
                        });
                    }
                });
            }
        }
    }
     */
}