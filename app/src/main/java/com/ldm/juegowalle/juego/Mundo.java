package com.ldm.juegowalle.juego;

import java.util.Random;

public class Mundo {
    static final int MUNDO_ANCHO = 10;
    static final int MUNDO_ALTO = 13;
    static final int INCREMENTO_PUNTUACION = 10;
    static final float TICK_INICIAL = 0.5f;
    static final float TICK_DECREMENTO = 0.05f;

    public Robot robot;
    public Basura basura;
    public boolean finalJuego = false;
    public int puntuacion = 0;

    boolean[][] campos = new boolean[MUNDO_ANCHO][MUNDO_ALTO];
    Random random = new Random();
    float tiempoTick = 0;
    static float tick = TICK_INICIAL;

    public Mundo() {
        robot = new Robot();
        colocarBasura();
    }

    private void colocarBasura() {
        for (int x = 0; x < MUNDO_ANCHO; x++) {
            for (int y = 0; y < MUNDO_ALTO; y++) {
                campos[x][y] = false;
            }
        }

        int len = robot.partes.size();
        for (int i = 0; i < len; i++) {
            Cubo parte = robot.partes.get(i);
            campos[parte.x][parte.y] = true;
        }

        int basuraX = random.nextInt(MUNDO_ANCHO);
        int basuraY = random.nextInt(MUNDO_ALTO);
        while (true) {
            if (!campos[basuraX][basuraY])
                break;
            basuraX += 1;
            if (basuraX >= MUNDO_ANCHO) {
                basuraX = 0;
                basuraY += 1;
                if (basuraY >= MUNDO_ALTO) {
                    basuraY = 0;
                }
            }
        }
        basura = new Basura(basuraX, basuraY, random.nextInt(3));
    }

    public void update(float deltaTime) {
        if (finalJuego)

            return;

        tiempoTick += deltaTime;

        while (tiempoTick > tick) {
            tiempoTick -= tick;
            robot.avance();
            if (robot.comprobarChoque()) {
                finalJuego = true;
                return;
            }

            Cubo head = robot.partes.get(0);
            if (head.x == basura.x && head.y == basura.y) {
                puntuacion += INCREMENTO_PUNTUACION;
                robot.recoger();
                if (robot.partes.size() == MUNDO_ANCHO * MUNDO_ALTO) {
                    finalJuego = true;
                    return;
                } else {
                    colocarBasura();
                }

                if (puntuacion % 100 == 0 && tick - TICK_DECREMENTO > 0) {
                    tick -= TICK_DECREMENTO;
                }
            }
        }
    }
}

