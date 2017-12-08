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

package com.ap.fahm.bakingapp.model.pojo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.ap.fahm.bakingapp.model.localdata.db.RecipeDataContract;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.ap.fahm.bakingapp.model.localdata.db.RecipeDataContract.RecipeEntry.COLUMN_DATE_ADDED;


@Entity(tableName = RecipeDataContract.RecipeEntry.TABLE_NAME)
public class Recipe implements Parcelable {

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    @PrimaryKey
    @ColumnInfo(index = true, name = RecipeDataContract.RecipeEntry.COLUMN_ID)
    @SerializedName(RecipeDataContract.RecipeEntry.COLUMN_ID)
    @Expose
    private long id;

    @ColumnInfo(name = RecipeDataContract.RecipeEntry.COLUMN_NAME)
    @SerializedName(RecipeDataContract.RecipeEntry.COLUMN_NAME)
    @Expose
    private String name;

    @Ignore
    @SerializedName(RecipeDataContract.RecipeEntry.ENTITY_INGREDIENTS)
    @Expose
    private ArrayList<Ingredient> ingredients = null;

    @Ignore
    @SerializedName(RecipeDataContract.RecipeEntry.ENTITY_STEPS)
    @Expose
    private ArrayList<Step> steps = null;

    @ColumnInfo(name = RecipeDataContract.RecipeEntry.COLUMN_SERVINGS)
    @SerializedName(RecipeDataContract.RecipeEntry.COLUMN_SERVINGS)
    @Expose
    private int servings;

    @ColumnInfo(name = RecipeDataContract.RecipeEntry.COLUMN_IMAGE)
    @SerializedName(RecipeDataContract.RecipeEntry.COLUMN_IMAGE)
    @Expose
    private String image;

    /*
    * Addition, not exist in JSON
    * */
    @ColumnInfo(name = RecipeDataContract.RecipeEntry.COLUMN_FAVORITE)
    private boolean favorite;

    @ColumnInfo(name = COLUMN_DATE_ADDED)
    private long dateAdded;

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        servings = in.readInt();
        image = in.readString();
        favorite = in.readByte() != 0;
        dateAdded = in.readLong();
    }

    public static Recipe fromContentValues(ContentValues values) {
        final Recipe recipe = new Recipe();
        if (values.containsKey(RecipeDataContract.RecipeEntry.COLUMN_ID)) {
            recipe.id = values.getAsLong(RecipeDataContract.RecipeEntry.COLUMN_ID);
        }
        if (values.containsKey(RecipeDataContract.RecipeEntry.COLUMN_NAME)) {
            recipe.name = values.getAsString(RecipeDataContract.RecipeEntry.COLUMN_NAME);
        }
        if (values.containsKey(RecipeDataContract.RecipeEntry.ENTITY_INGREDIENTS)) {
            recipe.ingredients = new Gson().fromJson(values.getAsString(
                    RecipeDataContract.RecipeEntry.ENTITY_INGREDIENTS),
                    new TypeToken<List<Ingredient>>() {
                    }.getType());
        }
        if (values.containsKey(RecipeDataContract.RecipeEntry.ENTITY_STEPS)) {
            recipe.steps = new Gson().fromJson(values.getAsString(
                    RecipeDataContract.RecipeEntry.ENTITY_STEPS),
                    new TypeToken<List<Step>>() {
                    }.getType());
        }
        if (values.containsKey(RecipeDataContract.RecipeEntry.COLUMN_SERVINGS)) {
            recipe.servings = values.getAsInteger(RecipeDataContract.RecipeEntry.COLUMN_SERVINGS);
        }
        if (values.containsKey(RecipeDataContract.RecipeEntry.COLUMN_IMAGE)) {
            recipe.image = values.getAsString(RecipeDataContract.RecipeEntry.COLUMN_IMAGE);
        }
        if (values.containsKey(RecipeDataContract.RecipeEntry.COLUMN_FAVORITE)) {
            recipe.favorite = values.getAsBoolean(RecipeDataContract.RecipeEntry.COLUMN_FAVORITE);
        }
        if (values.containsKey(COLUMN_DATE_ADDED)) {
            recipe.dateAdded = values.getAsLong(COLUMN_DATE_ADDED);
        }

        return recipe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeLong(dateAdded);
    }

    @Override
    public String toString() {
        String recipe = "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'';
        if (ingredients != null) {
            recipe += ", ingredients=" + ingredients.size() + " items ";
        }
        if (steps != null) {
            recipe += ", steps=" + steps.size() + " items ";
        }
        recipe += ", servings=" + servings +
                ", image='" + image + '\'' +
                ", favorite=" + favorite +
                ", dateAdded=" + dateAdded +
                '}';

        return recipe;
    }
}
