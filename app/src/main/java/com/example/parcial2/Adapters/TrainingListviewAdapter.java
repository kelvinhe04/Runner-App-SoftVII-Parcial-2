package com.example.parcial2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.parcial2.Models.Training;
import com.example.parcial2.R;

import java.util.List;

public class TrainingListviewAdapter extends ArrayAdapter<Training> {

    private List<Training> trainings;

    public TrainingListviewAdapter(@NonNull Context context, @NonNull List<Training> trainings) {
        super(context, R.layout.listview_trainings, trainings);
        this.trainings = trainings;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listview_trainings,null);

        TextView lblEntrenamiento  = item.findViewById(R.id.lblEntrenamiento);
        lblEntrenamiento.setText("Entrenamiento " + (position + 1));

        TextView lblFecha = (TextView) item.findViewById(R.id.lblFecha);
        lblFecha.setText("\uD83D\uDCC5 " +trainings.get(position).getFecha());

        TextView lblDistancia = (TextView) item.findViewById(R.id.lblDistancia);
        lblDistancia.setText("\uD83D\uDCCF "+Float.toString(trainings.get(position).getDistanciaKm())+" km");

        TextView lblTiempo = (TextView) item.findViewById(R.id.lblTiempo);
        lblTiempo.setText("⏱\uFE0F "+Integer.toString(trainings.get(position).getTiempoMin())+" min");

        TextView lblTipo= (TextView) item.findViewById(R.id.lblTipo);
        lblTipo.setText("\uD83C\uDFC3 "+trainings.get(position).getTipo());

        return item;
    }




}
