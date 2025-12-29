package com.ldm.juegowalle.juego;
import java.util.List;
import com.ldm.juegowalle.Juego;
import com.ldm.juegowalle.Graficos;
import com.ldm.juegowalle.Input.TouchEvent;
import com.ldm.juegowalle.Pantalla;


public class PantallaMaximasPuntuaciones extends Pantalla {
    String[] lineas = new String[5];

    public PantallaMaximasPuntuaciones(Juego juego) {
        super(juego);

        for (int i = 0; i < 5; i++) {
            lineas[i] = (i + 1) + " " + Configuraciones.maxPuntuaciones[i];
        }
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = juego.getInput().getTouchEvents();
        juego.getInput().getKeyEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x < 64 && event.y > 416) {
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

        g.drawPixmap(Assets.fondo, 0, 0);
        g.drawPixmap(Assets.menuprincipal, 64, 20, 0, 49, 196, 43);

        int y = 100;
        for (int i = 0; i < 5; i++) {
            dibujarTexto(g, lineas[i], 20, y);
            y += 50;
        }

        g.drawPixmap(Assets.botones, 0, 416, 64, 59, 60, 54);
    }

    public void dibujarTexto(Graficos g, String linea, int x, int y) {
        int len = linea.length();
        for (int i = 0; i < len; i++) {
            char character = linea.charAt(i);

            if (character == ' ') {
                x += 20;
                continue;
            }

            int srcX = 0;
            int srcY = 0;
            int srcWidth = 0;
            int srcHeight = 0;
            
            switch (character) {
                case '0':
                    srcX = 10;
                    srcY = 19;
                    srcWidth = 36;
                    srcHeight = 39;
                    break;
                case '1':
                    srcX = 52;
                    srcY = 19;
                    srcWidth = 36;
                    srcHeight = 39;
                    break;
                case '2':
                    srcX = 88;
                    srcY = 19;
                    srcWidth = 36;
                    srcHeight = 39;
                    break;
                case '3':
                    srcX = 128;
                    srcY = 19;
                    srcWidth = 36;
                    srcHeight = 39;
                    break;
                case '4':
                    srcX = 165;
                    srcY = 19;
                    srcWidth = 36;
                    srcHeight = 39;
                    break;
                case '5':
                    srcX = 10;
                    srcY = 79;
                    srcWidth = 38;
                    srcHeight = 39;
                    break;
                case '6':
                    srcX = 52;
                    srcY = 79;
                    srcWidth = 36;
                    srcHeight = 39;
                    break;
                case '7':
                    srcX = 88;
                    srcY = 79;
                    srcWidth = 36;
                    srcHeight = 39;
                    break;
                case '8':
                    srcX = 126;
                    srcY = 79;
                    srcWidth = 39;
                    srcHeight = 39;
                    break;
                case '9':
                    srcX = 165;
                    srcY = 79;
                    srcWidth = 36;
                    srcHeight = 39;
                    break;
            }

            g.drawPixmap(Assets.numeros, x, y, srcX, srcY, srcWidth, srcHeight);
            x += srcWidth;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}

