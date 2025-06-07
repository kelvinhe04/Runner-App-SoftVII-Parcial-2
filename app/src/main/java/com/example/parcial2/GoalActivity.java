package com.example.parcial2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GoalActivity extends AppCompatActivity {

    EditText goalInput;
    TextView currentGoalText;
    Button saveGoalButton;
    SharedPreferences prefs;

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
            String meta = prefs.getString("meta", " ");
            String goal = goalInput.getText().toString().trim();
            if (goal.isEmpty()) {
                Toast.makeText(this, "Ingresa una meta válida", Toast.LENGTH_SHORT).show();
            }else if(goal.equals(meta)){
                Toast.makeText(this, "La meta no ha cambiado", Toast.LENGTH_SHORT).show();
            }else if(goal.equals("0")){
                Toast.makeText(this, "La meta no puede ser 0", Toast.LENGTH_SHORT).show();
            } else {
                prefs.edit().putString("meta", goal).apply();
                Toast.makeText(this, "Meta guardada", Toast.LENGTH_SHORT).show();
                currentGoalText.setText("Meta actual: " + goal);
                goalInput.setText("");
                Flag.nuevaMeta = true;
                finish();


            }
        });
    }
    private void inicializarControles() {
        goalInput = findViewById(R.id.goalInput);
        saveGoalButton = findViewById(R.id.saveGoalButton);
        currentGoalText = findViewById(R.id.currentGoalText);

    }


}
