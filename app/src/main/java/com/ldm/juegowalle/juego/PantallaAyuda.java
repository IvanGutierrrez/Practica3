package com.ldm.juegowalle.juego;
import java.util.List;
import com.ldm.juegowalle.Juego;
import com.ldm.juegowalle.Graficos;
import com.ldm.juegowalle.Input.TouchEvent;
import com.ldm.juegowalle.Pantalla;

public class PantallaAyuda extends Pantalla {
    public PantallaAyuda(Juego juego) {
        super(juego);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = juego.getInput().getTouchEvents();
        juego.getInput().getKeyEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 256 && event.y > 416 ) {
                    juego.setScreen(new PantallaAyuda2(juego));
                    if(Configuraciones.sonidoHabilitado)
                        Assets.pulsar.play(1);
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graficos g = juego.getGraphics();
        g.drawPixmap(Assets.fondo, 0, 0);
        g.drawPixmap(Assets.ayuda1, 64, 100);
        g.drawPixmap(Assets.botones, 256, 416, 4, 59, 60, 54);
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