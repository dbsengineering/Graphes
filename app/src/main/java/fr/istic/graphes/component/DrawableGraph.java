package fr.istic.graphes.component;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;

/**
 * Created by cavronjeremy on 10/10/2017.
 */

public class DrawableGraph extends android.graphics.drawable.Drawable {


    /**
     *
     */
    public DrawableGraph(){

    }

    @Override
    public void draw(Canvas canvas){

    }

    @Override
    public void setAlpha(int alpha){

    }

    @Override
    public void setColorFilter(ColorFilter cf){

    }
    @Override
    public int getOpacity(){
        return PixelFormat.TRANSLUCENT;
    }
}
