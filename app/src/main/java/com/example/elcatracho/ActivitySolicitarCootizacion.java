package com.example.elcatracho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class ActivitySolicitarCootizacion extends AppCompatActivity {
    Spinner spCotizacionTipoServicio, spCotizacionTipoUbicacion;
    EditText vehiculo, fecha, hora;

    String latitud, longitud;

    String TipoServicio[] = {"Cambio de Aceite", "Lavado"};
    String TipoDireccion[] = {"En Local", "A Domicilio"};
    Boolean error;
    FusedLocationProviderClient fusedLocationProviderClient;

    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_cootizacion);

        SharedPreferences datos = getSharedPreferences("datos", 0);
        user = datos.getString("user","null");
        pass = datos.getString("pass","null");

        vehiculo = (EditText) findViewById(R.id.txtCotizacionVehiculo);
        fecha = (EditText) findViewById(R.id.txtCotizarFecha);
        hora = (EditText) findViewById(R.id.txtCotizarHora);


        spCotizacionTipoServicio = (Spinner) findViewById(R.id.spCotizacionTipoServicio);
        spCotizacionTipoUbicacion = (Spinner) findViewById(R.id.spCotizacionTipoUbicacion);


        ArrayAdapter<String> spinnerArrayAdapterSerivicio = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TipoServicio);
        spinnerArrayAdapterSerivicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCotizacionTipoServicio.setAdapter(spinnerArrayAdapterSerivicio);


        ArrayAdapter<String> spinnerArrayAdapterUbicacion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TipoDireccion);
        spinnerArrayAdapterUbicacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCotizacionTipoUbicacion.setAdapter(spinnerArrayAdapterUbicacion);

        Button cotizar = (Button) findViewById(R.id.btnClienteSeleccionar);

        cotizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = new ProgressDialog(ActivitySolicitarCootizacion.this);
                progressDialog.setMessage("Cargando...");

                error = false;
                if (vehiculo.getText().toString().isEmpty()) {
                    error = true;
                    Toast.makeText(getApplicationContext(), "Debe completar el campo de vehiculo", Toast.LENGTH_LONG).show();
                } else if (fecha.getText().toString().isEmpty()) {
                    error = true;
                    Toast.makeText(getApplicationContext(), "Debe completar el campo de fecha", Toast.LENGTH_LONG).show();
                } else if (hora.getText().toString().isEmpty()) {
                    error = true;
                    Toast.makeText(getApplicationContext(), "Debe completar el campo de hora", Toast.LENGTH_LONG).show();
                } else if (spCotizacionTipoServicio.getSelectedItem().toString().equals("Cambio de Aceite") && spCotizacionTipoUbicacion.getSelectedItem().toString().equals("A Domicilio")) {
                    error = true;
                    Toast.makeText(getApplicationContext(), "Este Servicio solo esta disponible en local", Toast.LENGTH_LONG).show();
                }

                if(!error){
                    progressDialog.show();
                    StringRequest request = new StringRequest(Request.Method.POST, restApiMethods.endPointCrearCotizacion, response -> {
                        if (response.length() > 0) {
                            Toast.makeText(getApplicationContext(), "Datos Insertados", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Datos no Insertados", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }, error -> {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("vehiculo", vehiculo.getText().toString());
                            params.put("tipoServicio", spCotizacionTipoServicio.getSelectedItem().toString());
                            params.put("tipoUbicacion", spCotizacionTipoUbicacion.getSelectedItem().toString());
                            params.put("fecha", fecha.getText().toString());
                            params.put("hora", hora.getText().toString());
                            params.put("username", user);
                            params.put("latitud", latitud);
                            params.put("longitud", longitud);
                            params.put("costo", "");

                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(request);
                }
            }
        });

        //PERMISO DE UBICACION
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ActivitySolicitarCootizacion.this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ActivitySolicitarCootizacion.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(ActivitySolicitarCootizacion.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    if (location != null) {
                        latitud = String.valueOf(location.getLatitude());
                        longitud = String.valueOf(location.getLongitude());
                    }
                    else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        //Initialize location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                Location location1 = locationResult.getLastLocation();

                                latitud = String.valueOf(location1.getLatitude());
                                longitud = String.valueOf(location1.getLongitude());
                            }
                        };

                        //Request Location Updates
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                locationCallback, Looper.myLooper());

                    }

                }
            });
        }
        else {
            //Cuando el servicio de ubicacion no esta habilitado
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}