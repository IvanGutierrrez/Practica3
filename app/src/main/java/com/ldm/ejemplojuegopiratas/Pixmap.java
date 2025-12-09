package com.ldm.ejemplojuegopiratas;

import com.ldm.ejemplojuegopiratas.Graficos.PixmapFormat;

public interface Pixmap {
    int getWidth();

    int getHeight();

    PixmapFormat getFormat();

    void dispose();
}

