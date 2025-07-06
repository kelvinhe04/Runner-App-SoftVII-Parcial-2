package com.example.parcial2;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;



import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Size;
import nl.dionsegijn.konfetti.models.Shape;

import android.graphics.Color;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DashboardActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private LinearLayout btnRegistrar;
    private LinearLayout btnFrases;
    private LinearLayout btnHistorial;
    private LinearLayout btnMetas;
    private LinearLayout btnLogOut;

    private TextView metaInfo, metaRestante, percentText, greetingTextView, dateText;
    private ProgressBar metaProgressBar;
    private KonfettiView konfettiView;

    private MaterialCardView percentRectangle;

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

        this.LogOut();



    }


    @Override
    protected void onResume() {

        super.onResume();
        if(Flag.sinRegistros){
            mostrarProgreso();  // Refrescar meta al volver
            mostrarAnimacionProgreso();
            Flag.sinRegistros = false;

        }



        if (Flag.nuevoRegistro) {

            confettiShown = false;


            mostrarProgreso();  // Refrescar meta al volver
            mostrarAnimacionProgreso();


            Flag.nuevoRegistro = false; // resetear la bandera


        }



        if (Flag.nuevaMeta) {
            confettiShown = false;

            mostrarProgreso();  // Refrescar meta al volver
            mostrarAnimacionProgreso();


            Flag.nuevaMeta = false; // resetear la bandera



        }







        this.ObtenerFechaActual();




    }

    private void LogOut() {
        btnLogOut.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(DashboardActivity.this, R.style.RoundedAlertDialog);
            builder.setTitle("¿Deseas cerrar sesión?")
                    .setMessage("Esto borrará el usuario actual y todos los registros guardados. Tendrás que iniciar sesión nuevamente con otro usuario.")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        borrarDatosDelUsuario();
                        Toast.makeText(DashboardActivity.this, "Usuario y registros eliminados", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
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
        btnLogOut = findViewById(R.id.btnLogOut);




        mostrarProgreso();


    }

    private void borrarDatosDelUsuario() {
        SharedPreferences prefs = getSharedPreferences("RunnerPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply(); // Borra todos los datos del usuario

        // Si también guardas entrenamientos u otros datos en archivos:
        deleteFile("entrenamientos.txt"); // Si usas este archivo, bórralo también
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
            metaRestante.setVisibility(View.GONE);
            metaProgressBar.setProgress(0);
            metaProgressBar.setVisibility(View.GONE);
            percentText.setVisibility(View.GONE);
            percentRectangle.setVisibility(View.GONE);
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
            metaRestante.setText("¡Felicidades! \n\nHas completado tu meta del mes.");
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




        if (progreso >= 100 && !confettiShown) {
            List<Integer> konfettiColors;
            confettiShown = true;

            konfettiView.setVisibility(View.VISIBLE);


            int nightModeFlags;
            nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;


            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                // 🎯 Modo oscuro: colores más tenues y contrastantes
                konfettiColors = Arrays.asList(
                        Color.parseColor("#EACA97"),
                        Color.parseColor("#ADD5B0"),
                        Color.parseColor("#9265F5"),
                        Color.parseColor("#C87D84"),
                        Color.parseColor("#7FC8E6")
                );
            } else {
                // ☀️ Modo claro: colores más vivos
                konfettiColors = Arrays.asList(
                        Color.parseColor("#F4A7A7"),
                        Color.parseColor("#A7F4BE"),
                        Color.parseColor("#A7C7F4"),
                        Color.parseColor("#F4E3A7"),
                        Color.parseColor("#C8A7F4")
                );
            }

            konfettiView.post(() -> {
                konfettiView.build()
                        .addColors(
                                konfettiColors.get(0),
                                konfettiColors.get(1),
                                konfettiColors.get(2),
                                konfettiColors.get(3),
                                konfettiColors.get(4)
                        )

                        .setDirection(0, 360)
                        .setSpeed(3f, 7f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(10000L)
                        .addShapes(Shape.RECT, Shape.CIRCLE)
                        .addSizes(new Size(8, 15f), new Size(12, 20f))
                        .setPosition(konfettiView.getWidth() / 2f, 0f)
                        .stream(150, 2400L);

                mostrarAnimacionProgreso();
            });





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
