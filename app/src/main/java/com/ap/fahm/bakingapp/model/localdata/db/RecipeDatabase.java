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
package com.ap.fahm.bakingapp.model.localdata.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.ap.fahm.bakingapp.model.localdata.dao.IngredientDao;
import com.ap.fahm.bakingapp.model.localdata.dao.RecipeDao;
import com.ap.fahm.bakingapp.model.localdata.dao.StepDao;
import com.ap.fahm.bakingapp.model.pojo.Ingredient;
import com.ap.fahm.bakingapp.model.pojo.Recipe;
import com.ap.fahm.bakingapp.model.pojo.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase sInstance;

    public static synchronized RecipeDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), RecipeDatabase.class,
                            "recipe")
                    .build();
        }
        return sInstance;
    }

    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                RecipeDatabase.class).build();
    }

    @SuppressWarnings("WeakerAccess")
    public abstract RecipeDao recipeDao();

    @SuppressWarnings("WeakerAccess")
    public abstract IngredientDao ingredientDao();

    @SuppressWarnings("WeakerAccess")
    public abstract StepDao stepDao();
}
