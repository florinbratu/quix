package com.killerappzz.spider.rendering;

import java.util.List;

import android.graphics.Canvas;

import com.killerappzz.spider.objects.DrawableObject;
import com.killerappzz.spider.rendering.CanvasSurfaceView.Renderer;

/**
 * An extremely simple renderer based on the CanvasSurfaceView drawing
 * framework.  Simply draws a list of sprites to a canvas every frame.
 * Do we need more ???
 */
public class GameRenderer implements Renderer {

	private List<DrawableObject> drawQueue;
	
    public GameRenderer(List<DrawableObject> drawQueue) {
    	this.drawQueue = drawQueue;
	}
    
    public synchronized void drawFrame(Canvas canvas) {
    	for(DrawableObject object : drawQueue) {
    		object.draw(canvas);
    	}
    }
    
    public synchronized updateDrawQueue() {
    	
    }

    public void sizeChanged(int width, int height) {
        // huh???
    }

	public synchronized void waitDrawingComplete() {
		// wait until finished drawing current frame
	}

}
