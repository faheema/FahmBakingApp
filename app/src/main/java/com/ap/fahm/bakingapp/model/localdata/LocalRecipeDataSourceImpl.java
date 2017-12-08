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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.ap.fahm.bakingapp.model.localdata.db.RecipeDataContract;
import com.ap.fahm.bakingapp.model.pojo.Ingredient;
import com.ap.fahm.bakingapp.model.pojo.Recipe;
import com.ap.fahm.bakingapp.model.pojo.Step;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static android.R.attr.id;

public class LocalRecipeDataSourceImpl implements LocalRecipeDataSource {

    private Context context;

    public LocalRecipeDataSourceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void getAll(LocalDataObserver<ArrayList<Recipe>> dataObserver) {
        Observable.create(new ObservableOnSubscribe<ArrayList<Recipe>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ArrayList<Recipe>> e) throws Exception {
                // Get Recipe List
                Cursor recipeCursor = context.getContentResolver()
                        .query(RecipeDataContract.RecipeEntry.RECIPE_CONTENT_URI,
                                null,
                                null,
                                null,
                                RecipeDataContract.RecipeEntry.COLUMN_DATE_ADDED + " DESC");

                ArrayList<Recipe> recipes = new ArrayList<>();
                if (recipeCursor != null) {
                    while (recipeCursor.moveToNext()) {
                        Recipe recipe = getRecipeFromCursor(recipeCursor);
                        recipes.add(recipe);
                    }
                    recipeCursor.close();

                    for (int i = 0; i < recipes.size(); i++) {
                        recipes.get(i).setIngredients(getIngredients(recipes, i));
                        recipes.get(i).setSteps(getSteps(recipes, i));
                    }
                    e.onNext(recipes);
                } else {
                    e.onError(new NullPointerException("Failed to query all data"));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataObserver);
    }

    @android.support.annotation.NonNull
    private ArrayList<Ingredient> getIngredients(ArrayList<Recipe> recipes, int recipePosition) {
        Uri baseIngredientUri = RecipeDataContract
                .IngredientEntry
                .INGREDIENT_CONTENT_ITEM_URI.build();

        String ingredientUriString = baseIngredientUri.toString() + "/" +
                recipes.get(recipePosition).getId();
        Uri ingredientUri = Uri.parse(ingredientUriString);

        Cursor ingredientCursor = context.getContentResolver()
                .query(ingredientUri,
                        null,
                        null,
                        null,
                        RecipeDataContract.IngredientEntry.COLUMN_ID + " ASC");

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        if (ingredientCursor != null) {
            while (ingredientCursor.moveToNext()) {
                Ingredient ingredient = getIngredientFromCursor(ingredientCursor);
                ingredients.add(ingredient);
            }
            ingredientCursor.close();
        }
        return ingredients;
    }

    @android.support.annotation.NonNull
    private ArrayList<Step> getSteps(ArrayList<Recipe> recipes, int recipePosition) {
        Uri baseStepUri = RecipeDataContract
                .StepEntry
                .STEP_CONTENT_ITEM_URI.build();

        String stepUriString = baseStepUri.toString() + "/" + recipes.get(recipePosition).getId();
        Uri stepUri = Uri.parse(stepUriString);

        Cursor stepCursor = context.getContentResolver()
                .query(stepUri,
                        null,
                        null,
                        null,
                        RecipeDataContract.StepEntry.COLUMN_ID + " ASC");

        ArrayList<Step> steps = new ArrayList<>();
        if (stepCursor != null) {
            while (stepCursor.moveToNext()) {
                Step step = getStepFromCursor(stepCursor);
                steps.add(step);
            }
            stepCursor.close();
        }
        return steps;
    }

    @android.support.annotation.NonNull
    private Ingredient getIngredientFromCursor(Cursor ingredientCursor) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientCursor.getLong(ingredientCursor
                .getColumnIndex(RecipeDataContract.IngredientEntry.COLUMN_ID)));
        ingredient.setRecipeId(ingredientCursor.getLong(ingredientCursor
                .getColumnIndex(RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID)));
        ingredient.setIngredient(ingredientCursor.getString(ingredientCursor
                .getColumnIndex(RecipeDataContract.IngredientEntry.COLUMN_INGREDIENT)));
        ingredient.setQuantity(ingredientCursor.getDouble(ingredientCursor
                .getColumnIndex(RecipeDataContract.IngredientEntry.COLUMN_QUANTITY)));
        ingredient.setMeasure(ingredientCursor.getString(ingredientCursor
                .getColumnIndex(RecipeDataContract.IngredientEntry.COLUMN_MEASURE)));
        return ingredient;
    }

    @android.support.annotation.NonNull
    private Step getStepFromCursor(Cursor stepCursor) {
        Step step = new Step();
        step.setId(stepCursor.getLong(stepCursor
                .getColumnIndex(RecipeDataContract.StepEntry.COLUMN_ID)));
        step.setRecipeId(stepCursor.getLong(stepCursor
                .getColumnIndex(RecipeDataContract.StepEntry.COLUMN_RECIPE_ID)));
        step.setShortDescription(stepCursor.getString(stepCursor
                .getColumnIndex(RecipeDataContract.StepEntry.COLUMN_SHORT_DESCRIPTION)));
        step.setDescription(stepCursor.getString(stepCursor
                .getColumnIndex(RecipeDataContract.StepEntry.COLUMN_DESCRIPTION)));
        step.setThumbnailURL(stepCursor.getString(stepCursor
                .getColumnIndex(RecipeDataContract.StepEntry.COLUMN_THUMBNAIL_URL)));
        step.setVideoURL(stepCursor.getString(stepCursor
                .getColumnIndex(RecipeDataContract.StepEntry.COLUMN_VIDEO_URL)));
        return step;
    }

    @android.support.annotation.NonNull
    private Recipe getRecipeFromCursor(Cursor recipeCursor) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeCursor.getLong(recipeCursor.getColumnIndex(
                RecipeDataContract.RecipeEntry.COLUMN_ID)));
        recipe.setImage(recipeCursor.getString(recipeCursor.getColumnIndex(
                RecipeDataContract.RecipeEntry.COLUMN_IMAGE)));
        recipe.setFavorite(recipeCursor.getInt(recipeCursor.getColumnIndex(
                RecipeDataContract.RecipeEntry.COLUMN_FAVORITE)) == 1);
        recipe.setDateAdded(recipeCursor.getLong(recipeCursor.getColumnIndex(
                RecipeDataContract.RecipeEntry.COLUMN_DATE_ADDED)));
        recipe.setName(recipeCursor.getString(recipeCursor.getColumnIndex(
                RecipeDataContract.RecipeEntry.COLUMN_NAME)));
        recipe.setServings(recipeCursor.getInt(recipeCursor.getColumnIndex(
                RecipeDataContract.RecipeEntry.COLUMN_SERVINGS)));
        return recipe;
    }

    @Override
    public void getById(final long id, LocalDataObserver<Cursor> cursorFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Cursor> e) throws Exception {
                String stringId = Long.toString(id);
                Uri uri = RecipeDataContract.RecipeEntry.RECIPE_CONTENT_URI.buildUpon()
                        .appendPath(stringId)
                        .build();
                Cursor cursor = context.getContentResolver()
                        .query(uri,
                                null,
                                RecipeDataContract.RecipeEntry.COLUMN_ID + " = ?",
                                new String[]{String.valueOf(id)},
                                RecipeDataContract.RecipeEntry.COLUMN_DATE_ADDED + " DESC");

                if (cursor != null) {
                    Log.d("QueryCursor", String.valueOf(cursor.getCount()));
                    e.onNext(cursor);
                } else {
                    e.onError(new NullPointerException("Failed to query data with id: " + id));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cursorFavoriteDataObserver);
    }

    @Override
    public void insert(final Recipe recipe, LocalDataObserver<Uri> uriFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Uri> e) throws Exception {
                insertSingleRecipe(e, recipe);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uriFavoriteDataObserver);
    }

    private void insertSingleRecipe(@NonNull ObservableEmitter<Uri> e, Recipe recipe) {
        ContentValues contentValues = getContentValues(recipe);

        Uri uri = context.getContentResolver()
                .insert(RecipeDataContract.RecipeEntry.RECIPE_CONTENT_URI, contentValues);
        if (uri != null) {
            Log.d("InsertUri", uri.toString());
            e.onNext(uri);
        } else {
            e.onError(new NullPointerException("Failed to insert data"));
        }
    }

    @Override
    public void insertMany(final ArrayList<Recipe> recipes, LocalDataObserver<Uri> uriFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Uri> e) throws Exception {
                for (int i = 0; i < recipes.size(); i++) {
                    Recipe recipe = recipes.get(i);
                    insertSingleRecipe(e, recipe);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uriFavoriteDataObserver);
    }

    @Override
    public void update(final Recipe recipe, LocalDataObserver<Integer> integerFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                String stringId = Long.toString(recipe.getId());
                Uri uri = RecipeDataContract.RecipeEntry.RECIPE_CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                ContentValues contentValues = getContentValues(recipe);

                int count = context.getContentResolver().update(uri, contentValues, null, null);

                if (count != 0) {
                    Log.d("UpdateCount", String.valueOf(count));
                    e.onNext(count);
                } else {
                    e.onError(new NullPointerException("Failed to update data with id: " + id));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integerFavoriteDataObserver);
    }

    @android.support.annotation.NonNull
    private ContentValues getContentValues(Recipe recipe) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeDataContract.RecipeEntry.COLUMN_ID, recipe.getId());
        contentValues.put(RecipeDataContract.RecipeEntry.COLUMN_NAME, recipe.getName());
        contentValues.put(RecipeDataContract.RecipeEntry.COLUMN_SERVINGS, recipe.getServings());
        contentValues.put(RecipeDataContract.RecipeEntry.COLUMN_IMAGE, recipe.getImage());
        contentValues.put(RecipeDataContract.RecipeEntry.COLUMN_FAVORITE, recipe.isFavorite());
        contentValues.put(RecipeDataContract.RecipeEntry.COLUMN_DATE_ADDED, recipe.getDateAdded());
        contentValues.put(RecipeDataContract.RecipeEntry.ENTITY_INGREDIENTS,
                new Gson().toJson(recipe.getIngredients()));
        contentValues.put(RecipeDataContract.RecipeEntry.ENTITY_STEPS,
                new Gson().toJson(recipe.getSteps()));
        return contentValues;
    }

    @Override
    public void delete(final long id, LocalDataObserver<Integer> integerFavoriteDataObserver) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                String stringId = Long.toString(id);
                Uri uri = RecipeDataContract.RecipeEntry.RECIPE_CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                int count = context.getContentResolver().delete(uri, null, null);

                if (count != 0) {
                    Log.d("DeleteCount", String.valueOf(count));
                    e.onNext(count);
                } else {
                    e.onError(new NullPointerException("Failed to delete data with id: " + id));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integerFavoriteDataObserver);
    }
}