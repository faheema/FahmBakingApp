/*
 * Copyright 2017.  FahmApps
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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ap.fahm.bakingapp.ui.ingredient.IngredientFragment;
import com.ap.fahm.bakingapp.ui.step.StepFragment;


public class RecipeDetailViewPagerAdapter extends FragmentPagerAdapter {

    private IngredientFragment ingredientFragment;
    private StepFragment stepFragment;

    public RecipeDetailViewPagerAdapter(FragmentManager fm,
                                        IngredientFragment ingredientFragment,
                                        StepFragment stepFragment) {
        super(fm);
        this.ingredientFragment = ingredientFragment;
        this.stepFragment = stepFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ingredientFragment;
            case 1:
                return stepFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Ingredients";
            case 1:
                return "Steps";
        }
        return null;
    }
}
