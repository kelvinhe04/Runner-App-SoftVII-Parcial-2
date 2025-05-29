package com.example.parcial2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView greetingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Enlazar vistas
        greetingTextView = findViewById(R.id.greeting);
        Button btnRegistrar = findViewById(R.id.btnRegister);
        Button btnFrases = findViewById(R.id.btnMotivation);
        Button btnHistorial = findViewById(R.id.btnHistory);
        Button btnMetas = findViewById(R.id.btnGoals);

        // Obtener preferencias
        prefs = getSharedPreferences("RunnerPrefs", MODE_PRIVATE);
        String nombre = prefs.getString("name", "Corredor");

        // Mostrar saludo
        greetingTextView.setText("Hola, " + nombre + "!");



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
