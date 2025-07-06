package com.example.parcial2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Random;

public class MotivationActivity extends AppCompatActivity {

    private Button fraseButton, backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation);

        fraseButton = findViewById(R.id.motivateButton);
        backButton = findViewById(R.id.backButton);

        this.regresarActivity();


        fraseButton.setOnClickListener(v -> mostrarFraseAleatoria());
    }

    private void regresarActivity() {
        backButton.setOnClickListener(v -> finish());

    }

    private void mostrarFraseAleatoria() {
        String[] frases = getResources().getStringArray(R.array.motivational_phrases);
        int index = new Random().nextInt(frases.length);
        String fraseSeleccionada = frases[index];

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.RoundedAlertDialog);
        builder.setTitle(getString(R.string.motivation_dialog_title))
                .setMessage(fraseSeleccionada)
                .setPositiveButton(getString(R.string.dialog_close_button), null)
                .show();


    }
}
