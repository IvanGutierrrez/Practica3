package com.ldm.juegowalle.juego;

import java.util.Random;

public class Mundo {
    static final int MUNDO_ANCHO = 10;
    static final int MUNDO_ALTO = 13;
    static final int INCREMENTO_PUNTUACION = 10;
    static final float TICK_INICIAL = 0.5f;
    static final float TICK_DECREMENTO = 0.05f;
    static final float POWERUP_DURACION = 5.0f;
    static final float POWERUP_PUNTOS_DURACION = 20.0f;
    static final float POWERUP_MIN_TIME = 30.0f;
    static final float POWERUP_MAX_TIME = 60.0f;
    static final float DURACION_BOMBA = 10.0f;
    static final float INTERVALO_BOMBA = 30.0f;

    public Robot robot;
    public Basura basura;
    public PowerUp powerUp;
    public Bomba bomba;

    public boolean finalJuego = false;
    public int puntuacion = 0;

    boolean[][] campos = new boolean[MUNDO_ANCHO][MUNDO_ALTO];
    Random random = new Random();
    float tiempoTick = 0;
    static float tick = TICK_INICIAL;
    float tickOriginal = TICK_INICIAL;
    float tiempoPowerUp = 0;
    float tiempoProximoPowerUp;
    boolean puntosDoblesActivos = false;
    float tiempoBomba = 0;


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
        powerUp = new PowerUp(powerUpX, powerUpY, random.nextInt(3));
    }

    private void colocarBomba() {
        for (int x = 0; x < MUNDO_ANCHO; x++)
            for (int y = 0; y < MUNDO_ALTO; y++)
                campos[x][y] = false;

        for (Cubo parte : robot.partes)
            campos[parte.x][parte.y] = true;

        if (basura != null)
            campos[basura.x][basura.y] = true;

        int bombaX, bombaY;
        Cubo head = robot.partes.get(0);

        while (true) {
            bombaX = random.nextInt(MUNDO_ANCHO);
            bombaY = random.nextInt(MUNDO_ALTO);

                if (!campos[bombaX][bombaY] &&
                    Math.abs(bombaX - head.x) > 1 &&
                    Math.abs(bombaY - head.y) > 1)
                break;
        }

        bomba = new Bomba(bombaX, bombaY);
    }


    public void update(float deltaTime) {
        if (finalJuego)
            return;

        tiempoPowerUp += deltaTime;
        
        if (powerUp == null && tiempoPowerUp >= tiempoProximoPowerUp) {
            colocarPowerUp();
            tiempoPowerUp = 0;
            tiempoProximoPowerUp = POWERUP_MIN_TIME + random.nextFloat() * (POWERUP_MAX_TIME - POWERUP_MIN_TIME);
        }

        if (powerUp != null && powerUp.activo) {
            powerUp.tiempoRestante -= deltaTime;

            if (powerUp.tiempoRestante <= 0) {

                if (powerUp.tipo == PowerUp.TIPO_ACELERAR ||
                        powerUp.tipo == PowerUp.TIPO_RALENTIZAR) {
                    tick = tickOriginal;
                }

                if (powerUp.tipo == PowerUp.TIPO_PUNTOS_DOBLES) {
                    puntosDoblesActivos = false;
                }

                powerUp = null;
            }
        }
        
        tiempoBomba += deltaTime;

        if (bomba == null && puntuacion < 300 && tiempoBomba >= INTERVALO_BOMBA) {
            colocarBomba();
            tiempoBomba = 0;
        }

        if (bomba != null) {
            bomba.tiempoRestante -= deltaTime;
            if (bomba.tiempoRestante <= 0)
                bomba = null;
        }

        if (bomba != null) {
            Cubo head = robot.partes.get(0);
            if (head.x == bomba.x && head.y == bomba.y)
                finalJuego = true;
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
            
            if (head.x == basura.x && head.y == basura.y) {
                if (puntosDoblesActivos)
                    puntuacion += INCREMENTO_PUNTUACION * 2;
                else
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
            
            if (powerUp != null && !powerUp.activo && head.x == powerUp.x && head.y == powerUp.y) {
                powerUp.activo = true;
                powerUp.tiempoRestante = POWERUP_DURACION;
                tickOriginal = tick;
                
                if (powerUp.tipo == PowerUp.TIPO_ACELERAR) {
                    tick = tick * 0.5f;
                } else if (powerUp.tipo == PowerUp.TIPO_RALENTIZAR) {
                    tick = tick * 2.0f;
                } else if (powerUp.tipo == PowerUp.TIPO_PUNTOS_DOBLES) {
                    powerUp.tiempoRestante = POWERUP_PUNTOS_DURACION;
                    puntosDoblesActivos = true;
                }
            }
        }
    }
}

