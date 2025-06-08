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

    private EditText dateInput, distanceInput, timeInput, typeInput;
    private Button saveButton;
    private static final String ARCHIVO = "entrenamientos.txt";

    private Toast toastActivo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.inicializarControles();


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

    private void mostrarToastUnico(String mensaje) {
        if (toastActivo != null) {
            toastActivo.cancel(); // Cancela el anterior si aún está visible
        }
        toastActivo = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toastActivo.show();
    }


    private void guardarEntrenamiento() {

        Flag.nuevoRegistro = true;

        String fecha = dateInput.getText().toString().trim();
        String distanciaStr = distanceInput.getText().toString().trim();
        String tiempoStr = timeInput.getText().toString().trim();
        String tipo = typeInput.getText().toString().trim();

        if (fecha.isEmpty() || distanciaStr.isEmpty() || tiempoStr.isEmpty() || tipo.isEmpty()) {
            mostrarToastUnico("Completa todos los campos");
            return;
        }


        if (!validarFecha(fecha)) {
            mostrarToastUnico("La fecha debe estar en formato dd/MM/yyyy y ser válida");
            return;
        }

        try {
            float distancia = Float.parseFloat(distanciaStr);
            int tiempo = Integer.parseInt(tiempoStr);

            // Validación: distancia y tiempo deben ser mayores a 0
            if (distancia <= 0 || tiempo <= 0) {
                mostrarToastUnico("La distancia y el tiempo deben ser mayores a 0");
                return;
            }

            // Formato del entrenamiento
            String registro = fecha + "|" + distancia + "|" + tiempo + "|" + tipo + "\n";

            // Guardar en archivo local
            FileOutputStream fos = openFileOutput(ARCHIVO, MODE_APPEND);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(registro);
            writer.close();





            Toast.makeText(this, "Entrenamiento guardado", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Distancia y tiempo deben ser números válidos", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar el entrenamiento", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
