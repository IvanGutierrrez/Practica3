package com.ldm.juegowalle;

import com.ldm.juegowalle.Graficos.PixmapFormat;

public interface Pixmap {
    int getWidth();

    int getHeight();

    PixmapFormat getFormat();

    void dispose();
}

