package com.killerappzz.spider;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class MainActivity extends Activity {
	
    private CanvasSurfaceView mCanvasSurfaceView;
    // Describes the image format our bitmaps should be converted to.
    private static BitmapFactory.Options sBitmapOptions 
        = new BitmapFactory.Options();
    private Bitmap[] mBitmaps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCanvasSurfaceView = new CanvasSurfaceView(this);
        SimpleCanvasRenderer spriteRenderer = new SimpleCanvasRenderer();
       
        // Sets our preferred image format to 16-bit, 565 format.
        sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;

        // Clear out any old profile results.
        ProfileRecorder.sSingleton.resetAll();
        
        // Allocate space for the robot sprites + one background sprite.
        CanvasSprite[] spriteArray = new CanvasSprite[2];    
        
        mBitmaps = new Bitmap[2];
        mBitmaps[0] = loadBitmap(this, R.drawable.background);
        mBitmaps[1] = loadBitmap(this, R.drawable.spider);
        
        // We need to know the width and height of the display pretty soon,
        // so grab the information now.
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        // Make the background.
        // Note that the background image is larger than the screen, 
        // so some clipping will occur when it is drawn.
        CanvasSprite background = new CanvasSprite(mBitmaps[0]);
        background.width = mBitmaps[0].getWidth();
        background.height = mBitmaps[0].getHeight();
        spriteArray[0] = background;
        
        // This list of things to move. It points to the same content as
        // spriteArray except for the background.
        CanvasSprite spider = new CanvasSprite(mBitmaps[1]);
        spider.width = 32;
        spider.height = 32;
        // Pick a random location for this sprite.
        spider.x = (float)(Math.random() * dm.widthPixels);
        spider.y = (float)(Math.random() * dm.heightPixels);
        spriteArray[1] = spider;
       
        // Now's a good time to run the GC.  Since we won't do any explicit
        // allocation during the test, the GC should stay dormant and not
        // influence our results.
        Runtime r = Runtime.getRuntime();
        r.gc();
        
        spriteRenderer.setSprites(spriteArray);
        mCanvasSurfaceView.setRenderer(spriteRenderer);

        setContentView(mCanvasSurfaceView);
    }
    
    
    /** Recycles all of the bitmaps loaded in onCreate(). */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCanvasSurfaceView.clearEvent();
        mCanvasSurfaceView.stopDrawing();
        
        for (int x = 0; x < mBitmaps.length; x++) {
            mBitmaps[x].recycle();
            mBitmaps[x] = null;
        }
    }


    /**
     * Loads a bitmap from a resource and converts it to a bitmap.  This is
     * a much-simplified version of the loadBitmap() that appears in
     * SimpleGLRenderer.
     * @param context  The application context.
     * @param resourceId  The id of the resource to load.
     * @return  A bitmap containing the image contents of the resource, or null
     *     if there was an error.
     */
    protected Bitmap loadBitmap(Context context, int resourceId) {
        Bitmap bitmap = null;
        if (context != null) {
          
            InputStream is = context.getResources().openRawResource(resourceId);
            try {
                bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }

        return bitmap;
    }
}