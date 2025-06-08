package com.example.parcial2;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.parcial2.Adapters.OnTrainingRemovedListener;
import com.example.parcial2.Adapters.TrainingListviewAdapter;
import com.example.parcial2.Models.Training;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.view.View;


public class HistoryActivity extends AppCompatActivity implements OnTrainingRemovedListener {

    private TrainingListviewAdapter adapter;


    ListView listView;
    CardView cardViewHistorial;
    private static final String ARCHIVO = "entrenamientos.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.listView);
        cardViewHistorial = findViewById(R.id.cardViewHistorial);

        cargarHistorial();
    }

    @Override
    public void onTrainingRemoved() {
        if (adapter.isEmpty()) {
            listView.setVisibility(View.GONE);
            cardViewHistorial.setVisibility(View.VISIBLE);
        }
    }


    private void cargarHistorial() {
        List<Training> trainings= new ArrayList<>();



        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(openFileInput(ARCHIVO))
            );

            String linea;
            while ((linea = reader.readLine()) != null) {

                String[] partes = linea.split("\\|");
                if (partes.length == 4) {

                    Training training = new Training(
                            partes[0],
                            Float.parseFloat(partes[1]),
                            Integer.parseInt(partes[2]),
                            partes[3]

                    );
                    trainings.add(training);

                }
            }
            reader.close();

            cardViewHistorial.setVisibility(trainings.isEmpty() ? CardView.VISIBLE : CardView.GONE);
            listView.setVisibility(trainings.isEmpty() ? ListView.GONE : ListView.VISIBLE);

        } catch (Exception e) {
            Toast.makeText(this, "No hay entrenamientos registrados.", Toast.LENGTH_SHORT).show();
        }


        adapter = new TrainingListviewAdapter(this, trainings); // usamos "this" porque esta clase implementa OnTrainingRemovedListener
        adapter.setOnTrainingRemovedListener(this);
        listView.setAdapter(adapter);

    }
}
