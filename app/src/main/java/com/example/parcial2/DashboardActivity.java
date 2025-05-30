package com.example.parcial2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class DashboardActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView greetingTextView;
    Button btnRegistrar;
    Button btnFrases;
    Button btnHistorial;
    Button btnMetas;
    TextView metaInfo, metaRestante;
    ProgressBar metaProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Obtener preferencias
        prefs = getSharedPreferences("RunnerPrefs", MODE_PRIVATE);
        String nombre = prefs.getString("name", "Corredor");

        this.inicializarControles();
        this.irOtrasPantallas();


        // Mostrar saludo
        greetingTextView.setText("Hola, " + nombre + "!");





    }
    private void inicializarControles() {
        // Enlazar vistas
        greetingTextView = findViewById(R.id.greeting);
        btnRegistrar = findViewById(R.id.btnRegister);
        btnFrases = findViewById(R.id.btnMotivation);
        btnHistorial = findViewById(R.id.btnHistory);
        btnMetas = findViewById(R.id.btnGoals);
        metaInfo = findViewById(R.id.metaInfo);
        metaRestante = findViewById(R.id.metaRestante);
        metaProgressBar = findViewById(R.id.metaProgressBar);


        mostrarProgreso();




    }
    private void mostrarProgreso() {
        String metaTexto = prefs.getString("meta", "");
        if (metaTexto.isEmpty()) {
            metaInfo.setText("No has definido una meta aún.");
            metaRestante.setText("");
            metaProgressBar.setProgress(0);
            return;
        }

        // Extraer número de km de la meta
        int metaKm = 0;
        try {
            metaKm = Integer.parseInt(metaTexto);
        } catch (Exception e) {
            metaKm = 0;
            Toast.makeText(this, "error en metakm.", Toast.LENGTH_SHORT).show();
        }
        if (metaKm == 0) {
            metaInfo.setText("Meta inválida (0 km)");
            metaRestante.setText("Define una meta válida.");
            metaProgressBar.setProgress(0);
            return;
        }


        metaInfo.setText("Meta del mes: " + metaKm + " km");

        // Calcular km acumulados
        float kmActuales = calcularKmAcumulados();

        // Calcular faltantes y progreso
        float faltan = metaKm - kmActuales;
        if (faltan <= 0) {
            metaRestante.setText("¡Felicidades! Has completado tu meta del mes.");
        } else {
            metaRestante.setText("Te faltan " + String.format("%.1f", faltan) + " km para alcanzar tu meta");
        }


        int progreso = (int)((kmActuales / metaKm) * 100);
        if (progreso > 100) progreso = 100;

        metaProgressBar.setProgress(progreso);
    }

    private float calcularKmAcumulados() {
        float total = 0f;
        try {
            FileInputStream fis = openFileInput("entrenamientos.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String linea;

            while ((linea = reader.readLine()) != null) {
                // Ej: "29/05/2025|5.0 km|30 min|trote"
                String[] partes = linea.split("\\|");
                if (partes.length >= 2) {
                    String distanciaStr = partes[1].replace(" km", "").trim();
                    total += Float.parseFloat(distanciaStr);
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }



    private void irOtrasPantallas() {
        // Ir a otras pantallas
        btnRegistrar.setOnClickListener(v ->
                startActivity(new Intent( this, RegisterActivity.class)));

        btnFrases.setOnClickListener(v ->
                startActivity(new Intent(this, MotivationActivity.class)));

        btnHistorial.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        btnMetas.setOnClickListener(v ->
                startActivity(new Intent(this, GoalActivity.class)));

    }




}
