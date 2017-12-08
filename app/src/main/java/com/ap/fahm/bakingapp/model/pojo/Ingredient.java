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
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.ap.fahm.bakingapp.model.localdata.db.RecipeDataContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = RecipeDataContract.IngredientEntry.TABLE_NAME,
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = RecipeDataContract.RecipeEntry.COLUMN_ID,
                childColumns = RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID))
public class Ingredient implements Parcelable {

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @ColumnInfo(name = RecipeDataContract.IngredientEntry.COLUMN_QUANTITY)
    @SerializedName(RecipeDataContract.IngredientEntry.COLUMN_QUANTITY)
    @Expose
    private double quantity;

    @ColumnInfo(name = RecipeDataContract.IngredientEntry.COLUMN_MEASURE)
    @SerializedName(RecipeDataContract.IngredientEntry.COLUMN_MEASURE)
    @Expose
    private String measure;

    @ColumnInfo(name = RecipeDataContract.IngredientEntry.COLUMN_INGREDIENT)
    @SerializedName(RecipeDataContract.IngredientEntry.COLUMN_INGREDIENT)
    @Expose
    private String ingredient;

    /*
    * Addition, not exist in JSON
    * */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = RecipeDataContract.IngredientEntry.COLUMN_ID)
    private long id;

    @ColumnInfo(index = true, name = RecipeDataContract.IngredientEntry.COLUMN_RECIPE_ID)
    private long recipeId;

    public Ingredient() {
    }

    protected Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
        id = in.readLong();
        recipeId = in.readLong();
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
        dest.writeLong(id);
        dest.writeLong(recipeId);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                ", id=" + id +
                ", recipeId=" + recipeId +
                '}';
    }
}
