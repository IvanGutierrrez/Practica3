package com.ldm.juegowalle.juego;

import java.util.Random;

public class Mundo {
    static final int MUNDO_ANCHO = 10;
    static final int MUNDO_ALTO = 13;
    static final int INCREMENTO_PUNTUACION = 10;
    static final float TICK_INICIAL = 0.5f;
    static final float TICK_DECREMENTO = 0.05f;
    static final float POWERUP_DURACION = 5.0f;
    static final float POWERUP_MIN_TIME = 30.0f;
    static final float POWERUP_MAX_TIME = 60.0f;

    public Robot robot;
    public Basura basura;
    public PowerUp powerUp;
    public boolean finalJuego = false;
    public int puntuacion = 0;

    boolean[][] campos = new boolean[MUNDO_ANCHO][MUNDO_ALTO];
    Random random = new Random();
    float tiempoTick = 0;
    static float tick = TICK_INICIAL;
    float tickOriginal = TICK_INICIAL;
    float tiempoPowerUp = 0;
    float tiempoProximoPowerUp;

    public Mundo() {
        robot = new Robot();
        colocarBasura();
        tiempoProximoPowerUp = POWERUP_MIN_TIME + random.nextFloat() * (POWERUP_MAX_TIME - POWERUP_MIN_TIME);
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

    private void colocarPowerUp() {
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

        // También marcar la basura
        if (basura != null) {
            campos[basura.x][basura.y] = true;
        }

        int powerUpX = random.nextInt(MUNDO_ANCHO);
        int powerUpY = random.nextInt(MUNDO_ALTO);
        while (true) {
            if (!campos[powerUpX][powerUpY])
                break;
            powerUpX += 1;
            if (powerUpX >= MUNDO_ANCHO) {
                powerUpX = 0;
                powerUpY += 1;
                if (powerUpY >= MUNDO_ALTO) {
                    powerUpY = 0;
                }
            }
        }
        powerUp = new PowerUp(powerUpX, powerUpY, random.nextInt(2));
    }

    public void update(float deltaTime) {
        if (finalJuego)
            return;

        // Gestión del temporizador de power-up
        tiempoPowerUp += deltaTime;
        
        // Generar power-up si es el momento
        if (powerUp == null && tiempoPowerUp >= tiempoProximoPowerUp) {
            colocarPowerUp();
            tiempoPowerUp = 0;
            tiempoProximoPowerUp = POWERUP_MIN_TIME + random.nextFloat() * (POWERUP_MAX_TIME - POWERUP_MIN_TIME);
        }

        // Gestión de efectos activos de power-up
        if (powerUp != null && powerUp.activo) {
            powerUp.tiempoRestante -= deltaTime;
            if (powerUp.tiempoRestante <= 0) {
                // Restaurar velocidad normal
                tick = tickOriginal;
                powerUp.activo = false;
                powerUp = null;
            }
        }

        tiempoTick += deltaTime;

        while (tiempoTick > tick) {
            tiempoTick -= tick;
            robot.avance();
            if (robot.comprobarChoque()) {
                finalJuego = true;
                return;
            }

            Cubo head = robot.partes.get(0);
            
            // Comprobar colisión con basura
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
                    tickOriginal = tick;
                }
            }
            
            // Comprobar colisión con power-up
            if (powerUp != null && !powerUp.activo && head.x == powerUp.x && head.y == powerUp.y) {
                powerUp.activo = true;
                powerUp.tiempoRestante = POWERUP_DURACION;
                tickOriginal = tick;
                
                if (powerUp.tipo == PowerUp.TIPO_ACELERAR) {
                    tick = tick * 0.5f; // Acelera (tick más pequeño = más rápido)
                } else if (powerUp.tipo == PowerUp.TIPO_RALENTIZAR) {
                    tick = tick * 2.0f; // Ralentiza (tick más grande = más lento)
                }
            }
        }
    }
}

