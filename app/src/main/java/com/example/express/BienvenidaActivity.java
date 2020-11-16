package com.example.express;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(BienvenidaActivity.this);
                builder.setTitle("Ofrecer un servicio");
                builder.setMessage("Si eres un profesionista o trabajador de oficio que desea ofrecer sus servicios a posibles clientes esta es tu opción")
                        .setPositiveButton("Seleccionar esta opción", new DialogInterface.OnClickListener() {
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
                builder.setTitle("Solicitar un servicio");
                builder.setMessage("Si estás en busca de un servicio o contactar con un profesionista esta es tu opción")
                        .setPositiveButton("Seleccionar esta opción", new DialogInterface.OnClickListener() {
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
    }
}