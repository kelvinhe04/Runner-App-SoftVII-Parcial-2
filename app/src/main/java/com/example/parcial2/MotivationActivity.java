package com.example.parcial2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Random;

public class MotivationActivity extends AppCompatActivity {

    private Button fraseButton;
    String[] frases = {
            "¡Sigue adelante, cada paso cuenta!",
            "El dolor es temporal, la gloria es para siempre.",
            "Hoy es un buen día para correr más lejos.",
            "Cree en ti. Ya estás más cerca de tu meta.",
            "El único mal entrenamiento es el que no haces.",
            "No tienes que ir rápido, solo no te detengas.",
            "Convierte el cansancio en motivación.",
            "Corre con el corazón, no solo con las piernas.",
            "Tu único límite eres tú mismo.",
            "Cada kilómetro te hace más fuerte.",
            "No pares cuando estés cansado, para cuando hayas terminado.",
            "El esfuerzo de hoy es el orgullo de mañana.",
            "Hazlo por la persona que quieres ser.",
            "Corre como si ya fueras un campeón.",
            "Una mente fuerte supera un cuerpo cansado.",
            "Levántate. Respira. Intenta de nuevo.",
            "Entrena duro, brilla más.",
            "Nunca subestimes el poder de un buen entrenamiento.",
            "Una meta sin esfuerzo es solo un sueño.",
            "Hoy, da lo mejor de ti. Mañana, serás mejor."
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

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.RoundedAlertDialog);
        builder.setTitle("Frase Motivadora 🏃‍♂️")
                .setMessage(fraseSeleccionada)
                .setPositiveButton("Cerrar", null)
                .show();


    }
}
