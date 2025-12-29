package com.ldm.juegowalle.juego;

public class Bomba {
    public int x, y;
    public float tiempoRestante;

    public Bomba(int x, int y) {
        this.x = x;
        this.y = y;
        this.tiempoRestante = Mundo.DURACION_BOMBA;
    }
}

