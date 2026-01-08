package com.ldm.juegowalle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {
    InputStream leerArchivo(String nombreArchivo) throws IOException;

    OutputStream escribirArchivo(String nombreArchivo) throws IOException;

}

