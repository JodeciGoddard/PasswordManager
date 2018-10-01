package com.example.jodeci.passwordmanager.Util;

import android.graphics.drawable.GradientDrawable;

/**
 * Created by jodeci on 9/21/2018.
 */

public class Util {

    public static GradientDrawable createCircle(int col){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(col);
        shape.setStroke(2, col);
        return shape;
    }

}
