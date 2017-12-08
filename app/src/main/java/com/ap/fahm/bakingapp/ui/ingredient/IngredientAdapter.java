/*
 * Copyright 2017.  FaheemAppss
 * Copyright 2017.  FaheemAppss
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

package com.ap.fahm.bakingapp.ui.ingredient;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ap.fahm.bakingapp.R;
import com.ap.fahm.bakingapp.model.pojo.Ingredient;
import com.ap.fahm.bakingapp.util.TextUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private Context context;

    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        if (context == null) {
            context = itemView.getContext();
        }

        return new IngredientAdapter.IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        if (position % 2 == 1) {
            holder.llIngredient.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.colorPrimary));
        } else {
            holder.llIngredient.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.colorwhite));
        }

        Ingredient item = ingredients.get(position);
        holder.tvIngredient.setText(TextUtils.capitalizeEachWords(item.getIngredient()));
        String quantity = TextUtils.removeTrailingZero(String.valueOf(item.getQuantity())) + " " +
                item.getMeasure();
        holder.tvQuantity.setText(quantity);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_ingredient)
        LinearLayout llIngredient;
        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;
        @BindView(R.id.tv_quantity)
        TextView tvQuantity;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
