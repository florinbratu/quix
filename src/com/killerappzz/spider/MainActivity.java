package com.killerappzz.spider;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.killerappzz.spider.objects.ObjectManager;
import com.killerappzz.spider.objects.Sprite;
import com.killerappzz.spider.rendering.CanvasSurfaceView;
import com.killerappzz.spider.rendering.GameRenderer;

public class MainActivity extends Activity {
	
    private CanvasSurfaceView mCanvasSurfaceView;
    private ObjectManager manager;
    private static BitmapFactory.Options sBitmapOptions 
      = new BitmapFactory.Options();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCanvasSurfaceView = new CanvasSurfaceView(this);
        manager = new ObjectManager();
        // Sets our preferred image format to 16-bit, 565 format.
		sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        GameRenderer spriteRenderer = new GameRenderer(manager);
       
        // Clear out any old profile results.
        ProfileRecorder.sSingleton.resetAll();
        
        // We need to know the width and height of the display pretty soon,
        // so grab the information now.
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        // Make the background.
        // Note that the background image is larger than the screen, 
        // so some clipping will occur when it is drawn.
        manager.addObject(
        		new Sprite(this, sBitmapOptions, R.drawable.background));
        
        // Make the spider
        Sprite spider = new Sprite(this, sBitmapOptions, R.drawable.spider);
        // Spider location.
        int centerX = (dm.widthPixels - (int)spider.width) / 2;
        spider.x = centerX;
        spider.y = 0;
        manager.addObject(spider);

        // Now's a good time to run the GC.  Since we won't do any explicit
        // allocation during the test, the GC should stay dormant and not
        // influence our results.
        Runtime r = Runtime.getRuntime();
        r.gc();
        
        mCanvasSurfaceView.setRenderer(spriteRenderer);
        setContentView(mCanvasSurfaceView);
    }
    
    
    /** Recycles all of the bitmaps loaded in onCreate(). */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCanvasSurfaceView.clearEvent();
        mCanvasSurfaceView.stopDrawing();
        
        manager.cleanup();
    }

}