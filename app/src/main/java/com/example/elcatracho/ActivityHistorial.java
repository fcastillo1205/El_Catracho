package com.example.elcatracho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ActivityHistorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        ImageButton btnLavados = (ImageButton) findViewById(R.id.btnHisotialLavado);
        ImageButton btnAceite = (ImageButton) findViewById(R.id.btnHistorialAceite);

        btnLavados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityHistorialLavado.class);
                startActivity(intent);
            }
        });

        btnAceite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityHistorialAceite.class);
                startActivity(intent);
            }
        });


    }
}