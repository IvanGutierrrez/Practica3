package com.ldm.juegowalle.juego;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    public static final int ARRIBA = 0;
    public static final int IZQUIERDA= 1;
    public static final int ABAJO = 2;
    public static final int DERECHA = 3;

    public List<Cubo> partes = new ArrayList<>();
    public int direccion;

    public Robot() {
        direccion = ARRIBA;
        partes.add(new Cubo(5, 6));
        partes.add(new Cubo(5, 7));
        partes.add(new Cubo(5, 8));
    }

    public void girarIzquierda() {
        direccion += 1;
        if(direccion > DERECHA)
            direccion = ARRIBA;
    }

    public void girarDerecha() {
        direccion -= 1;
        if(direccion < ARRIBA)
            direccion = DERECHA;
    }

    public void recoger() {
        Cubo end = partes.get(partes.size()-1);
        partes.add(new Cubo(end.x, end.y));
    }

    public void avance() {
        Cubo robot = partes.get(0);

        int len = partes.size() - 1;
        for(int i = len; i > 0; i--) {
            Cubo antes = partes.get(i-1);
            Cubo parte = partes.get(i);
            parte.x = antes.x;
            parte.y = antes.y;
        }

        if(direccion == ARRIBA)
            robot.y -= 1;
        if(direccion == IZQUIERDA)
            robot.x -= 1;
        if(direccion == ABAJO)
            robot.y += 1;
        if(direccion == DERECHA)
            robot.x += 1;

        if(robot.x < 0)
            robot.x = 9;
        if(robot.x > 9)
            robot.x = 0;
        if(robot.y < 0)
            robot.y = 12;
        if(robot.y > 12)
            robot.y = 0;
    }

    public boolean comprobarChoque() {
        int len = partes.size();
        Cubo robot = partes.get(0);
        for(int i = 1; i < len; i++) {
            Cubo parte = partes.get(i);
            if(parte.x == robot.x && parte.y == robot.y)
                return true;
        }
        return false;
    }
}

