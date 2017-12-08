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

package com.ap.fahm.bakingapp.model.localdata.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;


import com.ap.fahm.bakingapp.model.localdata.db.RecipeDataContract;
import com.ap.fahm.bakingapp.model.pojo.Ingredient;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface IngredientDao {
    @Query("SELECT COUNT(*) FROM " + RecipeDataContract.IngredientEntry.TABLE_NAME)
    int count();

    @Insert(onConflict = REPLACE)
    long insert(Ingredient ingredient);

    @Insert(onConflict = REPLACE)
    long[] insertAll(Ingredient[] ingredients);

    @Query("SELECT * FROM " + RecipeDataContract.IngredientEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID + " = :recipeId")
    Cursor selectAll(long recipeId);

    @Query("SELECT * FROM " + RecipeDataContract.IngredientEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.IngredientEntry.COLUMN_ID + " = :ingredientId" + " AND "
            + RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID + " = :recipeId")
    Cursor selectById(long ingredientId, long recipeId);

    @Query("DELETE FROM " + RecipeDataContract.IngredientEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.IngredientEntry.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Update(onConflict = REPLACE)
    int update(Ingredient ingredient);
}
