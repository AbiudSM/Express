package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class RegistrarseActivity extends AppCompatActivity {

    public String TAG = " ";
    private FirebaseAuth mAuth;
    public String email, password;
    public EditText txt_correo;
    public EditText txt_contrasena, txt_confirmar_contrasena;
    public Button btn_registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        txt_correo = findViewById(R.id.correo_singUp);
        txt_contrasena = findViewById(R.id.contrasena_singUp);
        txt_confirmar_contrasena = findViewById(R.id.confirmar_contrasena_singUp);
        btn_registrarse = findViewById(R.id.singUpButton);

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registrarse();
            }
        });



    }// Fin OnCreate

    private void Registrarse() {
        email = txt_correo.getText().toString();
        password = txt_contrasena.getText().toString();
        String confimPassword = txt_confirmar_contrasena.getText().toString();

        if (email.equals("")){
            txt_correo.setError("Correo necesario");
            return;
        }
        if (password.equals("")){
            txt_contrasena.setError("Contraseña necesaria");
            return;
        }
        if (confimPassword.equals("")){
            txt_correo.setError("Required");
            return;
        }

        if(password.equals(confimPassword)){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this,  new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrarseActivity.this, "Error al autenticar su cuenta, favor de ingresar un correo existente.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent i = new Intent(this, InicioSesionExitoso.class);
        startActivity(i);
    }// Fin updateUI

}