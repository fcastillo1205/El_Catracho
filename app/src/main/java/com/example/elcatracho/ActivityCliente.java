package com.example.elcatracho;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class ActivityCliente extends AppCompatActivity {
    Boolean tomarFoto;
    String currentPhotoPath;
    Uri filepath;
    EditText txtClienteNombre;

    Bitmap bitmap;
    String encodeImage;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PETICION_ACCESO_CAN = 101;

    ImageView imgA;

    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        imgA = (ImageView) findViewById(R.id.imgViewUsuario);
        txtClienteNombre = (EditText) findViewById(R.id.txtClienteNombre);

        Button btnSeleccion = (Button) findViewById(R.id.btnClienteSeleccionar);
        Button btnGuardar = (Button) findViewById(R.id.btnClienteGuardar);

        btnSeleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarCliente();
            }
        });

        SharedPreferences datos = getSharedPreferences("datos", 0);
        user = datos.getString("user","null");
        pass = datos.getString("pass","null");
    }

    private void guardarCliente() {

        boolean pError;

        if (txtClienteNombre.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe completar el campo nombre", Toast.LENGTH_SHORT).show();
            pError = true;
        }else if ((encodeImage.isEmpty() || encodeImage == null)) {
            Toast.makeText(getApplicationContext(), "No hay Foto Seleccionada", Toast.LENGTH_SHORT).show();
            pError = true;
        }else{
            pError = false;
            StringRequest request = new StringRequest(Request.Method.POST, restApiMethods.endPointUpdateUsuario,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Usuario Editado", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), ActivityPantallaPrincipal.class));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("username", user);
                    params.put("nombre", txtClienteNombre.getText().toString());
                    params.put("imagen", encodeImage);

                    return params;
                }
            };

            if(!pError){
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(request);
            }
        }
    }

    private void tomarFoto() {
        final CharSequence[] options = { "Tomar Foto", "Seleccionar de galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar una Imagen!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Tomar Foto")) {
                    permisos();
                    tomarFoto = true;
                } else if (options[item].equals("Seleccionar de galeria")) {
                    tomarFoto = false;
                    //OBTENER IMAGEN DE GALERIA
                    Dexter.withContext(ActivityCliente.this)
                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent, "Buscar Imagen"), 1);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                    //FIN OBTENER IMAGEN DE GALERIA
                } else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                    //edicionCampos(false);
                }
            }
        });
        builder.show();
    }

    private void permisos() {
        //VALIDAR PERMISO QUE ESTA OTORGADO
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //otorga el permiso si no lo tengo
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PETICION_ACCESO_CAN);
        }else{
            TakePhotoDir();
        }
    }

    private void TakePhotoDir() {
        Intent Intenttakephoto= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Intenttakephoto.resolveActivity(getPackageManager()) != null){
            File foto = null;
            try {
                foto = createImageFile();
            }
            catch (Exception ex){
                ex.toString();
            }
            if (foto!= null){
                filepath = FileProvider.getUriForFile(getApplicationContext(), "com.example.elcatracho.fileprovider",foto);
                Intenttakephoto.putExtra(MediaStore.EXTRA_OUTPUT, filepath);
                startActivityForResult(Intenttakephoto, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //CODIGO DE IMAGEN
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(tomarFoto){
            if (requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK) {
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                    encodeBitmapImage(bitmap);
                    imgA.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            if (requestCode == 1 && resultCode == RESULT_OK) {
                filepath = data.getData();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(filepath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imgA.setImageBitmap(bitmap);
                    encodeBitmapImage(bitmap);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImage = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */);
        // Save a file: path for use with ACTION_VIEW
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}