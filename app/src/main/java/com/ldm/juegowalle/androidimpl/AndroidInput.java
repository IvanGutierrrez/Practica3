package com.ldm.juegowalle.androidimpl;

import java.util.List;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.ldm.juegowalle.Input;

public class AndroidInput implements Input {
    AccelerometerHandler accelHandler;
    KeyboardHandler keyHandler;
    TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        accelHandler = new AccelerometerHandler(context);
        keyHandler = new KeyboardHandler(view);
        touchHandler = new MultiTouchHandler(view, scaleX, scaleY); // Siempre usa MultiTouchHandler
    }

    @Override
    @NonNull
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}

