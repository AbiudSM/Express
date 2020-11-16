package com.example.express;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class OfrecerServicioActivity extends AppCompatActivity {

    ImageView avanzar;
    EditText userDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofrecer_servicio);

        //BackButton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userDescripcion = findViewById(R.id.txt_descripcion);

        avanzar = (ImageView) findViewById(R.id.btn_avanzar);
        avanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descripcion = userDescripcion.getText().toString();
                Intent i = new Intent(OfrecerServicioActivity.this, OfrecerServicio2Activity.class);
                i.putExtra("descripcionSend",descripcion);
                startActivity(i);
            }
        });
    }
}