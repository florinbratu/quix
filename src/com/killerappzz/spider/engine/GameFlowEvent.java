/*
 * Copyright (C) 2010 The Android Open Source Project
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
 
package com.killerappzz.spider.engine;

import android.util.Log;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.MainActivity;

public class GameFlowEvent implements Runnable {
	public static final int EVENT_INVALID = -1;
    public static final int EVENT_RESTART_LEVEL = 0;
    public static final int EVENT_GAME_OVER = 1;
    public static final int EVENT_GO_TO_NEXT_LEVEL = 2;
    
    public static GameFlowEvent[] allEvents(MainActivity activity) {
		return new GameFlowEvent[]{
			new GameFlowEvent(activity, EVENT_RESTART_LEVEL),
			new GameFlowEvent(activity, EVENT_GAME_OVER),
			new GameFlowEvent(activity, EVENT_GO_TO_NEXT_LEVEL),
		};
	}
    
    private final int mEventCode;
    private final MainActivity mMainActivity;
    
    public GameFlowEvent(MainActivity activity, int event) {
    	this.mMainActivity = activity;
    	this.mEventCode = event;
    }
    
    public void post() {
    	Log.d(Constants.LOG_TAG, "Post Game Flow Event: " + mEventCode);
    	mMainActivity.runOnUiThread(this);
    }
    
    public void postImmediate() {
    	run();
    }
    
    public void run() {
        Log.d(Constants.LOG_TAG, "Execute Game Flow Event: " + mEventCode);
        mMainActivity.onGameFlowEvent(mEventCode);
    }

}
