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

import com.ap.fahm.bakingapp.model.pojo.Recipe;

import java.util.ArrayList;

/**
 * Created by Faheem on 03/12/17.
 */

public interface IRecipieListContract {
    interface View {
        void setLoading(boolean status, @Nullable String message);

        void showError(String message);

        void updateRecipeList();

        void showNoConnection();

        void setIdlingResourceStatus(boolean isIdle);

        boolean isRunningTest();
    }

    interface ViewModel {
        void loadRecipes();

        ArrayList<Recipe> getLoadedRecipes();

        void setFavoriteRecipe(Recipe recipe, int position);

        Recipe getRecipeById(long recipeId);
    }

}
