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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public String TAG = " ";
    public String email, password;
    public EditText txt_correo;
    public EditText txt_contrasena;
    public Button btn_iniciarSesion;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        txt_correo = findViewById(R.id.correo);
        txt_contrasena = findViewById(R.id.contrasena);
        btn_iniciarSesion = findViewById(R.id.iniciarSesion);

        btn_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IniciarSesion();
            }
        });
    }

    private void IniciarSesion() {
        email = txt_correo.getText().toString();
        password = txt_contrasena.getText().toString();

        if (email.equals("")){
            txt_correo.setError("Ingrese un correo / Enter an email");
            return;
        }
        if (password.equals("")){
            txt_contrasena.setError("Ingrese su contraseña / Enter your password");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(!user.isEmailVerified()) {
                                Toast.makeText(MainActivity.this, R.string.Correo_electrónico_no_verificado, Toast.LENGTH_SHORT).show();
                                irInicioSesionExitoso();
                            }else {
                                irBienvenida();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, R.string.Correo_o_contraseña_incorrectas,
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()) {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser user) {
        Intent i = new Intent(this, InicioActivity.class);
        startActivity(i);
        overridePendingTransition(0,0);
    }

    public void irRegistrarse(View view){
        Intent i = new Intent(this, RegistrarseActivity.class);
        startActivity(i);
    }

    public void irInicioSesionExitoso(){
        Intent i = new Intent(this, InicioSesionExitoso.class);
        startActivity(i);
    }

    public  void irBienvenida(){
        Intent i = new Intent(this, BienvenidaActivity.class);
        startActivity(i);
    }
}