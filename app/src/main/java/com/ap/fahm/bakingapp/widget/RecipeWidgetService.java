package com.ap.fahm.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ap.fahm.bakingapp.R;
import com.ap.fahm.bakingapp.model.localdata.db.RecipeDataContract;
import com.ap.fahm.bakingapp.model.pojo.Ingredient;

import java.util.ArrayList;

public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private long recipeId;

    public RecipeRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        recipeId = Long.valueOf(intent.getData().getSchemeSpecificPart());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        ingredients = getIngredients(recipeId);
    }

    private ArrayList<Ingredient> getIngredients(long recipeId) {
        Uri baseIngredientUri = RecipeDataContract
                .IngredientEntry
                .INGREDIENT_CONTENT_ITEM_URI.build();

        String ingredientUriString = baseIngredientUri.toString() + "/" + recipeId;
        Uri ingredientUri = Uri.parse(ingredientUriString);

        Cursor ingredientCursor = mContext.getContentResolver()
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

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (ingredients.size() == 0) {
            return null;
        }

        Ingredient ingredient = ingredients.get(position);

        String ingredientName = ingredient.getIngredient();
        double quantity = ingredient.getQuantity();
        String measure = ingredient.getMeasure();

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_ingredient);
        views.setTextViewText(R.id.tv_ingredient, ingredientName);
        views.setTextViewText(R.id.tv_quantity, quantity + " " + measure);

        Bundle extras = new Bundle();
        extras.putLong("recipeId", recipeId);
        extras.putLong("ingredientId", ingredients.get(position).getId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.ll_ingredient, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

