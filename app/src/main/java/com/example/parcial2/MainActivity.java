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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    TextInputLayout nameInputLayout;
    TextInputEditText nameInput;
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
            String inputName = nameInput.getText().toString().trim();

            if (inputName.isEmpty()) {
                nameInputLayout.setError(" ");
                return;
            }

            nameInputLayout.setError(null); // Limpia el error si todo está bien
            prefs.edit().putString("name", inputName).apply();
            startDashboard();
        });


    }

    private void inicializarControles() {
        nameInput = findViewById(R.id.nameInput);
        startButton = findViewById(R.id.startButton);
        nameInputLayout = findViewById(R.id.nameInputLayout);
        nameInput = findViewById(R.id.nameInput);
    }

    private void startDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}