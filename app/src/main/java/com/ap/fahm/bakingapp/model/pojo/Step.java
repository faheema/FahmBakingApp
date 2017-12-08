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

@Entity(tableName = RecipeDataContract.StepEntry.TABLE_NAME,
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = RecipeDataContract.RecipeEntry.COLUMN_ID,
                childColumns = RecipeDataContract.StepEntry.COLUMN_RECIPE_ID))
public class Step implements Parcelable {

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = RecipeDataContract.StepEntry.COLUMN_ID)
    @SerializedName(RecipeDataContract.StepEntry.COLUMN_ID)
    @Expose
    private long id;

    @ColumnInfo(name = RecipeDataContract.StepEntry.COLUMN_SHORT_DESCRIPTION)
    @SerializedName(RecipeDataContract.StepEntry.COLUMN_SHORT_DESCRIPTION)
    @Expose
    private String shortDescription;

    @ColumnInfo(name = RecipeDataContract.StepEntry.COLUMN_DESCRIPTION)
    @SerializedName(RecipeDataContract.StepEntry.COLUMN_DESCRIPTION)
    @Expose
    private String description;

    @ColumnInfo(name = RecipeDataContract.StepEntry.COLUMN_VIDEO_URL)
    @SerializedName(RecipeDataContract.StepEntry.COLUMN_VIDEO_URL)
    @Expose
    private String videoURL;

    @ColumnInfo(name = RecipeDataContract.StepEntry.COLUMN_THUMBNAIL_URL)
    @SerializedName(RecipeDataContract.StepEntry.COLUMN_THUMBNAIL_URL)
    @Expose
    private String thumbnailURL;

    /*
    * Addition, not exist in JSON
    * */
    @ColumnInfo(index = true, name = RecipeDataContract.StepEntry.COLUMN_RECIPE_ID)
    private long recipeId;

    public Step() {
    }

    protected Step(Parcel in) {
        id = in.readLong();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
        recipeId = in.readLong();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
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
        dest.writeLong(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
        dest.writeLong(recipeId);
    }

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoURL='" + videoURL + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                ", recipeId=" + recipeId +
                '}';
    }
}
