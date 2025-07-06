package com.example.parcial2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.parcial2.Flag;
import com.example.parcial2.Models.Training;
import com.example.parcial2.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Locale;

public class TrainingListviewAdapter extends ArrayAdapter<Training> {

    private OnTrainingRemovedListener listener;  // <-- atributo listener


    private final Context context;
    private final List<Training> trainings;
    private final String ARCHIVO = "entrenamientos.txt";

    public void setOnTrainingRemovedListener(OnTrainingRemovedListener listener) {
        this.listener = listener;  // <-- método setter para el listener
    }

    public TrainingListviewAdapter(@NonNull Context context, @NonNull List<Training> trainings) {
        super(context, R.layout.listview_trainings, trainings);
        this.context = context;
        this.trainings = trainings;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listview_trainings, null);

        Training training = trainings.get(position);

        TextView lblEntrenamiento = item.findViewById(R.id.lblEntrenamiento);
        lblEntrenamiento.setText(training.getTipo());

        TextView lblFecha = item.findViewById(R.id.lblFecha);
        lblFecha.setText(context.getString(R.string.training_date) + " " + training.getFecha());


        TextView lblDistancia = item.findViewById(R.id.lblDistancia);
        float distancia = training.getDistanciaKm();
        if (distancia == (int) distancia) {
            lblDistancia.setText(context.getString(R.string.training_distance) + " " + (int) distancia + " " + context.getString(R.string.training_unit_km));
        } else {
            lblDistancia.setText(context.getString(R.string.training_distance) + " " + distancia + " " + context.getString(R.string.training_unit_km));
        }

        TextView lblTiempo = item.findViewById(R.id.lblTiempo);
        int tiempo = training.getTiempoMin();
        lblTiempo.setText(context.getString(R.string.training_time) + " " + tiempo + " " + context.getString(R.string.training_unit_min));


        TextView lblTipo = item.findViewById(R.id.lblTipo);
        lblTipo.setText(context.getString(R.string.training_type) + " " + training.getTipo());

        TextView lblRitmo = item.findViewById(R.id.lblRitmo);
        if (distancia > 0) {
            float ritmo = tiempo / distancia;
            if (ritmo == (int) ritmo) {
                lblRitmo.setText(context.getString(R.string.training_pace) + " " + (int) ritmo + " " + context.getString(R.string.training_unit_pace));
            } else {
                String ritmoTexto = context.getString(R.string.training_pace) + " " + String.format(Locale.getDefault(), "%.2f", ritmo) + " " + context.getString(R.string.training_unit_pace);
                lblRitmo.setText(ritmoTexto);
            }
        }

        // 🔴 BOTÓN DE ELIMINAR
        ImageButton btnEliminar = item.findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarDelArchivo(training);
                trainings.remove(position);
                Toast.makeText(context, context.getString(R.string.toast_training_deleted), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();

                if (listener != null) {
                    listener.onTrainingRemoved();  // notifica a la Activity


                }

                Flag.sinRegistros = true;
            }
        });

        return item;
    }

    // Método para eliminar la línea del archivo
    private void eliminarDelArchivo(Training training) {
        try {
            File file = new File(context.getFilesDir(), ARCHIVO);
            File tempFile = new File(context.getFilesDir(), "temp_" + ARCHIVO);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String linea;
            String target = training.getFecha() + "|" + training.getDistanciaKm() + "|" + training.getTiempoMin() + "|" + training.getTipo();

            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().equals(target)) {
                    writer.write(linea + "\n");
                }
            }


            writer.close();
            reader.close();

            file.delete();
            tempFile.renameTo(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
