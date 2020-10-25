package com.example.express;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesionExitoso extends AppCompatActivity {

    public TextView volverEnviarCorreo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion_exitoso);

        mAuth = FirebaseAuth.getInstance();
        volverEnviarCorreo = findViewById(R.id.enviar_correo);

        volverEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                user.sendEmailVerification();
                Toast.makeText(InicioSesionExitoso.this, "Correo de verificación enviado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user.isEmailVerified()){
            Intent i = new Intent(this, InicioActivity.class);
            startActivity(i);
        }
    }// Fin onStart
}