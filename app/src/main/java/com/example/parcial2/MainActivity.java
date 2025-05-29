package com.example.parcial2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText nameInput;
    Button startButton;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.inicializarControles();

        prefs = getSharedPreferences("RunnerPrefs", MODE_PRIVATE);
        String name = prefs.getString("name", "");

        if (!name.isEmpty()) {
            startDashboard();
        }


        startButton.setOnClickListener(v -> {
            String inputName = nameInput.getText().toString();
            prefs.edit().putString("name", inputName).apply();
            startDashboard();
        });


    }

    private void inicializarControles() {
        nameInput = findViewById(R.id.nameInput);
        startButton = findViewById(R.id.startButton);
    }

    private void startDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}