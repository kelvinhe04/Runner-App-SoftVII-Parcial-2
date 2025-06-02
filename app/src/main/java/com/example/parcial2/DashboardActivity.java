package com.example.parcial2;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Size;
import nl.dionsegijn.konfetti.models.Shape;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

public class DashboardActivity extends AppCompatActivity {

     SharedPreferences prefs;
    Button btnRegistrar;
    Button btnFrases;
    Button btnHistorial;
    Button btnMetas;
    TextView metaInfo, metaRestante, percentText, greetingTextView;
    ProgressBar metaProgressBar;
    KonfettiView konfettiView;



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

    @Override
    protected void onResume() {
        super.onResume();
        mostrarProgreso();  // Refrescar meta al volver
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
        percentText = findViewById(R.id.percentText);
        konfettiView = findViewById(R.id.konfettiView);


        mostrarProgreso();




    }
    private void mostrarProgreso() {
        String metaTexto = prefs.getString("meta", "");
        if (metaTexto.isEmpty()) {
            metaInfo.setText("No has definido una meta aún.");
            metaRestante.setText("");
            metaProgressBar.setProgress(0);
            metaProgressBar.setVisibility(View.INVISIBLE);
            percentText.setVisibility(View.INVISIBLE);
            return;
        }

        // Extraer número de km de la meta
        float metaKm = 0f;
        try {

            metaKm = Float.parseFloat(metaTexto.replaceAll("\\D+", ""));

        } catch (Exception e) {
            metaKm = 0f;
            Toast.makeText(this, "error en metakm.", Toast.LENGTH_SHORT).show();
        }
        if (metaKm == 0) {
            metaInfo.setText("Meta inválida (0 km)");
            metaRestante.setText("Define una meta válida.");
            metaProgressBar.setProgress(0);
            metaProgressBar.setVisibility(View.INVISIBLE);
            percentText.setVisibility(View.INVISIBLE);
            return;
        }


        if (metaKm == (int) metaKm) {
            // Mostrar como entero
            metaInfo.setText("Meta del mes: " + (int) metaKm + " km");
        } else {
            // Mostrar con 1 decimal
            metaInfo.setText("Meta del mes: " + String.format("%.1f", metaKm) + " km");
        }



        // Calcular km acumulados
        float kmActuales = calcularKmAcumulados();

        // Calcular faltantes y progreso
        float faltan = metaKm - kmActuales;

        float porcentajeTexto = (kmActuales / metaKm) * 100;

        if(porcentajeTexto > 100){
            porcentajeTexto = 100;

        }else{
            porcentajeTexto = (kmActuales / metaKm) * 100;

        }
        percentText.setText(String.format("%.0f%%", porcentajeTexto));


        if (faltan <= 0) {
            metaRestante.setText("¡Felicidades! Has completado tu meta del mes.");
            metaProgressBar.setVisibility(View.VISIBLE);
            percentText.setVisibility(View.VISIBLE);
        } else {
            if (faltan == (int) faltan) {
                // Es entero exacto, muestra sin decimales
                metaRestante.setText("Te faltan " + (int) faltan + " kma para alcanzar tu meta");
                metaProgressBar.setVisibility(View.VISIBLE);
                percentText.setVisibility(View.VISIBLE);



            } else {
                // Tiene decimales, muestra con un decimal
                metaRestante.setText("Te faltan " + String.format("%.1f", faltan) + " km para alcanzar tu meta");
                metaProgressBar.setVisibility(View.VISIBLE);
                percentText.setVisibility(View.VISIBLE);
            }

        }


        int progreso = (int)((kmActuales / metaKm) * 100);
        if (progreso > 100) progreso = 100;


        // Animar la barra desde 0 hasta el progreso calculado en 1 segundo
        ObjectAnimator animation = ObjectAnimator.ofInt(metaProgressBar, "progress", 0, progreso);
        animation.setDuration(1000);  // duración en milisegundos
        animation.start();


        if (progreso >= 100) {
            konfettiView.setVisibility(View.VISIBLE);

            konfettiView.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.BLUE, Color.RED)
                    .setDirection(0, 359)                  // dirección en 360 grados
                    .setSpeed(3f, 7f)                      // velocidad con rango más variado
                    .setFadeOutEnabled(true)
                    .setTimeToLive(3000L)                  // duración un poco más larga (3 segundos)
                    .addShapes(Shape.RECT, Shape.CIRCLE)  // agregué triángulos para más variedad
                    .addSizes(new Size(8, 5f), new Size(12, 7f))          // tamaños variados
                    .setPosition(konfettiView.getWidth() / 4f, 0f)        // posición inicial variada (cuarto ancho)
                    .stream(400, 3500L);                  // cambiar burst a stream para flujo continuo por 3.5 seg

            // Ocultar el confetti después de 4 segundos para no bloquear la pantalla
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                konfettiView.setVisibility(View.GONE);
            }, 9000);
        }


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
