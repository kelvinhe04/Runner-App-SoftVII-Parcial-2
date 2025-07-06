package com.example.parcial2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private Button saveButton, backButton;
    private static final String ARCHIVO = "entrenamientos.txt";

    private Toast toastActivo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.inicializarControles();
        this.regresarActivity();


        saveButton.setOnClickListener(v -> guardarEntrenamiento());
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
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
        backButton = findViewById(R.id.backButton);




    }

    private void regresarActivity() {
        backButton.setOnClickListener(v -> finish());
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
            mostrarToastUnico(getString(R.string.error_fecha_vacia));
            dateInputLayout.setError(getString(R.string.campo_requerido));;
            return;
        }else {
            dateInputLayout.setError(null);
            dateInputLayout.setErrorEnabled(false); // Esto elimina el espacio del error
        }

        if (distanciaStr.isEmpty()) {
            mostrarToastUnico(getString(R.string.error_distancia_vacia));
            distanceInputLayout.setError(getString(R.string.campo_requerido));
            return;
        } else {
            distanceInputLayout.setError(null);
            distanceInputLayout.setErrorEnabled(false);
        }

        if (tiempoStr.isEmpty()) {
            mostrarToastUnico(getString(R.string.error_tiempo_vacio));
            timeInputLayout.setError(getString(R.string.campo_requerido));
            return;
        } else {
            timeInputLayout.setError(null);
            timeInputLayout.setErrorEnabled(false);
        }

        if (tipo.isEmpty()) {
            mostrarToastUnico(getString(R.string.error_tipo_vacio));
            typeInputLayout.setError(getString(R.string.campo_requerido));
            return;
        } else {
            typeInputLayout.setError(null);
            typeInputLayout.setErrorEnabled(false);
        }

        if (!validarFecha(fecha)) {
            mostrarToastUnico(getString(R.string.error_fecha_invalida));
            dateInputLayout.setError(getString(R.string.error_fecha_invalida_campo));
            return;
        } else {
            dateInputLayout.setError(null);
            dateInputLayout.setErrorEnabled(false);
        }

        try {
            float distancia = Float.parseFloat(distanciaStr);
            int tiempo = Integer.parseInt(tiempoStr);

            if (distancia <= 0) {
                mostrarToastUnico(getString(R.string.error_distancia_menor_cero));
                distanceInputLayout.setError(getString(R.string.error_distancia_menor_cero));
                return;
            } else {
                distanceInputLayout.setError(null);
                distanceInputLayout.setErrorEnabled(false);
            }

            if (tiempo <= 0) {
                mostrarToastUnico(getString(R.string.error_tiempo_menor_cero));
                timeInputLayout.setError(getString(R.string.error_tiempo_menor_cero));
                return;
            } else {
                timeInputLayout.setError(null);
                timeInputLayout.setErrorEnabled(false);
            }


            // Formato del entrenamiento
            String registro = fecha + "|" + distancia + "|" + tiempo + "|" + tipo + "\n";

            // Guardar en archivo local
            FileOutputStream fos = openFileOutput(ARCHIVO, MODE_APPEND);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(registro);
            writer.close();





            Toast.makeText(this, getString(R.string.entrenamiento_guardado), Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.error_valores_invalidos), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_guardar_entrenamiento), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
