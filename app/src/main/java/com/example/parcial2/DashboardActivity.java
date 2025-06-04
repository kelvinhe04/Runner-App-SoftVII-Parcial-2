package com.example.parcial2;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Size;
import nl.dionsegijn.konfetti.models.Shape;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.google.android.material.card.MaterialCardView;

public class DashboardActivity extends AppCompatActivity {

    SharedPreferences prefs;
    LinearLayout btnRegistrar;
    LinearLayout btnFrases;
    LinearLayout btnHistorial;
    LinearLayout btnMetas;
    TextView metaInfo, metaRestante, percentText, greetingTextView, dateText;
    ProgressBar metaProgressBar;
    KonfettiView konfettiView;

    MaterialCardView percentRectangle;

    private boolean confettiShown = false;

    private int progreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Obtener preferencias
        prefs = getSharedPreferences("RunnerPrefs", MODE_PRIVATE);
        String nombre = prefs.getString("name", "Corredor");

        this.inicializarControles();
        this.irOtrasPantallas();
        this.ObtenerFechaActual();


        // Mostrar saludo
        greetingTextView.setText("Hola " + nombre + "!");


    }


    @Override
    protected void onResume() {
        super.onResume();
        mostrarProgreso();  // Refrescar meta al volver
        this.mostrarAnimacionProgreso();
        this.ObtenerFechaActual();


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
        konfettiView = findViewById(R.id.konfettiView);
        percentText = findViewById(R.id.percentText);
        percentRectangle = findViewById(R.id.percentRectangle);
        dateText = findViewById(R.id.dateText);


        mostrarProgreso();


    }

    private void ObtenerFechaActual() {
        // Obtener la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d 'de' MMMM", new Locale("es", "ES"));
        String fechaActual = dateFormat.format(new Date());

// Poner la primera letra en mayúscula (opcional)
        fechaActual = fechaActual.substring(0, 1).toUpperCase() + fechaActual.substring(1);

// Mostrar la fecha en el TextView
        dateText.setText(fechaActual);

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
            percentRectangle.setVisibility(View.INVISIBLE);
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

        if (porcentajeTexto > 100) {
            porcentajeTexto = 100;

        } else {
            porcentajeTexto = (kmActuales / metaKm) * 100;

        }
        percentText.setText(String.format("%.0f%%", porcentajeTexto));


        if (faltan <= 0) {
            metaRestante.setText("¡Felicidades! Has completado tu meta del mes.");
            metaProgressBar.setVisibility(View.VISIBLE);
            percentText.setVisibility(View.VISIBLE);
            percentRectangle.setVisibility(View.VISIBLE);
        } else {
            if (faltan == (int) faltan) {
                // Es entero exacto, muestra sin decimales
                metaRestante.setText("Te faltan " + (int) faltan + " km para alcanzar tu meta");
                metaProgressBar.setVisibility(View.VISIBLE);
                percentText.setVisibility(View.VISIBLE);
                percentRectangle.setVisibility(View.VISIBLE);


            } else {
                // Tiene decimales, muestra con un decimal
                metaRestante.setText("Te faltan " + String.format("%.1f", faltan) + " km para alcanzar tu meta");
                metaProgressBar.setVisibility(View.VISIBLE);
                percentText.setVisibility(View.VISIBLE);
                percentRectangle.setVisibility(View.VISIBLE);
            }

        }


        progreso = (int) ((kmActuales / metaKm) * 100);
        if (progreso > 100) progreso = 100;


        confettiShown = false;

        if (progreso >= 100 && !confettiShown) {
            confettiShown = true;
            konfettiView.setVisibility(View.VISIBLE);


            konfettiView.build()
                    .addColors(
                            Color.parseColor("#F4A7A7"),  // rosa pastel más oscuro
                            Color.parseColor("#A7F4BE"),  // verde menta intenso
                            Color.parseColor("#A7C7F4"),  // azul pastel más fuerte
                            Color.parseColor("#F4E3A7"),  // amarillo cálido pastel
                            Color.parseColor("#C8A7F4")   // lila oscuro
                    )

                    .setDirection(0, 359)                  // dirección en 360 grados
                    .setSpeed(3f, 7f)                      // velocidad con rango más variado
                    .setFadeOutEnabled(true)
                    .setTimeToLive(10000L)                  // duración un poco más larga (3 segundos)
                    .addShapes(Shape.RECT, Shape.CIRCLE)  // agregué triángulos para más variedad
                    .addSizes(new Size(8, 15f), new Size(12, 20f))
                    .setPosition(konfettiView.getWidth() / 4f, 0f)        // posición inicial variada (cuarto ancho)
                    .stream(150, 2400L);                  // cambiar burst a stream para flujo continuo por 3.5 seg

            mostrarAnimacionProgreso();


        }

        metaProgressBar.setProgress(progreso);
    }

    private void mostrarAnimacionProgreso() {
        // Animar la barra desde 0 hasta el progreso calculado en 1 segundo
        ObjectAnimator animation = ObjectAnimator.ofInt(metaProgressBar, "progress", 0, progreso);
        animation.setDuration(2000);  // duración en milisegundos
        animation.start();

        // Animación del número en el TextView
        ValueAnimator numberAnim = ValueAnimator.ofInt(0, progreso);
        numberAnim.setDuration(2000); // Debe ser la misma duración para que vayan sincronizados
        numberAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int valor = (int) animation.getAnimatedValue();
                percentText.setText(valor + "%");
            }
        });

        numberAnim.start();
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
                startActivity(new Intent(this, RegisterActivity.class)));

        btnFrases.setOnClickListener(v ->
                startActivity(new Intent(this, MotivationActivity.class)));

        btnHistorial.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        btnMetas.setOnClickListener(v ->
                startActivity(new Intent(this, GoalActivity.class)));

    }


}
