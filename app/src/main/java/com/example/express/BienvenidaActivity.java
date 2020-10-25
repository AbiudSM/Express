package com.example.express;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BienvenidaActivity extends AppCompatActivity {

    public Button ofrecerServicio;
    public Button solicitarServicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        ofrecerServicio = findViewById(R.id.btn_OfrecerServicio);
        solicitarServicio = findViewById(R.id.btn_SolicitarServicio);

        ofrecerServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BienvenidaActivity.this, OfrecerServicioActivity.class);
                startActivity(i);
            }
        });

        solicitarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BienvenidaActivity.this, SolicitarServicioActivity.class);
                startActivity(i);
            }
        });
    }
}