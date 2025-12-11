package com.ldm.juegowalle.juego;

import com.ldm.juegowalle.Pantalla;
import com.ldm.juegowalle.androidimpl.AndroidJuego;

public class JuegoWallE extends AndroidJuego {
    @Override
    public Pantalla getStartScreen() {
        return new LoadingScreen(this);
    }
}
