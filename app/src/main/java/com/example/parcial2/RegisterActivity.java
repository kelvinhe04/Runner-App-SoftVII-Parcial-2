package com.example.parcial2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;




public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout dateInputLayout, distanceInputLayout, timeInputLayout, typeInputLayout;

    private TextInputEditText dateInput, distanceInput, timeInput, typeInput;
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
        // Asegura el formato exacto dd/MM/yyyy con números
        if (!fecha.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            return false;
        }

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formato.setLenient(false);

        try {
            formato.parse(fecha);
            return true; // Fecha válida
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
        dateInputLayout = findViewById(R.id.dateInputLayout);
        distanceInputLayout = findViewById(R.id.distanceInputLayout);
        timeInputLayout = findViewById(R.id.timeInputLayout);
        typeInputLayout = findViewById(R.id.typeInputLayout);


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

        if (fecha.isEmpty()) {
            mostrarToastUnico("La fecha no puede estar vacía");
            dateInputLayout.setError("Campo requerido");
            return;
        }else {
            dateInputLayout.setError(null);
            dateInputLayout.setErrorEnabled(false); // Esto elimina el espacio del error
        }

        if (distanciaStr.isEmpty()) {
            mostrarToastUnico("La distancia no puede estar vacía");
            distanceInputLayout.setError("Campo requerido");
            return;
        }else {
            distanceInputLayout.setError(null);
            distanceInputLayout.setErrorEnabled(false); // Esto elimina el espacio del error
        }

        if (tiempoStr.isEmpty()) {
            mostrarToastUnico("El tiempo no puede estar vacío");
            timeInputLayout.setError("Campo requerido");
            return;
        }else {
            timeInputLayout.setError(null);
            timeInputLayout.setErrorEnabled(false); // Esto elimina el espacio del error
        }

        if (tipo.isEmpty()) {
            mostrarToastUnico("El entrenamiento no puede estar vacío");
            typeInputLayout.setError("Campo requerido");
            return;
        }else {
            typeInputLayout.setError(null);
            typeInputLayout.setErrorEnabled(false); // Esto elimina el espacio del error
        }



        if (!validarFecha(fecha)) {
            mostrarToastUnico("La fecha debe estar en formato \ndd/MM/yyyy y ser válida");
            dateInputLayout.setError("Debe de tener formato dd/MM/yyyy y ser válida");
            return;
        }else {
            dateInputLayout.setError(null);
            dateInputLayout.setErrorEnabled(false); // Esto elimina el espacio del error
        }

        try {
            float distancia = Float.parseFloat(distanciaStr);
            int tiempo = Integer.parseInt(tiempoStr);

            // Validación: distancia debe ser mayor a 0
            if (distancia <= 0) {
                mostrarToastUnico("La distancia debe ser mayor a 0");
                distanceInputLayout.setError("Debe de ser mayor a 0");
                return;
            }else {
                distanceInputLayout.setError(null);
                distanceInputLayout.setErrorEnabled(false); // Esto elimina el espacio del error
            }

            // Validación: tiempo debe ser mayor a 0
            if (tiempo <= 0) {
                mostrarToastUnico("El tiempo debe ser mayor a 0");
                timeInputLayout.setError("Debe de ser mayor a 0");
                return;
            }else {
                timeInputLayout.setError(null);
                timeInputLayout.setErrorEnabled(false); // Esto elimina el espacio del error
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
