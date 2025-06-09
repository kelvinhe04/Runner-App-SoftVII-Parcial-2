package com.example.parcial2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GoalActivity extends AppCompatActivity {

    private EditText goalInput;
    private TextView currentGoalText;
    private Button saveGoalButton;
    private SharedPreferences prefs;

    private Toast toastActivo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        this.inicializarControles();


        prefs = getSharedPreferences("RunnerPrefs", MODE_PRIVATE);

        // Mostrar meta actual si existe
        String currentGoal = prefs.getString("meta", "");
        if (!currentGoal.isEmpty()) {
            currentGoalText.setText("Meta actual: " + currentGoal);
        } else {
            currentGoalText.setText("No has establecido una meta aún.");
        }

        // Guardar nueva meta
        saveGoalButton.setOnClickListener(v -> {
            String metaActual = prefs.getString("meta", " ");
            String goal = goalInput.getText().toString().trim();

            if (goal.isEmpty()) {
                mostrarToastUnico("Ingresa una meta válida");
            } else if (!goal.matches(".*\\d.*")) {
                mostrarToastUnico("La meta debe contener al menos un número");
            } else if (goal.equals(metaActual)) {
                mostrarToastUnico("La meta no ha cambiado");
            } else if (goal.equals("0")) {
                mostrarToastUnico("La meta no puede ser 0");
            } else {
                prefs.edit().putString("meta", goal).apply();
                mostrarToastUnico("Meta guardada");
                currentGoalText.setText("Meta actual: " + goal);
                goalInput.setText("");
                Flag.nuevaMeta = true;
                finish();
            }
        });


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

    private void inicializarControles() {
        goalInput = findViewById(R.id.goalInput);
        saveGoalButton = findViewById(R.id.saveGoalButton);
        currentGoalText = findViewById(R.id.currentGoalText);

    }

    private void mostrarToastUnico(String mensaje) {
        if (toastActivo != null) {
            toastActivo.cancel(); // Cancela el anterior si aún está visible
        }
        toastActivo = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toastActivo.show();
    }



}
