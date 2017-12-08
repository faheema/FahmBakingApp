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
import com.ap.fahm.bakingapp.model.pojo.Step;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StepDao {
    @Query("SELECT COUNT(*) FROM " + RecipeDataContract.StepEntry.TABLE_NAME)
    int count();

    @Insert(onConflict = REPLACE)
    long insert(Step step);

    @Insert(onConflict = REPLACE)
    long[] insertAll(Step[] steps);

    @Query("SELECT * FROM " + RecipeDataContract.StepEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.StepEntry.COLUMN_RECIPE_ID + " = :recipeId")
    Cursor selectAll(long recipeId);

    @Query("SELECT * FROM " + RecipeDataContract.StepEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.StepEntry.COLUMN_ID + " = :stepId" + " AND "
            + RecipeDataContract.StepEntry.COLUMN_RECIPE_ID + " = :recipeId")
    Cursor selectById(long stepId, long recipeId);

    @Query("DELETE FROM " + RecipeDataContract.StepEntry.TABLE_NAME + " WHERE "
            + RecipeDataContract.StepEntry.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Update(onConflict = REPLACE)
    int update(Step step);
}
