package com.example.applistas;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuscadorPersonaje extends AppCompatActivity {

    RequestQueue requestQueue;
    final String URL = "https://dragonball-api.com/api/characters/";

    //Java
    EditText edtIdPersonaje, edtNombre, edtKi, edtRaza, edtGenero;
    Button btnBuscarPersonaje, btnReiniciar, btnTransformaciones;
    ImageView imgPersonaje;
    String listaTransformaciones = "";

    private void loadUi() {
        //Vinculacion
        edtIdPersonaje = findViewById(R.id.edtIdPersonaje);
        btnBuscarPersonaje = findViewById(R.id.btnBuscarPersonaje);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        btnTransformaciones = findViewById(R.id.btnTransformaciones);
        imgPersonaje = findViewById(R.id.imgPersonaje);
        edtNombre = findViewById(R.id.edtNombre);
        edtKi = findViewById(R.id.edtKi);
        edtRaza = findViewById(R.id.edtRaza);
        edtGenero = findViewById(R.id.edtGenero);
        
        //Abrir el canal de comunicación
        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscador_personaje);

        this.loadUi();

        //Event
        btnBuscarPersonaje.setOnClickListener(view -> { getDataCharacter();});
        btnReiniciar.setOnClickListener(view -> { reiniciarBusqueda(); });
        btnTransformaciones.setOnClickListener(view -> {
            if(!listaTransformaciones.isEmpty()) {
                new AlertDialog.Builder(BuscadorPersonaje.this)
                        .setTitle("Transformaciones")
                        .setMessage(listaTransformaciones)
                        .setPositiveButton("Cerrar", null)
                        .show();
            }
        });
    } //Oncreate

    private void reiniciarBusqueda() {
        edtIdPersonaje.setText("");
        edtNombre.setText("");
        edtKi.setText("");
        edtRaza.setText("");
        edtGenero.setText("");
        imgPersonaje.setImageResource(0);
        listaTransformaciones = "";
        btnTransformaciones.setEnabled(false);
        edtIdPersonaje.requestFocus();
    }

    private void getDataCharacter(){
        //Comunicacion con la API de Dragon Ball
        if (edtIdPersonaje.getText().toString().isEmpty()){
            edtIdPersonaje.setError("Escriba un ID");
            edtIdPersonaje.requestFocus();
            return;
        }
        String endPoint = URL+ edtIdPersonaje.getText().toString(); //Se agrega el ID

        //¿Qué tipo de dato me devuelve la API?
        //Volley las solicitudes tienen 5 partes
        //Verbo, URL, JSONEnviado, Resultado (JsonObject), Error
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                endPoint,
                null,
                this::showData,
                this::errorWS
        );

        //Enviamos la solicitud
        requestQueue.add(jsonObjectRequest);
    }

    private void showData(JSONObject jsonObject){
        Log.d("ResultadosWS", jsonObject.toString());

        //Java puede gestionar Json solo en entornos seguros
        try {
            edtNombre.setText(jsonObject.getString("name"));
            edtRaza.setText(jsonObject.getString("race"));
            edtGenero.setText(jsonObject.getString("gender"));
            edtKi.setText(jsonObject.getString("ki"));
            
            String imageUrl = jsonObject.getString("image");
            Glide.with(this).load(imageUrl).into(imgPersonaje);

            if (jsonObject.has("transformations")) {
                JSONArray transformationsArray = jsonObject.getJSONArray("transformations");
                if (transformationsArray.length() > 0) {
                    StringBuilder sb = new StringBuilder();

                    for(int i=0; i < transformationsArray.length(); i++) {
                        JSONObject trans = transformationsArray.getJSONObject(i);
                        sb.append("- ").append(trans.getString("name")).append("\n");
                    }
                    listaTransformaciones = sb.toString();
                    btnTransformaciones.setEnabled(true);
                } else{
                    listaTransformaciones = "";
                    btnTransformaciones.setEnabled(false);
                }
            } else{
                listaTransformaciones = "";
                btnTransformaciones.setEnabled(false);
            }
        }catch (Exception e){
            Log.e("Error Json", e.toString());
        }
    }

    private void errorWS(VolleyError e) {
        //Log.e("ErrorWS", e.toString());

        //Para gestionar errores, necesitamos un objeto
        NetworkResponse response = e.networkResponse;

        //Si existe una respuesta  (existe error)
        if (response != null && response.data != null){
            //¿Cual es el codigo de error?
            int statuscode = response.statusCode;

            //No lo encontramos
            if (statuscode == 400){
                String dataError = new String(response.data);
                try {
                    JSONObject jsonError = new JSONObject(dataError);
                    Toast.makeText(getApplicationContext(),"Personaje no encontrado", Toast.LENGTH_SHORT).show();
                    reiniciarBusqueda();
                    Log.e("ErrorWS", dataError);
                }catch (JSONException ex){
                    Toast.makeText(getApplicationContext(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    reiniciarBusqueda();
                }

            }
        } else {
            Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
        }
    }
}