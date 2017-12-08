/*
 * Copyright 2017.  Irfan Khoirul Muhlishin
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

package com.ap.fahm.bakingapp.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ap.fahm.bakingapp.R;
import com.ap.fahm.bakingapp.model.pojo.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleRecipeAdapter extends RecyclerView.Adapter<SimpleRecipeAdapter.SimpleRecipeViewHolder> {

    private ArrayList<Recipe> recipes = new ArrayList<>();
    private SimpleRecipeClickListener clickListener;

    public SimpleRecipeAdapter(ArrayList<Recipe> recipes, SimpleRecipeClickListener clickListener) {
        this.recipes = recipes;
        this.clickListener = clickListener;
    }

    @Override
    public SimpleRecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_simple, parent, false);

        return new SimpleRecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleRecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvRecipeName.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    interface SimpleRecipeClickListener {
        void onRecipeItemClick(Recipe review);
    }

    class SimpleRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_name)
        TextView tvRecipeName;

        SimpleRecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onRecipeItemClick(recipes.get(getAdapterPosition()));
        }
    }

}
