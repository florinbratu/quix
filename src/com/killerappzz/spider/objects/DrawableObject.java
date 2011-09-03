package com.killerappzz.spider.objects;

import android.graphics.Canvas;

/** 
 * Base class defining the core set of information necessary to render (and move
 * an object on the screen.  This is an abstract type and must be derived to
 * add methods to actually draw (see CanvasSprite and GLSprite).
 */
public abstract class DrawableObject {
    // Position.
    public float x;
    public float y;
    public float z;
    
    // Velocity.
    public float velocityX;
    public float velocityY;
    public float velocityZ;
    
    // Size.
    public float width;
    public float height;
    
    public abstract void draw(Canvas canvas);
    public abstract void cleanup();
}
