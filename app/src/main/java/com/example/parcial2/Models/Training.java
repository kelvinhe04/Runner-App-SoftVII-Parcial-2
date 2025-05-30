package com.example.parcial2.Models;

public class Training {

        private String fecha;
        private float distanciaKm;
        private int tiempoMin;
        private String tipo;


        public Training(String fecha, float distanciaKm, int tiempoMin, String tipo) {
            this.fecha = fecha;
            this.distanciaKm = distanciaKm;
            this.tiempoMin = tiempoMin;
            this.tipo = tipo;
        }


        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public float getDistanciaKm() {
            return distanciaKm;
        }

        public void setDistanciaKm(float distanciaKm) {
            this.distanciaKm = distanciaKm;
        }

        public int getTiempoMin() {
            return tiempoMin;
        }

        public void setTiempoMin(int tiempoMin) {
            this.tiempoMin = tiempoMin;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }





}
