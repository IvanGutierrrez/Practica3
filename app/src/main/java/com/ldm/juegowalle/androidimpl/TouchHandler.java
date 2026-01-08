package com.ldm.juegowalle.androidimpl;

import java.util.List;

import android.view.View.OnTouchListener;

import com.ldm.juegowalle.Input.TouchEvent;


public interface TouchHandler extends OnTouchListener {
    List<TouchEvent> getTouchEvents();
}

