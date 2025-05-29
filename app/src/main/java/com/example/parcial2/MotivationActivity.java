package com.example.parcial2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MotivationActivity extends AppCompatActivity {

    Button fraseButton;
    TextView fraseTexto;

    String[] frases = {
            "¡Sigue adelante, cada paso cuenta!",
            "El dolor es temporal, la gloria es para siempre.",
            "Hoy es un buen día para correr más lejos.",
            "Cree en ti. Ya estás más cerca de tu meta.",
            "El único mal entrenamiento es el que no haces.",
            "No tienes que ir rápido, solo no te detengas.",
            "Convierte el cansancio en motivación.",
            "Corre con el corazón, no solo con las piernas."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation);

        fraseButton = findViewById(R.id.motivateButton);


        fraseButton.setOnClickListener(v -> mostrarFraseAleatoria());
    }

    private void mostrarFraseAleatoria() {
        int index = new Random().nextInt(frases.length);
        String fraseSeleccionada = frases[index];

        new AlertDialog.Builder(this)
                .setTitle("Frase Motivadora 🏃‍♂️")
                .setMessage(fraseSeleccionada)
                .setPositiveButton("Cerrar", null)
                .show();


    }
}
