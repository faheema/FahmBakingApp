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
import com.ap.fahm.bakingapp.model.pojo.Recipe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {

    @Query("SELECT COUNT(*) FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME)
    int count();

    @Insert(onConflict = REPLACE)
    long insert(Recipe recipe);

    @Insert(onConflict = REPLACE)
    long[] insertAll(Recipe[] recipes);

    @Query("SELECT * FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT " + RecipeDataContract.RecipeEntry.TABLE_NAME + ".*, " +
            RecipeDataContract.IngredientEntry.TABLE_NAME + ".*, " +
            RecipeDataContract.StepEntry.TABLE_NAME + ".* " +
            "FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME + " " +
            "INNER JOIN " + RecipeDataContract.IngredientEntry.TABLE_NAME + " ON " +
            RecipeDataContract.IngredientEntry.TABLE_NAME + "." +
            RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID + " = " +
            RecipeDataContract.RecipeEntry.TABLE_NAME + "." + RecipeDataContract.RecipeEntry.COLUMN_ID +
            " INNER JOIN " + RecipeDataContract.StepEntry.TABLE_NAME + " ON " +
            RecipeDataContract.StepEntry.TABLE_NAME + "." +
            RecipeDataContract.StepEntry.COLUMN_RECIPE_ID + " = " +
            RecipeDataContract.RecipeEntry.TABLE_NAME + "." + RecipeDataContract.RecipeEntry.COLUMN_ID)
    Cursor selectAllWithChildElements();

    @Query("SELECT * FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.RecipeEntry.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("SELECT " + RecipeDataContract.RecipeEntry.TABLE_NAME + ".*, " +
            RecipeDataContract.IngredientEntry.TABLE_NAME + ".*, " +
            RecipeDataContract.StepEntry.TABLE_NAME + ".* " +
            "FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME + " " +
            "INNER JOIN " + RecipeDataContract.IngredientEntry.TABLE_NAME + " ON " +
            RecipeDataContract.IngredientEntry.TABLE_NAME + "." +
            RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID + " = " +
            RecipeDataContract.RecipeEntry.TABLE_NAME + "." + RecipeDataContract.RecipeEntry.COLUMN_ID +
            " INNER JOIN " + RecipeDataContract.StepEntry.TABLE_NAME + " ON " +
            RecipeDataContract.StepEntry.TABLE_NAME + "." +
            RecipeDataContract.StepEntry.COLUMN_RECIPE_ID + " = " +
            RecipeDataContract.RecipeEntry.TABLE_NAME + "." + RecipeDataContract.RecipeEntry.COLUMN_ID +
            " WHERE " + RecipeDataContract.RecipeEntry.TABLE_NAME + "." +
            RecipeDataContract.RecipeEntry.COLUMN_ID + " = :id")
    Cursor selectByIdWithChildElements(long id);

    @Query("DELETE FROM " + RecipeDataContract.RecipeEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.RecipeEntry.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Update(onConflict = REPLACE)
    int update(Recipe recipe);
}
