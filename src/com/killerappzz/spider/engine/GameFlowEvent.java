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

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.MainActivity;

import android.content.Context;
import android.util.Log;

public class GameFlowEvent implements Runnable {
	public static final int EVENT_INVALID = -1;
    public static final int EVENT_RESTART_LEVEL = 0;
    public static final int EVENT_GAME_OVER = 1;
    public static final int EVENT_GO_TO_NEXT_LEVEL = 2;
    
    private int mEventCode;
    private MainActivity mMainActivity;
    
    public void post(int event, int index, Context context) {
    	if (context instanceof MainActivity) {
        	Log.d(Constants.LOG_TAG, "Post Game Flow Event: " + event);
            mEventCode = event;
            mMainActivity = (MainActivity)context;
            mMainActivity.runOnUiThread(this);
        }
    }
    
    public void postImmediate(int event, Context context) {
        if (context instanceof MainActivity) {
        	Log.d(Constants.LOG_TAG, "Execute Immediate Game Flow Event: " + event);
            mEventCode = event;
            mMainActivity = (MainActivity)context;
            mMainActivity.onGameFlowEvent(mEventCode);
        }
    }
    
    public void run() {
        if (mMainActivity != null) {
        	Log.d(Constants.LOG_TAG, "Execute Game Flow Event: " + mEventCode);
            mMainActivity.onGameFlowEvent(mEventCode);
            mMainActivity = null;
        }
    }

}
