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

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeDataContract {

    public static final String AUTHORITY = "com.fahm.recipe";

    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    public static final class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipe";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_DATE_ADDED = "dateAdded";
        public static final String ENTITY_INGREDIENTS = "ingredients";
        public static final String ENTITY_STEPS = "steps";

        public static final int CODE_RECIPE_DIRECTORY = 100;
        public static final int CODE_RECIPE_ITEM = 101;

        public static final Uri RECIPE_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(TABLE_NAME)
                        .build();
    }

    public static final class IngredientEntry implements BaseColumns {
        public static final String TABLE_NAME = "ingredient";
        public static final String COLUMN_ID = "ingredientId";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";

        public static final int CODE_INGREDIENT_DIRECTORY = 200;
        public static final int CODE_INGREDIENT_ITEM = 201;

        public static final Uri INGREDIENT_CONTENT_DIRECTORY_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(TABLE_NAME)
                        .build();

        public static final Uri.Builder INGREDIENT_CONTENT_ITEM_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(TABLE_NAME);
    }

    public static final class StepEntry implements BaseColumns {
        public static final String TABLE_NAME = "step";
        public static final String COLUMN_ID = "stepId";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "videoURL";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnailURL";

        public static final int CODE_STEP_DIRECTORY = 300;
        public static final int CODE_STEP_ITEM = 301;

        public static final Uri STEP_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(TABLE_NAME)
                        .build();

        public static final Uri.Builder STEP_CONTENT_ITEM_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(TABLE_NAME);
    }

}
