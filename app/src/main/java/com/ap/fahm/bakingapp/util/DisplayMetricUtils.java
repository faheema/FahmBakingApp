/*
 * Copyright 2018.  FahmApps.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ap.fahm.bakingapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public final class DisplayMetricUtils {

    public static int convertPixelsToDp(int px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = (float) px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static int convertDpToPixel(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = (float) dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static int getDeviceWidth(Context activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) activity).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static int getDeviceHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int getDeviceOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

}
