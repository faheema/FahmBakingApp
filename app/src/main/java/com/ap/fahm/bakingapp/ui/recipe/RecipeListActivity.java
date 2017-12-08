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
package com.ap.fahm.bakingapp.ui.recipe;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ap.fahm.bakingapp.R;
import com.ap.fahm.bakingapp.util.SimpleIdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements RecipeListFragment.RecipeListFragmentListener {


    private AtomicBoolean runningTest;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Nullable
    @VisibleForTesting
    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        SimpleIdlingResource idlingResource = getIdlingResource();
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
            Log.d("SetIdlingStatus", String.valueOf(false));
        } else {
            Log.d("SetIdlingStatus", "Null");
        }

        if (savedInstanceState == null) {
            RecipeListFragment recipeFragment = RecipeListFragment.newInstance(
                    getIntent().getLongExtra("recipeId", 0L));
            recipeFragment.setRunningTest(isRunningTest());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_recipe_container, recipeFragment)
                    .commit();
        }
    }

    @Override
    public void onIdlingResourceStatusChanged(boolean isIdle) {
        SimpleIdlingResource idlingResource = getIdlingResource();
        if (idlingResource != null) {
            Log.d("SetIdlingStatus", String.valueOf(isIdle));
            idlingResource.setIdleState(isIdle);
        } else {
            Log.d("SetIdlingStatus", "Null");
        }
    }
    public synchronized boolean isRunningTest() {
        if (null == runningTest) {
            boolean isTest;

            try {
                Class.forName("android.support.test.espresso.Espresso");
                isTest = true;
            } catch (ClassNotFoundException e) {
                isTest = false;
            }

            runningTest = new AtomicBoolean(isTest);
        }

        return runningTest.get();
    }
}
