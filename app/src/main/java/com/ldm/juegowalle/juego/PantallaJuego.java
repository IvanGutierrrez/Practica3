package com.ldm.juegowalle.juego;

import java.util.List;
import android.graphics.Color;
import com.ldm.juegowalle.Juego;
import com.ldm.juegowalle.Graficos;
import com.ldm.juegowalle.Input.TouchEvent;
import com.ldm.juegowalle.Pixmap;
import com.ldm.juegowalle.Pantalla;

public class PantallaJuego extends Pantalla {
    enum EstadoJuego {
        Preparado,
        Ejecutandose,
        Pausado,
        FinJuego
    }

    EstadoJuego estado = EstadoJuego.Preparado;
    Mundo mundo;
    int antiguaPuntuacion = 0;
    String puntuacion = "0";

    public PantallaJuego(Juego juego) {
        super(juego);
        mundo = new Mundo();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = juego.getInput().getTouchEvents();
        juego.getInput().getKeyEvents();

        if(estado == EstadoJuego.Preparado)
            updateReady(touchEvents);
        if(estado == EstadoJuego.Ejecutandose)
            updateRunning(touchEvents, deltaTime);
        if(estado == EstadoJuego.Pausado)
            updatePaused(touchEvents);
        if(estado == EstadoJuego.FinJuego)
            updateGameOver(touchEvents);

    }

