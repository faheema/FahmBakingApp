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

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.ap.fahm.bakingapp.model.localdata.dao.IngredientDao;
import com.ap.fahm.bakingapp.model.localdata.dao.RecipeDao;
import com.ap.fahm.bakingapp.model.localdata.dao.StepDao;
import com.ap.fahm.bakingapp.model.pojo.Ingredient;
import com.ap.fahm.bakingapp.model.pojo.Recipe;
import com.ap.fahm.bakingapp.model.pojo.Step;

import java.util.ArrayList;

public class RecipeContentProvider extends ContentProvider {
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(RecipeDataContract.AUTHORITY, RecipeDataContract.RecipeEntry.TABLE_NAME,
                RecipeDataContract.RecipeEntry.CODE_RECIPE_DIRECTORY);
        MATCHER.addURI(RecipeDataContract.AUTHORITY, RecipeDataContract.RecipeEntry.TABLE_NAME + "/#",
                RecipeDataContract.RecipeEntry.CODE_RECIPE_ITEM);
        MATCHER.addURI(RecipeDataContract.AUTHORITY, RecipeDataContract.IngredientEntry.TABLE_NAME,
                RecipeDataContract.IngredientEntry.CODE_INGREDIENT_DIRECTORY);
        MATCHER.addURI(RecipeDataContract.AUTHORITY, RecipeDataContract.IngredientEntry.TABLE_NAME + "/#",
                RecipeDataContract.IngredientEntry.CODE_INGREDIENT_ITEM);
        MATCHER.addURI(RecipeDataContract.AUTHORITY, RecipeDataContract.StepEntry.TABLE_NAME,
                RecipeDataContract.StepEntry.CODE_STEP_DIRECTORY);
        MATCHER.addURI(RecipeDataContract.AUTHORITY, RecipeDataContract.StepEntry.TABLE_NAME + "/#",
                RecipeDataContract.StepEntry.CODE_STEP_ITEM);
    }

    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private StepDao stepDao;
    private Context context;

    @Override
    public boolean onCreate() {
        context = getContext();
        if (context == null) {
            return false;
        }
        recipeDao = RecipeDatabase.getInstance(context).recipeDao();
        ingredientDao = RecipeDatabase.getInstance(context).ingredientDao();
        stepDao = RecipeDatabase.getInstance(context).stepDao();
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_DIRECTORY:
                return "vnd.android.cursor.dir/" + RecipeDataContract.AUTHORITY + "." +
                        RecipeDataContract.RecipeEntry.TABLE_NAME;
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_ITEM:
                return "vnd.android.cursor.item/" + RecipeDataContract.AUTHORITY + "." +
                        RecipeDataContract.RecipeEntry.TABLE_NAME;

            case RecipeDataContract.IngredientEntry.CODE_INGREDIENT_DIRECTORY:
                return "vnd.android.cursor.dir/" + RecipeDataContract.AUTHORITY + "." +
                        RecipeDataContract.IngredientEntry.TABLE_NAME;
            case RecipeDataContract.IngredientEntry.CODE_INGREDIENT_ITEM:
                return "vnd.android.cursor.item/" + RecipeDataContract.AUTHORITY + "." +
                        RecipeDataContract.IngredientEntry.TABLE_NAME;

            case RecipeDataContract.StepEntry.CODE_STEP_DIRECTORY:
                return "vnd.android.cursor.dir/" + RecipeDataContract.AUTHORITY + "." +
                        RecipeDataContract.StepEntry.TABLE_NAME;
            case RecipeDataContract.StepEntry.CODE_STEP_ITEM:
                return "vnd.android.cursor.item/" + RecipeDataContract.AUTHORITY + "." +
                        RecipeDataContract.StepEntry.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        Cursor cursor;
        switch (code) {
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_DIRECTORY: {
                cursor = recipeDao.selectAll();
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;
            }
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_ITEM: {
                cursor = recipeDao.selectById(ContentUris.parseId(uri));
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;
            }
            case RecipeDataContract.IngredientEntry.CODE_INGREDIENT_DIRECTORY:
                throw new IllegalArgumentException("URI Not Implemented Yet: " + uri);
            case RecipeDataContract.IngredientEntry.CODE_INGREDIENT_ITEM:
                cursor = ingredientDao.selectAll(ContentUris.parseId(uri));
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;
            case RecipeDataContract.StepEntry.CODE_STEP_DIRECTORY:
                throw new IllegalArgumentException("URI Not Implemented Yet: " + uri);
            case RecipeDataContract.StepEntry.CODE_STEP_ITEM:
                cursor = stepDao.selectAll(ContentUris.parseId(uri));
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_DIRECTORY:
                Recipe recipe = Recipe.fromContentValues(values);
                final long recipeId = recipeDao.insert(recipe);
                if (recipeId > 0) {
                    // Insert ingredients
                    bulkInsertIngredients(recipe, recipeId);

                    // Insert steps
                    bulkInsertSteps(recipe, recipeId);
                }
                context.getContentResolver().notifyChange(uri, null);
                if (recipeId > 0) {
                    return ContentUris.withAppendedId(uri, recipeId);
                } else {
                    throw new SQLiteException("Failed to insert row into URI: " + uri);
                }
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private void bulkInsertSteps(Recipe recipe, long recipeId) {
        final Step[] steps = new Step[recipe.getSteps().size()];
        for (int i = 0; i < recipe.getSteps().size(); i++) {
            recipe.getSteps().get(i).setRecipeId(recipeId);
            steps[i] = recipe.getSteps().get(i);
        }
        long stepIds[] = stepDao.insertAll(steps);
    }

    private void bulkInsertIngredients(Recipe recipe, long recipeId) {
        final Ingredient[] ingredients = new Ingredient[recipe.getIngredients().size()];
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            recipe.getIngredients().get(i).setRecipeId(recipeId);
            ingredients[i] = recipe.getIngredients().get(i);
        }
        long ingredientIds[] = ingredientDao.insertAll(ingredients);
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        switch (MATCHER.match(uri)) {
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_DIRECTORY:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Recipe[] recipes = new Recipe[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    recipes[i] = Recipe.fromContentValues(valuesArray[i]);
                }
                long recipeIds[] = recipeDao.insertAll(recipes);
                for (int i = 0; i < recipeIds.length; i++) {
                    // Insert ingredients
                    bulkInsertIngredients(recipes[i], recipeIds[i]);

                    // Insert steps
                    bulkInsertSteps(recipes[i], recipeIds[i]);
                }
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int count;
        switch (MATCHER.match(uri)) {
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_DIRECTORY:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_ITEM:
                count = recipeDao.deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            case RecipeDataContract.IngredientEntry.CODE_INGREDIENT_ITEM:
                count = ingredientDao.deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            case RecipeDataContract.StepEntry.CODE_STEP_ITEM:
                count = stepDao.deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_DIRECTORY:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case RecipeDataContract.RecipeEntry.CODE_RECIPE_ITEM:
                final Recipe recipe = Recipe.fromContentValues(values);
                final int count = recipeDao.update(recipe);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final RecipeDatabase database = RecipeDatabase.getInstance(context);
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

}
