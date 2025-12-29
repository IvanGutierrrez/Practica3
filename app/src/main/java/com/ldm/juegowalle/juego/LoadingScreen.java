package com.ldm.juegowalle.juego;

import com.ldm.juegowalle.Juego;
import com.ldm.juegowalle.Graficos;
import com.ldm.juegowalle.Pantalla;
import com.ldm.juegowalle.Graficos.PixmapFormat;

public class LoadingScreen extends Pantalla{
    public LoadingScreen(Juego juego) {
        super(juego);
    }

    @Override
    public void update(float deltaTime) {
        Graficos g = juego.getGraphics();
        Assets.fondo = g.newPixmap("fondo.png", PixmapFormat.RGB565);
        Assets.logo = g.newPixmap("logo.png", PixmapFormat.ARGB4444); //Cambiar
        Assets.menuprincipal = g.newPixmap("menuprincipal.png", PixmapFormat.ARGB4444);//Cambiar
        Assets.botones = g.newPixmap("botones.png", PixmapFormat.ARGB4444);
        Assets.ayuda1 = g.newPixmap("ayuda1.png", PixmapFormat.ARGB4444); //Cambiar
        Assets.ayuda2 = g.newPixmap("ayuda2.png", PixmapFormat.ARGB4444); //Cambiar
        Assets.ayuda3 = g.newPixmap("ayuda3.png", PixmapFormat.ARGB4444); //Cambiar
        Assets.numeros = g.newPixmap("numeros.png", PixmapFormat.ARGB4444); //Cambiar
        Assets.preparado = g.newPixmap("preparado.png", PixmapFormat.ARGB4444); //Cambiar
        Assets.menupausa = g.newPixmap("menupausa.png", PixmapFormat.ARGB4444); //Cambiar
        Assets.finjuego = g.newPixmap("finjuego.png", PixmapFormat.ARGB4444); //Cambiar
        Assets.barcoarriba = g.newPixmap("robotarriba.png", PixmapFormat.ARGB4444);
        Assets.barcoizquierda = g.newPixmap("robotizquierda.png", PixmapFormat.ARGB4444);
        Assets.barcoabajo = g.newPixmap("robotabajo.png", PixmapFormat.ARGB4444);
        Assets.barcoderecha = g.newPixmap("robotderecha.png", PixmapFormat.ARGB4444);
        Assets.tripulacion = g.newPixmap("cubo.png", PixmapFormat.ARGB4444);
        Assets.botin1 = g.newPixmap("basura1.png", PixmapFormat.ARGB4444);
        Assets.botin2 = g.newPixmap("basura2.png", PixmapFormat.ARGB4444);
        Assets.botin3 = g.newPixmap("basura3.png", PixmapFormat.ARGB4444);
        Assets.acelerar = g.newPixmap("acelerar.png", PixmapFormat.ARGB4444);
        Assets.ralentizar = g.newPixmap("ralentizar.png", PixmapFormat.ARGB4444);
        Assets.puntosdobles = g.newPixmap("puntosx2.png", PixmapFormat.ARGB4444);
        Assets.bomba = g.newPixmap("bomba.png", PixmapFormat.ARGB4444);

        Assets.pulsar = juego.getAudio().nuevoSonido("pulsar.ogg");
        Assets.ataque = juego.getAudio().nuevoSonido("recoger.ogg");
        Assets.derrota = juego.getAudio().nuevoSonido("derrota.ogg");


        Configuraciones.cargar(juego.getFileIO());
        juego.setScreen(new MainMenuScreen(juego));
    }

    @Override
    public void present(float deltaTime) {

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