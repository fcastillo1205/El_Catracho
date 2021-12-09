package com.example.elcatracho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class ActivityHistorialLavado extends AppCompatActivity {
    ListView listcot;
    List<Cotizacion> cotList;
    ArrayList<String> arrayCot;

    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_lavado);

        listcot = (ListView) findViewById(R.id.listAceite);
        cotList = new ArrayList<>();
        arrayCot = new ArrayList<String>();

        SendRequest();

        SharedPreferences datos = getSharedPreferences("datos", 0);
        user = datos.getString("user","null");
    }

    private void SendRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = restApiMethods.endPointGetCotizacion;

        //realizar peticion a la url del endpoint
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray CotizacionArray = obj.getJSONArray("cotizacion");


                    for (int i=0; i < CotizacionArray.length(); i++){
                        //Guardar 1 a 1

                        JSONObject contizacionObject = CotizacionArray.getJSONObject(i);

                        String idVehiculo = contizacionObject.getString("idVehiculo");
                        String vehiculo = contizacionObject.getString("vehiculo");
                        String tipoServicio = contizacionObject.getString("tipoServicio");
                        String tipoUbicacion = contizacionObject.getString("tipoUbicacion");
                        String fecha = contizacionObject.getString("fecha");
                        String hora = contizacionObject.getString("hora");
                        String costo = contizacionObject.getString("costo");
                        String usuario = contizacionObject.getString("usuario");
                        String longitud = contizacionObject.getString("longitud");
                        String latitud = contizacionObject.getString("latitud");

                        if(usuario.equals(user) && tipoServicio.equals("Lavado")){
                            Cotizacion cot = new Cotizacion(idVehiculo,vehiculo,tipoServicio,tipoUbicacion,fecha,hora,costo,usuario
                                    ,latitud,longitud);
                            cotList.add(cot);
                            arrayCot.add("Vehiculo: " + cot.getVehiculo() + "\n\n"
                                    +"Tipo de Servicio: "+ cot.getTipoServicio() + "\n\n"
                                    +"Tipo de Ubicacion: "+ cot.getTipoUbicacion() + "\n\n"
                                    + "Fecha y Hora: " + cot.getFecha() + " " + cot.getHora());
                        }
                    }
                    ArrayAdapter adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrayCot);
                    listcot.setAdapter(adp);

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