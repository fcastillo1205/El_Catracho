package com.example.elcatracho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ActivityPantallaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

       ImageButton btnInicioCootizar = (ImageButton) findViewById(R.id.btnInicioCootizar);
        ImageButton btnInicioAdministrar = (ImageButton) findViewById(R.id.btnInicioAdministrar);
        ImageButton btnInicioCliente = (ImageButton) findViewById(R.id.btnInicioCliente);
        ImageButton btnInicioHistorial = (ImageButton) findViewById(R.id.btnInicioHistorial);

       btnInicioCootizar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), ActivitySolicitarCootizacion.class);
               startActivity(intent);
           }
       });

        btnInicioAdministrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences datos = getSharedPreferences("datos", 0);

                SharedPreferences.Editor editor = datos.edit();
                editor.putString("user", "null");
                editor.putString("pass", "null");
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnInicioCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityCliente.class);
                startActivity(intent);
            }
        });

        btnInicioHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityHistorial.class);
                startActivity(intent);
            }
        });

    }
}