/*
 * Copyright 2017.  FaheemApps.com
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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ap.fahm.bakingapp.R;
import com.ap.fahm.bakingapp.model.pojo.Ingredient;
import com.ap.fahm.bakingapp.util.DisplayMetricUtils;
import com.ap.fahm.bakingapp.util.RecyclerViewMarginDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientFragment extends Fragment {

    @BindView(R.id.rv_ingredient)
    RecyclerView rvIngredient;

    private IngredientAdapter ingredientAdapter;

    public IngredientFragment() {
        // Required empty public constructor
    }

    public static IngredientFragment newInstance(ArrayList<Ingredient> ingredients) {
        IngredientFragment ingredientFragment = new IngredientFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ingredients", ingredients);
        ingredientFragment.setArguments(bundle);

        return ingredientFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        int column = 1;
        int marginInPixel = DisplayMetricUtils.convertDpToPixel(8);

        RecyclerViewMarginDecoration decoration =
                new RecyclerViewMarginDecoration(RecyclerViewMarginDecoration.ORIENTATION_VERTICAL,
                        marginInPixel, column);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvIngredient.setLayoutManager(layoutManager);
        rvIngredient.addItemDecoration(decoration);
        ArrayList<Ingredient> ingredients = getArguments().getParcelableArrayList("ingredients");
        ingredientAdapter = new IngredientAdapter(ingredients);
        rvIngredient.setAdapter(ingredientAdapter);
    }

}
