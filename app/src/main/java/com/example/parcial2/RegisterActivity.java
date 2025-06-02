package com.example.parcial2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;




public class RegisterActivity extends AppCompatActivity {

    EditText dateInput, distanceInput, timeInput, typeInput;
    Button saveButton;
    SharedPreferences prefs;
    private static final String ARCHIVO = "entrenamientos.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.inicializarControles();

        prefs = getSharedPreferences("RunnerPrefs", MODE_PRIVATE);

        saveButton.setOnClickListener(v -> guardarEntrenamiento());
    }

    private boolean validarFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formato.setLenient(false); // Muy importante para validación estricta

        try {
            formato.parse(fecha);
            return true;  // Fecha válida
        } catch (ParseException e) {
            return false; // Fecha inválida
        }
    }

    private void inicializarControles() {
        // Referencias a vistas
        dateInput = findViewById(R.id.dateInput);
        distanceInput = findViewById(R.id.distanceInput);
        timeInput = findViewById(R.id.timeInput);
        typeInput = findViewById(R.id.typeInput);
        saveButton = findViewById(R.id.saveButton);


    }

    private void guardarEntrenamiento() {
        String fecha = dateInput.getText().toString().trim();
        String distanciaStr = distanceInput.getText().toString().trim();
        String tiempoStr = timeInput.getText().toString().trim();
        String tipo = typeInput.getText().toString().trim();

        if (fecha.isEmpty() || distanciaStr.isEmpty() || tiempoStr.isEmpty() || tipo.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validarFecha(fecha)) {
            Toast.makeText(this, "La fecha debe estar en formato dd/MM/yyyy y ser válida", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float distancia = Float.parseFloat(distanciaStr);
            int tiempo = Integer.parseInt(tiempoStr);

            // Formato del entrenamiento
            String registro = fecha + "|" + distancia + "|" + tiempo + "|" + tipo + "\n";

            // Guardar en archivo local
            FileOutputStream fos = openFileOutput(ARCHIVO, MODE_APPEND);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(registro);
            writer.close();





            Toast.makeText(this, "Entrenamiento guardado correctamente", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Distancia y tiempo deben ser números válidos", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar el entrenamiento", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
