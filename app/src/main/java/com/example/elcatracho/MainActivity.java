package com.example.elcatracho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText username,password;
    Boolean error,success;

    String us,pass;

    private List<Usuarios> usuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.txtLoginUsuario);
        password = (EditText) findViewById(R.id.txtLoginPass);

        Button btnIngresar = (Button) findViewById(R.id.btnLoginIniciarSesion);
        Button btnRegistrar = (Button) findViewById(R.id.btnClienteSeleccionar);

        usuarios = new ArrayList<>();


        SharedPreferences datos = getSharedPreferences("datos", 0);
        String user = datos.getString("user","null");
        String pass = datos.getString("pass","null");

        if(!user.equals("null") && !pass.equals("null")){
            Intent intent = new Intent(getApplicationContext(), ActivityPantallaPrincipal.class);
            startActivity(intent);
            Toast.makeText(this, "Bienvenido " + user , Toast.LENGTH_LONG).show();

        }

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityRegistrar.class);
                startActivity(intent);
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error = false;
                if(username.getText().toString().isEmpty()){
                    error = true;
                    Toast.makeText(getApplicationContext(), "Debe completar el campo de usuario", Toast.LENGTH_LONG).show();
                }else if(password.getText().toString().isEmpty()){
                    error = true;
                    Toast.makeText(getApplicationContext(), "Debe completar el campo de contraseña", Toast.LENGTH_LONG).show();
                }

                if(!error){
                    getUsuarios();
                }
            }


        });
    }

    private void validarUsuario(String pUser, String pPass) {
        us=username.getText().toString();
        pass=password.getText().toString();
        if(pUser.equals(us) && pPass.equals(pass)){
            Intent intent = new Intent(getApplicationContext(), ActivityPantallaPrincipal.class);

            SharedPreferences datos = getSharedPreferences("datos", 0);

            SharedPreferences.Editor editor = datos.edit();
            editor.putString("user", us);
            editor.putString("pass", pass);
            editor.commit();

            Toast.makeText(this, "Bienvenido " + pUser , Toast.LENGTH_LONG).show();
            startActivity(intent);
        }

    }


    private void getUsuarios() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = restApiMethods.endPointGetUsuarios;

        //realizar peticion a la url del endpoint
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray EmpleArray = obj.getJSONArray("usuarios");


                    for (int i=0; i < EmpleArray.length(); i++){
                        JSONObject usuarioObject = EmpleArray.getJSONObject(i);

                        us = usuarioObject.getString("username");
                        pass = usuarioObject.getString("password");

                        validarUsuario(us,pass);

                        /*Usuarios user = new Usuarios(usuarioObject.getString("idUsuario"),
                                usuarioObject.getString("username"),
                                usuarioObject.getString("password"),
                                usuarioObject.getString("telefono"),
                                usuarioObject.getString("correo"));

                        if(usuarioObject.getString("username").equals(username.getText().toString()) && usuarioObject.getString("password").equals(password.getText().toString())){
                            success = true;
                            //usuarios.add(user);
                        }*/

                    }


                }catch (JSONException ex){

                }

            }
        }, new Response.ErrorListener(){
            public void  onErrorResponse(VolleyError error){
                Log.i("Error",error.toString());
            }
        });

        queue.add(stringRequest);

    }

}