    private void updateReady(List<TouchEvent> touchEvents) {
        if (!touchEvents.isEmpty())
            estado = EstadoJuego.Ejecutandose;
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x < 64 && event.y < 64) {
                    if(Configuraciones.sonidoHabilitado)
                        Assets.pulsar.play(1);
                    estado = EstadoJuego.Pausado;
                    return;
                }
            }
            if(event.type == TouchEvent.TOUCH_DOWN) {
                if(event.x < 64 && event.y > 416) {
                    mundo.robot.girarIzquierda();
                }
                if(event.x > 256 && event.y > 416) {
                    mundo.robot.girarDerecha();
                }
            }
        }

        mundo.update(deltaTime);
        if(mundo.finalJuego) {
            if(Configuraciones.sonidoHabilitado)
                Assets.derrota.play(1);
            estado = EstadoJuego.FinJuego;
        }
        if(antiguaPuntuacion != mundo.puntuacion) {
            antiguaPuntuacion = mundo.puntuacion;
            puntuacion = "" + antiguaPuntuacion;
            if(Configuraciones.sonidoHabilitado)
                Assets.ataque.play(1);
        }
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 80 && event.x <= 240) {
                    if(event.y > 100 && event.y <= 148) {
                        if(Configuraciones.sonidoHabilitado)
                            Assets.pulsar.play(1);
                        estado = EstadoJuego.Ejecutandose;
                        return;
                    }
                    if(event.y > 148 && event.y < 196) {
                        if(Configuraciones.sonidoHabilitado)
                            Assets.pulsar.play(1);
                        juego.setScreen(new MainMenuScreen(juego));
                        return;
                    }
                }
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x >= 128 && event.x <= 192 &&
                        event.y >= 200 && event.y <= 264) {
                    if(Configuraciones.sonidoHabilitado)
                        Assets.pulsar.play(1);
                    juego.setScreen(new MainMenuScreen(juego));
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graficos g = juego.getGraphics();

        if (g != null) { // Verifica que g no sea null
            g.drawPixmap(Assets.fondo, 0, 0);
            drawWorld(mundo);
            if (estado == EstadoJuego.Preparado)
                drawReadyUI();
            if (estado == EstadoJuego.Ejecutandose)
                drawRunningUI();
            if (estado == EstadoJuego.Pausado)
                drawPausedUI();
            if (estado == EstadoJuego.FinJuego)
                drawGameOverUI();

            drawText(g, puntuacion, g.getWidth() / 2 - puntuacion.length() * 20 / 2, g.getHeight() - 42);
        }
    }
    private void drawWorld(Mundo mundo) {
        Graficos g = juego.getGraphics();
        Robot robot = mundo.robot;
        Cubo head = robot.partes.get(0);
        Basura basura = mundo.basura;

        Pixmap stainPixmap = null;
        if(basura.tipo== Basura.TIPO_1)
            stainPixmap = Assets.botin1;
        if(basura.tipo == Basura.TIPO_2)
            stainPixmap = Assets.botin2;
        if(basura.tipo == Basura.TIPO_3)
            stainPixmap = Assets.botin3;
        int x = basura.x * 32;
        int y = basura.y * 32;
        g.drawPixmap(stainPixmap, x, y);

        // Dibujar power-up si existe y no está activo
        if (mundo.powerUp != null && !mundo.powerUp.activo) {
            PowerUp powerUp = mundo.powerUp;
            Pixmap powerUpPixmap = null;
            if (powerUp.tipo == PowerUp.TIPO_ACELERAR)
                powerUpPixmap = Assets.acelerar;
            else if (powerUp.tipo == PowerUp.TIPO_RALENTIZAR)
                powerUpPixmap = Assets.ralentizar;
            else if (powerUp.tipo == PowerUp.TIPO_PUNTOS_DOBLES)
                powerUpPixmap = Assets.puntosdobles;
            
            if (powerUpPixmap != null) {
                int px = powerUp.x * 32;
                int py = powerUp.y * 32;
                g.drawPixmap(powerUpPixmap, px, py);
            }
        }

        // Dibujar bomba
        if (mundo.bomba != null) {
            int bx = mundo.bomba.x * 32;
            int by = mundo.bomba.y * 32;
            g.drawPixmap(Assets.bomba, bx, by);
        }

        int len = robot.partes.size();
        for(int i = 1; i < len; i++) {
            Cubo part = robot.partes.get(i);
            x = part.x * 32;
            y = part.y * 32;
            g.drawPixmap(Assets.tripulacion, x, y);
        }

        Pixmap headPixmap = null;
        if(robot.direccion == Robot.ARRIBA)
            headPixmap = Assets.barcoarriba;
        if(robot.direccion == Robot.IZQUIERDA)
            headPixmap = Assets.barcoizquierda;
        if(robot.direccion == Robot.ABAJO)
            headPixmap = Assets.barcoabajo;
        if(robot.direccion == Robot.DERECHA)
            headPixmap = Assets.barcoderecha;
        x = head.x * 32 + 16;
        y = head.y * 32 + 16;

        if (headPixmap != null) { // Verificación de null
        g.drawPixmap(headPixmap, x - headPixmap.getWidth() / 2, y - headPixmap.getHeight() / 2);
        } else {
            System.err.println("headPixmap es null, no se puede dibujar."); // Opcional: para registro de error
        }
    }

    private void drawReadyUI() {
        Graficos g = juego.getGraphics();
        g.drawPixmap(Assets.preparado, 47, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawRunningUI() {
        Graficos g = juego.getGraphics();
        g.drawPixmap(Assets.botones, 0, 0, 64, 115, 60, 55);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
        g.drawPixmap(Assets.botones, 0, 416, 64, 59, 60, 54);
        g.drawPixmap(Assets.botones, 256, 416, 4, 59, 60, 54);
    }

    private void drawPausedUI() {
        Graficos g = juego.getGraphics();
        g.drawPixmap(Assets.menupausa, 80, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawGameOverUI() {
        Graficos g = juego.getGraphics();
        g.drawPixmap(Assets.finjuego, 62, 100);
        g.drawPixmap(Assets.botones, 128, 200, 0, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    public void drawText(Graficos g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);

            if (character == ' ') {
                x += 20;
                continue;
            }

            int srcX;
            int srcWidth;

            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }

            g.drawPixmap(Assets.numeros, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }

    @Override
    public void pause() {
        if(estado == EstadoJuego.Ejecutandose)
            estado = EstadoJuego.Pausado;

        if(mundo.finalJuego) {
            Configuraciones.addScore(mundo.puntuacion);
            Configuraciones.save(juego.getFileIO());
        }
    }

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}