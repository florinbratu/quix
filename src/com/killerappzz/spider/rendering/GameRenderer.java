/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.killerappzz.spider.rendering;

import java.util.LinkedList;
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

    private final List<DrawableObject> objects; 
    
    public GameRenderer() {
    	this.objects = new LinkedList<DrawableObject>();
	}
    
    public void addObject(DrawableObject object) {
    	this.objects.add(object);
    }
    
    public void drawFrame(Canvas canvas) {
    	for(DrawableObject object : objects) {
    		object.draw(canvas);
    	}
    }

    public void sizeChanged(int width, int height) {
        // huh???
    }

}
