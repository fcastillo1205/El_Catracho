package com.example.elcatracho;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ActivityRegistrar extends AppCompatActivity {
    EditText username, password, cpassword, telefono, correo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        username = (EditText) findViewById(R.id.txtRegistrateNombre);
        password = (EditText) findViewById(R.id.txtRegistrateContrasenia);
        cpassword = (EditText) findViewById(R.id.txtRegistrateCContrasenia);
        telefono = (EditText) findViewById(R.id.txtRegistrateTelefono);
        correo = (EditText) findViewById(R.id.txtRegistrateEmail);

        Button btnRegistrarse = (Button) findViewById(R.id.btnClienteSeleccionar);
        
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarse();
            }
        });



    }

    private void registrarse() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");


        Boolean pError = false;
        if(username.getText().toString().isEmpty()){
            pError = true;
            Toast.makeText(getApplicationContext(), "Debe completar el campo de username", Toast.LENGTH_LONG).show();
        }else if(password.getText().toString().isEmpty()){
            pError = true;
            Toast.makeText(getApplicationContext(), "Debe completar el campo de contraseña", Toast.LENGTH_LONG).show();
        }else if(cpassword.getText().toString().isEmpty()){
            pError = true;
            Toast.makeText(getApplicationContext(), "Debe completar el campo de confirmacion de contraseña", Toast.LENGTH_LONG).show();
        }else if(telefono.getText().toString().isEmpty()){
            pError = true;
            Toast.makeText(getApplicationContext(), "Debe completar el campo de telefono", Toast.LENGTH_LONG).show();
        }else if(correo.getText().toString().isEmpty()){
            pError = true;
            Toast.makeText(getApplicationContext(), "Debe completar el campo de correo", Toast.LENGTH_LONG).show();
        }else if(!password.getText().toString().equals(cpassword.getText().toString())){
            pError = true;
            Toast.makeText(getApplicationContext(), "Las contraseñas deben coincidir", Toast.LENGTH_LONG).show();
        }



        if(!pError){
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, restApiMethods.endPointCrearUsuario, response -> {
                if (response.length() > 0) {
                    Toast.makeText(this, "Datos Insertados", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Datos NO Insertados", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }, error -> {
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("username", username.getText().toString().trim());
                    params.put("password", password.getText().toString().trim());
                    params.put("correo", correo.getText().toString().trim());
                    params.put("telefono", telefono.getText().toString().trim());

                    return params;
                }
            };

            if(!pError){
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(request);
            }
        }
    }
}