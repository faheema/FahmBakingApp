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

package com.ap.fahm.bakingapp.model.localdata;

import android.database.Cursor;
import android.net.Uri;


import com.ap.fahm.bakingapp.model.pojo.Recipe;

import java.util.ArrayList;

public interface LocalRecipeDataSource {
    void getAll(LocalDataObserver<ArrayList<Recipe>> cursorFavoriteDataObserver);

    void getById(long id, LocalDataObserver<Cursor> cursorFavoriteDataObserver);

    void insert(Recipe recipe, LocalDataObserver<Uri> uriFavoriteDataObserver);

    void insertMany(ArrayList<Recipe> recipes, LocalDataObserver<Uri> uriFavoriteDataObserver);

    void update(Recipe recipe, LocalDataObserver<Integer> integerFavoriteDataObserver);

    void delete(long id, LocalDataObserver<Integer> integerFavoriteDataObserver);
}
