package com.ldm.juegowalle.juego;

public class PowerUp {
    public static final int TIPO_ACELERAR = 0;
    public static final int TIPO_RALENTIZAR = 1;
    
    public int x, y;
    public int tipo;
    public boolean activo;
    public float tiempoRestante;
    
    public PowerUp(int x, int y, int tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.activo = false;
        this.tiempoRestante = 0;
    }
}
